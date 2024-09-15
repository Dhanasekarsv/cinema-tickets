package uk.gov.dwp.uc.pairtest;


import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketServiceImpl = new TicketServiceImpl();

    /**
     * Test case to Purchase Tickets without adult ticket
     * AssertThrows() helps to test multiple exceptions
     */

    @Test
    public void purchase_Tickets_Without_AdultTicket(){
        long accountId = 1234L;
        TicketTypeRequest ticketTypeRequestChild =  new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10);
        TicketTypeRequest ticketTypeRequestInfant =  new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestChild, ticketTypeRequestInfant));
        assertEquals("Please add atleast one adult", exception.getMessage());
    }

    /**
     * Test case to  Purchase Tickets with empty request
     */

    @Test
    public void purchase_Tickets_WithEmpty_TicketRequest(){
        long accountId = 1234L;
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId));
        assertEquals("Invalid ticket request", exception.getMessage());
    }

    /**
     * Test case to check with Invalid Account Number
     */

    @Test
    public void purchase_Tickets_With_InvalidAccountNumber(){
        long accountId = 0L;
        TicketTypeRequest ticketTypeRequestAdult =  new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
        TicketTypeRequest ticketTypeRequestInfant =  new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestAdult, ticketTypeRequestInfant));
        assertEquals("Account ID is less than or equal to zero, Invalid account id provided", exception.getMessage());
    }

    /**
     * Test case to check ticket count exceed maximum limit
     */
    @Test
    public void ticketCountExceedMaximumLimit_Throw_Exception(){
        long accountId = 1000L;
        TicketTypeRequest ticketTypeRequestAdult =  new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 11);
        TicketTypeRequest ticketTypeRequestChild =  new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10);
        TicketTypeRequest ticketTypeRequestInfant =  new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult,ticketTypeRequestChild,ticketTypeRequestInfant));
        assertEquals("Maximum ticket count exceeded", exception.getMessage());
    }
    /**
     * Test case to check ticket with Account Id is null
     */
    @Test
    public void accountIdIsNull_Throw_Exception(){
        Long accountId = null;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,3);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestAdult));
        assertEquals("Account ID is null, Invalid account id provided", exception.getMessage());
    }

    /**
     * Test case to check ticket with Account id is not valid
     */
    @Test
    public void ifAccountIdIsNotValid_Throw_Exception(){
        long accountId = -1;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,2);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult));
        assertEquals("Account ID is less than or equal to zero, Invalid account id provided", exception.getMessage());
    }

    /**
     * Invalid ticket purchase : Infant Only Request
     */
    @Test
    public void invalidPurchaseTicket_Throw_Exception(){
        long accountId = 12;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestChild));
        assertEquals("Invalid ticket request, Type of request is Infant alone", exception.getMessage());
    }

    /**
     * Infant and Child without adult ticket purchase
     */
    @Test
    public void infantAndChildWithoutAdultPurchaseTicket_Throw_Exception(){
        long accountId = 70;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestChild,ticketTypeRequestInfant));
        assertEquals("Please add atleast one adult", exception.getMessage());
    }


    /**
     * Infant and Child without adult ticket purchase
     */
    @Test
    public void ChildWithoutAdultPurchaseTicket_Throw_Exception(){
        long accountId = 99;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestInfant,ticketTypeRequestChild));
        assertEquals("Please add atleast one adult", exception.getMessage());
    }

    /**
     * Invalid ticket request : Ticket Details are missing
     */
    @Test
    public void ifTicketDetailsIsMissing_Throw_Exception(){
        long accountId = 45;
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId));
        assertEquals("Invalid ticket request", exception.getMessage());
    }


    /**
     * Multiple ticket purchase : Success Scenario
     */
    @Test
    public void multiple_ticket_purchase_success(){
        long accountId = 499;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,2);
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult,ticketTypeRequestChild,ticketTypeRequestInfant);
    }

    /**
     * Ticket purchase success : Success Scenario
     */
    @Test
    public void ticket_purchase_success(){
        long accountId = 777;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult);
    }
    /**
     * Ticket purchase success with Wrapper account id
     */
    @Test
    public void ticket_purchase_success_with_wrapper(){
        Long accountId = Long.valueOf(678);
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult);
    }
}
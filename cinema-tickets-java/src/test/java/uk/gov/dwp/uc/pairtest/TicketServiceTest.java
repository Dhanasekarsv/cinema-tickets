package uk.gov.dwp.uc.pairtest;


import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;



@DisplayName("TicketService Implementation Test Cases")
public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketServiceImpl = new TicketServiceImpl();

    /**
     * Test case to Purchase Tickets without adult ticket
     * AssertThrows() helps to test multiple exceptions
     */

    @Test
    @DisplayName("Ticket Purchase Without Adult Ticket")
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
    @DisplayName("Ticket Purchase With Empty Request")
    public void purchase_Tickets_WithEmpty_TicketRequest(){
        long accountId = 1234L;
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId));
        assertEquals("Invalid ticket request", exception.getMessage());
    }

    /**
     * Test case to check with Invalid Account Number
     */

    @Test
    @DisplayName("Ticket Purchase With Invalid Account Number")
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
    @DisplayName("Ticket Count Exceed max Limit")
    public void ticketCountExceedMaximumLimit_Throw_Exception(){
        long accountId = 100L;
        TicketTypeRequest ticketTypeRequestAdult =  new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 11);
        TicketTypeRequest ticketTypeRequestChild =  new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10);
        TicketTypeRequest ticketTypeRequestInfant =  new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult,ticketTypeRequestChild,ticketTypeRequestInfant));
        assertEquals("Maximum ticket count exceeded", exception.getMessage());
    }

    /**
     * Test case to check ticket Price Negative Value
     */
    @Test
    @DisplayName("Ticket Count with negative values")
    public void tickets_With_InValidRequest_Negative_count(){
        long accountId = 100L;
        TicketTypeRequest ticketTypeRequestAdult =  new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -10);
        TicketTypeRequest ticketTypeRequestChild =  new TicketTypeRequest(TicketTypeRequest.Type.CHILD, -5);
        TicketTypeRequest ticketTypeRequestInfant =  new TicketTypeRequest(TicketTypeRequest.Type.INFANT, -5);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestAdult,ticketTypeRequestChild, ticketTypeRequestInfant));
        assertEquals("Ticket Price on request cant be negative value", exception.getMessage());
    }


    /**
     * Test case to check ticket with Account Id is null
     */
    @Test
    @DisplayName("Ticket Purchase With Account Numer as Null")
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
    @DisplayName("Ticket Purchase With Invalid Account Number")
    public void ifAccountIdIsNotValid_Throw_Exception(){
        long accountId = -2;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,2);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult));
        assertEquals("Account ID is less than or equal to zero, Invalid account id provided", exception.getMessage());
    }

    /**
     * Invalid ticket purchase : Infant Only Request
     */
    @Test
    @DisplayName("Ticket Purchase With Only Infant Ticket")
    public void invalidPurchaseTicket_Throw_Exception(){
        long accountId = 21;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestChild));
        assertEquals("Invalid ticket request, Type of request is Infant alone", exception.getMessage());
    }

    /**
     * Infant and Child without adult ticket purchase
     */
    @Test
    @DisplayName("Ticket Purchase With Infant & Child Ticket")
    public void infantAndChildWithoutAdultPurchaseTicket_Throw_Exception(){
        long accountId = 80;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestChild,ticketTypeRequestInfant));
        assertEquals("Please add atleast one adult", exception.getMessage());
    }

    /**
     * Infant and Child without adult ticket purchase
     */
    @Test
    @DisplayName("Ticket Purchase With Infant & Child Ticket")
    public void ChildWithoutAdultPurchaseTicket_Throw_Exception(){
        long accountId = 33;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestInfant,ticketTypeRequestChild));
        assertEquals("Please add atleast one adult", exception.getMessage());
    }

    /**
     * Invalid ticket request : Ticket Details are missing
     */
    @Test
    @DisplayName("Ticket Purchase With No Tickets")
    public void ifTicketDetailsIsMissing_Throw_Exception(){
        long accountId = 54;
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, ()->ticketServiceImpl.purchaseTickets(accountId));
        assertEquals("Invalid ticket request", exception.getMessage());
    }

    /**
     * Multiple ticket purchase : Success Scenario
     */
    @Test
    @DisplayName("Success Scenario")
    public void multiple_ticket_purchase_success(){
        long accountId = 945;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,2);
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult,ticketTypeRequestChild,ticketTypeRequestInfant);
    }

    /**
     * Ticket purchase success : Success Scenario
     */
    @Test
    @DisplayName("Success Scenario")
    public void ticket_purchase_success(){
        long accountId = 45;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult);
    }

    /**
     * Ticket purchase success with Wrapper account id
     */
    @Test
    @DisplayName("Success Scenario with Wrapper for long account number")
    public void ticket_purchase_success_with_wrapper(){
        Long accountId = Long.valueOf(234);
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,1);
        ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult);
    }
}
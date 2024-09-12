package uk.gov.dwp.uc.pairtest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;


public class TicketServiceTest {

    private long ACCOUNT_ID = 1234L;

    @InjectMocks
    private TicketService ticketServiceImpl = new TicketServiceImpl();

    @Mock
    private TicketPaymentService paymentService;

    @Mock
    private SeatReservationService seatReservationService;

    @Test
    public void testPurchaseTicketsWithoutAdultTicket(){
        TicketTypeRequest t2 =  new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10);
        TicketTypeRequest t3 =  new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(ACCOUNT_ID, t2, t3);
        }, "Atleast one Adult ticket should be purchased");
    }

    @Test
    public void testPurchaseTicketsWithEmptyTicketRequest(){
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(ACCOUNT_ID);
        }, "Atleast one ticket should be bought");
    }

    @Test
    public void testPurchaseTicketsWhenMaxAllocatedTicketsExceeded(){
        TicketTypeRequest t1 =  new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
        TicketTypeRequest t2 =  new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 20);
        TicketTypeRequest t3 =  new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(ACCOUNT_ID, t1, t2, t3);
        }, "Total Number of tickets exceeded the purchase limit");
    }

    @Test
    public void testPurchaseTicketsWithInvalidAccountNumber(){
        TicketTypeRequest t2 =  new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
        TicketTypeRequest t3 =  new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);

        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(0L, t2, t3);
        }, "Invalid account id");
    }

    /**
     * Test case to check ticket count exceed maximum limit
     */
    @Test
    public void ticketCountExceedMaximumLimit_Throw_Exception(){
        long accountId = 1000L;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,21);
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult);
        }, "Maximum ticket count exceeded");
    }
    /**
     *
     */
    @Test
    public void accountIdIsNull_Throw_Exception(){
        Long accountId = null;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,3);
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestAdult);
        }, "Account ID is null, Invalid account id provided");
    }

    /**
     * Invalid ticket purchase
     */
    @Test
    public void invalidPurchaseTicket_Throw_Exception(){
        long accountId = 12;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestChild);
        }, "Invalid ticket request");
    }

    /**
     * Infant without adult ticket purchase
     */
    @Test
    public void infantWithoutAdultPurchaseTicket_Throw_Exception(){
        long accountId = 140;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestChild,ticketTypeRequestInfant);
        }, "Please add atleast one adult.");
    }

    /**
     * Child without adult ticket purchase
     */
    @Test
    public void childWithoutAdultPurchaseTicket_Throw_Exception(){
        long accountId = 70;
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(accountId, ticketTypeRequestChild,ticketTypeRequestInfant);
        }, "Please add atleast one adult.");
    }

    /**
     * Infant and Child without adult ticket purchase
     */
    @Test
    public void infantAndChildWithoutAdultPurchaseTicket_Throw_Exception(){
        long accountId = 99;
        TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(TicketTypeRequest.Type.INFANT,1);
        TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1);
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestInfant,ticketTypeRequestChild);
        }, "Please add atleast one adult.");
    }

    /**
     * Invalid ticket request
     */
    @Test
    public void ifTicketDetailsIsMissing_Throw_Exception(){
        long accountId = 45;
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(accountId);
        }, "Invalid ticket request.");
    }

    /**
     * Account id is not valid
     */
    @Test
    public void ifAccountIdIsNotValid_Throw_Exception(){
        long accountId = -1;
        TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(TicketTypeRequest.Type.ADULT,2);
        Assertions.assertThrows(InvalidPurchaseException.class, () -> {
            ticketServiceImpl.purchaseTickets(accountId,ticketTypeRequestAdult);
        }, "Account id is not valid.");

    }
    /**
     * Multiple ticket purchase
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
     * Ticket purchase success
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
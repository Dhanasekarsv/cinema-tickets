package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.utils.TicketConstants;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */
    static final Logger logger = Logger.getLogger(TicketServiceImpl.class);

    //SeatReservationServiceImpl For Seat Reservation
    private SeatReservationServiceImpl seatReservationService = new SeatReservationServiceImpl();

    //TicketPaymentServiceImpl For ticket payments
    private TicketPaymentServiceImpl ticketPaymentService = new TicketPaymentServiceImpl();

    /**
     * To check ticket count is exceeded the maximum ticket count
     * @param length
     * @return
     */
    private boolean isTicketCountExceeded(int length){
        if(length > TicketConstants.MAX_ALLOCATED_TICKETS){
            return true;
        }
        return false;
    }

    /**
     *  Calculate total seats to allocate for reservation
     * @param ticketRequest
     * @return
     */
    private int totalSeatsToAllocate(Map<TicketTypeRequest.Type, Integer> ticketRequest){
        return ticketRequest.entrySet().stream().filter(e -> e.getKey().getTicPrice() != 0).map(Map.Entry::getValue).reduce(0,Integer::sum);
    }

    /**
     * Calculate total amount to pay for the reserved seats
     * @param ticketRequest
     * @return
     */
    private int totalAmountToPay(Map<TicketTypeRequest.Type, Integer> ticketRequest){
        return ticketRequest.entrySet().stream().filter(e -> e.getKey().getTicPrice() != 0).map(e -> e.getKey().getTicPrice() * e.getValue()).reduce(0,Integer::sum);
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

        logger.info("purchaseTickets Method");

        if(accountId == null){
            logger.error("Account ID is null, Invalid account id provided");
            throw new InvalidPurchaseException("Account ID is null, Invalid account id provided");
        }

        if(accountId <= 0){
            logger.error("Account ID is less than or equal to zero, Invalid account id provided");
            throw new InvalidPurchaseException("Account ID is null, Invalid account id provided");
        }

        // Checking the request input to process ticket reservation.
        List<TicketTypeRequest> ticketTypeRequestsList = Arrays.asList(ticketTypeRequests);
        if(ticketTypeRequestsList.isEmpty()){
            logger.error("Invalid ticket request, Ticket Request is Empty");
            throw new InvalidPurchaseException("Invalid ticket request");
        }

        // Checking Ticket request for Infant and Child
        Map<TicketTypeRequest.Type, Integer> ticketRequestMap = ticketTypeRequestsList.stream().collect(Collectors.toMap(TicketTypeRequest::getTicketType,TicketTypeRequest::getNoOfTickets));
        if(ticketTypeRequestsList.size() == 1 && ticketRequestMap.containsKey(TicketTypeRequest.Type.INFANT)){
            logger.error("Invalid ticket request, Type of Ticket is Infant");
            throw new InvalidPurchaseException("Invalid ticket request");
        }
        if(ticketTypeRequestsList.size() == 1 && ticketRequestMap.containsKey(TicketTypeRequest.Type.CHILD)){
            logger.error("Invalid ticket request, Type of Ticket is Child");
            throw new InvalidPurchaseException("Invalid ticket request");
        }

        // Checking Child and Infant tickets as it cannot be purchased without purchasing an Adult ticket
        if(ticketRequestMap.containsKey(TicketTypeRequest.Type.INFANT) || ticketRequestMap.containsKey(TicketTypeRequest.Type.CHILD)){
            if(!ticketRequestMap.containsKey(TicketTypeRequest.Type.ADULT)){
                logger.error("Invalid ticket request, Please add atleast one adult");
                throw new InvalidPurchaseException("Please add atleast one adult");
            }
        }

        // Get total seats to allocate for ticket purchase.
        int totalSeatsToAllocate = totalSeatsToAllocate(ticketRequestMap);

        System.out.println("Total Seat allocated..."+totalSeatsToAllocate);

        if(isTicketCountExceeded(totalSeatsToAllocate)){
            logger.error("Maximum ticket count exceeded.");
            throw new InvalidPurchaseException("Maximum ticket count exceeded");
        }

        // Get total amount to pay for ticket purchase.
        int totalAmountToPay = totalAmountToPay(ticketRequestMap);

        System.out.println("Total Amount to pay..."+totalAmountToPay);

        // Seat reservation request.
        seatReservationService.reserveSeat(accountId, totalSeatsToAllocate);
        // Payment request.
        ticketPaymentService.makePayment(accountId,totalAmountToPay);


    }


}

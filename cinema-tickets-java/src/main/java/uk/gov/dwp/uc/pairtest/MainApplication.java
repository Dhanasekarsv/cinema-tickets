package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public class MainApplication {

    public static void main(String[] args) {
        long accountId = 100;
        TicketTypeRequest t1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10);
        TicketTypeRequest t2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 8);
        TicketTypeRequest t3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        TicketServiceImpl ticketServiceImpl = new TicketServiceImpl();
        ticketServiceImpl.purchaseTickets(accountId, t1, t2, t3);

        for (TicketTypeRequest.Type ticType : TicketTypeRequest.Type.values()) {
            System.out.println(ticType.getTicPrice());
        }
   }
}

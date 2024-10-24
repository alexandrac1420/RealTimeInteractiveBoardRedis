package com.example.boardwebsocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DrawingServiceController {
    

    private final TicketRepository ticketRepo;

    @Autowired
    public DrawingServiceController(TicketRepository ticketRepository){
        this.ticketRepo = ticketRepository;
    }

    @GetMapping("/status")
    public String status() {
        return "{\"status\":\"Greetings from Spring Boot. "
                + java.time.LocalDate.now() + ", "
                + java.time.LocalTime.now()
                + ". " + "The server is Runnig!\"}";
    }

    @GetMapping("/getticket")
    public String getTicket() {
        return "{\"ticket\":\"" + ticketRepo.getTicket() + "\"}";
    }
}

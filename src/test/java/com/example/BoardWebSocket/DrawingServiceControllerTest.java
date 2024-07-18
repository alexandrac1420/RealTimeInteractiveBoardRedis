package com.example.BoardWebSocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DrawingServiceControllerTest  {

    private DrawingServiceController drawingServiceController;
    private TicketRepository ticketRepository;

    @BeforeEach
    public void setUp() {
        ticketRepository = mock(TicketRepository.class);
        drawingServiceController = new DrawingServiceController();
        drawingServiceController.ticketRepo = ticketRepository;
    }

    @Test
    public void testStatus() {
        String response = drawingServiceController.status();
        assertTrue(response.contains("Greetings from Spring Boot"));
    }

    @Test
    public void testGetTicket() {
        when(ticketRepository.getTicket()).thenReturn(123);
        String response = drawingServiceController.getTicket();
        assertEquals("{\"ticket\":\"123\"}", response);
    }
}
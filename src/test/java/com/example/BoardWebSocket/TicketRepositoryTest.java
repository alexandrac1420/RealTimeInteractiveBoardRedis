package com.example.boardwebsocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

class TicketRepositoryTest {

    @InjectMocks
    private TicketRepository ticketRepository;

    @Mock
    private StringRedisTemplate template;

    @Mock
    private ListOperations<String, String> listTickets;

    @Mock
    private RedisOperations<String, String> redisOperations;

    @Mock
    private BoundListOperations<String, String> boundListOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(template.opsForList()).thenReturn(listTickets);
        when(listTickets.getOperations()).thenReturn(redisOperations);
        when(redisOperations.boundListOps("ticketStore")).thenReturn(boundListOperations);
        ticketRepository.ticketnumber = 0; // Inicializa el ticketnumber a 0 antes de cada prueba
    }

    @Test
    void testGetTicket() {
        Integer ticket = ticketRepository.getTicket();
        assertEquals(0, ticket);
        verify(listTickets, times(1)).leftPush("ticketStore", "0");
        
        ticket = ticketRepository.getTicket();
        assertEquals(1, ticket);
        verify(listTickets, times(1)).leftPush("ticketStore", "1");
    }

    @Test
    void testCheckTicketValid() {
        when(boundListOperations.remove(0, "0")).thenReturn(1L);
        boolean isValid = ticketRepository.checkTicket("0");
        assertTrue(isValid);
        verify(boundListOperations, times(1)).remove(0, "0");
    }

    @Test
    void testCheckTicketInvalid() {
        when(boundListOperations.remove(0, "1")).thenReturn(0L);
        boolean isValid = ticketRepository.checkTicket("1");
        assertFalse(isValid);
        verify(boundListOperations, times(1)).remove(0, "1");
    }
}
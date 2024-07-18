// package com.example.BoardWebSocket;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.data.redis.core.ListOperations;
// import org.springframework.data.redis.core.StringRedisTemplate;

// @SpringBootTest
// public class TicketRepositoryTests {

//     @Autowired
//     private TicketRepository ticketRepository;

//     @Autowired
//     private StringRedisTemplate redisTemplate;

//     @MockBean(name = "stringRedisTemplate")
//     private ListOperations<String, String> listOperations;

//     @BeforeEach
//     public void setUp() {
//         // Mock the list operations for Redis template
//         listOperations = redisTemplate.opsForList();
//     }

//     @Test
//     public void testGetTicket() {
//         Integer ticket1 = ticketRepository.getTicket();
//         Integer ticket2 = ticketRepository.getTicket();

//         assertEquals(ticket2, ticket1 + 1);
//     }

//     @Test
//     public void testCheckTicketValid() {
//         String ticket = ticketRepository.getTicket().toString();
//         assertTrue(ticketRepository.checkTicket(ticket));
//     }

//     @Test
//     public void testCheckTicketInvalid() {
//         assertFalse(ticketRepository.checkTicket("invalid_ticket"));
//     }
// }

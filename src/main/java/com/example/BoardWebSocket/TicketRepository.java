package com.example.boardwebsocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TicketRepository {
    private final StringRedisTemplate template;
    private final ListOperations<String, String> listTickets;
    int ticketnumber;


    @Autowired
    public TicketRepository(StringRedisTemplate template, ListOperations<String, String> listTickets) {
        this.template = template;
        this.listTickets = listTickets;
    }

    public synchronized Integer getTicket() {
        Integer a = ticketnumber++;
        listTickets.leftPush("ticketStore", a.toString());
        return a;
    }

    public boolean checkTicket(String ticket) {
        Long isValid = listTickets.getOperations().boundListOps("ticketStore").remove(0, ticket);
        return (isValid > 0l);
    }

}
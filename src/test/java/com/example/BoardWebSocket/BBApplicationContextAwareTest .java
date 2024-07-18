package com.example.BoardWebSocket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

class BBApplicationContextAwareTest {

    private BBApplicationContextAware contextAware;
    private ApplicationContext mockContext;

    @BeforeEach
    void setUp() {
        contextAware = new BBApplicationContextAware();
        mockContext = mock(ApplicationContext.class);
    }

    @Test
    void testSetApplicationContext() {
        contextAware.setApplicationContext(mockContext);
        assertEquals(mockContext, BBApplicationContextAware.getApplicationContext());
    }
}


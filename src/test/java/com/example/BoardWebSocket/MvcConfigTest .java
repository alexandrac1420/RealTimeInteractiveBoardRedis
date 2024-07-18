package com.example.BoardWebSocket;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

class MvcConfigTest {

    @Test
    void testMvcConfig() {
        MvcConfig config = new MvcConfig();
        assertTrue(config instanceof WebMvcConfigurer);
    }
}


package com.example.BoardWebSocket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
@TestPropertySource(locations = "classpath:application.properties")

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class WebSecurityConfigTest {

    @LocalServerPort
    private int port = 8443;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void accessToUnsecuredEndpointShouldBeAllowed() {
        ResponseEntity<String> response = restTemplate.getForEntity("https://localhost:" + port + "/index", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Your unsecured page content");
    }

    @Test
    void accessToSecuredEndpointShouldRequireAuthentication() {
        ResponseEntity<String> response = restTemplate.getForEntity("https://localhost:" + port + "/secure-endpoint", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void loginWithValidCredentialsShouldSucceed() {
        String loginUrl = "https://localhost:" + port + "/login";
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, new UserCredentials("user1", "password1"), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND); // Redirects upon successful login
    }

    @Test
    void loginWithInvalidCredentialsShouldFail() {
        String loginUrl = "https://localhost:" + port + "/login";
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, new UserCredentials("invalidUser", "invalidPassword"), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN); // Or another appropriate status code
    }

    private static class UserCredentials {
        private String username;
        private String password;

        public UserCredentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
}

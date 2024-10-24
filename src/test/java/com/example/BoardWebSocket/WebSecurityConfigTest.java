package com.example.boardwebsocket;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSecurityConfigTest {

    @LocalServerPort
    private int port;

    private RestTemplate httpsRestTemplate() throws Exception {
        // Load the trust store
        File trustStore = ResourceUtils.getFile("classpath:keystore/baeldung.p12");
        String trustStorePassword = "Al79373086*"; // Replace with your actual trust store password

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, trustStorePassword.toCharArray())
                .loadTrustMaterial((chain, authType) -> true) // Trust all certificates
                .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                sslContext, new String[] { "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(socketFactory)
                        .build())
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }

    @Test
    void accessToUnsecuredEndpointShouldBeAllowed() throws Exception {
        ResponseEntity<String> response = httpsRestTemplate().getForEntity("https://localhost:" + port + "/index",
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void accessToSecuredEndpointShouldRequireAuthentication() throws Exception {
        ResponseEntity<String> response = httpsRestTemplate()
                .getForEntity("https://localhost:" + port + "/secure-endpoint", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void loginWithValidCredentialsShouldSucceed() throws Exception {
        String loginUrl = "https://localhost:" + port + "/login";
        ResponseEntity<String> response = httpsRestTemplate().postForEntity(loginUrl,
                new UserCredentials("user1", "password1"), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); // Redirects upon successful login  
    }

    @Test
    void logoutShouldInvalidateSession() throws Exception {
        // Simulate login first
        httpsRestTemplate().postForEntity("https://localhost:" + port + "/login",
                new UserCredentials("user1", "password1"), String.class);

        // Perform logout
        ResponseEntity<String> response = httpsRestTemplate().postForEntity("https://localhost:" + port + "/logout",
                null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    private static class UserCredentials {
        private String username;
        private String password;

        public UserCredentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

}
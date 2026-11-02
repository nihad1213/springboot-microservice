package com.micro.order.client;

import com.micro.order.exception.DownstreamServiceException;
import com.micro.order.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(@Value("${services.user.url}") String baseUrl) {
        this.restClient = ClientSupport.build(baseUrl);
    }

    public void assertUserExists(String userId) {
        try {
            restClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .onStatus(s -> s.value() == 404, (req, res) -> {
                        throw new ResourceNotFoundException("User not found with id: " + userId);
                    })
                    .toBodilessEntity();
        } catch (ResourceAccessException e) {
            throw new DownstreamServiceException("User service is unavailable", e);
        }
    }
}

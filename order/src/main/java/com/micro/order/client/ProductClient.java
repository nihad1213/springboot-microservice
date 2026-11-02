package com.micro.order.client;

import com.micro.order.dto.ProductResponse;
import com.micro.order.exception.DownstreamServiceException;
import com.micro.order.exception.InsufficientStockException;
import com.micro.order.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(@Value("${services.product.url}") String baseUrl) {
        this.restClient = ClientSupport.build(baseUrl);
    }

    public ProductResponse getProduct(Long productId) {
        try {
            return restClient.get()
                    .uri("/api/products/{id}", productId)
                    .retrieve()
                    .onStatus(s -> s.value() == 404, (req, res) -> {
                        throw new ResourceNotFoundException("Product not found with id: " + productId);
                    })
                    .body(ProductResponse.class);
        } catch (ResourceAccessException e) {
            throw new DownstreamServiceException("Product service is unavailable", e);
        }
    }

    public void reserveStock(Long productId, int quantity) {
        try {
            restClient.post()
                    .uri("/api/products/{id}/reserve", productId)
                    .body(Map.of("quantity", quantity))
                    .retrieve()
                    .onStatus(s -> s.value() == 404, (req, res) -> {
                        throw new ResourceNotFoundException("Product not found with id: " + productId);
                    })
                    .onStatus(s -> s.value() == 409, (req, res) -> {
                        throw new InsufficientStockException("Not enough stock for product " + productId);
                    })
                    .toBodilessEntity();
        } catch (ResourceAccessException e) {
            throw new DownstreamServiceException("Product service is unavailable", e);
        }
    }

    public void releaseStock(Long productId, int quantity) {
        try {
            restClient.post()
                    .uri("/api/products/{id}/release", productId)
                    .body(Map.of("quantity", quantity))
                    .retrieve()
                    .toBodilessEntity();
        } catch (ResourceAccessException e) {
            throw new DownstreamServiceException("Product service is unavailable", e);
        }
    }
}
package com.micro.product.controller;

import com.micro.product.dto.ProductRequest;
import com.micro.product.dto.ProductResponse;
import com.micro.product.dto.StockRequest;
import com.micro.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.addProduct(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.fetchAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.fetchProductById(id));
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<Void> reserveStock(@PathVariable Long id, @RequestBody StockRequest request) {
        productService.reserveStock(id, request.getQuantity());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseStock(@PathVariable Long id, @RequestBody StockRequest request) {
        productService.releaseStock(id, request.getQuantity());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }
}
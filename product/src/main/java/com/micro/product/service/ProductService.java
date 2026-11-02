package com.micro.product.service;

import com.micro.product.dto.ProductRequest;
import com.micro.product.dto.ProductResponse;
import com.micro.product.exception.InsufficientStockException;
import com.micro.product.exception.ResourceNotFoundException;
import com.micro.product.model.Product;
import com.micro.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> fetchAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse addProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());

        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long id, ProductRequest updatedProductRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existingProduct.setName(updatedProductRequest.getName());
        existingProduct.setDescription(updatedProductRequest.getDescription());
        existingProduct.setPrice(updatedProductRequest.getPrice());
        existingProduct.setQuantity(updatedProductRequest.getStockQuantity());
        existingProduct.setCategory(updatedProductRequest.getCategory());
        existingProduct.setImageUrl(updatedProductRequest.getImageUrl());

        Product savedProduct = productRepository.save(existingProduct);
        return mapToProductResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(existingProduct);
    }

    public ProductResponse fetchProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public void reserveStock(Long id, Integer quantity) {
        requirePositive(quantity);
        if (productRepository.decrementStock(id, quantity) == 0) {
            if (!productRepository.existsById(id)) {
                throw new ResourceNotFoundException("Product not found with id: " + id);
            }
            throw new InsufficientStockException("Not enough stock for product " + id);
        }
    }

    @Transactional
    public void releaseStock(Long id, Integer quantity) {
        requirePositive(quantity);
        if (productRepository.incrementStock(id, quantity) == 0) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }

    private void requirePositive(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getQuantity());
        productResponse.setCategory(product.getCategory());
        productResponse.setImageUrl(product.getImageUrl());

        return productResponse;
    }
}
package com.example.product_api.service;

import com.example.product_api.dto.ProductResponseDTO;
import com.example.product_api.entity.Product;
import com.example.product_api.exception.ProductNotFoundException;
import com.example.product_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.product_api.dto.ProductRequestDTO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;



    @Test
    void shouldReturnProductWhenProductExists(){

        // Arrange
        Product product = new Product(
                1L,
                "Laptop",
                4200,
                "Electronics"
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // Act
        ProductResponseDTO result = productService.getProduct(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(4200, result.getPrice());
        assertEquals("Electronics", result.getCategory());
        verify(productRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        ProductNotFoundException exception =
                assertThrows(ProductNotFoundException.class,
                        () -> productService.getProduct(1L));

        assertEquals("Product with id 1 was not found!", exception.getMessage());
    }

    @Test
    void shouldSaveProduct() {

        // Arrange
        ProductRequestDTO requestDTO = new ProductRequestDTO(
                "Laptop",
                4200,
                "Electronics"
        );

        Product savedProduct = new Product(
                1L,
                "Laptop",
                4200,
                "Electronics"
        );

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        // Act
        ProductResponseDTO result = productService.save(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals(4200, result.getPrice());
        assertEquals("Electronics", result.getCategory());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {

        // Arrange
        Product product = new Product(
                1L,
                "Laptop",
                4200,
                "Electronics"
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingProduct() {

        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(1L));

        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void shouldUpdateProduct() {

        Product existingProduct = new Product(
                1L,
                "Laptop",
                4200,
                "Electronics"
        );

        ProductRequestDTO requestDTO = new ProductRequestDTO(
                "Gaming Laptop",
                5000,
                "Electronics"
        );

        Product updatedProduct = new Product(
                1L,
                "Gaming Laptop",
                5000,
                "Electronics"
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(any(Product.class)))
                .thenReturn(updatedProduct);

        ProductResponseDTO result =
                productService.updateProduct(1L, requestDTO);

        assertEquals("Gaming Laptop", result.getName());
        assertEquals(5000, result.getPrice());

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
    }


}

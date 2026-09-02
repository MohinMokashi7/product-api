package com.zest.product_api;

import com.zest.product_api.dto.request.CreateProductRequest;
import com.zest.product_api.dto.request.ItemRequest;
import com.zest.product_api.dto.request.UpdateProductRequest;
import com.zest.product_api.entity.Product;
import com.zest.product_api.exception.ResourceNotFoundException;
import com.zest.product_api.repository.ItemRepository;
import com.zest.product_api.repository.ProductRepository;
import com.zest.product_api.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setProductName("Test Product");
    }

    @Test
    void createProductShouldSaveProduct() {

        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("Test Product");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setQuantity(10);
        request.setItems(List.of(itemRequest));

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        var response = productService.createProduct(request, "admin");

        assertNotNull(response);
        assertEquals("Test Product", response.getProductName());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProductByIdShouldReturnProduct() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        var response = productService.getProductById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("Test Product", response.getProductName());

        verify(productRepository).findById(1);
    }

    @Test
    void getProductByIdShouldThrowWhenProductDoesNotExist() {

        when(productRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(99)
        );

        verify(productRepository).findById(99);
    }

    @Test
    void deleteProductShouldDeleteExistingProduct() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(1);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProductShouldThrowWhenProductDoesNotExist() {

        when(productRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.deleteProduct(99)
        );

        verify(productRepository, never()).delete(any());
    }
}
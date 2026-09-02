package com.zest.product_api.controller;

import com.zest.product_api.dto.request.CreateProductRequest;
import com.zest.product_api.dto.request.UpdateProductRequest;
import com.zest.product_api.dto.response.ItemResponse;
import com.zest.product_api.dto.response.ProductResponse;
import com.zest.product_api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            Authentication authentication
    ) {



        String username = authentication.getName();

        ProductResponse response =
                productService.createProduct(request, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                productService.getAllProducts(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProductRequest request,
            Authentication authentication
    ) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                productService.updateProduct(
                        id,
                        request,
                        username
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Integer id
    ) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemResponse>> getItemsByProductId(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                productService.getItemsByProductId(id)
        );
    }
}
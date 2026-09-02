package com.zest.product_api.service;

import com.zest.product_api.dto.request.CreateProductRequest;
import com.zest.product_api.dto.request.UpdateProductRequest;
import com.zest.product_api.dto.response.ItemResponse;
import com.zest.product_api.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request, String username);

    ProductResponse getProductById(Integer id);

    Page<ProductResponse> getAllProducts(int page, int size);;

    ProductResponse updateProduct(Integer id, UpdateProductRequest request, String username);

    void deleteProduct(Integer id);

    List<ItemResponse> getItemsByProductId(Integer productId);
}
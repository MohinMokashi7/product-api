package com.zest.product_api.service;

import com.zest.product_api.dto.request.CreateProductRequest;
import com.zest.product_api.dto.request.ItemRequest;
import com.zest.product_api.dto.request.UpdateProductRequest;
import com.zest.product_api.dto.response.ItemResponse;
import com.zest.product_api.dto.response.ProductResponse;
import com.zest.product_api.entity.Item;
import com.zest.product_api.entity.Product;
import com.zest.product_api.exception.ResourceNotFoundException;
import com.zest.product_api.repository.ItemRepository;
import com.zest.product_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ItemRepository itemRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request, String username) {

        Product product = new Product();

        product.setProductName(request.getProductName());
        product.setCreatedBy(username);
        product.setCreatedOn(LocalDateTime.now());

        List<Item> items = request.getItems()
                .stream()
                .map(itemRequest -> createItem(itemRequest, product))
                .toList();

        product.setItems(items);

        Product savedProduct = productRepository.save(product);

        return mapToProductResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Integer id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        return mapToProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAll(pageable)
                .map(this::mapToProductResponse);
    }

    @Override
    public ProductResponse updateProduct(
            Integer id,
            UpdateProductRequest request,
            String username
    ) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        product.setProductName(request.getProductName());
        product.setModifiedBy(username);
        product.setModifiedOn(LocalDateTime.now());

        product.getItems().clear();

        List<Item> updatedItems = request.getItems()
                .stream()
                .map(itemRequest -> createItem(itemRequest, product))
                .toList();

        product.getItems().addAll(updatedItems);

        Product updatedProduct = productRepository.save(product);

        return mapToProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + id
                        )
                );

        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getItemsByProductId(Integer productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    "Product not found with id: " + productId
            );
        }

        return itemRepository.findByProductId(productId)
                .stream()
                .map(this::mapToItemResponse)
                .toList();
    }

    private Item createItem(ItemRequest request, Product product) {

        Item item = new Item();
        item.setQuantity(request.getQuantity());
        item.setProduct(product);

        return item;
    }

    private ProductResponse mapToProductResponse(Product product) {

        List<ItemResponse> items = product.getItems()
                .stream()
                .map(this::mapToItemResponse)
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                product.getCreatedBy(),
                product.getCreatedOn(),
                product.getModifiedBy(),
                product.getModifiedOn(),
                items
        );
    }

    private ItemResponse mapToItemResponse(Item item) {

        return new ItemResponse(
                item.getId(),
                item.getQuantity()
        );
    }
}
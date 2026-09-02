package com.zest.product_api;

import com.zest.product_api.entity.Product;
import com.zest.product_api.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    @Test
    void getProductsWithoutAuthenticationShouldReturn401() throws Exception {

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void userShouldNotBeAbleToCreateProduct() throws Exception {

        String requestBody = """
            {
                "productName": "User Test Product",
                "items": [
                    {
                        "quantity": 10
                    }
                ]
            }
            """;

        mockMvc.perform(
                        post("/api/v1/products")
                                .with(user("normaluser").roles("USER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isForbidden());
    }
    @Test
    void adminShouldBeAbleToCreateProduct() throws Exception {

        String requestBody = """
            {
                "productName": "Admin Test Product",
                "items": [
                    {
                        "quantity": 10
                    }
                ]
            }
            """;

        mockMvc.perform(
                        post("/api/v1/products")
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated());
    }
    @Test
    void userShouldNotBeAbleToUpdateProduct() throws Exception {

        String requestBody = """
            {
                "productName": "User Updated Product",
                "items": [
                    {
                        "quantity": 20
                    }
                ]
            }
            """;

        mockMvc.perform(
                        put("/api/v1/products/1")
                                .with(user("normaluser").roles("USER"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isForbidden());
    }
    @Test
    void userShouldNotBeAbleToDeleteProduct() throws Exception {

        mockMvc.perform(
                        delete("/api/v1/products/1")
                                .with(user("normaluser").roles("USER"))
                )
                .andExpect(status().isForbidden());
    }
    @Test
    void adminShouldBeAbleToUpdateProduct() throws Exception {

        Product product = new Product();
        product.setProductName("Test Product");
        product.setCreatedBy("test");
        product.setCreatedOn(java.time.LocalDateTime.now());

        product = productRepository.save(product);

        String requestBody = """
        {
            "productName": "Admin Updated Product",
            "items": [
                {
                    "quantity": 25
                }
            ]
        }
        """;

        mockMvc.perform(
                        put("/api/v1/products/" + product.getId())
                                .with(user("admin").roles("ADMIN"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk());
    }
    @Test
    void adminShouldBeAbleToDeleteProduct() throws Exception {

        Product product = new Product();
        product.setProductName("Product To Delete");
        product.setCreatedBy("test");
        product.setCreatedOn(java.time.LocalDateTime.now());

        product = productRepository.save(product);

        mockMvc.perform(
                        delete("/api/v1/products/" + product.getId())
                                .with(user("admin").roles("ADMIN"))
                )
                .andExpect(status().isNoContent());
    }
}
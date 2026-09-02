package com.zest.product_api.repository;

import com.zest.product_api.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByProductId(Integer productId);
}
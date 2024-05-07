package com.givee.application.repository;

import com.givee.application.entity.Item;
import com.givee.application.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByReceipt(Receipt receipt);
}

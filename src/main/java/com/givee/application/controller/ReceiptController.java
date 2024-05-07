package com.givee.application.controller;

import com.givee.application.domain.ItemDTO;
import com.givee.application.domain.ReceiptDTO;
import com.givee.application.entity.Item;
import com.givee.application.entity.Receipt;
import com.givee.application.repository.ItemRepository;
import com.givee.application.repository.ReceiptRepository;
import com.givee.application.utils.ConvertUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptRepository receiptRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    // Create a new receipt
    @PostMapping
    public ResponseEntity<ReceiptDTO> createReceipt(@RequestBody ReceiptDTO receiptDTO) {
        // Convert ReceiptDTO to Receipt entity
        Receipt receipt = modelMapper.map(receiptDTO, Receipt.class);
        Receipt savedReceipt = receiptRepository.save(receipt);
        // Convert the saved Receipt entity back to ReceiptDTO
        ReceiptDTO savedReceiptDTO = modelMapper.map(savedReceipt, ReceiptDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReceiptDTO);
    }

    // Read all receipts
    @GetMapping
    public ResponseEntity<List<ReceiptDTO>> getAllReceipts() {
        List<Receipt> receipts = receiptRepository.findAll();
        List<ReceiptDTO> receiptDTOs = ConvertUtils.convertLists(modelMapper, receipts, ReceiptDTO.class);
        return ResponseEntity.ok(receiptDTOs);
    }

    // Read a single receipt by ID
    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDTO> getReceiptById(@PathVariable Long id) {
        Optional<Receipt> receipt = receiptRepository.findById(id);
        if (receipt.isPresent()) {
            ReceiptDTO receiptDTO = modelMapper.map(receipt.get(), ReceiptDTO.class);
            return ResponseEntity.ok(receiptDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update a receipt by ID
    @PutMapping("/{id}")
    public ResponseEntity<ReceiptDTO> updateReceipt(@PathVariable Long id, @RequestBody ReceiptDTO updatedReceipt) {
        if (receiptRepository.existsById(id)) {
            Receipt receipt = modelMapper.map(updatedReceipt, Receipt.class);
            receipt.setId(id);
            Receipt savedReceipt = receiptRepository.save(receipt);
            return ResponseEntity.ok(modelMapper.map(savedReceipt, ReceiptDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a receipt by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long id) {
        if (receiptRepository.existsById(id)) {
            receiptRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new item associated with a receipt
    @PostMapping("/{receiptId}/items")
    public ResponseEntity<List<ItemDTO>> createItemsForReceipt(@PathVariable Long receiptId, @RequestBody List<ItemDTO> items) {
        Optional<Receipt> receipt = receiptRepository.findById(receiptId);
        if (receipt.isPresent()) {
            List<Item> itemList = ConvertUtils.convertLists(modelMapper, items, Item.class);
            for (Item item : itemList) {
                item.setReceipt(receipt.get());
            }
            itemRepository.saveAll(itemList);
            return ResponseEntity.status(HttpStatus.CREATED).body(ConvertUtils.convertLists(modelMapper, items, ItemDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Read all items associated with a receipt
    @GetMapping("/{receiptId}/items")
    public ResponseEntity<List<ItemDTO>> getAllItemsForReceipt(@PathVariable Long receiptId) {
        Optional<Receipt> receipt = receiptRepository.findById(receiptId);
        if (receipt.isPresent()) {
            List<Item> items = itemRepository.findByReceipt(receipt.get());
            return ResponseEntity.ok(ConvertUtils.convertLists(modelMapper, items, ItemDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private static Specification<Receipt> filterReceipts(LocalDate startDate, LocalDate endDate, List<String> storeNames, List<String> paymentMethods) {
        return (Root<Receipt> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add filtering criteria based on provided parameters
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("receiptDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("receiptDate"), endDate));
            }
            if (storeNames != null && !storeNames.isEmpty()) {
                predicates.add(root.get("storeName").in(storeNames));
            }
            if (paymentMethods != null && !paymentMethods.isEmpty()) {
                predicates.add(root.get("paymentMethod").in(paymentMethods));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

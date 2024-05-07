package com.givee.application.controller;

import com.givee.application.domain.ItemDTO;
import com.givee.application.entity.Item;
import com.givee.application.repository.ItemRepository;
import com.givee.application.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    // Create a new item
    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
        Item item = modelMapper.map(itemDTO, Item.class);
        Item createdItem = itemRepository.save(item);
        ItemDTO createdItemDTO = modelMapper.map(createdItem, ItemDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItemDTO);
    }

    // Read all items
    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.ok(ConvertUtils.convertLists(modelMapper, items, ItemDTO.class));
    }

    // Read a single item by ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            ItemDTO itemDTO = modelMapper.map(item.get(), ItemDTO.class);
            return ResponseEntity.ok(itemDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing item by ID
    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
        if (itemRepository.existsById(id)) {
            Item item = modelMapper.map(itemDTO, Item.class);
            item.setId(id);
            Item savedItem = itemRepository.save(item);
            return ResponseEntity.ok(modelMapper.map(savedItem, ItemDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an item by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.practice.springjdbc86.controller;

import com.practice.springjdbc86.dao.ProductDao;
import com.practice.springjdbc86.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductDao productDao;

    @PutMapping("/{productId}/tags/{tagId}")
    public void addTagToProduct(@PathVariable int productId, @PathVariable int tagId) {
        productDao.addTag(productId, tagId);
    }

    @GetMapping // OK 200
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @GetMapping("/{id}") // OK 200
    public Product findById(@PathVariable int id) {
        return productDao.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // CREATED 201
    public Product create(@RequestBody Product product) {
        return productDao.create(product);
    }

    @PutMapping
    public Product update(@RequestBody Product product) {
        return productDao.update(product);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable int id) {
        productDao.removeById(id);
    }
}
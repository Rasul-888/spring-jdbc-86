package com.practice.springjdbc86.controller;

import com.practice.springjdbc86.dao.CategoryDao;
import com.practice.springjdbc86.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryDao categoryDao;

    @GetMapping
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @GetMapping("/{id}")
    public Category findById(@PathVariable int id) {
        return categoryDao.findById(id);
    }

    @PostMapping
    public Category create(Category category) {
        return categoryDao.create(category);
    }

    @PutMapping
    public Category update(Category category) {
        return categoryDao.update(category);
    }

    @DeleteMapping("/{id}")
    public void removeById(@PathVariable int id) {
        categoryDao.removeById(id);
    }
}

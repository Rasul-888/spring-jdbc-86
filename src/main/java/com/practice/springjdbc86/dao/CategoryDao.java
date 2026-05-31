package com.practice.springjdbc86.dao;

import com.practice.springjdbc86.model.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll();
    Category findById(int id);
    Category create(Category category);
    Category update(Category category);
    void removeById(int id);
}

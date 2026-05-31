package com.practice.springjdbc86.dao;
import com.practice.springjdbc86.model.Product;
import java.util.List;

public interface ProductDao {
    List<Product> findAll();
    Product findById(int id);
    Product create(Product product);
    Product update(Product product);
    void removeById(int id);
}


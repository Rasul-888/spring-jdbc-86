package com.practice.springjdbc86.dao;

import com.practice.springjdbc86.model.Category;
import com.practice.springjdbc86.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDaoImpl implements ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryDao categoryDao;

    @Override
    public List<Product> findAll() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from products");
        List<Product> list = new ArrayList<>();

        while (sqlRowSet.next()) {
            int id = sqlRowSet.getInt("id");
            String name = sqlRowSet.getString("name");
            double price = sqlRowSet.getDouble("price");
            int categoryId = sqlRowSet.getInt("category_id");

            Category category = categoryDao.findById(categoryId);

            Product product = new Product(id, name, price, category);
            list.add(product);
        }
        return list;
    }

    @Override
    public Product findById(int id) {

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(
                "select * from products where id = ?", id
        );

        if (sqlRowSet.next()) {
            String name = sqlRowSet.getString("name");
            double price = sqlRowSet.getDouble("price");
            int categoryId = sqlRowSet.getInt("category_id");
            Category category = categoryDao.findById(categoryId);
            return new Product(id, name, price, category);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Product create(Product product) {
        String sql = "insert into products (name, price, category_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            // Сообщаем PostgreSQL, что нужно вернуть сгенерированное поле "id"
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getCategory().getId());
            return ps;
        }, keyHolder);

        int generatedId = keyHolder.getKey().intValue();

        return findById(generatedId);
    }

    @Override
    public Product update(Product product) {
        String sql = "update products set name = ?, price = ?, category_id = ? where id = ?";

        jdbcTemplate.update(sql,
                product.getName(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getId()
        );

        return findById(product.getId());
    }

    @Override
    public void removeById(int id) {
        String sql = "delete from products where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
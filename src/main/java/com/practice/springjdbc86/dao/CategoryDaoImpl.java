package com.practice.springjdbc86.dao;

import com.practice.springjdbc86.dao.CategoryDao;
import com.practice.springjdbc86.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDaoImpl implements CategoryDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> findAll() { // select * from categories
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from categories");

        List<Category> list = new ArrayList<>();
        while (sqlRowSet.next()) {
            int id = sqlRowSet.getInt("id");
            String name = sqlRowSet.getString("name");
            Category category = new Category(id, name);
            list.add(category);
        }

        return list;
    }

    @Override
    public Category findById(int id) { // select * from categories where id = ?
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from categories where id = " + id);
        if (sqlRowSet.next()) {
            String name = sqlRowSet.getString("name");
            return new Category(id, name);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Category create(Category category) { // insert into categories
        return null;
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }
}
package com.practice.springjdbc86.dao;

import com.practice.springjdbc86.model.Category;
import com.practice.springjdbc86.model.Product;
import com.practice.springjdbc86.model.Tag;
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

    private static final String BASE_SELECT = """
            select p.id    as product_id,
                   p.name  as product_name,
                   p.price as product_price,
                   c.id    as category_id,
                   c.name  as category_name
              from products p
              join categories c on p.category_id = c.id
            """;


    private List<Tag> findTagsByProductId(int productId) {
        String sql = """
                select t.id, t.name 
                  from tags t
                  join product_tags pt on t.id = pt.tag_id
                 where pt.product_id = ?
                """;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, productId);
        List<Tag> tags = new ArrayList<>();
        while (rowSet.next()) {
            tags.add(new Tag(rowSet.getInt("id"),
                    rowSet.getString("name")));
        }
        return tags;
    }

    @Override
    public List<Product> findAll() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(BASE_SELECT);
        List<Product> list = new ArrayList<>();

        while (sqlRowSet.next()) {
            int productId = sqlRowSet.getInt("product_id");
            String productName = sqlRowSet.getString("product_name");
            double productPrice = sqlRowSet.getDouble("product_price");
            int categoryId = sqlRowSet.getInt("category_id");
            String categoryName = sqlRowSet.getString("category_name");

            Category category = new Category(categoryId, categoryName);


            List<Tag> tags = findTagsByProductId(productId);

            Product product = new Product(productId, productName, productPrice, category, tags);
            list.add(product);
        }
        return list;
    }

    @Override
    public Product findById(int id) {
        String sql = BASE_SELECT + " where p.id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);

        if (sqlRowSet.next()) {
            int productId = sqlRowSet.getInt("product_id");
            String productName = sqlRowSet.getString("product_name");
            double productPrice = sqlRowSet.getDouble("product_price");
            int categoryId = sqlRowSet.getInt("category_id");
            String categoryName = sqlRowSet.getString("category_name");
            Category category = new Category(categoryId, categoryName);
            List<Tag> tags = findTagsByProductId(productId);
            return new Product(productId, productName, productPrice, category, tags);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Product create(Product product) {
        String sql = "insert into products (name, price, category_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
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
    public void addTag(int productId, int tagId) {
        String sql = "insert into product_tags (product_id, tag_id) values (?, ?)";
        jdbcTemplate.update(sql, productId, tagId);
    }

    @Override
    public void removeById(int id) {
        String sql = "delete from products where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
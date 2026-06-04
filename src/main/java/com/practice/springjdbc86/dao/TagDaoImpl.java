import com.practice.springjdbc86.dao.TagDao;
import com.practice.springjdbc86.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Tag> findAll() {
        String sql = "select * from tags";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Tag findById(int id) {
        String sql = "select * from tags where id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Tag.class), id);
    }
}
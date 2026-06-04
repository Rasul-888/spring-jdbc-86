import com.practice.springjdbc86.dao.TagDao;
import com.practice.springjdbc86.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagDao tagDao;

    @GetMapping
    public List<Tag> findAll() {
        return tagDao.findAll();
    }

    @GetMapping("/{id}")
    public Tag findById(@PathVariable int id) {
        return tagDao.findById(id);
    }
}
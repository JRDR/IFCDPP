package repo;

import models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface PostRepository extends CrudRepository<Post, Long> {
}

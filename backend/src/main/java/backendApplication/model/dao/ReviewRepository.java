package backendApplication.model.dao;

import backendApplication.model.entities.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Integer> {

    Review findByToken(String token);

}

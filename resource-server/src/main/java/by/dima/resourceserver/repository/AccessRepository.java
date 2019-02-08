package by.dima.resourceserver.repository;

import by.dima.resourceserver.model.DocumentAccess;
import by.dima.resourceserver.model.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccessRepository extends CrudRepository<DocumentAccess, Long> {
    Optional<DocumentAccess> findByDepartmentAndPost(String department, String post);
}

package by.dima.resourceserver.repository;

import by.dima.resourceserver.model.Document;
import by.dima.resourceserver.model.DocumentAccess;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends CrudRepository<Document, Long> {
    List<Document> findAllByCreatorAndArchived(String creator, boolean archived);

    @Query("select d from Document d where (d.creator = ?1 or ?2 member d.accesses) and d.archived=?3")
    List<Document> findAllByCreatorOrAccessesContainsAndArchived(String username, DocumentAccess access, boolean archived);
    List<Document> findAllByNeedSignListNotNull();

    Optional<Document> findByName(String username);
    Optional<Document> findByIdAndNeedSignListContains(Long id, String username);
}

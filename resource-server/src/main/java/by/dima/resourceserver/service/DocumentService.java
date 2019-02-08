package by.dima.resourceserver.service;

import by.dima.resourceserver.model.Document;
import by.dima.resourceserver.model.User;

import java.util.Collection;
import java.util.List;

public interface DocumentService {
    void save(Document document, User user);
    void delete(Long id, User user);
    void archive(Long id, User user);
    void sign(Long id, User user);

    Document get(Long id, User user);
    Document get(String name, User user);

    List<Document> getAll(String username);
    List<Document> getActive(User user);
    List<Document> getArchived(User user);
    Collection<Document> getSign(User user);
}

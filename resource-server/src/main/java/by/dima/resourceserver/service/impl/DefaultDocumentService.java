package by.dima.resourceserver.service.impl;

import by.dima.resourceserver.model.Document;
import by.dima.resourceserver.model.User;
import by.dima.resourceserver.repository.DocumentRepository;
import by.dima.resourceserver.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultDocumentService implements DocumentService {
    private DocumentRepository documentRepository;
    private CheckAccessService checkAccessService;

    @Autowired
    public DefaultDocumentService(DocumentRepository documentRepository, CheckAccessService checkAccessService) {
        this.documentRepository = documentRepository;
        this.checkAccessService = checkAccessService;
    }

    @Override
    public void save(Document document, User user) {
        if (document.getNeedSignList() == null || document.getNeedSignList().size() == 0) {
            document.setAcceptDate(new Date());
        } else {
            document.setAcceptDate(null);
        }
        document.setCreator(user.getUsername());
        documentRepository.save(document);
    }

    @Override
    public void delete(Long id, User user) {
        documentRepository.findAllByCreatorAndArchived(user.getUsername(), false)
                .stream()
                .filter(document -> document.getId() == id)
                .findAny()
                .ifPresent(document -> documentRepository.delete(document));
    }

    @Override
    public void archive(Long id, User user) {
        documentRepository.findById(id).ifPresent(document -> {
            if (Objects.equals(document.getCreator(), user.getUsername())) {
                if (document.getAcceptDate() != null) {
                    document.setValidTo(new Date());
                }
                document.setArchived(true);
                documentRepository.save(document);
            }
        });
    }

    @Override
    public void sign(Long id, User user) {
        documentRepository.findById(id)
                .ifPresent(document -> {
                    List<User> signList = document.getSignList();
                    List<User> needSignList = document.getNeedSignList();
                    if (needSignList.contains(user) &&
                            !signList.contains(user)) {
                        signList.add(user);
                        if (signList.size() == document.getNeedSignList().size()) {
                            document.setAcceptDate(new Date());
                        }
                        documentRepository.save(document);
                    }
                });
    }

    @Override
    public Document get(Long id, User user) {
        return getByUser(documentRepository.findById(id), user);
    }

    @Override
    public Document get(String name, User user) {
        return getByUser(documentRepository.findByName(name), user);
    }

    private Document getByUser(Optional<Document> optionalDocument, User user) {
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            if (document.getCreator().equals(user.getUsername()) || checkAccessService.checkAccess(document, user)) {
                return document;
            }
        }

        return null;
    }

    @Override
    public List<Document> getAll(String username) {
        return documentRepository.findAllByCreatorAndArchived(username, false);
    }

    public List<Document> getByUserAndArchived(User user, boolean archived) {
        Set<Document> documents = new HashSet<>();
        user.getAccesses().forEach(access -> documents.addAll(documentRepository.findAllByCreatorOrAccessesContainsAndArchived(user.getUsername(), access, archived)));
        return new LinkedList<>(documents);
    }

    @Override
    public List<Document> getActive(User user) {
        return getByUserAndArchived(user, false);
    }

    @Override
    public List<Document> getArchived(User user) {
        return getByUserAndArchived(user, true);
    }

    @Override
    public Collection<Document> getSign(User user) {
        List<Document> documents = documentRepository.findAllByNeedSignListNotNull();

        return documents.stream().filter(document -> document.getNeedSignList().contains(user)).collect(Collectors.toSet());
    }
}

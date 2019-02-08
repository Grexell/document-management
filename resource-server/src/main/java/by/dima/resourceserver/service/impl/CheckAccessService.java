package by.dima.resourceserver.service.impl;

import by.dima.resourceserver.model.Document;
import by.dima.resourceserver.model.DocumentAccess;
import by.dima.resourceserver.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckAccessService {
    public boolean checkAccess(Document document, User user) {
        return checkAccess(document.getAccesses(), user.getAccesses());
    }

    public boolean checkAccess(List<DocumentAccess> destination, List<DocumentAccess> target) {
        return target.stream().anyMatch(destination::contains);
    }

}

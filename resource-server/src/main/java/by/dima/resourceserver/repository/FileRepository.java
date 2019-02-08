package by.dima.resourceserver.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {
    boolean exist(String filename);
    void create(MultipartFile file);
    void delete(String filename);
    Resource get(String filename);
}

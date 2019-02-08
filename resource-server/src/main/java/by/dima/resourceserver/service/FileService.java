package by.dima.resourceserver.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    boolean exist(String filename);
    void create(MultipartFile file);
    void delete(String filename);
    Resource get(String filename);
}

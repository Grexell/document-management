package by.dima.resourceserver.service.impl;

import by.dima.resourceserver.repository.FileRepository;
import by.dima.resourceserver.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DefaultFileService implements FileService {
    private FileRepository fileRepository;

    @Autowired
    public DefaultFileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public boolean exist(String filename) {
        return fileRepository.exist(filename);
    }

    @Override
    public void create(MultipartFile file) {
        fileRepository.create(file);
    }

    @Override
    public void delete(String filename) {
        fileRepository.delete(filename);
    }

    @Override
    public Resource get(String filename) {
        return fileRepository.get(filename);
    }
}

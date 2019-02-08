package by.dima.resourceserver.repository.impl;

import by.dima.resourceserver.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Repository
public class FileSystemRepository implements FileRepository {

    private Path fileStorageLocation;

    @Autowired
    public FileSystemRepository(@Value("${file.storage.location}") String fileStorageName) {
        this.fileStorageLocation = Paths.get(fileStorageName).toAbsolutePath().normalize();
    }

    @Override
    public boolean exist(String filename) {
        return fileStorageLocation.resolve(filename).toFile().exists();
    }

    @Override
    public void create(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetPath = fileStorageLocation.resolve(filename);
        try {
            targetPath.toFile().mkdirs();
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String filename) {
        Path targetPath = fileStorageLocation.resolve(filename);
        try {
            Files.delete(targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Resource get(String filename) {
        Path targetPath = fileStorageLocation.resolve(filename);

        try {
            return new UrlResource(targetPath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}

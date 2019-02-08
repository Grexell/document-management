package by.dima.resourceserver.controller;

import by.dima.resourceserver.model.User;
import by.dima.resourceserver.service.DocumentService;
import by.dima.resourceserver.service.FileService;
import by.dima.resourceserver.service.impl.UserExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    private FileService fileService;
    private DocumentService documentService;

    @Autowired
    public FileController(FileService fileService,  DocumentService documentService) {
        this.fileService = fileService;
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, Authentication authentication) {
        String documentId = file.getOriginalFilename();
        if (fileService.exist(file.getOriginalFilename()) && documentService.get(documentId, UserExtractor.extract(authentication)) == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        fileService.create(file);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{filename:.+}")
    public ResponseEntity deleteFile(@PathVariable String filename, Authentication authentication) {
        if (chckUser(filename, authentication)) return new ResponseEntity(HttpStatus.FORBIDDEN);
        fileService.delete(filename);
        return new ResponseEntity(HttpStatus.OK);
    }

    private boolean chckUser(String filename, Authentication authentication) {
        return documentService.get(filename, UserExtractor.extract(authentication)) == null;
    }

    @GetMapping("/{docName}/{filename:.+}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getFile(@PathVariable String docName, @PathVariable String filename, Authentication authentication, HttpServletRequest request) {
        filename = docName + "/" + filename;
        if (chckUser(filename, authentication)) return new ResponseEntity(HttpStatus.FORBIDDEN);
        String contentType = null;
        Resource resource = fileService.get(filename);
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

package com.exercise.fileupload.controller;

import com.exercise.fileupload.repository.FileDto;
import com.exercise.fileupload.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private FileService fileService;

    @Autowired
    FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/files")
    public ResponseEntity<FileDto> uploadFile(@ModelAttribute FileDto file) throws Exception {
        LOGGER.info("name: {}", file.getNamespace());
        LOGGER.info("file: {}", file.getFile().getOriginalFilename());

        return ResponseEntity.ok(fileService.create(file));
    }

    @GetMapping(value = "/files/{id}")
    public ResponseEntity<FileDto> getFileMetaInfo(@PathVariable UUID id) throws Exception {
        LOGGER.info("Getting meta info from");
        LOGGER.info("id: {}", id);

        FileDto file = fileService.get(id);

        return ResponseEntity.ok(file);
    }
}

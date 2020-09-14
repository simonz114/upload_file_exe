package com.exercise.fileupload.service;

import com.exercise.fileupload.repository.FileDto;
import com.exercise.fileupload.repository.MetaInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Value("upload.root.directory")
    private String root_dir;

    private MetaInfoRepository metaInfoRepository;

    @Autowired
    FileServiceImpl(MetaInfoRepository metaInfoRepository) {
        this.metaInfoRepository = metaInfoRepository;
    }

    @Override
    public FileDto get(UUID id) throws Exception {
        Optional<FileDto> fileById = metaInfoRepository.findById(id);
        return fileById.orElseThrow(() -> new Exception("file not found"));
    }

    @Override
    public FileDto create(FileDto file) throws Exception {
        storeFile(file);
        return metaInfoRepository.save(file);
    }

    private void storeFile(FileDto file) throws Exception {
        try {
            Path rootLocation = Paths.get(root_dir + '/' + file.getNamespace() + '/' + file.getPath());
            MultipartFile uploadFile = file.getFile();
            String filename = StringUtils.cleanPath(uploadFile.getOriginalFilename());
            if (uploadFile.isEmpty()) {
                throw new IOException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new IOException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = uploadFile.getInputStream()) {
                Files.createDirectories(rootLocation);
                Files.copy(inputStream, rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new Exception("Failed to store file.", e);
        }
    }
}

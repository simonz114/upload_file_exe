package com.exercise.fileupload.service;

import com.exercise.fileupload.repository.FileDto;

import java.util.UUID;

public interface FileService {
    FileDto get(UUID id) throws Exception;
    FileDto create(FileDto file) throws Exception;
}

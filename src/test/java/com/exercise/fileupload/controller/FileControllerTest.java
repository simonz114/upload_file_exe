package com.exercise.fileupload.controller;

import com.exercise.fileupload.repository.FileDto;
import com.exercise.fileupload.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileControllerTest {

    @Mock
    private FileService fileService;

    private FileController fileController;
    private  FileDto mockFile;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        fileController = new FileController(fileService);

        mockFile = new FileDto();
        mockFile.setId(UUID.randomUUID());
        mockFile.setNamespace("mock");
        mockFile.setPath("test/unit/");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("testfile", "testfile.txt", "text", new byte[]{'t', 'e', 's', 't', 'f', 'i', 'l', 'e'});
        mockFile.setFile(mockMultipartFile);
    }

    @Test
    void uploadFileWithFileDto_ExpectSuccess() throws Exception {
        Mockito.when(fileService.create(mockFile)).thenReturn(mockFile);

        ResponseEntity<FileDto> output = assertDoesNotThrow(() -> fileController.uploadFile(mockFile));
        assertEquals(output, ResponseEntity.ok(mockFile));
    }

    @Test
    void uploadFileWithInvalidFileDto_ExpectException() throws Exception {
        Mockito.when(fileService.create(mockFile)).thenThrow(new Exception("Failed to store file."));

        Exception e = assertThrows(Exception.class, () -> fileController.uploadFile(mockFile));
        assertEquals(e.getMessage(), "Failed to store file.");
    }

    @Test
    void getFileMetaInfoWithId_ExpectSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(fileService.get(id)).thenReturn(mockFile);

        ResponseEntity<FileDto> output = assertDoesNotThrow(() -> fileController.getFileMetaInfo(id));
        assertEquals(output, ResponseEntity.ok(mockFile));
    }

    @Test
    void getFileMetaInfoWithInvalidId_ExpectSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(fileService.get(id)).thenThrow(new Exception("Invalid input id."));

        Exception e = assertThrows(Exception.class, () -> fileController.getFileMetaInfo(id));
        assertEquals(e.getMessage(), "Invalid input id.");
    }
}
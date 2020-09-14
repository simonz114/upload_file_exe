package com.exercise.fileupload.service;

import com.exercise.fileupload.repository.FileDto;
import com.exercise.fileupload.repository.MetaInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceImplTest {

    @Mock
    private MetaInfoRepository metaInfoRepository;

    private FileServiceImpl fileService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fileService = new FileServiceImpl(metaInfoRepository);
    }

    @Test
    void getWithExistingId_ExpectSuccess() {
        UUID id = UUID.randomUUID();
        FileDto mockFile = new FileDto();

        Mockito.when(metaInfoRepository.findById(id)).thenReturn(Optional.of(mockFile));

        FileDto output = assertDoesNotThrow(() -> fileService.get(id));
        assertEquals(mockFile, output);
    }

    @Test
    void getWithNoneExistingId_ExpectException() {
        UUID id = UUID.randomUUID();
        Mockito.when(metaInfoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> fileService.get(id));
    }

    @Test
    void createWithValidFile_ExpectSuccess() {
        FileDto mockFile = new FileDto();
        mockFile.setId(UUID.randomUUID());
        mockFile.setNamespace("mock");
        mockFile.setPath("test/unit/");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("testfile", "testfile.txt", "text", new byte[]{'t', 'e', 's', 't', 'f', 'i', 'l', 'e'});
        mockFile.setFile(mockMultipartFile);
        Mockito.when(metaInfoRepository.save(mockFile)).thenReturn(mockFile);

        FileDto output = assertDoesNotThrow(() -> fileService.create(mockFile));
        assertEquals(mockFile, output);
    }

    @Test
    void createWithEmptyFile_ExpectException() {
        FileDto mockFile = new FileDto();
        mockFile.setId(UUID.randomUUID());
        mockFile.setNamespace("mock");
        mockFile.setPath("test/unit/");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("testfile", "testfile.txt", "text", new byte[]{});
        mockFile.setFile(mockMultipartFile);
        Mockito.when(metaInfoRepository.save(mockFile)).thenReturn(mockFile);

        Exception e = assertThrows(Exception.class, () -> fileService.create(mockFile));
        assertEquals("Failed to store file.", e.getMessage());
    }

    @Test
    void createWithInvalidFileName_ExpectException() {
        FileDto mockFile = new FileDto();
        mockFile.setId(UUID.randomUUID());
        mockFile.setNamespace("mock");
        mockFile.setPath("test/unit/");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("testfile", "../repo/testfile.txt", "text", new byte[]{});
        mockFile.setFile(mockMultipartFile);
        Mockito.when(metaInfoRepository.save(mockFile)).thenReturn(mockFile);

        Exception e = assertThrows(Exception.class, () -> fileService.create(mockFile));
        assertEquals("Failed to store file.", e.getMessage());
    }
}
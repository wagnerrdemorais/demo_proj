package com.demo.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FileServiceTest {
    FileService fileService;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setup() {
        fileService = new FileService();
        ReflectionTestUtils.setField(fileService, "testFolderPath", tempDir.toString()+"/");
    }

    @Test
    public void givenValidFile_whenExecuteGetNewFile_thenShouldCreateFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.zip",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());

        File newFile = fileService.getNewFile(file);
        Assertions.assertEquals("hello.zip", newFile.getName());
    }

    @Test
    public void givenNullFile_whenCallGetNewFile_shoudThrowException(){
        assertThrows(NullPointerException.class, ()-> fileService.getNewFile(null));
    }

//    @Test
//    public void testChecksumGenerator(){
//        File file =new File(tempDir.toString() + "/teste.Zip");
//        Files.write(tempDir.toString() + "/teste.Zip");
//
//    }

}
package com.accountify.demo.service;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

@Service
public class FileService {

    @Value("${default.test.folder}")
    private String testFolderPath;

    public void zipTest(String file, String name) throws ZipException {
        String zipPath = testFolderPath + name + ".zip";
        new ZipFile(zipPath).addFile(new File(file));
    }

    public File getNewFile(MultipartFile file) throws IOException {
        File newFile = new File(testFolderPath + file.getOriginalFilename());
        file.transferTo(newFile);
        newFile.createNewFile();
        return newFile;
    }

    public ProgressMonitor zipSplitFileWithProgress(MultipartFile file) throws IOException {
        File newFile = new File(testFolderPath+"tmp/" + file.getOriginalFilename());
        file.transferTo(newFile);

        ZipFile zipFile = new ZipFile(new File(testFolderPath+"test.zip"));
        zipFile.setRunInThread(true);

        ProgressMonitor progressMonitor = zipFile.getProgressMonitor();

        zipFile.createSplitZipFile(List.of(newFile), new ZipParameters(), true, 1000000);

        return progressMonitor;
    }

    public Long checksumGenerator(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        Checksum crc32 = new CRC32();
        crc32.update(bytes, 0, bytes.length);
        return crc32.getValue();
    }
}

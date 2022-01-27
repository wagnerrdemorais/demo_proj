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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
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

    public void createFile(List<String> queryReturn, String format) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.now();

        String fileName = testFolderPath + "queryResult" + localDateTime.getNano() + format;
        Path pathTo = Paths.get(fileName);

        boolean exists = Files.exists(pathTo);
        if (!exists) {
            Files.write(pathTo, "".getBytes(), StandardOpenOption.CREATE);
        }

        queryReturn.forEach(ret -> {
            try {
                Files.write(pathTo, (ret+", ").getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<String> listFilesPath() throws IOException {
        List<String> pathList = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(testFolderPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        String stringPath = path.toString();
                        pathList.add(stringPath);
                    });
        }
        return pathList;
    }
}

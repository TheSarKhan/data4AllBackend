package org.example.dataprotal.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final Path storageDirectory;
    private final Path root = Paths.get("data");

    public FileService() throws IOException {
        this.storageDirectory = Paths.get("data");
        if (!Files.exists(storageDirectory)) {
            Files.createDirectories(storageDirectory);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Boş fayl  yüklənə bilməz.");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IOException("Fayl uzantısı tapılmadı.");
        }

        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueName = timestamp + "_" + UUID.randomUUID() + fileExtension;

        Path destination = this.storageDirectory.resolve(uniqueName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueName;
    }

    public Resource getFile(String storedName) throws MalformedURLException {
        Path filePath = root.resolve(storedName);
        return new UrlResource(filePath.toUri());
    }

    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.storageDirectory.resolve(fileName);
            boolean deleted = Files.deleteIfExists(filePath);
            System.out.println("Silindi mi? " + fileName + ": " + deleted);
            return deleted;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
package study.mf.uploadAndDownload.storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.mf.uploadAndDownload.config.StorageConfig;
import study.mf.uploadAndDownload.storage.dto.FileResponseDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

@Service
public class StorageService {

    private final Path folderPath;

    public StorageService(StorageConfig storageConfig) {
        folderPath = Path.of(storageConfig.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(folderPath);
        } catch (Exception ex) {
            throw new RuntimeException("Sorry, folder could not be created.");
        }
    }

    public FileResponseDto upload(MultipartFile file){
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File cannot be null or empty");
        }

        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() :
                String.format("%d-file", Instant.now().toEpochMilli());

        fileName = fileName.contains("..") ? fileName.replace("..", ".") :
                fileName;

        Path filePath = folderPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return new FileResponseDto(fileName, file.getSize() + "B");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

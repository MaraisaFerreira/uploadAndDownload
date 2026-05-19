package study.mf.uploadAndDownload.storage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import study.mf.uploadAndDownload.storage.dto.FileResponseDto;
import study.mf.uploadAndDownload.storage.service.StorageService;

@RestController
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload/file")
    public ResponseEntity<FileResponseDto> uploadFile(@RequestParam(name = "file")MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                storageService.upload(file)
        );
    }
}

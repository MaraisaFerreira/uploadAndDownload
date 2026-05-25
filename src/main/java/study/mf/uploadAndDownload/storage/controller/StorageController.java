package study.mf.uploadAndDownload.storage.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.mf.uploadAndDownload.storage.dto.FileResponseDto;
import study.mf.uploadAndDownload.storage.service.StorageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/upload/files")
    public ResponseEntity<List<FileResponseDto>> uploadFiles(@RequestParam(name = "files")MultipartFile[] files){
        List<FileResponseDto> uploads = new ArrayList<>();

        for (MultipartFile file : files) {
            uploads.add(storageService.upload(file));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(uploads);
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
        Resource resource = storageService.downloadFile(fileName);
        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new RuntimeException("mime-type cannot be defined");
        }

        if (contentType == null) contentType = "appication/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\""
                        + resource.getFilename() +"\"").body(resource);

    }
}

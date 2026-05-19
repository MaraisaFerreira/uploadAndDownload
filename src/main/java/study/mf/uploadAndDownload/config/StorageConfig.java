package study.mf.uploadAndDownload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${api.storage.dir}")
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }
}

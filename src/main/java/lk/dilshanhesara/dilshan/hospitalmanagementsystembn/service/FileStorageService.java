package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory for uploads.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // 1. Original file name එක ලබාගන්නවා
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            // 2. File extension එක වෙන් කරලා ගන්නවා (උදා: ".png")
            String fileExtension = "";
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }

            // 3. අලුත් unique නම හදද්දී extension එකත් එකතු කරනවා
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // 4. File එක save කරනවා
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 5. Frontend එකට පාවිච්චි කරන්න පුළුවන් path එක return කරනවා
            return "/uploads/" + newFileName;

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName, ex);
        }
    }
}
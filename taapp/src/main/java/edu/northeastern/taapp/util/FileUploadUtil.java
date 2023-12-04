package edu.northeastern.taapp.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
	
	public static boolean isPDF(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("application/pdf");
    }

    public static boolean isJPEG(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("image/jpeg");
    }

    public static String saveFile(MultipartFile file, String nuid, String type) {
        try {
            String uploadDir = "/Users/sho7/Documents/Enterprise_Software_Design/Project/Files/" + type;
            String extension;
            if(type.equalsIgnoreCase("Photo")) {
            	extension = ".jpg";
            } else {
            	extension = ".pdf";
            }
            String fileName = nuid + extension;
            Path uploadPath = Path.of(uploadDir);
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving file: " + e.getMessage());
        }
    }
}


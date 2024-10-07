package e_com.bichitra.e_com02092024.service.impl;

import e_com.bichitra.e_com02092024.service.ImageUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageUpdateServiceImpl implements ImageUpdateService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // File names of current / original file
        String originalFilename = file.getOriginalFilename();
        // Generate a unique file name
        var randomId = UUID.randomUUID().toString();

        assert originalFilename != null;
        // mat.jpg --> 1234 --> 1234.jpg
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf('.')));
        //appending file path
        String fullFilePath=path + File.separator + fileName;

        // Check if path exist and create
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdir();

        // Upload to server
        Files.copy(file.getInputStream(), Paths.get(fullFilePath));

        return fileName;
    }
}

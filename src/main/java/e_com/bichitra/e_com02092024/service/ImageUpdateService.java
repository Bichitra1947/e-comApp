package e_com.bichitra.e_com02092024.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUpdateService {
    String uploadImage(String path, MultipartFile file) throws IOException;
}

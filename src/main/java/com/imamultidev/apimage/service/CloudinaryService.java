package com.imamultidev.apimage.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.imamultidev.apimage.model.Imagen;
import com.imamultidev.apimage.repository.ImagenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final ImagenRepository imagenRepository;

    // Subir imagen a Cloudinary y guardar URL en PostgreSQL
    public String uploadImage(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(), 
                ObjectUtils.asMap(
                    "folder", "game-images",
                    "resource_type", "auto"
                )
            );
            String url = uploadResult.get("url").toString();

            Imagen imagen = new Imagen();
            imagen.setUrl(url);
            imagenRepository.save(imagen);

            log.info("Imagen subida exitosamente: {}", url);
            return url;
        } catch (IOException e) {
            log.error("Error al subir la imagen: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar la imagen: " + e.getMessage());
        }
    }

    // Descargar imagen usando la URL almacenada
    public byte[] downloadImage(String publicUrl) throws IOException {
        try {
            URL url = URI.create(publicUrl).toURL();
            try (InputStream in = url.openStream()) {
                return in.readAllBytes();
            }
        } catch (IOException e) {
            log.error("Error al descargar la imagen: {}", e.getMessage(), e);
            throw new IOException("Error al descargar la imagen: " + e.getMessage());
        }
    }
}
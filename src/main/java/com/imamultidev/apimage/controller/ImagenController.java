package com.imamultidev.apimage.controller;

import com.imamultidev.apimage.model.Imagen;
import com.imamultidev.apimage.repository.ImagenRepository;
import com.imamultidev.apimage.service.CloudinaryService;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImagenController {

    private final CloudinaryService cloudinaryService;
    private final ImagenRepository imagenRepository;

    // Subir imagen
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(imageUrl);
    }

    // Obtener todas las URLs
    @GetMapping
    public ResponseEntity<List<Imagen>> getAllImages() {
        return ResponseEntity.ok(imagenRepository.findAll());
    }

    // Descargar imagen por ID
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) throws IOException {
        Imagen imagen = imagenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        byte[] imageBytes = cloudinaryService.downloadImage(imagen.getUrl());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
}
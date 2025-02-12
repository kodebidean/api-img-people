package com.imamultidev.apimage.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data // Genera getters, setters, toString, etc.
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los campos
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
}
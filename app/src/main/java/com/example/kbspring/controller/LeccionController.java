/*
package com.example.kbspring.controller;

import com.example.kidzbraindb.dto.LeccionDto;
import com.example.kidzbraindb.model.Leccion;
import com.example.kidzbraindb.service.LeccionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/lecciones")
@RestController
@AllArgsConstructor
public class LeccionController {
    private final LeccionService leccionService;

    @GetMapping
    public ResponseEntity<List<LeccionDto>> getAll() {
        List<Leccion> lecciones = leccionService.findAll();
        List<LeccionDto> dtos = lecciones.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private LeccionDto convertirAEntidadDTO(Leccion leccion) {
        return LeccionDto.builder()
                .leccionId(leccion.getId())
                .materiaId(leccion.getMateria().getId())
                .gradoId(leccion.getGrado().getId())
                .nombre(leccion.getNombre())
                .descripcion(leccion.getDescripcion())
                .build();
    }
}
*/
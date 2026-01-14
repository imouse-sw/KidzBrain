/*
package com.example.kbspring.controller;

import com.example.kidzbraindb.dto.MateriaDto;
import com.example.kidzbraindb.model.Materia;
import com.example.kidzbraindb.service.MateriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/materias")
@RestController
@AllArgsConstructor
public class MateriaController {
    private final MateriaService materiaService;

    @GetMapping
    public ResponseEntity<List<MateriaDto>> getAll() {
        List<Materia> materias = materiaService.findAll();
        List<MateriaDto> dtos = materias.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private MateriaDto convertirAEntidadDTO(Materia materia) {
        return MateriaDto.builder()
                .materiaId(materia.getId())
                .nombre(materia.getNombre())
                .build();
    }
}
*/
/*
package com.example.kbspring.controller;

import com.example.kidzbraindb.dto.JuegoDto;
import com.example.kidzbraindb.model.Juego;
import com.example.kidzbraindb.service.JuegoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/juegos")
@RestController
@AllArgsConstructor
public class JuegoController {
    private final JuegoService juegoService;

    @GetMapping
    public ResponseEntity<List<JuegoDto>> getAll() {
        List<Juego> juegos = juegoService.findAll();
        List<JuegoDto> dtos = juegos.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JuegoDto> getById(@PathVariable Integer id) {
        Juego juego = juegoService.findById(id);
        if (juego == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAEntidadDTO(juego));
    }

    private JuegoDto convertirAEntidadDTO(Juego juego) {
        return JuegoDto.builder()
                .juegoId(juego.getId())
                .leccionId(juego.getLeccion().getId())
                .nombre(juego.getNombre())
                .tipo(juego.getTipo())
                .build();
    }
}
*/
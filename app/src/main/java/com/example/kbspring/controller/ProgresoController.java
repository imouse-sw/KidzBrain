/*
package com.example.kbspring.controller;

import com.example.kidzbraindb.dto.ProgresoDto;
import com.example.kidzbraindb.model.Juego;
import com.example.kidzbraindb.model.Progreso;
import com.example.kidzbraindb.model.Usuario;
import com.example.kidzbraindb.service.JuegoService;
import com.example.kidzbraindb.service.ProgresoService;
import com.example.kidzbraindb.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/progreso")
@RestController
@AllArgsConstructor
public class ProgresoController {

    private final ProgresoService progresoService;
    private final UsuarioService usuarioService;
    private final JuegoService juegoService;

    @GetMapping
    public ResponseEntity<List<ProgresoDto>> getAll() {
        List<Progreso> progresos = progresoService.getAll();
        List<ProgresoDto> dtos = progresos.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProgresoDto> getById(@PathVariable Integer id) {
        Progreso progreso = progresoService.getById(id);
        if (progreso == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAEntidadDTO(progreso));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ProgresoDto>> getByUsuario(@PathVariable Integer idUsuario) {
        List<Progreso> progresos = progresoService.getByUsuario(idUsuario);
        List<ProgresoDto> dtos = progresos.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/puntuacion/usuario/{idUsuario}/materia/{idMateria}")
    public ResponseEntity<Integer> sumByMateria(@PathVariable Integer idUsuario, @PathVariable Integer idMateria) {
        Integer suma = progresoService.sumByMateria(idUsuario, idMateria);
        return ResponseEntity.ok(suma);
    }

    @PostMapping
    public ResponseEntity<ProgresoDto> save(@RequestBody ProgresoDto dto) {
        Progreso progresoParaGuardar = convertirAEntidad(dto);
        Progreso progresoGuardado = progresoService.save(progresoParaGuardar);
        return ResponseEntity.ok(convertirAEntidadDTO(progresoGuardado));
    }

    private ProgresoDto convertirAEntidadDTO(Progreso progreso) {
        return ProgresoDto.builder()
                .progresoId(progreso.getId())
                .usuarioId(progreso.getUsuario().getId())
                .juegoId(progreso.getJuego().getId())
                .completado(progreso.getCompletado())
                .puntuacion(progreso.getPuntuacion())
                .intentos(progreso.getIntentos())
                .fecha(progreso.getFecha())
                .build();
    }

    private Progreso convertirAEntidad(ProgresoDto dto) {
        Usuario usuario = usuarioService.getById(dto.getUsuarioId());
        Juego juego = juegoService.getById(dto.getJuegoId());

        return Progreso.builder()
                .id(dto.getProgresoId())
                .usuario(usuario)
                .juego(juego)
                .completado(dto.getCompletado())
                .puntuacion(dto.getPuntuacion())
                .intentos(dto.getIntentos())
                .fecha(dto.getFecha())
                .build();
    }
}
*/
/*
package com.example.kbspring.controller; // Asegúrate de que sea tu paquete 'controller'
import com.example.kidzbraindb.dto.GradoDto;
import com.example.kidzbraindb.model.Grado;
import com.example.kidzbraindb.service.GradoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/grados")
@RestController
@AllArgsConstructor
public class GradoController {

    private final GradoService gradoService;

    @GetMapping
    public ResponseEntity<List<GradoDto>> getAll() {
        List<Grado> grados = gradoService.getAll();

        List<GradoDto> dtos = grados.stream()
                .map(this::convertirAEntidadDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<GradoDto> getById(@PathVariable Integer id) {
        Grado grado = gradoService.getById(id);
        if (grado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirAEntidadDTO(grado));
    }

    @PostMapping
    public ResponseEntity<GradoDto> save(@RequestBody GradoDto dto) {
        Grado gradoParaGuardar = convertirAEntidad(dto);

        Grado gradoGuardado = gradoService.save(gradoParaGuardar);

        return ResponseEntity.ok(convertirAEntidadDTO(gradoGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradoDto> update(@PathVariable Integer id, @RequestBody GradoDto dto) {
        Grado gradoParaActualizar = convertirAEntidad(dto);

        Grado gradoActualizado = gradoService.update(id, gradoParaActualizar);

        return ResponseEntity.ok(convertirAEntidadDTO(gradoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        gradoService.delete(id);
        return ResponseEntity.noContent().build();
    }


    private GradoDto convertirAEntidadDTO(Grado grado) {
        return GradoDto.builder()
                .gradoId(grado.getId())
                .nombre(grado.getNombre())
                .rangoEdad(grado.getRango_edad())
                .build();
    }

    private Grado convertirAEntidad(GradoDto dto) {
        return Grado.builder()
                .id(dto.getGradoId())
                .nombre(dto.getNombre())
                .rango_edad(dto.getRangoEdad())
                .build();
    }
}
*/
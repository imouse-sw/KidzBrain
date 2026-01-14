/*
package com.example.kbspring.controller;

import com.example.kidzbraindb.dto.UsuarioDto;
import com.example.kidzbraindb.model.Usuario;
import com.example.kidzbraindb.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/KidzBrain/api/usuarios")
@RestController
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private List<UsuarioDto> usuarioDtos;

    public void loadList() {
        usuarioDtos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            usuarioDtos.add(
                    UsuarioDto.builder()
                            .usuarioId(i++)
                            .nombre("Usuario "+i)
                            .correo("correo"+i+"@gmail.com")
                            .password("password"+i)
                            .edadHijo(i)
                            .fechaRegistro(Instant.now())
                            .build()
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> list(@RequestParam (name = "nombre", defaultValue = "", required = false) String user) {
        List<Usuario> usuarios = usuarioService.getAll();
        if (usuarios == null || usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(user != null && !user.isEmpty()) {
            return ResponseEntity.ok(
                    usuarios.stream()
                            .filter(u -> u.getNombre().equals(user))
                            .map(u ->
                                    UsuarioDto.builder()
                                            .usuarioId(u.getId())
                                            .nombre(u.getNombre())
                                            .correo(u.getCorreo())
                                            .password(u.getPassword())
                                            .edadHijo(u.getEdad())
                                            .fechaRegistro(u.getFecha_registro())
                                            .build())
                            .collect(Collectors.toList())
            );
        }
        return ResponseEntity.ok(
                usuarios.stream()
                        .map(u ->
                                UsuarioDto.builder()
                                        .usuarioId(u.getId())
                                        .nombre(u.getNombre())
                                        .correo(u.getCorreo())
                                        .password(u.getPassword())
                                        .edadHijo(u.getEdad())
                                        .fechaRegistro(u.getFecha_registro())
                                        .build())
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioDto>getById(@PathVariable Integer id) {
        Usuario u = usuarioService.getById( id );

        if(u == null )
        {
            return ResponseEntity.notFound( ).build( );
        }
        return ResponseEntity.ok(
                UsuarioDto.builder()
                        .usuarioId(u.getId())
                        .nombre(u.getNombre())
                        .correo(u.getCorreo())
                        .password(u.getPassword())
                        .edadHijo(u.getEdad())
                        .fechaRegistro(u.getFecha_registro())
                        .build()
        );
    }

    @GetMapping("/mail/{correo}")
    public ResponseEntity<UsuarioDto> getByCorreo(@PathVariable String correo) {
        Usuario u = usuarioService.getByCorreo(correo);

        if (u == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                UsuarioDto.builder()
                        .usuarioId(u.getId())
                        .nombre(u.getNombre())
                        .correo(u.getCorreo())
                        .password(u.getPassword())
                        .edadHijo(u.getEdad())
                        .fechaRegistro(u.getFecha_registro())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> save(@RequestBody UsuarioDto usuarioDto) {
        Usuario u = Usuario
                .builder()
                .id(usuarioDto.getUsuarioId())
                .nombre(usuarioDto.getNombre())
                .correo(usuarioDto.getCorreo())
                .password(usuarioDto.getPassword())
                .edad(usuarioDto.getEdadHijo())
                .fecha_registro(usuarioDto.getFechaRegistro())
                .build();

        Usuario guardado = usuarioService.save(u);

        return ResponseEntity.ok(
                UsuarioDto.builder()
                        .usuarioId(guardado.getId())
                        .nombre(guardado.getNombre())
                        .correo(guardado.getCorreo())
                        .password(guardado.getPassword())
                        .edadHijo(guardado.getEdad())
                        .fechaRegistro(guardado.getFecha_registro())
                        .build()
        );
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<UsuarioDto> delete(@PathVariable Integer id)
    {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<UsuarioDto> update(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        Usuario u = usuarioService.update(id, Usuario
                .builder()
                .id(usuarioDto.getUsuarioId())
                .nombre(usuarioDto.getNombre())
                .correo(usuarioDto.getCorreo())
                .password(usuarioDto.getPassword())
                .edad(usuarioDto.getEdadHijo())
                .fecha_registro(usuarioDto.getFechaRegistro())
                .build());

        return ResponseEntity.ok(
                UsuarioDto.builder()
                        .usuarioId(u.getId())
                        .nombre(u.getNombre())
                        .correo(u.getCorreo())
                        .password(u.getPassword())
                        .edadHijo(u.getEdad())
                        .fechaRegistro(u.getFecha_registro())
                        .build()
        );
    }
}
*/
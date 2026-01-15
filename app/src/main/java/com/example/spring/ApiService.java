package com.example.spring;

import com.example.spring.dto.LeccionResponseDto;
import com.example.spring.dto.ProgresoDto;
import com.example.spring.dto.UsuarioDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/KidzBrain/api/progreso/puntuacion/usuario/{idUsuario}/materia/{idMateria}")
    Call<Integer> getPuntuacionPorMateria(
        @Path("idUsuario") int idUsuario,
        @Path("idMateria") int idMateria
    );

    @GET("/KidzBrain/api/usuarios/mail/{correo}")
    Call<UsuarioDto> getUsuarioPorCorreo(
        @Path("correo") String correo
    );

    @POST("/KidzBrain/api/usuarios")
    Call<UsuarioDto> crearUsuario(@Body UsuarioDto usuario);

    // CORRECCIÓN: Agregamos {idGrado} al final
    @GET("/KidzBrain/api/lecciones/desbloqueada/usuario/{idUsuario}/materia/{idMateria}/grado/{idGrado}")
    Call<Integer> getSiguienteLeccion(
            @Path("idUsuario") int idUsuario,
            @Path("idMateria") int idMateria,
            @Path("idGrado") int idGrado // <--- NUEVO PARÁMETRO
    );

    @POST("/KidzBrain/api/progreso")
    Call<ProgresoDto> guardarProgreso(@Body ProgresoDto progreso);

    @GET("/KidzBrain/api/lecciones/materia/{idMateria}/grado/{idGrado}")
    Call<List<LeccionResponseDto>> getLeccionesPorGrado(
            @Path("idMateria") int idMateria,
            @Path("idGrado") int idGrado
    );

}

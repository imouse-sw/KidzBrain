package com.example.spring;

import com.example.spring.dto.UsuarioDto;

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

}

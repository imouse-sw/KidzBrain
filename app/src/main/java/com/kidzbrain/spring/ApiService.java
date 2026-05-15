package com.kidzbrain.spring;

import com.kidzbrain.spring.dto.AccesoDto;
import com.kidzbrain.spring.dto.BorrarCuentaDto;
import com.kidzbrain.spring.dto.JuegoResponseDto;
import com.kidzbrain.spring.dto.LeccionResponseDto;
import com.kidzbrain.spring.dto.LoginRequest;
import com.kidzbrain.spring.dto.ProgresoDto;
import com.kidzbrain.spring.dto.RestablecerPasswordDto;
import com.kidzbrain.spring.dto.SolicitudRecuperacionDto;
import com.kidzbrain.spring.dto.UsuarioDto;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/KidzBrain/api/usuarios/reset-request")
    Call<Void> solicitarRecuperacion(@Body SolicitudRecuperacionDto dto);

    @POST("/KidzBrain/api/usuarios/reset-password")
    Call<Void> restablecerPassword(@Body RestablecerPasswordDto dto);

    @HTTP(method = "DELETE", path = "/KidzBrain/api/usuarios/borrar-cuenta", hasBody = true)
    Call<Void> borrarCuenta(@Body BorrarCuentaDto dto);

    @GET("/KidzBrain/api/progreso/puntuacion/usuario/{idUsuario}/materia/{idMateria}")
    Call<Integer> getPuntuacionPorMateria(
        @Path("idUsuario") int idUsuario,
        @Path("idMateria") int idMateria
    );

    @POST("/KidzBrain/api/usuarios/login")
    Call<UsuarioDto> loginUsuario(@Body LoginRequest request);

    @GET("/KidzBrain/api/progreso/usuario/{idUsuario}")
    Call<List<ProgresoDto>> getProgresoPorUsuario(
            @Path("idUsuario") int idUsuario
    );

    @POST("KidzBrain/api/accesos")
    Call<AccesoDto> registrarAcceso(@Body AccesoDto acceso);

    @GET("/KidzBrain/api/usuarios/mail/{correo}")
    Call<UsuarioDto> getUsuarioPorCorreo(
        @Path("correo") String correo
    );

    @POST("/KidzBrain/api/usuarios/registro")
    Call<UsuarioDto> crearUsuario(@Body UsuarioDto usuario);

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

    @Multipart
    @POST("KidzBrain/api/usuarios/id/{id}/foto")
    Call<String> subirFotoPerfil(
            @Path("id") int idUsuario,
            @Part MultipartBody.Part foto
    );


    @GET("/KidzBrain/api/juegos")
    Call<List<JuegoResponseDto>> getAllJuegos();

}

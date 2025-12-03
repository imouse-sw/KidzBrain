package com.example.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import com.example.api.model.Usuarios;



public interface servicioUsuario {
    // Petición GET para obtener una lista de todos los usuarios
    @GET("Usuarios")
    Call<List<Usuarios>> getAllUsuarioss();

    // Petición GET para obtener un usuario por su ID
    @GET("id/{usuariosId}")
    Call<Usuarios> getUsuariosById(@Path("usuariosId") int usuariosId);

    // Petición POST para crear un nuevo usuario
    @POST("usuarios")
    Call<Usuarios> createUsuarios(@Body Usuarios newusuarios);

    @GET("usuarios/search") // ⬅️ Usa la nueva ruta de búsqueda del servidor
    Call<Usuarios> getUsuarioByCorreo(@Query("correo") String correo);
// ➡️ Retrofit genera: /usuarios/search?correo=usuario@ejemplo.com
}

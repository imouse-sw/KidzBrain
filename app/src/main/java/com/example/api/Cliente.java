package com.example.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Cliente {
    //No le muevan o se muere la app 😭
    private static final String BASE_URL = "http://10.0.2.2:8080/KidzBrain/api/";
    private static Retrofit retrofit;

    public static servicioUsuario getUsuariosService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(servicioUsuario.class);
    }
}

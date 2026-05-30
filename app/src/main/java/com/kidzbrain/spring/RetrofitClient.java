package com.kidzbrain.spring;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://antonio-des.centralus.cloudapp.azure.com";

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            // creamos el interceptor que le pega el token a cada petición
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    // buscamos algún token guardado
                    SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                    String token = prefs.getString("userToken", "");

                    // tomamos la petición que se quiere enviar
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder();

                    // si encontramos un token lo agregamos a la cabecera de la petición
                    if (!token.isEmpty()) {
                        builder.header("Authorization", "Bearer " + token);
                    }

                    // dejamos que la petición pase
                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                }
            }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService(Context context) {
        return getClient(context).create(ApiService.class);
    }
}

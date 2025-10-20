package com.kidzbrain.utilidades;

import android.os.Looper;
import android.view.View;
import android.os.Handler;
import android.view.animation.AlphaAnimation;

public final class Animaciones {

    private Animaciones() {
        // Esta clase no debe ser instanciada.
    }

    public static void mostrarConAnimacion(final View view, int delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            view.setVisibility(View.VISIBLE);
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(500);
            view.startAnimation(fadeIn);
        }, delayMillis);
    }
}

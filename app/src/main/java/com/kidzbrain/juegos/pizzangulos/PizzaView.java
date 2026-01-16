package com.kidzbrain.juegos.pizzangulos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.kidzbrain.login.R;

public class PizzaView extends View {
    Paint linea;
    Bitmap pizzita;
    float anguloActual = 0f;
    int centroX, centroY, radio;
    boolean mostrarLineas  = true;

    public PizzaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linea = new Paint();
        linea.setColor(Color.WHITE);
        linea.setStrokeWidth(5f);
        linea.setAntiAlias(true);

        setImagenPizza(R.drawable.px_pizza);
    }

    public void setImagenPizza(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);

        if (radio > 0) {
            pizzita = Bitmap.createScaledBitmap(bitmap, radio * 2, radio * 2, true);
        } else {
            pizzita = bitmap;
        }

        invalidate();
    }

    public void setMostrarLineas(boolean mostrar) {
        this.mostrarLineas = mostrar;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centroX = w / 2;
        centroY = h / 2;
        radio = Math.min(centroX, centroY);

        pizzita = Bitmap.createScaledBitmap(pizzita, radio * 2, radio * 2, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(pizzita, centroX - radio, centroY - radio, null);

        if(mostrarLineas) {
            canvas.drawLine(centroX, centroY, centroX + radio, centroY, linea);

            canvas.save();
            canvas.rotate(-anguloActual, centroX, centroY);
            canvas.drawLine(centroX, centroY, centroX + radio, centroY, linea);
            canvas.restore();
        }
    }

    public void setAngulo(float angulo) {
        this.anguloActual = angulo;
        invalidate();
    }
}

package com.kidzbrain.utilidades;

import android.app.DatePickerDialog;
import android.content.Context; // Import Context
import android.widget.EditText;
import java.util.Calendar;

// Clase para el calendario.
public final class Calendario {

    // Para no instanciar la clase.
    private Calendario() {
    }
    public static void mostrarDatePicker(Context context, final EditText etFechaNacimiento) {
        final Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etFechaNacimiento.setText(fechaSeleccionada);
                }, año, mes, dia);
        datePickerDialog.show();
    }
}

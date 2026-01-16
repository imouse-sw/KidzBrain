package com.kidzbrain.utilidades.leccionutil;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.kidzbrain.login.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class mapa_niveles extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTituloMateria;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private String materia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_niveles);

        materia = getIntent().getStringExtra("materia");

        tvTituloMateria = findViewById(R.id.tvTituloMateria);
        btnBack = findViewById(R.id.btnBack);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        personalizarInterfaz();
        setupViewPager();

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refrescar el ViewPager para que muestre el progreso actualizado
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Básico"); break;
                case 1: tab.setText("Intermedio"); break;
                case 2: tab.setText("Avanzado"); break;
            }
        }).attach();
    }

    private void personalizarInterfaz() {
        if ("matematicas".equalsIgnoreCase(materia)) {
            tvTituloMateria.setText("Matemáticas");
        } else if ("ciencias".equalsIgnoreCase(materia)) {
            tvTituloMateria.setText("Ciencias");
        }
    }
}
package com.kidzbrain.utilidades.leccionutil;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return LeccionFragment.newInstance(1); // Nivel Básico
            case 1:
                return LeccionFragment.newInstance(2); // Nivel Intermedio
            case 2:
                return LeccionFragment.newInstance(3); // Nivel Avanzado
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Tenemos 3 pestañas
    }
}
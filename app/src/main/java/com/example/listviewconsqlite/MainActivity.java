package com.example.listviewconsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void agregarAlumno(View view) {
        Intent intent = new Intent(this, AgregarAlumnoActivity.class);
        startActivity(intent);
    }

    public void mostrarDatos(View view) {
        Intent intent = new Intent(this, MostrarDatosActivity.class);
        startActivity(intent);
    }

    public void Salir(View view) {
        finishAffinity(); // Cierra todas las actividades abiertas en la aplicación
        System.exit(0); // Cierra completamente la aplicación
    }

}

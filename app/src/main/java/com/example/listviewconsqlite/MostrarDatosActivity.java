package com.example.listviewconsqlite;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MostrarDatosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private DatabaseManager db;
    private static final int REQUEST_EDIT_ALUMNO = 1;

    private RecyclerView recyclerViewAlumnos;
    private AlumnoAdapter alumnoAdapter;
    private List<Alumno> listaAlumnos;
    private SearchView searchView;


    public void agregarAlumno(View view) {
        Intent intent = new Intent(this, AgregarAlumnoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_datos);

        db = new DatabaseManager(this);
        recyclerViewAlumnos = findViewById(R.id.recyclerViewAlumnos);

        listaAlumnos = new ArrayList<>();
        alumnoAdapter = new AlumnoAdapter(this, listaAlumnos);
        recyclerViewAlumnos.setAdapter(alumnoAdapter);
        recyclerViewAlumnos.setLayoutManager(new LinearLayoutManager(this));

        mostrarAlumnos();

        recyclerViewAlumnos.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerViewAlumnos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Alumno selectedAlumno = listaAlumnos.get(position);

                                // Abre la ventana de edición del alumno y pasa los datos del alumno seleccionado
                                Intent intent = new Intent(MostrarDatosActivity.this, AgregarAlumnoActivity.class);
                                intent.putExtra("ALUMNO_ID", selectedAlumno.getId());
                                intent.putExtra("ALUMNO_NOMBRE", selectedAlumno.getNombre());
                                intent.putExtra("ALUMNO_MATRICULA", selectedAlumno.getMatricula());
                                intent.putExtra("ALUMNO_FOTO", selectedAlumno.getFoto());
                                startActivityForResult(intent, REQUEST_EDIT_ALUMNO);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Alumno> listaFiltrada = db.buscarAlumnos(newText);
        listaAlumnos.clear();

        // Filtrar por nombre y matrícula
        for (Alumno alumno : listaFiltrada) {
            if (alumno.getNombre().toLowerCase().contains(newText.toLowerCase()) ||
                    alumno.getMatricula().toLowerCase().contains(newText.toLowerCase())) {
                listaAlumnos.add(alumno);
            }
        }

        alumnoAdapter.notifyDataSetChanged();
        return true;
    }

    private void mostrarAlumnos() {
        listaAlumnos.clear();
        listaAlumnos.addAll(db.obtenerAlumnos());
        alumnoAdapter.notifyDataSetChanged();
    }

    public void regresar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private GestureDetectorCompat gestureDetector;
        private OnItemClickListener mListener;

        interface OnItemClickListener {
            void onItemClick(View view, int position);
            void onLongItemClick(View view, int position);
        }

        RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && mListener != null) {
                        mListener.onLongItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });

            recyclerView.addOnItemTouchListener(this);
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && gestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}

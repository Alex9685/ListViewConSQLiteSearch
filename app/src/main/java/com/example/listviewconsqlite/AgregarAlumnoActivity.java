package com.example.listviewconsqlite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class AgregarAlumnoActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private DatabaseManager db;
    private ImageView imageView;
    private int alumnoId; // Nuevo atributo para almacenar el ID del alumno seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_alumno);

        db = new DatabaseManager(this);
        imageView = findViewById(R.id.imageView);

        // Verifica si se enviaron datos extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            alumnoId = extras.getInt("ALUMNO_ID"); // Guarda el ID del alumno seleccionado

            String alumnoNombre = extras.getString("ALUMNO_NOMBRE");
            String alumnoMatricula = extras.getString("ALUMNO_MATRICULA");
            byte[] alumnoFoto = extras.getByteArray("ALUMNO_FOTO");

            // Establece los valores en los campos correspondientes
            EditText etNombre = findViewById(R.id.etNombre);
            EditText etMatricula = findViewById(R.id.etMatricula);

            etNombre.setText(alumnoNombre);
            etMatricula.setText(alumnoMatricula);

            // Carga la imagen del alumno si está disponible
            if (alumnoFoto != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(alumnoFoto, 0, alumnoFoto.length);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void guardarAlumno(View view) {
        EditText etNombre = findViewById(R.id.etNombre);
        EditText etMatricula = findViewById(R.id.etMatricula);

        String nombre = etNombre.getText().toString();
        String matricula = etMatricula.getText().toString();

        // Verifica que todos los campos estén completos
        if (nombre.isEmpty() || matricula.isEmpty() || imageView.getDrawable() == null) {
            Toast.makeText(this, "Por favor, ingresa todos los datos requeridos", Toast.LENGTH_SHORT).show();
            return; // Sale del método sin guardar el alumno
        }

        // Obtén los datos de la imagen como un array de bytes
        byte[] foto = obtenerDatosImagen();

        if (alumnoId != 0) {
            // Si hay un ID de alumno válido, se actualiza el alumno existente
            Alumno alumno = db.obtenerAlumnoPorId(alumnoId);
            alumno.setNombre(nombre);
            alumno.setMatricula(matricula);
            alumno.setFoto(foto);
            db.actualizarAlumno(alumno);
            Toast.makeText(this, "Alumno actualizado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            // Si no hay ID de alumno, se agrega un nuevo alumno
            db.agregarAlumno(nombre, matricula, foto);
            Toast.makeText(this, "Alumno agregado correctamente", Toast.LENGTH_SHORT).show();
        }

        // Regresar al MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private byte[] obtenerDatosImagen() {
        byte[] datosImagen = null;

        try {
            // Obtén la imagen como Bitmap desde el ImageView
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            // Convierte el Bitmap a un array de bytes
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            datosImagen = stream.toByteArray();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datosImagen;
    }


    public void seleccionarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            // Obtiene la imagen seleccionada de la galería y la muestra en el ImageView
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void regresar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

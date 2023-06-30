package com.example.listviewconsqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "alumnos.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ALUMNO = "alumno";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_MATRICULA = "matricula";
    private static final String COLUMN_FOTO = "foto";


    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALUMNO_TABLE = "CREATE TABLE " + TABLE_ALUMNO + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_MATRICULA + " TEXT,"
                + COLUMN_FOTO + " BLOB"
                + ")";
        db.execSQL(CREATE_ALUMNO_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALUMNO);
        onCreate(db);
    }

    public void agregarAlumno(String nombre, String matricula, byte[] foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_MATRICULA, matricula);
        values.put(COLUMN_FOTO, foto);
        long resultado = db.insert(TABLE_ALUMNO, null, values);
        if (resultado == -1) {

        }
        db.close();
    }

    public void eliminarAlumno(int idAlumno) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALUMNO, COLUMN_ID + " = ?", new String[]{String.valueOf(idAlumno)});
        db.close();
    }

    public Alumno obtenerAlumnoPorId(int alumnoId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ALUMNO,
                new String[] { COLUMN_ID, COLUMN_NOMBRE, COLUMN_MATRICULA, COLUMN_FOTO },
                COLUMN_ID + "=?",
                new String[] { String.valueOf(alumnoId) },
                null,
                null,
                null,
                null
        );

        Alumno alumno = null;
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
            int columnIndexNombre = cursor.getColumnIndex(COLUMN_NOMBRE);
            int columnIndexMatricula = cursor.getColumnIndex(COLUMN_MATRICULA);
            int columnIndexFoto = cursor.getColumnIndex(COLUMN_FOTO);

            if (columnIndexId != -1 && columnIndexNombre != -1 && columnIndexMatricula != -1 && columnIndexFoto != -1) {
                alumno = new Alumno(
                        cursor.getInt(columnIndexId),
                        cursor.getString(columnIndexNombre),
                        cursor.getString(columnIndexMatricula),
                        cursor.getBlob(columnIndexFoto)
                );
            }

            cursor.close();
        }

        return alumno;
    }

    public void actualizarAlumno(Alumno alumno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, alumno.getNombre());
        values.put(COLUMN_MATRICULA, alumno.getMatricula());
        values.put(COLUMN_FOTO, alumno.getFoto());

        db.update(
                TABLE_ALUMNO,
                values,
                COLUMN_ID + "=?",
                new String[] { String.valueOf(alumno.getId()) }
        );

        db.close();
    }


    public List<Alumno> buscarAlumnos(String query) {
        List<Alumno> listaAlumnos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALUMNO +
                " WHERE " + COLUMN_NOMBRE + " LIKE '%" + query + "%' OR " +
                COLUMN_MATRICULA + " LIKE '%" + query + "%'", null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
            int columnIndexNombre = cursor.getColumnIndex(COLUMN_NOMBRE);
            int columnIndexMatricula = cursor.getColumnIndex(COLUMN_MATRICULA);
            int columnIndexFoto = cursor.getColumnIndex(COLUMN_FOTO);

            do {
                Alumno alumno = new Alumno();
                if (columnIndexId != -1) {
                    alumno.setId(cursor.getInt(columnIndexId));
                }
                if (columnIndexNombre != -1) {
                    alumno.setNombre(cursor.getString(columnIndexNombre));
                }
                if (columnIndexMatricula != -1) {
                    alumno.setMatricula(cursor.getString(columnIndexMatricula));
                }
                if (columnIndexFoto != -1) {
                    alumno.setFoto(cursor.getBlob(columnIndexFoto));
                }

                listaAlumnos.add(alumno);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaAlumnos;
    }

    public List<Alumno> obtenerAlumnos() {
        List<Alumno> listaAlumnos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALUMNO, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
            int columnIndexNombre = cursor.getColumnIndex(COLUMN_NOMBRE);
            int columnIndexMatricula = cursor.getColumnIndex(COLUMN_MATRICULA);
            int columnIndexFoto = cursor.getColumnIndex(COLUMN_FOTO);

            do {
                Alumno alumno = new Alumno();
                if (columnIndexId != -1) {
                    alumno.setId(cursor.getInt(columnIndexId));
                }
                if (columnIndexNombre != -1) {
                    alumno.setNombre(cursor.getString(columnIndexNombre));
                }
                if (columnIndexMatricula != -1) {
                    alumno.setMatricula(cursor.getString(columnIndexMatricula));
                }
                if (columnIndexFoto != -1) {
                    alumno.setFoto(cursor.getBlob(columnIndexFoto));
                }

                listaAlumnos.add(alumno);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaAlumnos;
    }
}

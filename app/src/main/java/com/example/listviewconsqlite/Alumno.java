package com.example.listviewconsqlite;

public class Alumno {
    private int id;
    private String nombre;
    private String matricula;
    private byte[] foto;

    public Alumno() {
    }

    public Alumno(int id, String nombre, String matricula, byte[] foto) {
        this.id = id;
        this.nombre = nombre;
        this.matricula = matricula;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}

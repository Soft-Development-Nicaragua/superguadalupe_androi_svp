package com.sdn.modelo;

public class Producto {
    String codigo="";
    String line="";
    String nombre="";
    Integer unidad=0;

    public Producto() {
    }

    public Producto(String codigo, String line, String nombre, Integer unidad) {
        this.codigo = codigo;
        this.line = line;
        this.nombre = nombre;
        this.unidad = unidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getUnidad() {
        return unidad;
    }

    public void setUnidad(Integer unidad) {
        this.unidad = unidad;
    }

    public String toString2() {
        return "Producto{" +
                "codigo='" + codigo + '\'' +
                ", line='" + line + '\'' +
                ", nombre='" + nombre + '\'' +
                ", unidad=" + unidad +
                '}';
    }

    @Override
    public String toString() {
        return nombre;
    }
}

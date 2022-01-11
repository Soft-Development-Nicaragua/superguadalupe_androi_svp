package com.sdn.modelo;

public class Operador {
    int id;
    String Usuario;
    String Clave;
    String Nombre;
    int tipo;

    public Operador() {
        this.id=-1;
    }

    public Operador( String usuario, String clave, String nombre, int tipo) {
        this.id = -1;
        Usuario = usuario;
        Clave = clave;
        Nombre = nombre;
        this.tipo = tipo;
    }

    public Operador(int id, String usuario, String clave, String nombre, int tipo) {
        this.id = id;
        Usuario = usuario;
        Clave = clave;
        Nombre = nombre;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

   @Override
    public String toString() {
        return  Nombre;
    }


    public String toString2() {
        return "Operador{" +
                "id=" + id +
                ", Usuario='" + Usuario + '\'' +
                ", Clave='" + Clave + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}

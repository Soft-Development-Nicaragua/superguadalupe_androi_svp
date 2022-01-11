package com.sdn.modelo;

public class Parametro {
    String campo;
    String clave;

    public Parametro(String campo, String clave) {
        this.campo = campo;
        this.clave = clave;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String toString() {
        return clave;
    }
}

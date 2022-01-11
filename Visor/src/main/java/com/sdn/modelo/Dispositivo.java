package com.sdn.modelo;

public class Dispositivo {
    int id;
    String no_serie;
    String no_dispositivo;
    int no_correlativo;

    public Dispositivo() {
        this.id = -1;
    }

    public Dispositivo(int id, String no_serie, String no_dispositivo, int no_correlativo) {
        this.id = id;
        this.no_serie = no_serie;
        this.no_dispositivo = no_dispositivo;
        this.no_correlativo = no_correlativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNo_serie() {
        return no_serie;
    }

    public void setNo_serie(String no_serie) {
        this.no_serie = no_serie;
    }

    public String getNo_dispositivo() {
        return no_dispositivo;
    }

    public void setNo_dispositivo(String no_dispositivo) {
        this.no_dispositivo = no_dispositivo;
    }

    public int getNo_correlativo() {
        return no_correlativo;
    }

    public void setNo_correlativo(int no_correlativo) {
        this.no_correlativo = no_correlativo;
    }

    @Override
    public String toString() {
        return getNo_dispositivo();
    }
}

package com.sdn.modelo;

public class Promocion {
    Integer Tipo =-1;
    String codigo ="";
    Double Descuento =0.00;
    boolean FiltroxDias;
    boolean Domingo,Lunes, Martes, Miercoles, Jueves, Viernes, Sabado;

    public Promocion() {
        Tipo = -1;
        this.codigo = "";
    }

    public Promocion(Integer tipo, String codigo, Double descuento, boolean filtroxDias, boolean domingo, boolean lunes, boolean martes, boolean miercoles, boolean jueves, boolean viernes, boolean sabado) {
        Tipo = tipo;
        this.codigo = codigo;
        Descuento = descuento;
        FiltroxDias = filtroxDias;
        Domingo = domingo;
        Lunes = lunes;
        Martes = martes;
        Miercoles = miercoles;
        Jueves = jueves;
        Viernes = viernes;
        Sabado = sabado;
    }

    public Integer getTipo() {
        return Tipo;
    }

    public void setTipo(Integer tipo) {
        Tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getDescuento() {
        return Descuento;
    }

    public void setDescuento(Double descuento) {
        Descuento = descuento;
    }

    public boolean getFiltroxDias() {
        return FiltroxDias;
    }

    public void setFiltroxDias(boolean filtroxDias) {
        FiltroxDias = filtroxDias;
    }

    public boolean getDomingo() {
        return Domingo;
    }

    public void setDomingo(boolean domingo) {
        Domingo = domingo;
    }

    public boolean getLunes() {
        return Lunes;
    }

    public void setLunes(boolean lunes) {
        Lunes = lunes;
    }

    public boolean getMartes() {
        return Martes;
    }

    public void setMartes(boolean martes) {
        Martes = martes;
    }

    public boolean getMiercoles() {
        return Miercoles;
    }

    public void setMiercoles(boolean miercoles) {
        Miercoles = miercoles;
    }

    public boolean getJueves() {
        return Jueves;
    }

    public void setJueves(boolean jueves) {
        Jueves = jueves;
    }

    public boolean getViernes() {
        return Viernes;
    }

    public void setViernes(boolean viernes) {
        Viernes = viernes;
    }

    public boolean getSabado() {
        return Sabado;
    }

    public void setSabado(boolean sabado) {
        Sabado = sabado;
    }

    @Override
    public String toString() {
        return "" + Descuento ;
    }

    public String toString2() {
        return "Promocion{" +
                "Tipo=" + Tipo +
                ", codigo='" + codigo + '\'' +
                ", Descuento=" + Descuento +
                ", FiltroxDias=" + FiltroxDias +
                ", Domingo=" + Domingo +
                ", Lunes=" + Lunes +
                ", Martes=" + Martes +
                ", Miercoles=" + Miercoles +
                ", Jueves=" + Jueves +
                ", Viernes=" + Viernes +
                ", Sabado=" + Sabado +
                '}';
    }
}

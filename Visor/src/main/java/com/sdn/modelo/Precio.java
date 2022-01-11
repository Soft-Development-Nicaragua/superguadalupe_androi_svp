package com.sdn.modelo;

public class Precio {
    String presentacion ="";
    String precio="";
    boolean promocion =false;
    Double descuento=0.00;

    public Precio() {
    }

    public Precio(String presentacion, String precio) {
        this.presentacion = presentacion;
        this.precio = precio;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public boolean isPromocion() {
        return promocion;
    }

    public void setPromocion(boolean promocion) {
        this.promocion = promocion;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    @Override
    public String toString() {
        return precio;
    }

    public String toString2() {
        return "Precio{" +
                "presentacion='" + presentacion + '\'' +
                ", precio='" + precio + '\'' +
                ", promocion=" + promocion +
                ", descuento=" + descuento +
                '}';
    }
}

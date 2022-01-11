package com.sdn.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sdn.bd.local.BDLocal;
import com.sdn.modelo.Operador;

import java.util.Vector;

public class Tbl_Operador {
    private static final String SQLNuevoid = "Select (ifnull(max(id),0))+1 AS ID  from operador";
    private static final String SQLClearTable ="DELETE from operador WHERE id>0";
    private static final String SQLObtenerTodosRegistros = "SELECT id,usuario,clave,nombre,tipo FROM operador";
    private static final String SQLObtenerRegistros = "SELECT id,usuario,clave,nombre,tipo FROM operador WHERE tipo=?";
    private static final String SQLLogin ="SELECT id,usuario,clave,nombre,tipo  FROM operador  WHERE usuario=? AND clave=?";
    private static final String SQLObtenerRegistro ="SELECT id,usuario,clave,nombre,tipo  FROM operador  WHERE usuario=?";
    private static final String SQLObtenerRegistroXID ="SELECT id,usuario,clave,nombre,tipo  FROM operador  WHERE id=?";

    public static int nuevoID(Context contexto) {
        int idgenerado = 0;

        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLNuevoid, null);
        if (result.moveToFirst()) {
            do {
                idgenerado = Integer.parseInt(result.getString(0));
            } while (result.moveToNext());
        }
        result.close();
        BDLocal.cerrar();
        return idgenerado;
    }

    public static void vaciarTabla(Context contexto) {
        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.execSQL(SQLClearTable);
        BDLocal.cerrar();
       // Tbl_Operador.guardar(contexto, operador);
    }

    public static Operador validarUsuario(Context contexto, String usuario, String clave) {
        Operador operador = new Operador();

        String[] args = new String[]{usuario, clave};
        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLLogin, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    operador.setId(Integer.parseInt(result.getString(0)));
                    operador.setUsuario(result.getString(1));
                    operador.setClave(result.getString(2));
                    operador.setNombre(result.getString(3));
                    operador.setTipo(Integer.parseInt(result.getString(4)));
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return operador;
    }
/*
    public static Operador validarUsuario(Context contexto, String usuario, String clave) {
        Operador operador = new Operador();

        String[] args = new String[]{Utils.Encriptar(contexto,usuario), Utils.Encriptar(contexto,clave)};
        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLLogin, args);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    operador.setId(Integer.parseInt(result.getString(0)));
                    operador.setUsuario(Utils.Desencriptar(contexto, result.getString(1)));
                    operador.setClave(Utils.Desencriptar(contexto,result.getString(2)));
                    operador.setNombre(result.getString(3));
                    operador.setTipo(Integer.parseInt(result.getString(4)));
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return operador;
    }


*/

}

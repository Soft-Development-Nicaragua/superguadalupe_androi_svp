package com.sdn.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sdn.bd.local.BDLocal;
import com.sdn.modelo.Parametro;

import java.util.Vector;

public class Tbl_Parametro {
    private static final String SQLListarHistorico = "SELECT campo, valor FROM parametro";

    private static final String SQLGet = "SELECT valor FROM parametro WHERE campo=?";

    public static boolean modificar(Context contexto, String campo, String valor) {
        long resultado = -1;
        String[] args = new String[]{"" + campo};

        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("valor", valor);

        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.update("parametro", nuevoRegistro, "campo=?", args);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static Vector<Parametro> obtenerRegistros(Context contexto) {
        Vector<Parametro> Registros = new Vector<Parametro>();

        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLListarHistorico, null);
        result.moveToLast();

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Registros.addElement(new Parametro(result.getString(0), result.getString(1)));
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return Registros;
    }

    public static String getClave(Context contexto, String campo) {
        String[] args = new String[]{campo};
        String Clave = "";

        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLGet, args);
        result.moveToLast();

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Clave = result.getString(0);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return Clave;
    }

}

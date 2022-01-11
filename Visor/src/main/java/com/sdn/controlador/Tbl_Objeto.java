package com.sdn.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sdn.bd.local.BDLocal;
import com.sdn.modelo.Objeto;

import java.util.ArrayList;

public class Tbl_Objeto {

    public static void vaciarTabla(Context contexto, String tabla) {
        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.execSQL("DELETE FROM " + tabla + " WHERE _rowid_ > 0");
        BDLocal.cerrar();
    }

    public static boolean guardar(Context contexto, Objeto objeto, String tabla) {
        long resultado = -1;
        ContentValues nuevoRegistro = new ContentValues();

        nuevoRegistro.put("id", objeto.getId());
        nuevoRegistro.put("nombre", objeto.getNombre());
        BDLocal.abrir(contexto);
        resultado = BDLocal.BD_SQLITE.insert(tabla, null, nuevoRegistro);
        BDLocal.cerrar();

        return resultado > 0;
    }

    public static ArrayList<Objeto> obtenerRegistros(Context contexto, String tabla) {
        ArrayList<Objeto> Registros = new ArrayList<Objeto>();

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery("SELECT id,nombre FROM " + tabla+"  ORDER BY nombre ASC", null);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Objeto obj = new Objeto();
                    obj.setId(result.getString(0));
                    obj.setNombre(result.getString(1));
                    Registros.add(obj);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return Registros;
    }
}

package com.sdn.bd.local;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class BDUtil {
    private static final String LOG_TAG = BDUtil.class.getSimpleName();

    public static ArrayList<String> getTableNames(Context contexto) {
       ArrayList<String> columnas = new ArrayList<String>();

        BDLocal.abrir(contexto);


        Cursor result = BDLocal.BD_SQLITE.rawQuery("SELECT tbl_name FROM sqlite_master WHERE type = \"table\";", null);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    columnas.add(result.getString(0));
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return columnas;
    }

    public static ArrayList<String> getColumnNames(Context contexto, String SQLQuery){
        String[] columnas;
        BDLocal.abrir(contexto);
        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLQuery, null);
        columnas = result.getColumnNames();
        for(int i=0; i<columnas.length;i++){
            Log.e(LOG_TAG, columnas[i].toString());
        }
        result.close();
        BDLocal.cerrar();
        return  new ArrayList<String>(Arrays.asList(columnas));
    }

    public static void LimpiarTabla(Context contexto,String tabla) {
        BDLocal.abrir(contexto);
        BDLocal.BD_SQLITE.delete(tabla,null, null);
        BDLocal.cerrar();
    }

    public static void LimpiarBB(Context contexto) {
        ArrayList<String> tablas = getTableNames(contexto);

        BDLocal.abrir(contexto);
        for(int i=0; i<tablas.size();i++){
            BDLocal.BD_SQLITE.delete(tablas.get(i),null, null);
        }
        BDLocal.cerrar();
    }

    public static ArrayList<ArrayList> executeQuery(Context contexto, String SQLQuery){
        ArrayList<ArrayList> Registros = new ArrayList<ArrayList>();
        ArrayList<String> valor = new ArrayList<String>();;
        String[] columnas;

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLQuery, null);
        columnas = result.getColumnNames();

        for(int i=0; i<columnas.length;i++){
            valor.add(columnas[i].toString());
        }
        Registros.add(valor);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    valor = new ArrayList<String>();
                    for(int i=0; i<result.getColumnCount();i++){
                        valor.add(result.getString(i));
                    }
                    Registros.add(valor);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return Registros;
    }

    public static ArrayList<ArrayList> getData(Context contexto, String SQLQuery){
        ArrayList<ArrayList> Registros = new ArrayList<ArrayList>();
        ArrayList<String> Celdas = new ArrayList<String>();;
        Integer CantColumnas=0;

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery(SQLQuery, null);
        CantColumnas = result.getColumnCount();


        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {
                    Celdas = new ArrayList<String>();
                    for(int i=0; i<CantColumnas;i++){
                        Celdas.add(result.getString(i));
                    }
                    //Log.e(LOG_TAG, Celdas.toString());
                    Registros.add(Celdas);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return Registros;
    }

    public static ArrayList<ArrayList> get_RowsFromTable(Context contexto, String tabla){
        ArrayList<ArrayList> Registros = new ArrayList<ArrayList>();
        ArrayList<String> valor = new ArrayList<String>();;
        String[] columnas;

        BDLocal.abrir(contexto);

        Cursor result = BDLocal.BD_SQLITE.rawQuery("SELECT * FROM "+tabla, null);
        columnas = result.getColumnNames();

        for(int i=0; i<columnas.length;i++){
            valor.add(columnas[i].toString());
        }
        Registros.add(valor);

        if (result.getCount() != 0) {
            if (result.moveToFirst()) {
                do {

                    valor = new ArrayList<String>();
                    for(int i=0; i<result.getColumnCount();i++){
                        valor.add(result.getString(i));
                    }
                    Registros.add(valor);
                } while (result.moveToNext());
            }
        }
        result.close();
        BDLocal.cerrar();
        return Registros;
    }

}

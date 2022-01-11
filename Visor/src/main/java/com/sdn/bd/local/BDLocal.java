package com.sdn.bd.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by bismarck on 12/01/2019
 */

public class BDLocal {
    private static String BD_NAME = "dbsdj.s3db";
    public static SQLiteDatabase BD_SQLITE;
    private static Context myContext;

    private static void copyBDFromSambox(String Path_destino) {
        File PATH_BD = new File(Path_destino);

        InputStream myInput;
        OutputStream myOutput;

        try {

            myInput = myContext.getAssets().open(BD_NAME, Context.MODE_PRIVATE);
            myOutput = new FileOutputStream(PATH_BD);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (IOException ex) {
            Toast.makeText(myContext, "Error en copyBDFromSambox:["+ex.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
    }

    public static SQLiteDatabase abrir(Context contexto) {
        boolean WorkingLocalDirectory =false;

        myContext = contexto;

        String PATH_BDLOCAL= Environment.getExternalStorageDirectory().getAbsolutePath();
       // String PATH_BDSambox =File.separator+"data"+File.separator+"data"+File.separator+contexto.getPackageName()+File.separator+"databases"+File.separator+BD_NAME; //"/data/data/com.novaterra.lectura/databases/";
        String PATH_DESTINO="";

        try {
            /*if (!new File(PATH_BDSambox).exists()){
                Toast.makeText(contexto, "La base de datos "+BD_NAME+", No existe en el Sambox"+PATH_BDSambox, Toast.LENGTH_LONG).show();
                BD_SQLITE = null;
            }else{

            }*/
            if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                PATH_DESTINO =PATH_BDLOCAL+ File.separator + BD_NAME;

                if (!new File(PATH_DESTINO).exists())
                    copyBDFromSambox(PATH_DESTINO);
                WorkingLocalDirectory =true;

            }/*else{
                PATH_DESTINO =PATH_BDSambox;
            }*/
            BD_SQLITE = myContext.openOrCreateDatabase(PATH_DESTINO, Context.MODE_PRIVATE, null);
        }catch (Exception e){
            Toast.makeText(contexto, "Base de datos:["+BD_NAME+"] Ubicacion:["+ (WorkingLocalDirectory?"Local":"Sambox") +"], Message:["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
        return BD_SQLITE;
    }

    public static String getUbicacion(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + BD_NAME;
    }

    public static void cerrar() {
        if (BD_SQLITE != null)
            BD_SQLITE.close();
    }
}

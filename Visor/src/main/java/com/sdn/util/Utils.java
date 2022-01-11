package com.sdn.util;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.sdn.controlador.Tbl_Parametro;
import com.sdn.modelo.Objeto;
import com.sdn.svp.FrmSplash;
import com.sdn.svp.FrmVisorGeneral;
import com.sdn.svp.R;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
    public static String Encriptar(Context pantalla, String texto) {

        String secretKey = Tbl_Parametro.getClave(pantalla,"SUPPLIER"); //llave para encriptar datos
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

    public static String Desencriptar(Context pantalla,String textoEncriptado)  {

        String secretKey = Tbl_Parametro.getClave(pantalla,"SUPPLIER");  //llave para desenciptar datos
        String base64EncryptedString = "";

        try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }

    public static String C_DateToFormat(Date fecha, String format){
        String valor="";
        try {
            valor= fecha == null ? null : new SimpleDateFormat(format).format(new java.sql.Date(fecha.getTime()));
        }catch (Exception e){
            valor ="";
        }
        return  valor;
    }

    public static String getWorkDirectory(Context pantalla) {
       String PATH_DIRECTORYWORK= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"SVP"+File.separator;
        try {
            if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                createandnotifyToAndroid(pantalla,new File(PATH_DIRECTORYWORK));
            }
        }catch (Exception e){
            Toast.makeText(pantalla, "Direccion:["+ PATH_DIRECTORYWORK +"], Message:["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
        return PATH_DIRECTORYWORK;
    }

    public static String getBrandDirectory(Context pantalla) {
        String PATH_LOGOS= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"SVP"+File.separator+"Logo"+File.separator;
        try {
            if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                createandnotifyToAndroid(pantalla,new File(PATH_LOGOS));
            }
        }catch (Exception e){
            Toast.makeText(pantalla, "Direccion:["+ PATH_LOGOS +"], Message:["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
        return PATH_LOGOS;
    }

    public static String getScreenDirectory(Context pantalla) {
        String PATH_SCREEN= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"SVP"+File.separator+"Pantalla"+File.separator;
        try {
            if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                createandnotifyToAndroid(pantalla,new File(PATH_SCREEN));
            }
        }catch (Exception e){
            Toast.makeText(pantalla, "Direccion:["+ PATH_SCREEN +"], Message:["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
        return PATH_SCREEN;
    }

    public static String getPromotionDirectory(Context pantalla) {
        String PATH_PROMOTION= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"SVP"+File.separator+"Promociones"+File.separator;
        try {
            if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
                createandnotifyToAndroid(pantalla,new File(PATH_PROMOTION));
            }
        }catch (Exception e){
            Toast.makeText(pantalla, "Direccion:["+ PATH_PROMOTION +"], Message:["+e.getMessage()+"]", Toast.LENGTH_LONG).show();
        }
        return PATH_PROMOTION;
    }

    private static void createandnotifyToAndroid(Context referencia, File carpeta) {
        if(!carpeta.exists()){
            carpeta.mkdirs();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(carpeta);
                mediaScanIntent.setData(contentUri);
                referencia.sendBroadcast(mediaScanIntent);
            } else {
                referencia.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(carpeta.getAbsolutePath())));
            }
        }
    }

    public static Bitmap cargarBitImageFromPromotionDirectory(Context pantalla, String PathImage) {
        Bitmap image = null;
        try{
            image = BitmapFactory.decodeFile(PathImage);
        }catch (Exception e){
            image = BitmapFactory.decodeResource(pantalla.getResources(), R.drawable.ic_up);
        }
        return image;
    }

    public static Bitmap cargarBitImageFromScreenDirectory(Context pantalla, String NombreImagen) {
        Bitmap image = null;
        try{
            System.out.println("Cargando Pantalla...."+Utils.getScreenDirectory(pantalla)+NombreImagen);
             image = BitmapFactory.decodeFile(Utils.getScreenDirectory(pantalla)+NombreImagen);
        }catch (Exception e){
            image = BitmapFactory.decodeResource(pantalla.getResources(), R.drawable.ic_up);
        }
        return image;
    }

    public static Bitmap cargarBitImageFromBrandDirectory(Context pantalla, String NombreImagen) {
        Bitmap image = null;
        try{
            System.out.println("Cargando Logo...."+Utils.getBrandDirectory(pantalla)+NombreImagen);
            image = BitmapFactory.decodeFile(Utils.getBrandDirectory(pantalla)+NombreImagen);
        }catch (Exception e){
            image = BitmapFactory.decodeResource(pantalla.getResources(), R.drawable.img_deviceerror_24x24);
        }
        return image;
    }


    public static ArrayList<Objeto> cargarListaDeImagenesPromocionales(Context pantalla) {
        ArrayList<Objeto> lista = new ArrayList<Objeto>();

        File directory = new File(getPromotionDirectory(pantalla));
        File[] files = directory.listFiles();
        for(File file : files) {
            // convert the file name into string
            String fileName = file.toString();

            int index = fileName.lastIndexOf('.');
            if(index > 0) {
                String extension = fileName.substring(index + 1);
                if(extension.equals("png") || extension.equals("jpg")){
                    lista.add(new Objeto(file.getName(),file.getAbsolutePath()));
                }
            }
        }
        return  lista;
    }
}

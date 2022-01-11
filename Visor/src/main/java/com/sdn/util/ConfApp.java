package com.sdn.util;

import android.content.Context;
import android.provider.Settings;

import com.sdn.bd.remote.BDOperacion;
import com.sdn.controlador.Tbl_Parametro;
import com.sdn.modelo.Operador;

import java.text.DecimalFormat;

/**
 * Created by bismarck on 15/8/2016.
 */
public class ConfApp {

    public static String SCREEN_NAME="";

    /**
     * VARIABLES GLOBAL DEL SQLITE
     */
  //  public static String PATH_BDSAMBOX;
   // public static String PATH_BDDESTINO;
   // public static String BD_NAME ;
  //  public static String PATH_BD ;

    /**
     * VARIABLES GLOBAL DE SERVIDOR REMOTO SQL SERVER
     */
    public static String SERVER;
    public static String BDNAME_CATALOG;
    public static String BDNAME_LICENSE;
    public static String BDUSER;
    public static String BDPASS;

    /**
     * VARIABLES GLOBAL DEL SISTEMA
     */
   // public static Boolean USER_DTS = false;
   // public static Boolean USER_ADMIN = false;

    public static String UUID_FROM_DEVICE;
    public static String UUID_ENCRYPTED;
    public static String UUID_DESENCRYPTED;
    public static boolean DEVICEAUTORIZED = false;
    public static boolean ISSHOWING_DIALOG=false;

    public static String SYSTEM_USER;
    public static String SYSTEM_PASS;

    public static String BRANCH_DEFAULT;
    public static Integer VISOR_DEFAULT;

    public static Integer TIMER_SCREEN_WAIT;
    public static Integer TIMER_VISOR_GENERAL;
    public static Integer TIMER_VISOR_MAYORISTA;
    public static Integer COLOR_DEFAULT;

    public static boolean SHOW_MODULE_PROMO;
    public static Integer TIMER_PROPO_VISOR;
    public static Integer TIMER_PROMO_DURATION;
    public static boolean SHOW_AFTER_BARCODE_READ;

    public static Boolean STOP_SCREEN=true;

    public static Operador OPERADORLOGEADO = new Operador();
    public static BDOperacion BDOPERATION;

    public static String ISO8601_DATE;
    public static final String ISO8601_DATE_FORMAT_1="dd/MM/yyyy";
    public static final String ISO8601_DATE_FORMAT_2="yyyy-MM-dd";

    public static String ISO8601_DATETIME;
    public static final String ISO8601_TIME_FORMAT_1="HH:MM:SS";
    public static final String ISO8601_TIME_FORMAT_2="hh:mm:ss aaa";

    public static String ISO8601_TIME;
    public static final String ISO8601_DATE_TIME_FORMAT_1="dd/MM/yyyy hh:mm:ss aaa";
    public static final String ISO8601_DATE_TIME_FORMAT_2="YYYY-MM-dd HH:MM:SS";

    public static final  DecimalFormat ISO8601_DECIMAL_FORMAT_1 = new DecimalFormat("#,###.00");

    public static void loadParameters(Context pantalla) {
        ConfApp.ISSHOWING_DIALOG=false;


        /*******************************CARGAR CONFIG SERVIDOR*****************************/
        ConfApp.SERVER = Utils.Desencriptar(pantalla, Tbl_Parametro.getClave(pantalla, "SERVER"));
        ConfApp.BDNAME_CATALOG = Utils.Desencriptar(pantalla,Tbl_Parametro.getClave(pantalla, "BDNAMECATALOG"));
        ConfApp.BDNAME_LICENSE = Utils.Desencriptar(pantalla,Tbl_Parametro.getClave(pantalla, "BDNAMELICENSE"));
        ConfApp.BDUSER =Utils.Desencriptar(pantalla, Tbl_Parametro.getClave(pantalla, "BDUSER"));
        ConfApp.BDPASS = Utils.Desencriptar(pantalla,Tbl_Parametro.getClave(pantalla, "BDPASS"));


        /*******************************CARGAR CONFIG LOCAL*****************************/

        ConfApp.SYSTEM_USER = Utils.Desencriptar(pantalla, Tbl_Parametro.getClave(pantalla, "SYSTEM_USER"));
        ConfApp.SYSTEM_PASS = Utils.Desencriptar(pantalla, Tbl_Parametro.getClave(pantalla, "SYSTEM_PASS"));

        ConfApp.UUID_FROM_DEVICE = Settings.Secure.getString(pantalla.getContentResolver(), Settings.Secure.ANDROID_ID);
        ConfApp.UUID_ENCRYPTED =Tbl_Parametro.getClave(pantalla, "UUID");
        ConfApp.UUID_DESENCRYPTED =Utils.Desencriptar(pantalla, UUID_ENCRYPTED);
        ConfApp.DEVICEAUTORIZED = ConfApp.UUID_FROM_DEVICE.trim().toString().equals(ConfApp.UUID_DESENCRYPTED.trim().toString());

        /*******************************CARGAR CONFIG APP*****************************/

        ConfApp.BRANCH_DEFAULT = Tbl_Parametro.getClave(pantalla, "BRANCH_DEFAULT");

        try{
            ConfApp.VISOR_DEFAULT = Integer.parseInt(Tbl_Parametro.getClave(pantalla, "VISOR_DEFAULT"))==0?0:1;
        }catch (Exception e){
            ConfApp.VISOR_DEFAULT = 0;
        }

        try{
            ConfApp.TIMER_SCREEN_WAIT = Integer.parseInt(Tbl_Parametro.getClave(pantalla, "TIMER_SCREEN_WAIT"));
        }catch (Exception e){
            ConfApp.TIMER_SCREEN_WAIT = 5;
        }

        try{
            ConfApp.TIMER_VISOR_GENERAL = Integer.parseInt(Tbl_Parametro.getClave(pantalla, "TIMER_VISOR_GENERAL"));
        }catch (Exception e){
            ConfApp.TIMER_VISOR_GENERAL = 6;
        }

        try{
            ConfApp.TIMER_VISOR_MAYORISTA = Integer.parseInt(Tbl_Parametro.getClave(pantalla, "TIMER_VISOR_MAYORISTA"));
        }catch (Exception e){
            ConfApp.TIMER_VISOR_MAYORISTA = 8;
        }

        try{
            ConfApp.COLOR_DEFAULT = Integer.parseInt(Tbl_Parametro.getClave(pantalla, "COLOR_DEFAULT"));
        }catch (Exception e){
            ConfApp.COLOR_DEFAULT = 500044;
        }

        /*******************************CARGAR CONF PROMOCIONES*****************************/
        try{
            ConfApp.SHOW_MODULE_PROMO = (Integer.parseInt(Tbl_Parametro.getClave(pantalla, "SHOW_MODULE_PROMO"))==1)?true:false;
        }catch (Exception e){
            ConfApp.SHOW_MODULE_PROMO = true;
        }

        try{
            ConfApp.TIMER_PROPO_VISOR = Integer.parseInt(Tbl_Parametro.getClave(pantalla, "TIMER_PROMO_VISOR"));
        }catch (Exception e){
            ConfApp.TIMER_PROPO_VISOR = 25;
        }

        try{
            ConfApp.TIMER_PROMO_DURATION = Integer.parseInt(Tbl_Parametro.getClave(pantalla, "TIMER_PROMO_DURATION"));
        }catch (Exception e){
            ConfApp.TIMER_PROMO_DURATION = 2;
        }

        try{
            ConfApp.SHOW_AFTER_BARCODE_READ = (Integer.parseInt(Tbl_Parametro.getClave(pantalla, "SHOW_AFTER_BARCODE_READ"))==1)?true:false;
        }catch (Exception e){
            ConfApp.SHOW_AFTER_BARCODE_READ = true;
        }
    }

    public static void createDirectoryWork(Context pantalla) {
        Utils.getWorkDirectory(pantalla);
        Utils.getBrandDirectory(pantalla);
        Utils.getScreenDirectory(pantalla);
        Utils.getPromotionDirectory(pantalla);
    }



    public static String print() {
        return "ConfApp{"+
                "URL='" + ConfApp.SERVER + '\'' +
                ", BD=" + ConfApp.BDNAME_CATALOG +
                ", USER ='" + ConfApp.BDUSER + '\'' +
                ", PASS='" + ConfApp.BDPASS + '\'' +
                ", UUID_FROM_DEVICE ='" + ConfApp.UUID_FROM_DEVICE  + '\'' +
                ", UUID_CHECK_DESENCRYPTED='" + ConfApp.UUID_DESENCRYPTED + '\'' +
                ", DEVICEAUTORIZED ='" + ConfApp.DEVICEAUTORIZED  + '\'' +
               /* ", BD_NAME='" + ConfApp.BD_NAME + '\'' +
                ", PATH_BDSAMBOX='" + ConfApp.PATH_BDSAMBOX + '\'' +
                ", PATH_BDDESTINO='" + ConfApp.PATH_BDDESTINO + '\'' +*/
                "'}'";
    }

    public static void loadConection() {
        BDOPERATION = new BDOperacion();
    }
}

package com.sdn.bd.remote;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BDServidorInterface extends AsyncTask {
   public static BDCatalogConeccion V_SPOOL_CATALOG = new BDCatalogConeccion();
 public static BDLicenseConeccion V_SPOOL_LICENSE = new BDLicenseConeccion();

    public static Connection V_OBJECTCONECTION = null;
    public static PreparedStatement V_PREPAREDSTATEMENT =null;
    public static Statement V_STATEMENT;
    public static ResultSet V_RESULSET;

    public static String V_SQLQUERY;
    public static String V_SQLQUERYSELECT;
    public static String V_SQLQUERYFILTER;
    public static String V_SQLQUERYSAVE;
    public static String V_SQLQUERYEDIT;
    public static String V_SQLQUERYINSERT;
    public static String V_SQLQUERYSEARCH;

    public static String V_ERROR_MESSAGE;

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }

}

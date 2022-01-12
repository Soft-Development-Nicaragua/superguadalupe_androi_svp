package com.sdn.bd.remote;

import android.content.Context;

import com.sdn.modelo.Objeto;
import com.sdn.modelo.Precio;
import com.sdn.modelo.Producto;
import com.sdn.modelo.Promocion;
import com.sdn.svp.FrmSplash;
import com.sdn.svp.R;
import com.sdn.util.ConfApp;
import com.sdn.util.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BDOperacion extends BDServidorInterface{


/*
    ArrayList<Object> DatosLineales = new ArrayList<Object>();
    ArrayList<ArrayList<Object>> DatosMatrix = new ArrayList<ArrayList<Object>>();
*/
    public BDOperacion() {
    }

    public boolean GetStatusConecctionCatog() {
        Boolean respuesta = false;
        try {

            V_OBJECTCONECTION = V_SPOOL_CATALOG.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_OBJECTCONECTION.close();
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println(this.getClass().getName() + " Metodo GetStatusConecction - SQLException" + e.getMessage());
        }
        return respuesta;
    }

    public boolean GetStatusConecctionLicense() {
        Boolean respuesta = false;
        try {

            V_OBJECTCONECTION = V_SPOOL_LICENSE.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_OBJECTCONECTION.close();
                respuesta = true;
            }
        } catch (SQLException e) {
            System.out.println(this.getClass().getName() + " Metodo GetStatusConecction - SQLException" + e.getMessage());
        }
        return respuesta;
    }

    public boolean estaAutorizado(String Usuario, String Clave) {
        V_SQLQUERYSEARCH = "SELECT id FROM usuario WHERE  usuario=?  and  clave=? and tipo=1";//OR CODIGO_BARRA like ?
        boolean existe = false;

        try {
            V_OBJECTCONECTION = V_SPOOL_LICENSE.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                V_PREPAREDSTATEMENT.setString(1, Usuario);
                V_PREPAREDSTATEMENT.setString(2, Clave);

                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();
                V_RESULSET.last();
                existe = V_RESULSET.getRow()>0;
                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(this.getClass().getName() + " GetProducto - SQLException" + e.getMessage());
        }
        return existe;
    }

    public Producto GetProducto(String codigo) {
        V_SQLQUERYSEARCH = "SELECT TOP 1 COD_PROD,COD_LIN,NOM_PROD,U_MEDIDA FROM [Facturacion Productos] WHERE  (COD_PROD=? OR  CODIGO_BARRA LIKE ? ) AND LIQUIDADO= ?";//OR CODIGO_BARRA like ?
        Producto producto = new Producto();

        try {
            V_OBJECTCONECTION = V_SPOOL_CATALOG.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                V_PREPAREDSTATEMENT.setString(1, codigo);
                V_PREPAREDSTATEMENT.setString(2, codigo);
                V_PREPAREDSTATEMENT.setString(3, "N");

                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();
                V_RESULSET.beforeFirst();
                while (V_RESULSET.next()) {
                    producto.setCodigo(V_RESULSET.getString(1));
                    producto.setLine(V_RESULSET.getString(2));
                    producto.setNombre(V_RESULSET.getString(3));
                    producto.setUnidad(V_RESULSET.getInt(4));
                }

                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            System.out.println(this.getClass().getName() + " GetProducto - SQLException" + e.getMessage());
            e.printStackTrace();
        }
        return producto;
    }

    public ArrayList<Objeto> GetPresentacion(boolean MostrarSoloPreciosDeContado) {
        V_SQLQUERYSEARCH = "SELECT "+ (MostrarSoloPreciosDeContado? " TOP 1" :"")  +" [Cod_Tip_Prec], [Descripcion] FROM [Tabla Precios (Tipos)]";
        ArrayList<Objeto> Lista_Presentaciones = new ArrayList<Objeto>();
        boolean DEVICE_REGISTER = false;

        try {
            V_OBJECTCONECTION = V_SPOOL_CATALOG.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();
                V_RESULSET.beforeFirst();
                while (V_RESULSET.next()) {
                    Lista_Presentaciones.add(new Objeto(""+V_RESULSET.getInt(1),V_RESULSET.getString(2)));
                }

                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(this.getClass().getName() + " GetProducto - SQLException" + e.getMessage());
        }
        return Lista_Presentaciones;
    }

    public ArrayList<ArrayList<Precio>> GetPrecios(Producto producto, ArrayList<Objeto> ListaPrecios){
        ArrayList<ArrayList<Precio>> Matriz = new ArrayList<ArrayList<Precio>>();

        for(int it=0; it<ListaPrecios.size();it++){
            Matriz.add(GetPreciosxPresentacion(producto,Integer.parseInt(ListaPrecios.get(it).getId()),it));
        }
        return  Matriz;
    }

    public ArrayList<Precio> GetPreciosxPresentacion(Producto producto, Integer TipoPrecio,Integer indice) {
        V_SQLQUERYSEARCH = "SELECT [Tabla Precios].Precio,CAST( [Facturacion Productos (Precios) Det].Valor_C * ([Tabla Impuestos].VALOR / 100 + 1) as decimal(10,2)) AS TOTAL\n" +
                " FROM [Facturacion Productos (Precios) Det] INNER JOIN [Tabla Precios] ON [Facturacion Productos (Precios) Det].Cod_Prec = [Tabla Precios].Cod_Prec INNER JOIN [Facturacion Productos] ON [Facturacion Productos (Precios) Det].COD_PROD = [Facturacion Productos].COD_PROD INNER JOIN [Tabla Impuestos] ON [Facturacion Productos].COD_IMP = [Tabla Impuestos].COD_IMP\n" +
                " WHERE ([Facturacion Productos (Precios) Det].COD_PROD = ?) AND ([Facturacion Productos (Precios) Det].Cod_Tip_Prec = ?)\n" +
                " ORDER BY [Tabla Precios].Precio DESC";
        ArrayList<Precio> Registros = new ArrayList<Precio>();

        try {
            V_OBJECTCONECTION = V_SPOOL_CATALOG.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                V_PREPAREDSTATEMENT.setString(1, producto.getCodigo());
                V_PREPAREDSTATEMENT.setInt(2, TipoPrecio);

                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();
                V_RESULSET.beforeFirst();
                while (V_RESULSET.next()) {
                    Registros.add(new Precio(""+V_RESULSET.getString(1),V_RESULSET.getString(2)));
                }

                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(this.getClass().getName() + " GetProducto - SQLException" + e.getMessage());
        }

        if(indice==0){ //ES PRESENTACION DE CONTADO
           // System.out.println("Presentacion de contado");
            Promocion promocion =  getHappyHour(producto);
            //System.out.println("getHappyHour Retorna:"+promocion.toString2());

            if(promocion.getTipo()!=-1){//SI TIENE HAPPYHOUR
                //System.out.println("Tiene HappyHour:"+promocion.toString2());

                for(int it=0;it< Registros.size();it++){
                    //System.out.println("Precio antes del cambio: "+Registros.get(it).toString2());
                    Double PrecioAnterior = Double.parseDouble( Registros.get(it).getPrecio());
                    Double precio = PrecioAnterior - (PrecioAnterior * (promocion.getDescuento()/100));
                    Registros.get(it).setPrecio(""+precio);//new DecimalFormat("#.0#").format(precio));
                    Registros.get(it).setPromocion(true);
                    Registros.get(it).setDescuento(promocion.getDescuento());
                    //System.out.println("Precio despues del cambio: "+Registros.get(it).toString2());
                }
            }else{
                //System.out.println("No Tiene HappyHour");
            }
        }else {
            //System.out.println("Presentacion no valida");
        }
        return Registros;
    }

    private  Promocion getHappyHour(Producto producto) {
       ArrayList<Promocion> promo_x_producto = new ArrayList<Promocion>();
        ArrayList<Promocion> promo_x_linea = new ArrayList<Promocion>();
        Promocion promo_valida = new Promocion();
        java.util.Date FechaActual = GetCurrentTimeStamp();

       // System.out.println("Producto: "+ producto.toString2());
        promo_x_producto = getPromocion(1,producto.getCodigo());

        if(promo_x_producto.isEmpty()){ //NO TIENE PROMOCION POR PRODUCTO
           // System.out.println("Tipo Descuento: 3  Linea"+producto.getLine());
            promo_x_linea = getPromocion(3,producto.getLine());
           // System.out.println("Promocion de Linea");

            if(!promo_x_linea.isEmpty()){ //SI TIENE PROMOCION POR LINEA
                if(promo_x_linea.get(0).getFiltroxDias()){
                  //  System.out.println("Filtrado por dias");
                    promo_valida = ValidarIntervaloPromocion(FechaActual, promo_x_linea.get(0));
                }else{
                 //   System.out.println("Filtrado por rangos");
                    promo_valida= promo_x_linea.get(0);
                }
            }
        }else{// SI TIENE PROMOCION POR PRODUCTO
            //System.out.println("Promocion de Producto");
            if(promo_x_producto.get(0).getFiltroxDias()) {
                System.out.println("Filtrado por dias");
                promo_valida = ValidarIntervaloPromocion(FechaActual, promo_x_producto.get(0));
            }else {
              //  System.out.println("Filtrado por rangos");
                promo_valida = promo_x_producto.get(0);
            }
        }
        return promo_valida;
    }

    private Promocion ValidarIntervaloPromocion(Date fechaActual, Promocion promo) {
        Calendar c= Calendar.getInstance();
        c.setTime(fechaActual);
        Integer dia = c.get(Calendar.DAY_OF_WEEK);
        Boolean respuesta = false;

        switch (dia){
            case Calendar.SUNDAY:
               respuesta = promo.getDomingo();
            case Calendar.MONDAY:
                respuesta = promo.getLunes();
            case Calendar.TUESDAY:
                respuesta = promo.getMartes();
            case Calendar.WEDNESDAY:
                respuesta = promo.getMiercoles();
            case Calendar.THURSDAY:
                respuesta = promo.getJueves();
            case Calendar.FRIDAY:
                respuesta = promo.getViernes();
            case Calendar.SATURDAY:
                respuesta = promo.getSabado();
        }
        return  respuesta?promo: new Promocion();
    }

    private ArrayList<Promocion> getPromocion(Integer TipoDescuento, String ID){
        V_SQLQUERYSEARCH = "SELECT [TIPO_DESC]\n" +
                "      ,[ID]\n" +
                "      ,[DESCUENTO]\n" +
                "      ,CAST( CASE WHEN DIAS_VALIDOS IS NOT NULL THEN 1 ELSE 0 END AS BIT) AS DIAS_VALIDOS\n" +
                "      ,CAST(COALESCE( [DOMINGO],0) AS BIT)  AS DOMINGO\n" +
                "      ,CAST(COALESCE( [LUNES],0) AS BIT)  AS LUNES\n" +
                "      ,CAST(COALESCE( [MARTES],0) AS BIT)  AS MARTES\n" +
                "      ,CAST(COALESCE( [MIERCOLES],0) AS BIT)  AS MIERCOLES\n" +
                "      ,CAST(COALESCE( [JUEVES],0)  AS BIT) AS JUEVES\n" +
                "      ,CAST(COALESCE( [VIERNES],0) AS BIT)  AS  VIERNES \n" +
                "      ,CAST(COALESCE( [SABADO],0)  AS BIT) AS SABADO" +
                "  FROM [scm].[dbo].[Facturacion Descuentos]\n" +
                "  WHERE TIPO_DESC=? AND ID=? AND COD_SUC=? AND (GETDATE()>=(DATEADD(day, 0, DATEDIFF(day, 0, F_INICIO)) +DATEADD(day, 0 - DATEDIFF(day, 0, H_INICIO), H_INICIO))) AND (GETDATE()<=(DATEADD(day, 0, DATEDIFF(day, 0, F_FIN)) +DATEADD(day, 0 - DATEDIFF(day, 0, H_FIN), H_FIN)))\n";
        ArrayList<Promocion> Registros = new ArrayList<Promocion>();

       // System.out.println("Parametro busqueda: TipoDescuento:[" + TipoDescuento+"] ID:["+ID+"] Sucursal:["+ConfApp.BRANCH_DEFAULT+"]");

        try {
            V_OBJECTCONECTION = V_SPOOL_CATALOG.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                V_PREPAREDSTATEMENT.setInt(1, TipoDescuento);
                V_PREPAREDSTATEMENT.setString(2, ID);
                V_PREPAREDSTATEMENT.setString(3, ConfApp.BRANCH_DEFAULT);

                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();
                V_RESULSET.beforeFirst();
                while (V_RESULSET.next()) {
                    Promocion p = new Promocion();
                    p.setTipo(V_RESULSET.getInt(1));
                    p.setCodigo(V_RESULSET.getString(2));
                    p.setDescuento(V_RESULSET.getDouble(3));
                    p.setFiltroxDias(V_RESULSET.getBoolean(4));
                    p.setDomingo(V_RESULSET.getBoolean(5));
                    p.setLunes(V_RESULSET.getBoolean(6));
                    p.setMartes(V_RESULSET.getBoolean(7));
                    p.setMiercoles(V_RESULSET.getBoolean(8));
                    p.setJueves(V_RESULSET.getBoolean(9));
                    p.setViernes(V_RESULSET.getBoolean(10));
                    p.setSabado(V_RESULSET.getBoolean(11));
                    Registros.add(p);
                   // System.out.println(p.toString2());
                }

                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
           // System.out.println(this.getClass().getName() + " getPromocion - SQLException" + e.getMessage());
        }
       // System.out.println("Cantidad de promocion: " + Registros.size());
        return Registros;
    }

    public ArrayList<Objeto> GetSucursales() {
        V_SQLQUERYSEARCH = "SELECT COD_SUC,Nombre FROM Sucursales";
        ArrayList<Objeto> Registros = new ArrayList<Objeto>();

        try {
            V_OBJECTCONECTION = V_SPOOL_CATALOG.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();
                V_RESULSET.beforeFirst();
                while (V_RESULSET.next()) {
                    Registros.add(new Objeto(""+V_RESULSET.getString(1),V_RESULSET.getString(2).toUpperCase()));
                }

                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(this.getClass().getName() + " GetSucursales - SQLException" + e.getMessage());
        }
        return Registros;
    }

    public java.util.Date GetCurrentTimeStamp() {
        V_SQLQUERYSEARCH = "SELECT GETDATE()";
        java.util.Date fechaDestino = new java.util.Date();

        try {
            V_OBJECTCONECTION = V_SPOOL_CATALOG.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH);
                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();
                while (V_RESULSET.next()) {
                    fechaDestino = new java.util.Date(V_RESULSET.getTimestamp(1).getTime());
                    //valor = Utils.C_DateToFormat(fechaDestino, ConfApp.ISO8601_DATE_TIME_FORMAT_1);// Utils.C_TimeStampToCustom(fecha, ConfApp.ISO8601_FORMATTIMESTAMP_APP);
                }
                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            System.out.println(this.getClass().getName() + " GetStatusConecction - SQLException" + e.getMessage());
        }
        return fechaDestino;
    }

    public boolean GetMyLicense() {
        V_SQLQUERYSEARCH = "SELECT TOP 1 activo FROM dispositivo WHERE id=? AND licencia=?";
        boolean DEVICE_REGISTER = false;

        try {
            V_OBJECTCONECTION = V_SPOOL_LICENSE.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                V_PREPAREDSTATEMENT.setString(1, ConfApp.UUID_FROM_DEVICE);
                V_PREPAREDSTATEMENT.setString(2, ConfApp.UUID_ENCRYPTED);

                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();

                int size = 0;
                V_RESULSET.last();
                size = V_RESULSET.getRow();
                V_RESULSET.beforeFirst();
                while (V_RESULSET.next()) {
                    DEVICE_REGISTER = V_RESULSET.getBoolean(1);
                }

                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            System.out.println(this.getClass().getName() + " GetStatusConecction - SQLException" + e.getMessage());
        }
        return DEVICE_REGISTER;
    }

    public boolean SetDeviceLicense() {
        V_SQLQUERYINSERT = "IF NOT EXISTS(SELECT * FROM DISPOSITIVO WHERE id=?) INSERT INTO DISPOSITIVO(id,licencia,numero_serie,activo) VALUES (?,?,'',0) ELSE  UPDATE DISPOSITIVO SET licencia=? WHERE id=?";
        int resultado_query = 0;

        try {
            V_OBJECTCONECTION = V_SPOOL_LICENSE.getConnection();

            // referencia.appendMessage("<B>REGISTRANDO DISPOSITO EL SERVIDOR...</B>");

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYINSERT);
                V_PREPAREDSTATEMENT.setString(1, ConfApp.UUID_FROM_DEVICE);
                V_PREPAREDSTATEMENT.setString(2, ConfApp.UUID_FROM_DEVICE);
                V_PREPAREDSTATEMENT.setString(3, ConfApp.UUID_ENCRYPTED);
                V_PREPAREDSTATEMENT.setString(4, ConfApp.UUID_ENCRYPTED);
                V_PREPAREDSTATEMENT.setString(5, ConfApp.UUID_FROM_DEVICE);
                resultado_query = V_PREPAREDSTATEMENT.executeUpdate();

                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            System.out.println(this.getClass().getName() + " GetMyLicense - SQLException" + e.getMessage());
        }
        return resultado_query > 0;
    }

    public boolean GetStatusLicense(Context referencia) {
        FrmSplash Pantalla = (FrmSplash) referencia;


        V_SQLQUERYSEARCH = "DECLARE @t TABLE (i uniqueidentifier default newsequentialid(),  t as LOWER(i))";
        V_SQLQUERYSEARCH += "INSERT INTO @t default values;";
        V_SQLQUERYSEARCH += "SELECT (SELECT VALOR FROM PARAMETER_INV WHERE CAMPO='UUID') as UUID_PARAM, (select top 1 [file_guid] from [sys].[database_files]) as UUID_BD, (SELECT VALOR FROM PARAMETER_INV WHERE CAMPO='MACADDRESS') as MACADDRESS_PARAM,substring(t,25,2) + '-' + substring(t,27,2) + '-' + substring(t,29,2) + '-' + substring(t,31,2) + '-' + substring(t,33,2) + '-' +  substring(t,35,2) AS MACADRESS_BD, (SELECT VALOR FROM PARAMETER_INV WHERE CAMPO='LICENSE') as LICENSE_TOTAL, (SELECT COUNT(*) FROM dispositivo) AS LICENSE_USED, @@servername AS SERVER_NAME, (SELECT VALOR FROM PARAMETER_INV WHERE CAMPO='SERVER_NAME') as SERVER_NAME_PARAM, DB_NAME() AS BD_NAME, (SELECT VALOR FROM PARAMETER_INV WHERE CAMPO='SERVICE') as BD_NAME_PARAM from @t";

        String BD_UUID_PARAM = "", BD_UUID = " ", SERVER_MACADDRESS_PARAM = "", SERVER_MACADDRESS = " ", BD_NAME = "", BD_NAME_PARAM = "", SERVER_NAME = "", SERVER_NAME_PARAM = "";
        Integer LICENSE_TOTAL = 0, LICENSE_USED = 0;

        boolean STATUS_LICENSE = false;

        try {
            V_OBJECTCONECTION = V_SPOOL_LICENSE.getConnection();

            if (V_OBJECTCONECTION != null) {
                V_PREPAREDSTATEMENT = (PreparedStatement) V_OBJECTCONECTION.prepareStatement(V_SQLQUERYSEARCH);
                V_RESULSET = V_PREPAREDSTATEMENT.executeQuery();

                while (V_RESULSET.next()) {
                    BD_UUID_PARAM = Utils.Desencriptar(referencia, V_RESULSET.getString(1));
                    BD_UUID = V_RESULSET.getString(2);
                    SERVER_MACADDRESS_PARAM = Utils.Desencriptar(referencia, V_RESULSET.getString(3));
                    SERVER_MACADDRESS = V_RESULSET.getString(4);
                    LICENSE_TOTAL = Integer.parseInt(Utils.Desencriptar(referencia, V_RESULSET.getString(5)));
                    LICENSE_USED = V_RESULSET.getInt(6);
                    SERVER_NAME = V_RESULSET.getString(7);
                    SERVER_NAME_PARAM = Utils.Desencriptar(referencia, V_RESULSET.getString(8));
                    BD_NAME = V_RESULSET.getString(9);
                    BD_NAME_PARAM = Utils.Desencriptar(referencia, V_RESULSET.getString(10));
                }

                V_RESULSET.close();
                V_PREPAREDSTATEMENT.close();
                V_OBJECTCONECTION.close();
            }
        } catch (SQLException e) {
            System.out.println(this.getClass().getName() + " GetStatusLicense - SQLException" + e.getMessage());
            e.printStackTrace();
        }

        if (SERVER_NAME.equals(SERVER_NAME_PARAM) && SERVER_MACADDRESS.equals(SERVER_MACADDRESS_PARAM)){
            if (BD_NAME.equals(BD_NAME_PARAM) && BD_UUID.equals(BD_UUID_PARAM)){
                if (LICENSE_USED <= LICENSE_TOTAL) {
                    if (LICENSE_USED < LICENSE_TOTAL)
                        SetDeviceLicense();
                    STATUS_LICENSE = GetMyLicense();
                    if(!STATUS_LICENSE) Pantalla.printToas( referencia.getResources().getString(R.string.dipositivo_noautorizado));
                }else Pantalla.printToas(referencia.getResources().getString(R.string.licencias_excedidas));
            }else  Pantalla.printToas(referencia.getResources().getString(R.string.bd_noconcuerda));  //System.out.println("<B><font color='#AB2A3E'>LA LICENCIA DE LA BD NO CONCUERDA</FONT><B>");
        }else Pantalla.printToas(referencia.getResources().getString(R.string.servidor_noconcuerda));  //System.out.println("<B><font color='#AB2A3E'>LA DIRECCION DEL SERVIDOR NO CONCUERDA</FONT><B>");

        return STATUS_LICENSE;
    }

}

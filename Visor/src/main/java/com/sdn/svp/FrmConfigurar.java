package com.sdn.svp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.sdn.controlador.Tbl_Parametro;
import com.sdn.modelo.Objeto;
import com.sdn.util.ConfApp;
import com.sdn.util.Utils;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class FrmConfigurar extends AppCompatActivity {

    TabHost tabs;
    Spinner VISOR_DEFAULT,BRANCH_DEFAULT;
    public TextView SERVER, BDNAMECATALOG, BDUSER, BDPASS,SYSTEM_USER, SYSTEM_PASS, TIMER_SCREEN_WELCOME,BDNAMELICENSE;
    private EditText TIMER_VISOR_GENERAL,TIMER_VISOR_MAYORISTA;
    private EditText TIMER_PROPO_VISOR,TIMER_PROMO_DURATION;
    private TextView Carpeta_Logo,Carpeta_Bienvenida,Carpeta_Promocion;
    private EditText IDDISPOSITIVO ;
    private ListView ListaImagenes;

    private Switch   Attrib_Promocion, Modulo_Promocion;
    private Button BtnGuardarConfSevidor,BtnGuardarProbarSevidor,BtnGuardarConfLocal,BtnGuardarConfApp,BtnGuardarConfPromocion;

    ArrayAdapter<Objeto> BRANCH_ADAPTER;
    ArrayList<Objeto> ListaSucursales= new ArrayList<Objeto>();
    ArrayList<Objeto> ListaVisor = new ArrayList<Objeto>();
    ArrayList<String> ListaArchivo = new ArrayList<String>();
    ArrayAdapter<String> adapterImg;
    private TextView Color_Fondo;


/*    @Override
    protected void onPostResume() {
        super.onPostResume();
        System.out.println("OnPostResume se ejecuta despues de Oncreate"+this.getLocalClassName());
        ocultarTeclado();
    }*/
    @Override public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//Sin este metodo el boton regresar no funciona
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.frm_configurar);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        setItemsOnTab();
        ConfApp.SCREEN_NAME = this.getClass().getName();

        SERVER = (EditText) findViewById(R.id.p1_txtServidor);
        SERVER.setText(Utils.Desencriptar(FrmConfigurar.this,Tbl_Parametro.getClave(FrmConfigurar.this,"SERVER")));

        BDNAMECATALOG =(EditText) findViewById(R.id.p1_txtCatalogo);
        BDNAMECATALOG.setText(Utils.Desencriptar(FrmConfigurar.this,Tbl_Parametro.getClave(FrmConfigurar.this,"BDNAMECATALOG")));

        BDNAMELICENSE=(EditText) findViewById(R.id.p1_txtPermisos);
        BDNAMELICENSE.setText(Utils.Desencriptar(FrmConfigurar.this,Tbl_Parametro.getClave(FrmConfigurar.this,"BDNAMELICENSE")));

        BDUSER = (EditText) findViewById(R.id.p1_txtUsuario);
        BDUSER.setText(Utils.Desencriptar(FrmConfigurar.this,Tbl_Parametro.getClave(FrmConfigurar.this,"BDUSER")));

        BDPASS = (EditText) findViewById(R.id.p1_txtClave);
        BDPASS.setText(Utils.Desencriptar(FrmConfigurar.this,Tbl_Parametro.getClave(FrmConfigurar.this,"BDPASS")));

        BtnGuardarConfSevidor = (Button)findViewById(R.id.p11_btnGuardarConfServidor);
        BtnGuardarConfSevidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarConfiguracionServidor();
                ConfApp.loadParameters(FrmConfigurar.this);
            }
        });

        BtnGuardarProbarSevidor = (Button)findViewById(R.id.p11_btnProbarConeccion);
        BtnGuardarProbarSevidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarConfiguracionServidor();
                ConfApp.loadParameters(FrmConfigurar.this);
                mostrarEstadoDeConeccion();
            }
        });

        SYSTEM_USER = (EditText) findViewById(R.id.p11_txtusuariolocal);
        SYSTEM_USER.setText(ConfApp.SYSTEM_USER);

        SYSTEM_PASS = (EditText) findViewById(R.id.p11_txtclavelocal);
        SYSTEM_PASS.setText(ConfApp.SYSTEM_PASS);

        IDDISPOSITIVO = (EditText) findViewById(R.id.p11_txtIdDispositivo);
        IDDISPOSITIVO.setText(ConfApp.UUID_DESENCRYPTED);
        IDDISPOSITIVO.setCompoundDrawablesWithIntrinsicBounds(null,null,ConfApp.DEVICEAUTORIZED?getResources().getDrawable(R.drawable.img_deviceok_24x24):getResources().getDrawable(R.drawable.img_deviceerror_24x24),null);

        BtnGuardarConfLocal = (Button)findViewById(R.id.p11_btnGuardarConfLocal);
        BtnGuardarConfLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarCOnfiguracionLocal();
                ConfApp.loadParameters(FrmConfigurar.this);
                IDDISPOSITIVO.setCompoundDrawablesWithIntrinsicBounds(null,null,ConfApp.DEVICEAUTORIZED?getResources().getDrawable(R.drawable.img_deviceok_24x24):getResources().getDrawable(R.drawable.img_deviceerror_24x24),null);
            }
        });

        BRANCH_ADAPTER = new ArrayAdapter<Objeto>(this, R.layout.item_spinner, R.id.i05_codigo_token, ListaSucursales); //ArrayAdapter.createFromResource(this,R.array.Visor, android.R.layout.simple_spinner_item);
        BRANCH_ADAPTER.setDropDownViewResource(R.layout.item_spinner);
        {
            BRANCH_DEFAULT =(Spinner) findViewById(R.id.p11_txtsucursal);
            BRANCH_DEFAULT.setAdapter(BRANCH_ADAPTER);
            BRANCH_DEFAULT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    Carpeta_Logo.setText(Utils.getBrandDirectory(FrmConfigurar.this)+""+ListaSucursales.get(position).getId()+".png");
                    Carpeta_Bienvenida.setText(Utils.getScreenDirectory(FrmConfigurar.this)+""+ListaSucursales.get(position).getId()+".jpng");
                }catch (Exception e){
                    Carpeta_Logo.setText(Utils.getBrandDirectory(FrmConfigurar.this));
                    Carpeta_Bienvenida.setText(Utils.getScreenDirectory(FrmConfigurar.this));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

            });
        }

        VISOR_DEFAULT = (Spinner) findViewById(R.id.p11_txtVisor);
        {
            ListaVisor.add(new Objeto("0",getResources().getString(R.string.lblVisorGeneral).toUpperCase()));
            ListaVisor.add(new Objeto("1",getResources().getString(R.string.lblVisorMayorista).toUpperCase()));
            ArrayAdapter<Objeto> adapter = new ArrayAdapter<Objeto>(this, R.layout.item_spinner, R.id.i05_codigo_token, ListaVisor);
            adapter.setDropDownViewResource(R.layout.item_spinner);
            VISOR_DEFAULT.setAdapter(adapter);
            VISOR_DEFAULT.setSelection(ConfApp.VISOR_DEFAULT);
        }

        TIMER_SCREEN_WELCOME = (EditText) findViewById(R.id.p11_txtTiempoEspera);
        TIMER_SCREEN_WELCOME.setText(ConfApp.TIMER_SCREEN_WAIT.toString());

        TIMER_VISOR_GENERAL = (EditText) findViewById(R.id.p11_txtTiempoVisorGeneral);
        TIMER_VISOR_GENERAL.setText(ConfApp.TIMER_VISOR_GENERAL.toString());

        TIMER_VISOR_MAYORISTA = (EditText) findViewById(R.id.p11_txtTiempoVisorMayorista);
        TIMER_VISOR_MAYORISTA.setText(ConfApp.TIMER_VISOR_MAYORISTA.toString());

        Carpeta_Logo = (TextView) findViewById(R.id.p11_txtCarpetaLogo);
        Carpeta_Logo.setText(Utils.getBrandDirectory(FrmConfigurar.this));

        Carpeta_Bienvenida = (TextView) findViewById(R.id.p11_txtCarpetaBienvenida);
        Carpeta_Bienvenida.setText(Utils.getScreenDirectory(FrmConfigurar.this));

        Color_Fondo = (TextView) findViewById(R.id.p11_txtColorFondo);
        Color_Fondo.setBackgroundColor(ConfApp.COLOR_DEFAULT);
        Color_Fondo.setTextColor(ConfApp.COLOR_DEFAULT);
        Color_Fondo.setText(""+ConfApp.COLOR_DEFAULT);
        Color_Fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialogColor();
            }
        });


        BtnGuardarConfApp = (Button)findViewById(R.id.p11_btnGuardarConfApp);
        BtnGuardarConfApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarConfiguracionApp();
                ConfApp.loadParameters(FrmConfigurar.this);
            }
        });

        Carpeta_Promocion = (TextView) findViewById(R.id.p11_txtCarpetaPromocion);
        Carpeta_Promocion.setText(Utils.getPromotionDirectory(FrmConfigurar.this));

        Modulo_Promocion = (Switch) findViewById(R.id.p11_cbxpromocion);
        {
            Modulo_Promocion.setChecked(ConfApp.SHOW_MODULE_PROMO);
            //((RelativeLayout) findViewById(R.id.p11_pnlpromocion)).setEnabled(Modulo_Promocion.isChecked());
            ((RelativeLayout) findViewById(R.id.p11_pnlpromocion)).setVisibility( Modulo_Promocion.isChecked() ?View.VISIBLE:View.INVISIBLE);
            Modulo_Promocion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //((RelativeLayout) findViewById(R.id.p11_pnlpromocion)).setEnabled(Modulo_Promocion.isChecked());
                    ((RelativeLayout) findViewById(R.id.p11_pnlpromocion)).setVisibility( Modulo_Promocion.isChecked() ?View.VISIBLE:View.INVISIBLE);
                }
            });
        }

        TIMER_PROPO_VISOR = (EditText) findViewById(R.id.p11_txtTiempoPromocion);
        TIMER_PROPO_VISOR.setText(ConfApp.TIMER_PROPO_VISOR.toString());

        TIMER_PROMO_DURATION = (EditText) findViewById(R.id.p11_txtTiempoDuracionPromocion);
        TIMER_PROMO_DURATION.setText(ConfApp.TIMER_PROMO_DURATION.toString());

        Attrib_Promocion = (Switch) findViewById(R.id.p11_cbxpromocionattrib);
        Attrib_Promocion.setChecked(ConfApp.SHOW_AFTER_BARCODE_READ);

        ListaImagenes = (ListView) findViewById(R.id.p11_txtListaImagenes);
        adapterImg = new ArrayAdapter<String>(this, R.layout.item_spinner, R.id.i05_codigo_token, ListaArchivo);
        adapterImg.setDropDownViewResource(R.layout.item_spinner);
        ListaImagenes.setAdapter(adapterImg);

        BtnGuardarConfPromocion = (Button)findViewById(R.id.p11_btnGuardarConfPromocion);
        BtnGuardarConfPromocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarConfiguracionPromocion();
                ConfApp.loadParameters(FrmConfigurar.this);
            }
        });

        ocultarTeclado();
    }

    public void showSelectDialogColor(){
        AmbilWarnaDialog dialog2 = new AmbilWarnaDialog(FrmConfigurar.this, ConfApp.COLOR_DEFAULT, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Color_Fondo.setBackgroundColor(ConfApp.COLOR_DEFAULT);
                Color_Fondo.setTextColor(ConfApp.COLOR_DEFAULT);
                Color_Fondo.setText(""+ConfApp.COLOR_DEFAULT);
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
               Color_Fondo.setBackgroundColor(color);
               Color_Fondo.setTextColor(color);
               Color_Fondo.setText(""+color);
            }
        });
        dialog2.show();
    }

    public void mostrarEstadoDeConeccion(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    cargarEstadoDeConeccion( ConfApp.BDOPERATION.GetStatusConecctionCatog(),ConfApp.BDOPERATION.GetStatusConecctionLicense());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void cargarEstadoDeConeccion(boolean getStatusConecctionCatog, boolean getStatusConecctionLicense) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BDNAMECATALOG.setCompoundDrawablesWithIntrinsicBounds(null,null,getStatusConecctionCatog?getResources().getDrawable(R.drawable.ic_action_wireless):getResources().getDrawable(R.drawable.ic_action_wireless_disabled),null);
                BDNAMELICENSE.setCompoundDrawablesWithIntrinsicBounds(null,null,getStatusConecctionLicense?getResources().getDrawable(R.drawable.ic_action_wireless):getResources().getDrawable(R.drawable.ic_action_wireless_disabled),null);
            }
        });
    }

    public void mostrarListaSucursales(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    cargarListaSucursales( ConfApp.BDOPERATION.GetSucursales());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public  void cargarListaSucursales(ArrayList<Objeto> lista){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Integer IndiceSeleccionado =-1;
                ListaSucursales.clear();
                BRANCH_ADAPTER.notifyDataSetChanged();

                for(int it=0 ; it<lista.size(); it++){
                    ListaSucursales.add(lista.get(it));
                    BRANCH_ADAPTER.notifyDataSetChanged();

                    if(lista.get(it).getId().equals(ConfApp.BRANCH_DEFAULT)){
                        IndiceSeleccionado =it;
                    }
                }

                try {
                    BRANCH_DEFAULT.setSelection(IndiceSeleccionado);
                }catch (Exception e){}
            }
        });
    }

    public void mostrarListaImagenesPromocionales(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    cargarListaImagenesPromocionadas(Utils.cargarListaDeImagenesPromocionales(FrmConfigurar.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public  void cargarListaImagenesPromocionadas(ArrayList<Objeto> lista){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListaArchivo.clear();
                adapterImg.notifyDataSetChanged();
                for (int it =0; it< lista.size();it++){
                    ListaArchivo.add((it+1)+") "+lista.get(it).getId());
                    adapterImg.notifyDataSetChanged();
                }
            }
        });
    }

    public void guardarConfiguracionServidor(){
        guardarParametro("SERVER", SERVER.getText().toString(),true);
        guardarParametro("BDNAMECATALOG", BDNAMECATALOG.getText().toString(),true);
        guardarParametro("BDNAMELICENSE", BDNAMELICENSE.getText().toString(),true);
        guardarParametro("BDUSER", BDUSER.getText().toString(),true);
        guardarParametro("BDPASS", BDPASS.getText().toString(),true);
        Toast.makeText(getApplicationContext() ,"Registros Modificado",Toast.LENGTH_LONG).show();
    }

    public  void guardarCOnfiguracionLocal(){
        guardarParametro("SYSTEM_USER", SYSTEM_USER.getText().toString(),true);
        guardarParametro("SYSTEM_PASS", SYSTEM_PASS.getText().toString(),true);
        guardarParametro("UUID", IDDISPOSITIVO.getText().toString(),true);
        Toast.makeText(getApplicationContext() ,"Registros Modificado",Toast.LENGTH_LONG).show();
    }

    public void guardarConfiguracionApp(){
        if(ListaSucursales.size()>0){
            Objeto item = (Objeto) BRANCH_DEFAULT.getSelectedItem();
            guardarParametro("BRANCH_DEFAULT", item.getId(),false);
        }else{
            Toast.makeText(FrmConfigurar.this, "Parametro: BRANCH_DEFAULT No puede ser vacio", Toast.LENGTH_SHORT).show();
        }
        guardarParametro("VISOR_DEFAULT", ""+VISOR_DEFAULT.getSelectedItemPosition(),false);
        guardarParametro("TIMER_SCREEN_WAIT", TIMER_SCREEN_WELCOME.getText().toString(),false);
        guardarParametro("TIMER_VISOR_GENERAL", TIMER_VISOR_GENERAL.getText().toString(),false);
        guardarParametro("TIMER_VISOR_MAYORISTA", TIMER_VISOR_MAYORISTA.getText().toString(),false);
        guardarParametro("COLOR_DEFAULT", Color_Fondo.getText().toString(),false);
        Toast.makeText(getApplicationContext() ,"Registros Modificado",Toast.LENGTH_LONG).show();
    }

    public void guardarConfiguracionPromocion(){
        guardarParametro("SHOW_MODULE_PROMO",Modulo_Promocion.isChecked()?"1":"0",false);
        guardarParametro("TIMER_PROMO_VISOR", TIMER_PROPO_VISOR.getText().toString(),false);
        guardarParametro("TIMER_PROMO_DURATION", TIMER_PROMO_DURATION.getText().toString(),false);
        guardarParametro("SHOW_AFTER_BARCODE_READ",Attrib_Promocion.isChecked()?"1":"0",false);
        Toast.makeText(getApplicationContext() ,"Registros Modificado",Toast.LENGTH_LONG).show();
    }

    private void guardarParametro(String Parametro, String Valor, Boolean Encriptar) {
        if(Valor.length()>0){
            Tbl_Parametro.modificar(FrmConfigurar.this,Parametro,(Encriptar?Utils.Encriptar(FrmConfigurar.this,Valor):Valor) );
        }else{
            Toast.makeText(FrmConfigurar.this, "Parametro: "+Parametro+ "No puede ser vacio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnu_configurar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.m1_action_ocultarteclado) {
            ocultarTeclado();
        }
        if (id == R.id.m1_action_cerrar) {
            cerrarApliacion();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setItemsOnTab() {
        Resources res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec1, spec2, spec3,spec4;

        spec1 = tabs.newTabSpec(getResources().getString(R.string.Tab1));
        spec1.setContent(R.id.tab1);
        spec1.setIndicator(getResources().getString(R.string.Tab1));

        tabs.addTab(spec1);

        spec2 = tabs.newTabSpec(getResources().getString(R.string.Tab2));
        spec2.setContent(R.id.tab2);
        spec2.setIndicator(getResources().getString(R.string.Tab2));

        tabs.addTab(spec2);

        spec3 = tabs.newTabSpec(getResources().getString(R.string.Tab3));
        spec3.setContent(R.id.tab3);
        spec3.setIndicator(getResources().getString(R.string.Tab3));

        tabs.addTab(spec3);

        spec4 = tabs.newTabSpec(getResources().getString(R.string.Tab4));
        spec4.setContent(R.id.tab4);
        spec4.setIndicator(getResources().getString(R.string.Tab4));

        tabs.addTab(spec4);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                ocultarTeclado();

                if (tabId.equals(getResources().getString(R.string.Tab3))) {
                    mostrarListaSucursales();
                } else if (tabId.equals(getResources().getString(R.string.Tab4))) {
                    mostrarListaImagenesPromocionales();
                }

            }
        });

    }

    private void cerrarApliacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<small>Desea cerrar la Aplicacion..?</small>"))
                .setIcon(R.drawable.img_user_profile32x32)
                .setCancelable(false)
                .setTitle(Html.fromHtml("<font color='#FF7F27'><small>Confirmar cierre de programa</small></font>"))
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("SI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
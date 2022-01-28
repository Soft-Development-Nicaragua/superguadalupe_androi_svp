package com.sdn.svp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdn.modelo.Objeto;
import com.sdn.modelo.Precio;
import com.sdn.modelo.Producto;
import com.sdn.sound.SoundManager;
import com.sdn.util.ConfApp;
import com.sdn.util.Utils;

import java.util.ArrayList;

public class FrmVisorGeneral extends AppCompatActivity {

    private boolean estaLeyendo;
    private SoundManager sound;
    private int msg_error, msg_ok;
    private TextView txtNombreProducto, txtPrecioUnitario;
    private LinearLayout panelPresentacion, panelPrecio;
    ArrayList<Objeto> ListaPresentaciones;
    ArrayList<ArrayList<Precio>> ListaPrecios;
    private Producto producto = new Producto();
    private Integer PRECIOAMOSTRAR = 0;
    private boolean ISVISORGENERAL = false;
    ArrayList<Objeto> ListaImagenesPromociones = new ArrayList<Objeto>();
    Integer indiceImagenAPromocionar = 0;
    private AlertDialog mAlertDialog;

    LinearLayout.LayoutParams lppresentaciones = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.MATCH_PARENT);
    private LinearLayout panelPrincipal;
    private ImageView logoSuperior;
    private TextView lblEscanearProducto;
    String CODIGO_LEIDO="";
    private ImageView image_promo1,image_promo2;

    {
        lppresentaciones.leftMargin = 10;
        lppresentaciones.topMargin = 10;
        lppresentaciones.rightMargin = 10;
        lppresentaciones.bottomMargin = 10;
        lppresentaciones.weight = 1;
        lppresentaciones.gravity = Gravity.CENTER_VERTICAL;
    }

    LinearLayout.LayoutParams lpprecio = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
    {
        lpprecio.leftMargin = 40;
        lpprecio.topMargin = 40;
        lpprecio.rightMargin = 40;
        lpprecio.bottomMargin = 40;
        lpprecio.weight = 1;
    }

    LinearLayout.LayoutParams lptitulo = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.MATCH_PARENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
    {
        lptitulo.weight = 1;
        lptitulo.gravity = Gravity.CENTER_HORIZONTAL;
    }

    LinearLayout.LayoutParams lpvalor = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.MATCH_PARENT, 75);
    {
        lptitulo.weight = 1;
        lptitulo.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_visorgeneral);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ConfApp.SCREEN_NAME = FrmVisorGeneral.this.getClass().getName();
        ISVISORGENERAL = (ConfApp.VISOR_DEFAULT == 0) ? true : false;
        ConfApp.ISSHOWING_DIALOG =false;
        cargarImagenesDePromociones();

        sound = new SoundManager(getApplicationContext());
        msg_error = sound.load(R.raw.msg_error);
        msg_ok = sound.load(R.raw.msg_ok);

        panelPrincipal = (LinearLayout) findViewById(R.id.p3_pnlPrincipal);
        panelPrincipal.setBackground(RoundedBitmapDrawableFactory.create(getResources(),Utils.cargarBitImageFromScreenDirectory(FrmVisorGeneral.this, ConfApp.BRANCH_DEFAULT + ".png")));

        logoSuperior = (ImageView) findViewById(R.id.p3_imglogo);
        logoSuperior.setImageBitmap(Utils.cargarBitImageFromBrandDirectory(FrmVisorGeneral.this, ConfApp.BRANCH_DEFAULT + ".png"));
        logoSuperior.setVisibility(View.INVISIBLE);
        logoSuperior.setBackgroundColor(ConfApp.COLOR_DEFAULT);

        panelPresentacion = (LinearLayout) findViewById(R.id.p4_pnlpresentacion);
        ListaPresentaciones = new ArrayList<Objeto>();

        panelPrecio = (LinearLayout) findViewById(R.id.p4_pnlprecio);
        ListaPrecios = new ArrayList<ArrayList<Precio>>();

        txtNombreProducto = (TextView) findViewById(R.id.p3_txtNombreProducto);
        txtPrecioUnitario = (TextView) findViewById(R.id.p3_txtPrecioUnitario);
        lblEscanearProducto= (TextView) findViewById(R.id.p3_lblColocarProducto);

        image_promo1 =(ImageView)findViewById(R.id.p3_imgpromo1);
        image_promo1.setImageBitmap(Utils.cargarBitImageFromWorkDirectory(FrmVisorGeneral.this,  "Icono1.png"));

        image_promo2 =(ImageView)findViewById(R.id.p3_imgpromo2);
        image_promo2.setImageBitmap(Utils.cargarBitImageFromWorkDirectory(FrmVisorGeneral.this,  "Icono2.png"));

        limpiarFormulario(false);

        if (ConfApp.SHOW_MODULE_PROMO && !ConfApp.SHOW_AFTER_BARCODE_READ)
            mostrarPropaganda();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && !estaLeyendo) {
            if(event.isPrintingKey())
                CODIGO_LEIDO+= ""+ (char) event.getUnicodeChar(event.getMetaState());
            if ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                String codigo = CODIGO_LEIDO.replaceAll("\\s", "");
                if(!estaLeyendo){
                    if ((codigo.length() > 0) && !estaLeyendo) {
                        continuar_Comprobacion(codigo);
                    }
                }else{
                    limpiarInmediatamente();
                }
            }
        }

        if (event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_BACK) && !estaLeyendo ) {
            onBackPressed();
        }
        return false;
    }

    private void cargarImagenesDePromociones() {
        ListaImagenesPromociones.clear();
        ListaImagenesPromociones = Utils.cargarListaDeImagenesPromocionales(FrmVisorGeneral.this);
        if (ListaImagenesPromociones.size() > 0)
            indiceImagenAPromocionar = 0;

    }

    private void continuar_Comprobacion(final String codigo) {
        panelPresentacion.removeAllViews();
        panelPrecio.removeAllViews();
        ListaPresentaciones.clear();
        ListaPrecios.clear();

        panelPrincipal.setBackground(null);
        panelPrincipal.setBackgroundColor(ConfApp.COLOR_DEFAULT);
        logoSuperior.setVisibility(View.VISIBLE);
        lblEscanearProducto.setVisibility(View.INVISIBLE);

        mostrarNombreDeProducto(getResources().getString(R.string.producto_buscado).toUpperCase());
        estaLeyendo = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    producto = ConfApp.BDOPERATION.GetProducto(codigo);
                    if (!producto.getNombre().isEmpty()) {
                        sound.play(msg_ok);
                        ListaPresentaciones = ConfApp.BDOPERATION.GetPresentacion(ISVISORGENERAL);
                        ListaPrecios = ConfApp.BDOPERATION.GetPrecios(producto, ListaPresentaciones);
                        mostrarNombreDeProducto(producto.getNombre());
                        mostrarListaPresentaciones(ListaPresentaciones);
                        mostrarPrecioDeProducto(ListaPrecios);
                        limpiazaProgramada();
                    } else {
                        sound.play(msg_error);
                        mostrarNombreDeProducto(getResources().getString(R.string.produto_noencontrado).toUpperCase());
                        limpiarInmediatamente();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void mostrarPrecioDeProducto(ArrayList<ArrayList<Precio>> listaPrecios) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                panelPrecio.removeAllViews();

                for (int itprecio = 0; itprecio < listaPrecios.size(); itprecio++) {
                    ArrayList<Precio> precio = listaPrecios.get(itprecio);
                    if (itprecio == PRECIOAMOSTRAR) {
                        for (int it = 0; it < precio.size(); it++) {
                            image_promo1.setVisibility(precio.get(it).isPromocion()? View.VISIBLE:View.INVISIBLE);
                            image_promo2.setVisibility(precio.get(it).isPromocion()? View.VISIBLE:View.INVISIBLE);

                            if (it == 0) {
                                try {
                                    txtPrecioUnitario.setText(precio.get(it).getPresentacion() + " " + "C$ " + ConfApp.ISO8601_DECIMAL_FORMAT_1.format(Double.parseDouble(precio.get(it).getPrecio())));
                                } catch (Exception e) {
                                    txtPrecioUnitario.setText(precio.get(it).getPresentacion()+ " " + "C$ " + precio.get(it).getPrecio());
                                }
                                //txtPrecioUnitario.setCompoundDrawables(null,null,precio.get(it).isPromocion()? getResources().getDrawable(R.drawable.img_offert):null,null);
                               // txtPrecioUnitario.setCompoundDrawablesWithIntrinsicBounds(null,null,precio.get(it).isPromocion()? getResources().getDrawable(R.drawable.img_offert):null,null);
                            } else {

                                LinearLayout contenedor = new LinearLayout(FrmVisorGeneral.this);
                                contenedor.setLayoutParams(lpprecio);
                                contenedor.setOrientation(LinearLayout.VERTICAL);

                                TextView txtTitulo = new TextView(FrmVisorGeneral.this);
                                txtTitulo.setLayoutParams(lptitulo);
                                txtTitulo.setText(precio.get(it).getPresentacion());
                                txtTitulo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                txtTitulo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                                contenedor.addView(txtTitulo);

                                TextView textValor = new TextView(FrmVisorGeneral.this);
                                textValor.setLayoutParams(lpvalor);
                                textValor.setBackground(ContextCompat.getDrawable(FrmVisorGeneral.this, R.drawable.backgroup_price));
                                //textValor.setCompoundDrawablesWithIntrinsicBounds(null,null,precio.get(it).isPromocion()? getResources().getDrawable(R.drawable.img_offert):null,null);
                                // textValor.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.img_offert), null);
                                try {
                                    textValor.setText("C$ " + ConfApp.ISO8601_DECIMAL_FORMAT_1.format(Double.parseDouble(precio.get(it).getPrecio())));
                                } catch (Exception e) {
                                    textValor.setText("C$ " + precio.get(it).getPrecio());
                                }

                                textValor.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                textValor.setTextColor(getResources().getColor(R.color.black));
                                textValor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 60);
                                textValor.setTypeface(Typeface.DEFAULT_BOLD);
                                //textValor.setCompoundDrawables(null,null,precio.get(it).isPromocion()?getResources().getDrawable(R.drawable.img_offert):null,null);
                                contenedor.addView(textValor);

                                panelPrecio.addView(contenedor);
                            }
                        }
                    }
                }
            }
        });
    }

    private void mostrarListaPresentaciones(ArrayList<Objeto> listaPresentaciones) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!ISVISORGENERAL) {
                    for (int it = 0; it < ListaPresentaciones.size(); it++) {
                        Button textView = new Button(FrmVisorGeneral.this);
                        textView.setLayoutParams(lppresentaciones);
                        textView.setBackground(ContextCompat.getDrawable(FrmVisorGeneral.this, R.drawable.pnlingreso_backgroud));
                        textView.setText(ListaPresentaciones.get(it).getNombre());
                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        textView.setTag(it);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        textView.setTypeface(Typeface.DEFAULT_BOLD);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //((TextView) view).setBackground(ContextCompat.getDrawable(FrmVisorGeneral.this, R.drawable.backgroup_barcode));
                                PRECIOAMOSTRAR = (Integer) ((TextView) view).getTag();
                                mostrarPrecioDeProducto(ListaPrecios);
                            }
                        });


                        /*if(hasFocus){
                                    ((Button)view).setBackground(ContextCompat.getDrawable(FrmVisorGeneral.this, R.drawable.backgroup_barcode));
                                    ((Button)view).setTextColor(getResources().getColor(R.color.price_color));
                                }else{
                                    ((Button)view).setBackground(ContextCompat.getDrawable(FrmVisorGeneral.this, R.drawable.pnlingreso_backgroud));
                                    ((Button)view).setTextColor(getResources().getColor(R.color.black));
                                }*/

                        panelPresentacion.addView(textView);
                    }
                }
            }
        });
    }

    private void mostrarNombreDeProducto(String info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtNombreProducto.setText(info);
            }
        });
    }

    public void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void limpiazaProgramada() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CountDownTimer countDownTimer = new CountDownTimer((ISVISORGENERAL ? ConfApp.TIMER_VISOR_GENERAL : ConfApp.TIMER_VISOR_MAYORISTA) * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {limpiarInmediatamente();}
                };
                countDownTimer.start();
            }
        });
    }

    private void limpiarInmediatamente() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CountDownTimer countDownTimer = new CountDownTimer(500, 100) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        ocultarTeclado();
                        limpiarFormulario(true);
                    }
                };
                countDownTimer.start();
            }
        });
    }

    private void limpiarFormulario(boolean activarPropaganda) {
        producto = new Producto();
        panelPresentacion.removeAllViews();
        panelPrecio.removeAllViews();
        ListaPresentaciones.clear();
        ListaPrecios.clear();
        txtNombreProducto.setText("");
        txtPrecioUnitario.setText("");
        txtPrecioUnitario.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        CODIGO_LEIDO ="";
        estaLeyendo = false;
        image_promo1.setVisibility(View.INVISIBLE);
        image_promo2.setVisibility(View.INVISIBLE);
        panelPrincipal.setBackgroundColor(Color.TRANSPARENT);
        panelPrincipal.setBackground(RoundedBitmapDrawableFactory.create(getResources(),Utils.cargarBitImageFromScreenDirectory(FrmVisorGeneral.this, ConfApp.BRANCH_DEFAULT + ".png")));
        logoSuperior.setVisibility(View.INVISIBLE);
        lblEscanearProducto.setVisibility(View.VISIBLE);

        if(activarPropaganda){
            if (ConfApp.SHOW_MODULE_PROMO && ConfApp.SHOW_AFTER_BARCODE_READ)
                mostrarPropaganda();
        }
    }

    private void mostrarPropaganda() {

        if (!ConfApp.ISSHOWING_DIALOG && !((Activity) FrmVisorGeneral.this).isFinishing()) {
            CountDownTimer countDownTimer = new CountDownTimer(ConfApp.TIMER_PROPO_VISOR * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    //System.out.println("onFinish de mostrarPropaganda  txtNombre:"+txtNombreProducto.getText().toString().isEmpty()+" !ConfApp.ISSHOWING_DIALOG:"+ConfApp.ISSHOWING_DIALOG+" ConfApp.SCREEN_NAME:"+ConfApp.SCREEN_NAME.equals(getClass().getName()));
                   // System.out.println("ConfApp.SCREEN_NAME"+ConfApp.SCREEN_NAME);

                    if (txtNombreProducto.getText().toString().isEmpty() && !ConfApp.ISSHOWING_DIALOG && ConfApp.SCREEN_NAME.equals(FrmVisorGeneral.this.getClass().getName()) ) {
                        createDialog();
                    }else{
                        if (ConfApp.SHOW_MODULE_PROMO && !ConfApp.SHOW_AFTER_BARCODE_READ)
                            createDialogRetarded();
                    }
                }
            };
            countDownTimer.start();
        }

    }

    private void createDialogRetarded() {

        //System.out.println("Entro a createDialogRetarded");
        CountDownTimer countDownTimer = new CountDownTimer(ConfApp.TIMER_PROPO_VISOR * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                mostrarPropaganda();
            }
        };
        countDownTimer.start();
    }

    public void createDialog() {

        //System.out.println("Crearndo Alert");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = this.getLayoutInflater().inflate(R.layout.mostrarpropaganda, null);

        if (indiceImagenAPromocionar >= ListaImagenesPromociones.size())
            indiceImagenAPromocionar = 0;

        ((ImageView) dialogView.findViewById(R.id.p1_imgpropaganda)).setImageBitmap(Utils.cargarBitImageFromPromotionDirectory(FrmVisorGeneral.this, ListaImagenesPromociones.get(indiceImagenAPromocionar).getNombre()));
        indiceImagenAPromocionar += 1;

        builder.setView(dialogView);

        mAlertDialog = builder.create();
        System.out.println(FrmVisorGeneral.this.getClass().getName());

       if (ConfApp.SHOW_MODULE_PROMO && !ConfApp.ISSHOWING_DIALOG &&  txtNombreProducto.getText().toString().isEmpty() && ConfApp.SCREEN_NAME.equals("com.sdn.svp.FrmVisorGeneral") && !((Activity) FrmVisorGeneral.this).isFinishing()){
            mAlertDialog.show();

           final Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   if(mAlertDialog != null && mAlertDialog.isShowing()){
                       mAlertDialog.dismiss();
                       ConfApp.ISSHOWING_DIALOG =false;

                       if (ConfApp.SHOW_MODULE_PROMO && !ConfApp.SHOW_AFTER_BARCODE_READ)
                            mostrarPropaganda();
                   }
               }
           },ConfApp.TIMER_PROMO_DURATION*1000);
        }
    }
}
package com.sdn.svp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.sdn.util.ConfApp;
import com.sdn.util.TimerCustom;
import com.sdn.util.Utils;

import java.lang.reflect.Method;

public class FrmSplash extends AppCompatActivity {
    public TextView lblInformacion, lbluuid;
    private TimerCustom temporizador = null;
    private boolean IniciarCuentaAtras = false;
    private ImageView Fondo_Pantalla;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ConfApp.SCREEN_NAME = this.getClass().getName();
       // System.out.println("OnPostResume se ejecuta despues de Oncreate");
        IniciarCuentaAtras = true;
        Fondo_Pantalla.setImageBitmap(Utils.cargarBitImageFromScreenDirectory(FrmSplash.this, ConfApp.BRANCH_DEFAULT + ".png"));

        if (ConfApp.DEVICEAUTORIZED)
            CrearTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfApp.createDirectoryWork(FrmSplash.this);
        ConfApp.loadParameters(FrmSplash.this);
        ConfApp.loadConection();

        setContentView(R.layout.frm_splash);
        lblInformacion = (TextView) findViewById(R.id.textView1);
        lbluuid = (TextView) findViewById(R.id.textView3);

        Fondo_Pantalla = (ImageView) findViewById(R.id.p0_imgpantalla);
        Fondo_Pantalla.setImageBitmap(Utils.cargarBitImageFromScreenDirectory(FrmSplash.this, ConfApp.BRANCH_DEFAULT + ".png"));
    }

    private void CrearTimer() {
        ConfApp.loadParameters(FrmSplash.this);
        if (IniciarCuentaAtras) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    temporizador = new TimerCustom(FrmSplash.this, ConfApp.TIMER_SCREEN_WAIT);
                    temporizador.execute();

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                appStatusConeccion(" Version 1.0 - " + ConfApp.UUID_FROM_DEVICE + " - BDCatalog:" + (ConfApp.BDOPERATION.GetStatusConecctionCatog() ? "Online" : "OffLine") + " - BDLicense:" + (ConfApp.BDOPERATION.GetStatusConecctionLicense() ? "Online" : "Offline"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
            });
        }
    }

    public void abrirlogin(View v) {
        IniciarCuentaAtras = false;
        temporizador.cancel(true);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FrmSplash.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.pnl_ingreso, null);

        ImageButton Logo_Pantalla = (ImageButton) dialogView.findViewById(R.id.img_logo);
        Logo_Pantalla.setImageBitmap(Utils.cargarBitImageFromBrandDirectory(FrmSplash.this, ConfApp.BRANCH_DEFAULT + ".png"));

        dialogBuilder.setView(dialogView);

        final TextInputEditText txtusuario = (TextInputEditText) dialogView.findViewById(R.id.txtUsuario);
        final TextInputEditText txtClave = (TextInputEditText) dialogView.findViewById(R.id.txtClave);

        dialogBuilder.setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //ConfApp.loadParameters(FrmSplash.this);//cargamos configuraciones actualizadas

                if (ConfApp.DEVICEAUTORIZED) {
                    if (!txtusuario.getText().toString().isEmpty() && !txtClave.getText().toString().isEmpty()) {
                        if (txtusuario.getText().toString().equals(ConfApp.SYSTEM_USER) && txtClave.getText().toString().equals(ConfApp.SYSTEM_PASS)) {
                            // ConfApp.USER_DTS = true;
                            // ConfApp.USER_ADMIN = true;
                            Intent nuevaPantalla = new Intent(FrmSplash.this, FrmConfigurar.class);
                            startActivity(nuevaPantalla);
                        } else {
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (!ConfApp.BDOPERATION.estaAutorizado(txtusuario.getText().toString(), txtClave.getText().toString())) {
                                            aperturarVenta(v);
                                        } else {
                                            ConfApp.VISOR_DEFAULT = 1;
                                            openScreen();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario o contrasena no pueden ser vacios ", Toast.LENGTH_SHORT).show();
                        abrirlogin(v);
                    }
                } else {
                    Toast.makeText(FrmSplash.this, "Dispositivo no registrado", Toast.LENGTH_SHORT).show();
                    abrirlogin(v);
                }
            }
        });

        dialogBuilder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                IniciarCuentaAtras = true;
                CrearTimer();
            }
        });

        dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                System.out.println("Evento setOnCancelListener");
                IniciarCuentaAtras = true;
                CrearTimer();
            }
        });

        AlertDialog b = dialogBuilder.create();

        b.show();
    }

    private void aperturarVenta(View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FrmSplash.this, "Error de Usuario o contrasena ", Toast.LENGTH_SHORT).show();
                abrirlogin(v);
            }
        });
    }

    public void openScreen() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean dispostivoRegistrado = ConfApp.BDOPERATION.GetStatusLicense(FrmSplash.this);
                    if (dispostivoRegistrado)
                        continue_openScreen();
                    else {
                        CrearTimer();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void printToas(String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FrmSplash.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void continue_openScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IniciarCuentaAtras = false;
                Intent nuevaPantalla = new Intent(FrmSplash.this, FrmVisorGeneral.class);
                startActivity(nuevaPantalla);
            }
        });
    }

    public void appendMessage(String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lblInformacion.setText(mensaje);
            }
        });
    }

    public void appStatusConeccion(String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lbluuid.setText(mensaje);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if(keyCode == KeyEvent.KEYCODE_HOME)
        {
            Log.i("Home Button","Clicked");
        }
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            finish();
        }*/
        return false;
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }*/

   /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            if (!hasFocus) {
                Object service = getSystemService("statusbar");
                Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                Method collapse = statusbarManager.getMethod("collapse");
                collapse.setAccessible(true);
                collapse.invoke(service);
            }
        } catch (Exception e) {
            //Log.e(TAG, "onWindowFocusChanged - " + e.getCause());
        }
    }*/

/*
    String palabra="";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if(event.isPrintingKey())
                palabra+= ""+ (char) event.getUnicodeChar(event.getMetaState());
            if ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                System.out.println("Palabra escaneada al fondo "+palabra);
            }
            return false;
        }

        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try {
            if (!hasFocus) {
                Object service = getSystemService("statusbar");
                Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                Method collapse = statusbarManager.getMethod("collapse");
                collapse.setAccessible(true);
                collapse.invoke(service);
            }
        } catch (Exception e) {
            //Log.e(TAG, "onWindowFocusChanged - " + e.getCause());
        }
    }*/
}
package com.sdn.util;

import android.os.AsyncTask;

import com.sdn.svp.FrmSplash;

public class TimerCustom extends AsyncTask {
    final FrmSplash refencia;
    private int waitTime =0;
    public boolean wasCanceled=false;

    public TimerCustom(FrmSplash pantalla,Integer Duracion) {
        this.refencia = pantalla;
        waitTime =Duracion;
        refencia.appendMessage("");
    }

    private boolean initContenRegresivo() {
        for(int it = waitTime; it>0; it--){
            refencia.appendMessage(String.format("Modo Kiosko comienza en %s Segundos", waitTime));

            if(!isCancelled()){
                try
                {
                    Thread.sleep(1000);
                    waitTime--;
                }catch(InterruptedException e){ }
            }else {
                refencia.appendMessage(String.format("Modo Kiosko cancelado"));
                wasCanceled = true;
                break ;
            }
        }


        return isCancelled();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return initContenRegresivo();
    }

    @Override
    protected void onPostExecute(Object o) {
        System.out.println("Fin del evento asincronoco con valor: isCancelled"+isCancelled()+" this.getstatus"+this.getStatus());
      //  super.onPostExecute(o);
        if(!isCancelled())
            refencia.openScreen();
    }
}

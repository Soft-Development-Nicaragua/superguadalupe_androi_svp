package com.sdn.sound;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.sdn.svp.R;

public class SoundManager {

    private Context pContext;
    private SoundPool sndPool;
    private float rate = 1.0f;
    private float leftVolume = 1.0f;
    private float rightVolume = 1.0f;
    private int msg_error, msg_ok;

    //La clase SoundPool administra y ejecuta todos los recursos de audio de la aplicacion.

    //Nuestro constructor, que determina la configuracion de audio del contexto de nuestra aplicacion
    public SoundManager(Context context)
    {
       sndPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
        pContext = context;

        msg_error = load(R.raw.msg_error);
        msg_ok = load(R.raw.msg_ok);
    }
    //Obtiene el sonido y retorna el id del mismo
    public int load(int idSonido)
    {
        return sndPool.load(pContext, idSonido, 1);
    }
    //Ejecuta el sonido, toma como parametro el id del sonido a ejecutar.
    public void play(int idSonido)
    {
        sndPool.play(idSonido, leftVolume, rightVolume, 1, 0, rate);
    }

    public void play_Ok()
    {
        play(msg_ok);
    }

    public void play_Error()
    {
        play(msg_error);
    }

    // Libera memoria de todos los objetos del sndPool que ya no son requeridos.
    public void unloadAll()
    {
        sndPool.release();
    }
}


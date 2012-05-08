package projet.poker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import com.badlogic.gdx.backends.android.AndroidApplication;
import projet.splash.SplashAnimation;

public class SplashScreen extends AndroidApplication{
    private static Handler              _messageHandler;
    private Intent                      _i;
    private SplashAnimation             _g;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        _messageHandler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.obj.toString().equals("STOP")){
                    
                    Log.v("splash", "finish");
                }
            }
        };
        
        _g = new SplashAnimation();
        
        initialize(_g, false);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * Fonction appelée lorsque l'animation est terminée.
     * 
     * @author Jessy Bonnotte
     * 
     * @param message 
     */
    public static void finSplash(String message){
        Message msg = new Message();
        msg.obj = message;
        _messageHandler.sendMessage(msg);
    }
}

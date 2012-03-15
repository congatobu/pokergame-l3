/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.splash;

import projet.poker.SplashScreen;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import projet.poker.Parametre;

/**
 *
 * @author Shyzkanza
 */
public class SplashAnimation implements ApplicationListener {

    private SpriteBatch             batch;
    private Texture                 textureJeton;
    private Sprite                  spriteJeton;
    
    private float                   posXJeton = -176;
    private float                   posYJeton;
    private float                   posYTitre;
    private float                   espace;
    private float                   color = 0f;
    
    private float[]                 Xlettres;
    private int                     compteur;
    
    private int                     etape = 0;
    
    private CharSequence            str1;
    private BitmapFont              fontTitre;
    
    public void create() {
        batch = new SpriteBatch();
        textureJeton = new Texture(Gdx.files.internal("jetonSplash.png"));
        spriteJeton = new Sprite(textureJeton, 128, 128);
        spriteJeton.rotate(-175f);
        posYJeton = (Gdx.graphics.getHeight()/2) - 64;
        fontTitre = new BitmapFont(Gdx.files.internal("data/font48.fnt"),Gdx.files.internal("data/font48.png"), false);
        fontTitre.setColor(Color.BLACK);
        posYTitre = (Gdx.graphics.getHeight()/2) - 16;
        Xlettres = new float[10];
        espace = fontTitre.getSpaceWidth();
        Gdx.gl.glClearColor(color, color, color, 0f);
    }

    public void resize(int i, int i1) {
        
    }

    public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.      
        Gdx.gl.glClearColor(color, color, color, 0f);
        
        if(color < 1f && etape == 0){
            color += 0.01f;
        }else if(etape == 0){
            etape = 1;
        }
        
        if(posXJeton < 368 && etape == 1){
            posXJeton++;
            spriteJeton.rotate(-1f);
        }else if(etape == 1){
            etape = 2;
        }
        
        if (compteur < 200 && etape == 2){
            compteur++;
        }else if(etape == 2){
            etape = 3;
        }
        
        if(color > 0.01f && etape == 3){
            color -= 0.01f;
        }else if(etape == 3){
            color -= 0.01f;
            etape = 4;
        }
        
        if(etape ==4){
            SplashScreen.finSplash("STOP");
        }
        
        
        batch.begin();
        batch.setColor(color, color, color, 0f);
        drawFront(posXJeton+64f);
        if(etape == 3){
            spriteJeton.setColor(color, color, color, 1f);
        } else if(etape == 4){
            spriteJeton.setColor(color, color, color, 0f);
        }
        if(etape != 4){
            spriteJeton.setPosition(posXJeton, posYJeton);        
            spriteJeton.draw(batch);
        }
        //batch.draw(textureJeton, 0, 0, 256, 256);
        batch.end();
    }

    public void pause() {
        
    }

    public void resume() {
        
    }

    public void dispose() {
        
    }
    
    private void drawFront(float posX){
        str1 = "";
        if(posX > 30){
            str1 = "P";
            if(posX >70){
                str1 = "Po";
                if(posX >110){
                    str1 = "Pok";
                    if(posX >150){
                        str1 = "Poke";
                        if(posX >190){
                            str1 = "Poker";
                            if(posX >210){
                                str1 = "Poker ";
                                if(posX >230){
                                    str1 = "Poker G";
                                    if(posX >260){
                                        str1 = "Poker Ga";
                                        if(posX >290){
                                            str1 = "Poker Gam";
                                            if(posX >310){
                                                str1 = "Poker Game";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        fontTitre.draw(batch, str1, 30, ((Gdx.graphics.getHeight()/2)+(fontTitre.getXHeight()*2)));
    }
}

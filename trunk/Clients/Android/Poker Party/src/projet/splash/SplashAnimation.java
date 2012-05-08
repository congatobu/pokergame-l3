/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.splash;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import projet.poker.SplashScreen;

/**
 *
 * @author Shyzkanza
 */
public class SplashAnimation implements ApplicationListener {

    private SpriteBatch             _batch;
    private Texture                 _textureJeton;
    private Sprite                  _spriteJeton;
    
    private float                   _posXJeton = -176;
    private float                   _posYJeton;
    private float                   _posYTitre;
    private float                   _espace;
    private float                   _color = 0f;
    
    private float[]                 _Xlettres;
    private int                     _compteur;
    
    private int                     _etape = 0;
    
    private CharSequence            _str1;
    private BitmapFont              _fontTitre;
    
    public void create() {
        _batch = new SpriteBatch();
        _textureJeton = new Texture(Gdx.files.internal("jetonSplash.png"));
        _spriteJeton = new Sprite(_textureJeton, 128, 128);
        _spriteJeton.rotate(-175f);
        _posYJeton = (Gdx.graphics.getHeight()/2) - 64;
        _fontTitre = new BitmapFont(Gdx.files.internal("data/font48.fnt"),Gdx.files.internal("data/font48.png"), false);
        _fontTitre.setColor(Color.BLACK);
        _posYTitre = (Gdx.graphics.getHeight()/2) - 16;
        _Xlettres = new float[10];
        _espace = _fontTitre.getSpaceWidth();
        Gdx.gl.glClearColor(_color, _color, _color, 0f);
    }

    public void resize(int i, int i1) {
        
    }

    public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.      
        Gdx.gl.glClearColor(_color, _color, _color, 0f);
        
        if(_color < 1f && _etape == 0){
            _color += 0.01f;
        }else if(_etape == 0){
            _etape = 1;
        }
        
        if(_posXJeton < 368 && _etape == 1){
            _posXJeton++;
            _spriteJeton.rotate(-1f);
        }else if(_etape == 1){
            _etape = 2;
        }
        
        if (_compteur < 200 && _etape == 2){
            _compteur++;
        }else if(_etape == 2){
            _etape = 3;
        }
        
        if(_color > 0.01f && _etape == 3){
            _color -= 0.01f;
        }else if(_etape == 3){
            _color -= 0.01f;
            _etape = 4;
        }
        
        if(_etape ==4){
            SplashScreen.finSplash("STOP");
        }
        
        
        _batch.begin();
        _batch.setColor(_color, _color, _color, 0f);
        drawFront(_posXJeton+64f);
        if(_etape == 3){
            _spriteJeton.setColor(_color, _color, _color, 1f);
        } else if(_etape == 4){
            _spriteJeton.setColor(_color, _color, _color, 0f);
        }
        if(_etape != 4){
            _spriteJeton.setPosition(_posXJeton, _posYJeton);        
            _spriteJeton.draw(_batch);
        }
        //batch.draw(textureJeton, 0, 0, 256, 256);
        _batch.end();
    }

    public void pause() {
        
    }

    public void resume() {
        
    }

    public void dispose() {
        
    }
    
    private void drawFront(float posX){
        _str1 = "";
        if(posX > 30){
            _str1 = "P";
            if(posX >70){
                _str1 = "Po";
                if(posX >110){
                    _str1 = "Pok";
                    if(posX >150){
                        _str1 = "Poke";
                        if(posX >190){
                            _str1 = "Poker";
                            if(posX >210){
                                _str1 = "Poker ";
                                if(posX >230){
                                    _str1 = "Poker G";
                                    if(posX >260){
                                        _str1 = "Poker Ga";
                                        if(posX >290){
                                            _str1 = "Poker Gam";
                                            if(posX >310){
                                                _str1 = "Poker Game";
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
        _fontTitre.draw(_batch, _str1, 30, ((Gdx.graphics.getHeight()/2)+(_fontTitre.getXHeight()*2)));
    }
}

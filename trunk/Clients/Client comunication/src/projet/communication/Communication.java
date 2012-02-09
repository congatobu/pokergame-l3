package projet.communication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Communication extends Activity
{
    Button connection;
    Button deconnection;
    Button envoyer;
    
    EditText adresse;
    EditText port;
    EditText message;
    
    private static ListView list;
    private static List<String> listItem = new ArrayList<String>();
    private static ArrayAdapter<String> aa;
    
    Connection connect;
    
    boolean init = false;
    boolean close = true;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        liaison();
        initAction();
        majBouttonInit();
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem);
        connect = new Connection();
        
        listItem.add("ready");
        list.setAdapter(aa);
        aa.notifyDataSetChanged();
    }
    
    @Override
    public void onStop(){
        connect.dispose();
        super.onStop();
    }
    
    public void liaison(){
        connection = (Button) findViewById(R.id.connection);
        deconnection = (Button) findViewById(R.id.deconnection);
        envoyer = (Button) findViewById(R.id.envoyer);
        
        list = (ListView) findViewById(R.id.list);
        
        adresse = (EditText) findViewById(R.id.adresse);
        port = (EditText) findViewById(R.id.port);
        message = (EditText) findViewById(R.id.message);
    }
    
    public void initAction(){
        connection.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                init = connect.init(adresse.getText().toString(), port.getText().toString());
                if(!init){
                    Toast.makeText(getApplicationContext(), "Erreur de connection", Toast.LENGTH_SHORT).show();
                }
                majBouttonInit();
            }
        });
        
        envoyer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                try {
                    connect.say("<message> "+message.getText().toString());
                } catch (IOException ex) {
                    Toast.makeText(getApplicationContext(), "Erreur d'envois", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        deconnection.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                close = connect.dispose();
                if(!close){
                    Toast.makeText(getApplicationContext(), "Erreur de déconnection", Toast.LENGTH_SHORT).show();
                }
                majBouttonClose();
            }
        });
    }
    
    public static void majListe(String mot){
        listItem.add(mot);
        list.setAdapter(aa);
        aa.notifyDataSetChanged();
    }
    
    private void majBouttonClose(){
        if(close){
            connection.setEnabled(true);
            deconnection.setEnabled(false);
            envoyer.setEnabled(false);
            init = false;
        }else{
            connection.setEnabled(false);
            deconnection.setEnabled(true);
            envoyer.setEnabled(true);
        }
    }
    
    private void majBouttonInit(){
        if(init){
            connection.setEnabled(false);
            deconnection.setEnabled(true);
            envoyer.setEnabled(true);
            close = false;
        }else{
            connection.setEnabled(true);
            deconnection.setEnabled(false);
            envoyer.setEnabled(false);
        }
    }
}

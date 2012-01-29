package projet.ping;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.UpdateLayout;
import android.view.View;
import android.widget.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ping extends Activity
{
    Timer timer;
    Handler hand;
    ListView list;
    List<String> listItem = new ArrayList< String>();
    ArrayAdapter<String> aa;
    Socket t;
    DataInputStream dis;
    PrintStream ps;
    FutureTask<?> theTask = null;
    long depart = 0;
    long arrive = 0;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem);
        Button start = (Button) findViewById(R.id.start);
        Button effacer = (Button) findViewById(R.id.efface);
        list = (ListView) findViewById(R.id.list);
        final EditText texte = (EditText) findViewById(R.id.text);
        
        start.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                ping(texte.getText().toString());
            }
        });
        
        effacer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                listItem.clear();
                list.setAdapter(aa);
                aa.notifyDataSetChanged();
            }
        });
    }
    
    void ping(String host){
        
        int timeOut = 3000; // I recommend 3 seconds at least int timeout = 3000 / / Je recommande 3 secondes au moins 
        Date d = new Date();
        Calendar c;
        boolean res = false;
        long result = 0;
        listItem.add("Ping start");     
        list.setAdapter(aa);
        try {
            for (int i = 0; i < 4; i++) {   
                
                t = new Socket(host, 80);
                dis = new DataInputStream(t.getInputStream());
                ps = new PrintStream(t.getOutputStream());
                
                
                
                try {
                    // create new task
                    theTask = new FutureTask<Object>(new Runnable() {
                        public void run() {
                            try {
                                depart = System.currentTimeMillis();
                                ps.println("Hello");
                                String str = dis.readLine();
                                arrive = System.currentTimeMillis();
                            } catch (IOException ex) {
                                Logger.getLogger(Ping.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }, null);

                    // start task in a new thread
                    new Thread(theTask).start();

                    // wait for the execution to finish, timeout after 10 secs 
                    theTask.get(4L, TimeUnit.SECONDS); 
                }
                catch (TimeoutException e) {
                    listItem.add("   "+i+" : TTL : timeOut");
                
                    //aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem);
                    list.setAdapter(aa);
                    aa.notifyDataSetChanged();
                    ps.close();
                    dis.close();
                    t.close();
                    continue;
                }
                
                
                result = arrive - depart;
                
                listItem.add("   "+i+" : ping : "+result+" ms");
                
                list.setAdapter(aa);
                aa.notifyDataSetChanged();
                ps.close();
                dis.close();
                t.close();
            }
        } catch (Exception e) {
                // TODO Auto-generated catch block
                Toast.makeText(this, "Dommage", Toast.LENGTH_SHORT).show();
        }
        listItem.add("Ping stop");
        list.setAdapter(aa);
        aa.notifyDataSetChanged();
    }
}

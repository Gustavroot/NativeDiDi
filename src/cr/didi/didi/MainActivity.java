package cr.didi.didi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void haciaLaBusqueda(View view) {
    	
    	//System.out.print("La cosa.");
    	
    	//Put up the Yes/No message box
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Volarse toda la info de su cel");
    	builder.setMessage("Seguro?");
    	builder.setIcon(R.drawable.logo_dd);
    	builder.setPositiveButton("See", new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int which) {
    	    	//Yes button clicked, do something
    	    	
    	    	DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
    	    	HttpGet httpget = new HttpGet("http://tecmo.webfactional.com/didi/provincias");
    	    	// Depends on your web service
    	    	httpget.setHeader("Content-type", "application/json");
    	    	InputStream inputStream = null;
    	    	String result = null;
    	    	try {
    	    	    HttpResponse response = httpclient.execute(httpget);
    	    		Toast.makeText(MainActivity.this, "sin error", Toast.LENGTH_SHORT).show();
    	    	    HttpEntity entity = response.getEntity();

    	    	    inputStream = entity.getContent();
    	    	    // json is UTF-8 by default
    	    	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
    	    	    StringBuilder sb = new StringBuilder();

    	    	    String line = null;
    	    	    while ((line = reader.readLine()) != null)
    	    	    {
    	    	    	Toast.makeText(MainActivity.this, line, Toast.LENGTH_SHORT).show();
    	    	        sb.append(line + "\n");
    	    	    }
    	    	    result = sb.toString();
    	    	} catch (Exception e) {
    	    		Toast.makeText(MainActivity.this, "error alguno...", Toast.LENGTH_SHORT).show();
    	    	    // Oops
    	    	}
    	    	finally {
    	    		Toast.makeText(MainActivity.this, "entro a finally...", Toast.LENGTH_SHORT).show();
    	    	    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
    	    	}
    	    	
    	    	//Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    	    }
    	});
    	builder.setNegativeButton("No", null);						//Do nothing on no
    	builder.show();
    	
    	
    }
    
}

package cr.didi.didi;

import cr.didi.didi.R;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE_RESULT_SEARCH = "cr.didi.didi.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Se mantiene oculta la barra de progreso desde el inicio
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /** Called when the user clicks the Send button */
    public void haciaLaBusqueda() {
    	
    	//System.out.print("La cosa.");
    	String message=null;
    	
    	//Por que no sirven estos try, catch e if??
    	//FALTA VALIDAR EL CASO DE ESTAR VACIO EL EDItTEXT
    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
    	message = editText.getText().toString();

    	//Toast.makeText(MainActivity.this, "sin error...", Toast.LENGTH_SHORT).show();
    	//Toast.makeText(MainActivity.this, "http://tecmo.webfactional.com/didi/productos?idClienteParam="+message, Toast.LENGTH_SHORT).show();

    	DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
    	HttpGet httpget = new HttpGet("http://tecmo.webfactional.com/didi/productos?idClienteParam="+message);
    	// Depends on your web service
    	httpget.setHeader("Content-type", "application/json");
    	InputStream inputStream = null;
    	String result = null;
    	try {
    	    HttpResponse response = httpclient.execute(httpget);
    		//Toast.makeText(MainActivity.this, "sin error", Toast.LENGTH_SHORT).show();
    	    HttpEntity entity = response.getEntity();

    	    inputStream = entity.getContent();
    	    // json is UTF-8 by default
    	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
    	    StringBuilder sb = new StringBuilder();

    	    String line = null;
    	    while ((line = reader.readLine()) != null)
    	    {
    	        sb.append(line + "\n");
    	    }
    	    result = sb.toString();
    	    //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    	} catch (Exception e) {
    		Toast.makeText(MainActivity.this, "error alguno...", Toast.LENGTH_SHORT).show();
    	    // Oops
    	}
    	finally {
    		//Toast.makeText(MainActivity.this, "entro a finally...", Toast.LENGTH_SHORT).show();
    	    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
    	}

    	
    	Intent intent = new Intent(this, DisplayListActivity.class);
    	//string msg
    	intent.putExtra(EXTRA_MESSAGE_RESULT_SEARCH, result);
    	startActivity(intent);
    	
    	//Put up the Yes/No message box
    	/**
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Volarse toda la info de su cel");
    	builder.setMessage("Seguro?");
    	builder.setIcon(R.drawable.logo_dd);
    	builder.setPositiveButton("See", new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int which) {
    	    	//Yes button clicked, do something
    	    	
    	    	
    	    	//Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    	    }
    	});
    	builder.setNegativeButton("No", null);						//Do nothing on no
    	builder.show();
    	*/
    }


    
    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
    	@Override
    	protected Double doInBackground(String... params) {
    		// TODO Auto-generated method stub
    	    haciaLaBusqueda();
    	    return null;
    	}
    	protected void onPostExecute(Double result){
    	    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
    		pb.setVisibility(View.GONE);
    		Toast.makeText(getApplicationContext(), "Listo", Toast.LENGTH_LONG).show();
    	}
    	protected void onProgressUpdate(Integer... progress){
    	    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
    	    pb.setProgress(progress[0]);
    	}
    }
    
    public void clickeadoAlBotonBusqueda(View view) {
    	String value=null;
    	int longValue=0;
    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
    	value = editText.getText().toString();
    	longValue=value.length();
    	// TODO Auto-generated method stub
    	if(longValue<1){
    		// out of range
    		Toast.makeText(this, "Por favor ingrese algo.", Toast.LENGTH_LONG).show();
    	}else{
    		ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
    		pb.setVisibility(View.VISIBLE);
    		new MyAsyncTask().execute(value);		
    	}
    }
}
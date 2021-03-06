package cr.didi.didi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cr.didi.widget.AnimationLayout;

public class DecripcionEmpresaActivity extends Activity implements AnimationLayout.Listener {

	public final static String EXTRA_MESSAGE_LAT = "com.example.myfirstapp.MESSAGE_LAT";
	public final static String EXTRA_MESSAGE_LNG = "com.example.myfirstapp.MESSAGE_LNG";
	public final static String EXTRA_MESSAGE_LOGO = "com.example.myfirstapp.MESSAGE_LOGO";
	public final static String EXTRA_MESSAGE_RESULT_SEARCH = "cr.didi.didi.MESSAGE";

	private static String latitud_cliente = "";
	private static String longitud_cliente = "";
	private static String logo_cliente = "";
	private static String descripcion_cliente = "";
	private static String nombre_cliente = "";
	private static String banner_cliente = "";
	
	public final static String EXTRA_MESSAGE_EDIT_TEXT = "cr.didi.didi.MESSAGE_EDIT_TEXT";
	
    public final static String TAG = "Demo";

    protected ListView mList;
    protected AnimationLayout mLayout;
    protected String[] mStrings = {"PERFIL", "Nombre de usuario", "Notificaciones", "Perfil", "Reservas", "\n * Favoritos", "Empresas", "Eventos"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decripcion_empresa);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Get intent
		Intent intent = getIntent();
		latitud_cliente= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_LAT);
		longitud_cliente= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_LNG);
		logo_cliente= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_LOGO_CLIENT);
		descripcion_cliente= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_DESCRIPCION_CLIENTE);
		nombre_cliente= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_NAME_CLIENT);
		banner_cliente= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_BANNER_CLIENTE);
		//Toast.makeText(DecripcionEmpresaActivity.this, "PENDIENTE: "+logo_cliente, Toast.LENGTH_SHORT).show();
		//Se asigna el logo con el url obtenido del request
		//logocliente
		if(logo_cliente.substring(logo_cliente.length()-3).equals("png")){
			try{
				ImageView imgLogoClient=(ImageView)findViewById(R.id.logocliente);
				Bitmap bimage=  getBitmapFromURL(logo_cliente);
				imgLogoClient.setImageBitmap(bimage);
			}
			catch(Exception e){
				Toast.makeText(DecripcionEmpresaActivity.this, "Error al crear bitmap...", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(DecripcionEmpresaActivity.this, "Logo no es png...", Toast.LENGTH_SHORT).show();
		}
		if(banner_cliente.substring(banner_cliente.length()-3,banner_cliente.length()).equals("png")){
			try{
				ImageView imgBannerClient=(ImageView)findViewById(R.id.bannercliente);
				Bitmap bimage2=  getBitmapFromURL(banner_cliente);
				imgBannerClient.setImageBitmap(bimage2);
			}
			catch(Exception e){
				Toast.makeText(DecripcionEmpresaActivity.this, "Error al crear bitmap...", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(DecripcionEmpresaActivity.this, "Banner no es png...", Toast.LENGTH_SHORT).show();
		}
		
		//Toast.makeText(DecripcionEmpresaActivity.this, logo_cliente, Toast.LENGTH_LONG).show();
		//Toast.makeText(DecripcionEmpresaActivity.this, banner_cliente, Toast.LENGTH_LONG).show();
		/*
		try{
			ImageView imgLogoClient=(ImageView)findViewById(R.id.logocliente);
			Bitmap bimage=  getBitmapFromURL(logo_cliente);
			imgLogoClient.setImageBitmap(bimage);
			//-------------------
			ImageView imgBannerClient=(ImageView)findViewById(R.id.bannercliente);
			Bitmap bimage2=  getBitmapFromURL(banner_cliente);
			imgBannerClient.setImageBitmap(bimage2);
		}
		catch(Exception e){
			Toast.makeText(DecripcionEmpresaActivity.this, "Error al crear bitmap...", Toast.LENGTH_SHORT).show();
		}
		*/
		//textdescripcioncliente
		TextView textclientedescripcion=(TextView)findViewById(R.id.textdescripcioncliente);
		textclientedescripcion.setText(descripcion_cliente);
		
		//
		TextView textclientenombre=(TextView)findViewById(R.id.textnombrecliente);
		textclientenombre.setText(nombre_cliente);
		
        //Se mantiene oculta la barra de progreso desde el inicio
        //ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
        //pb.setVisibility(View.GONE);
		
		//EXTRA_MESSAGE_DESCRIPCION_CLIENTE
		
        
		//String result= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_RESULT_SEARCH);
		String result2= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_EDIT_TEXT);
        EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
        editText.setText(result2);
        
        mLayout = (AnimationLayout) findViewById(R.id.animation_layout);
        mLayout.setListener(this);

        mList   = (ListView) findViewById(R.id.sidebar_list);
        mList.setAdapter(
                new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1
                    , mStrings));
	}
	
	//El siguiente metodo se utiliza en la implementacion de una imagen
	public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
	
    /** Called when the user clicks the Send button */
    public void haciaLaBusqueda() {
    	
    	//System.out.print("La cosa.");
    	String message=null;
    	
    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
    	message = editText.getText().toString();

    	//Toast.makeText(MainActivity.this, "sin error...", Toast.LENGTH_SHORT).show();
    	//Toast.makeText(MainActivity.this, "http://tecmo.webfactional.com/didi/productos?idClienteParam="+message, Toast.LENGTH_SHORT).show();

    	DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
    	HttpGet httpget = new HttpGet("http://tecmo.webfactional.com/didi/buscarcliente?palabraClave="+message+"&start=0&limit=25");
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
    	        //
    	    }
    	    result = sb.toString();
    	    //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    	} catch (Exception e) {
    		Toast.makeText(DecripcionEmpresaActivity.this, "error alguno...", Toast.LENGTH_SHORT).show();
    	    // Oops
    	}
    	finally {
    		//Toast.makeText(MainActivity.this, "entro a finally...", Toast.LENGTH_SHORT).show();
    	    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
    	}

    	
    	Intent intent = new Intent(this, DisplayListActivity.class);
    	//string msg
        intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
    	intent.putExtra(EXTRA_MESSAGE_RESULT_SEARCH, result);
    	startActivity(intent);
    	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    	
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
    	Toast.makeText(DecripcionEmpresaActivity.this, "Por implementar...", Toast.LENGTH_SHORT).show();
    	/*
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
    	*/
    }
    
    public void pasarAViewPedirTaxi(View view) {
    	//Paso al view de pedir taxi
        Intent intent = new Intent(this, PedirTaxiActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
        try{
        	startActivity(intent);
        	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        catch(Exception e){
        	Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }
    
    //Ejecucion que se le asigna al boton de Eventos para el menu principal
    public void clickeadoAlBotonEventos(View view) {
    	Intent intent = new Intent(this, EventosActivity.class);
    	//EditText editText = (EditText) findViewById(R.id.edit_message);
    	//String message = editText.getText().toString();
    	//intent.putExtra(EXTRA_MESSAGE, message);
    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
    	startActivity(intent);
    	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    
    //Ejecucion que se le asigna al boton de Eventos para el menu principal
    public void clickeadoAlBotonReservas(View view) {
    	Intent intent = new Intent(this, ReservasActivity.class);
    	//EditText editText = (EditText) findViewById(R.id.edit_message);
    	//String message = editText.getText().toString();
    	//intent.putExtra(EXTRA_MESSAGE, message);
    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
    	startActivity(intent);
    	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

	

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.decripcion_empresa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
    /** Called when the user clicks the Send button */
    public void clickeadoButtonHaciaLayoutMapa(View view) {
    	Intent intent = new Intent(this, GoogleMapActivity.class);
    	//String latitud="9.23435";
    	//String longitud="-84.23435";
    	intent.putExtra(EXTRA_MESSAGE_LAT, latitud_cliente);
    	intent.putExtra(EXTRA_MESSAGE_LNG, longitud_cliente);
    	//intent.putExtra(EXTRA_MESSAGE_NAME_CLIENT, nombre_cliente);
    	startActivity(intent);
    	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void onClickContentButton(View v) {
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
    	imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        mLayout.toggleSidebar();
    }

    @Override
    public void onBackPressed() {
        if (mLayout.isOpening()) {
            mLayout.closeSidebar();
        } else {
            finish();
        }
    }

    /* Callback of AnimationLayout.Listener to monitor status of Sidebar */
    @Override
    public void onSidebarOpened() {
        Log.d(TAG, "opened");
    }

    /* Callback of AnimationLayout.Listener to monitor status of Sidebar */
    @Override
    public void onSidebarClosed() {
        Log.d(TAG, "opened");
    }

    /* Callback of AnimationLayout.Listener to monitor status of Sidebar */
    @Override
    public boolean onContentTouchedWhenOpening() {
        // the content area is touched when sidebar opening, close sidebar
        Log.d(TAG, "going to close sidebar");
        mLayout.closeSidebar();
        return true;
    }
}
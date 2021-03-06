package cr.didi.didi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cr.didi.widget.AnimationLayout;

public class CategoriasActivity extends Activity implements AnimationLayout.Listener {
	
	public final static String EXTRA_MESSAGE_RESULT_SEARCH = "cr.didi.didi.MESSAGE";
	public final static String EXTRA_MESSAGE_EDIT_TEXT = "cr.didi.didi.MESSAGE_EDIT_TEXT";
	private static float init_x = 0;
	public final static String EXTRA_MESSAGE_ID_CAT = "com.example.myfirstapp.ID_CAT";
	private static String id_cat_cliente = "";
	
	private static String nombre_cliente = "";
	private static JSONArray jArray;
	
    public final static String TAG = "Demo";

    protected ListView mList;
    protected AnimationLayout mLayout;
    protected String[] mStrings = {"PERFIL", "Nombre de usuario", "Notificaciones", "Perfil", "Reservas", "\n * Favoritos", "Empresas", "Eventos"};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categorias);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Esta linea me permite hacer la transicion con ingreso lateral, y no desvanecida
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
		
        //Se mantiene oculta la barra de progreso desde el inicio
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        
		//Get intent
		Intent intent = getIntent();
		String result= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_CAT_REQUEST);
		
		//Get intent
		String result2= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_EDIT_TEXT);
	    EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
	    editText.setText(result2);
		
        
	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    textView.setText(result);

	    //filling the list with products result
	    try{
	    	//Primero hay que convertir el string json en un diccionario
	    	JSONObject json = new JSONObject(result);
	    	//json.get("ptm");
	    	//JSONArray jsonArray = new JSONArray(result);
	    	JSONArray jsonArray = new JSONArray(json.get("ptm").toString());
	    	jArray=jsonArray;
	    	int length = jsonArray.length();
	    	List<String> listContents = new ArrayList<String>(length);
	    	for (int i = 0; i < length; i++)
	        {
	    		//Toast.makeText(DisplayListActivity.this, "no problem...", Toast.LENGTH_SHORT).show();
	    		//Toast.makeText(DisplayListActivity.this, jsonArray.getJSONObject(i).get("nombreProducto").toString(), Toast.LENGTH_SHORT).show();
	    		//jsonArray.getJSONObject(i);
	            listContents.add("Cat.: "+jsonArray.getJSONObject(i).get("nombre").toString());
	            ListView myListView = (ListView) findViewById(R.id.lista_despliegue_search);
	            myListView.setOnItemClickListener(new OnItemClickListener() {
	            	@Override
	            	public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
	            		//String tipoprod="";
	            		try{
	            			//Toast.makeText(getApplicationContext(), "Click ListItem Number " + jArray.getJSONObject(position).get("idCategoria").toString(), Toast.LENGTH_LONG).show();
	            			id_cat_cliente=jArray.getJSONObject(position).get("idCategoria").toString();
	            			clickeadoElementosLista();
	            		}
	            		catch(Exception e){
	            		}
	            		///Toast.makeText(getApplicationContext(), "Tipo producto " + tipoprod, Toast.LENGTH_LONG).show();
	            	    //Toast.makeText(getApplicationContext(), "Click ListItem Number " + jsonArray.getJSONObject(i).get("tipoProducto").toString(), Toast.LENGTH_LONG).show();
	            		//Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
	            	    //clickeadoElementosLista();
	                }
	            });
	            myListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContents));
	        }
	    	
	    	//Toast.makeText(DisplayListActivity.this, json.get("ptm").toString(), Toast.LENGTH_SHORT).show();
	    	
		    // Set the text view as the activity layout
		    //setContentView(textView);

	    	//int length = jsonArray.length();
	    	//List<String> listContents = new ArrayList<String>(length);
	    	//for (int i = 0; i < length; i++)
	        //{
	        //  listContents.add(jsonArray.getString(i));
	        //}
	    	
	    }
	    catch(Exception e){}
        mLayout = (AnimationLayout) findViewById(R.id.animation_layout);
        mLayout.setListener(this);

        mList   = (ListView) findViewById(R.id.sidebar_list);
        mList.setAdapter(
                new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1
                    , mStrings));
	}

	//--------------------------------------------------------------------------------------
    /** Called when the user clicks the Send button */
    public void haciaListadoSubcategorias() {
    	//Request de las subcategorias
    	DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
    	HttpGet httpget = new HttpGet("http://tecmo.webfactional.com/didi/subcategorias?idCategoriaParam="+id_cat_cliente);
    	//Depends on your web service
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
    		Toast.makeText(CategoriasActivity.this, "error alguno...", Toast.LENGTH_SHORT).show();
    	    // Oops
    	}
    	finally {
    		//Toast.makeText(MainActivity.this, "entro a finally...", Toast.LENGTH_SHORT).show();
    	    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
    	}

    	
    	Intent intent = new Intent(this, SubcategoriasActivity.class);
    	//String latitud="9.23435";
    	//String longitud="-84.23435";
    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
    	intent.putExtra(EXTRA_MESSAGE_ID_CAT, id_cat_cliente);
    	intent.putExtra(EXTRA_MESSAGE_RESULT_SEARCH, result);
    	startActivity(intent);
    	overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    	
    	/*
    	int suma=0;
    	for (int i = 0; i < 1000000000; i++)
        {
    		suma=suma+1;
        }
        */
    	
    	//Toast.makeText(getApplicationContext(), "Hacia google map...", Toast.LENGTH_LONG).show();
    }


    
    private class MyAsyncTaskClickList extends AsyncTask<String, Integer, Double>{
    	@Override
    	protected Double doInBackground(String... params) {
    		// TODO Auto-generated method stub
    	    haciaListadoSubcategorias();
    	    return null;
    	}
    	protected void onPostExecute(Double result){
    	    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
    		pb.setVisibility(View.GONE);
    		//Toast.makeText(getApplicationContext(), "Listo", Toast.LENGTH_LONG).show();
    	}
    	protected void onProgressUpdate(Integer... progress){
    	    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
    	    pb.setProgress(progress[0]);
    	}
    }
    
    public void clickeadoElementosLista() {
    	//Toast.makeText(getApplicationContext(), "Previous...", Toast.LENGTH_LONG).show();
		ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
		pb.setVisibility(View.VISIBLE);
		new MyAsyncTaskClickList().execute();		

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
	//--------------------------------------------------------------------------------------
	
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
    		Toast.makeText(CategoriasActivity.this, "error alguno...", Toast.LENGTH_SHORT).show();
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
		getMenuInflater().inflate(R.menu.categorias, menu);
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

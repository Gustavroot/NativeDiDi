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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DisplayListActivity extends Activity {
	
	private static String latitud_cliente = "";
	private static String longitud_cliente = "";
	private static String descripcion_cliente = "";
	private static String nombre_cliente = "";
	private static String logo_cliente = "";
	private static String banner_cliente = "";
	private static String tipo_plan_cliente;
	private static JSONArray jArray;
	
	public final static String EXTRA_MESSAGE_EDIT_TEXT = "cr.didi.didi.MESSAGE_EDIT_TEXT";

	//Creacion e iniciacion de las dos constantes utilizadas aqui para el paso
	//hacia el view de GoogleMap
	public final static String EXTRA_MESSAGE_LAT = "com.example.myfirstapp.MESSAGE_LAT";
	public final static String EXTRA_MESSAGE_LNG = "com.example.myfirstapp.MESSAGE_LNG";
	public final static String EXTRA_MESSAGE_DESCRIPCION_CLIENTE = "com.example.myfirstapp.MESSAGE_DESCRIPCION_CLIENTE";
	public final static String EXTRA_MESSAGE_NAME_CLIENT = "com.example.myfirstapp.MESSAGE_NAME_CLIENT";
	public final static String EXTRA_MESSAGE_LOGO_CLIENT = "com.example.myfirstapp.MESSAGE_LOGO_CLIENT";
	public final static String EXTRA_MESSAGE_BANNER_CLIENTE = "com.example.myfirstapp.MESSAGE_BANNER_CLIENT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_list);
		
        //Se mantiene oculta la barra de progreso desde el inicio
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Esta linea me permite hacer la transicion con ingreso lateral, y no desvanecida
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
		
		//Get intent
		Intent intent = getIntent();
		String result= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_RESULT_SEARCH);
		//Se retoma el string de busqueda
		String result2= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_EDIT_TEXT);
        EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
        editText.setText(result2);

		
        /*
	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(40);
	    textView.setText(result);
	    */

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
	            listContents.add("NOMBRE: "+jsonArray.getJSONObject(i).get("nombre").toString()+"\n"+"DESCRIPCION: "+"\n"+"TELEFONO: "+jsonArray.getJSONObject(i).get("telefono").toString()+"\n"+"EMAIL: "+jsonArray.getJSONObject(i).get("email").toString());
	            ListView myListView = (ListView) findViewById(R.id.lista_despliegue_search);
	            myListView.setOnItemClickListener(new OnItemClickListener() {
	            	@Override
	            	public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
	            		//String tipoprod="";
	            		try{
	            			latitud_cliente=jArray.getJSONObject(position).get("latitud").toString();
	            			longitud_cliente=jArray.getJSONObject(position).get("longitud").toString();
	            			nombre_cliente=jArray.getJSONObject(position).get("nombre").toString();
	            			logo_cliente=jArray.getJSONObject(position).get("logo").toString();
	            			descripcion_cliente=jArray.getJSONObject(position).get("descripcion").toString();
	            			banner_cliente=jArray.getJSONObject(position).get("banner").toString();
	            			tipo_plan_cliente=jArray.getJSONObject(position).get("tipoPlan").toString();
	            		}
	            		catch(Exception e){
	            			latitud_cliente="0";
	            			longitud_cliente="0";
	            			nombre_cliente="";
	            			tipo_plan_cliente="3";
	            		}
	            		///Toast.makeText(getApplicationContext(), "Tipo producto " + tipoprod, Toast.LENGTH_LONG).show();
	            	    //Toast.makeText(getApplicationContext(), "Click ListItem Number " + jsonArray.getJSONObject(i).get("tipoProducto").toString(), Toast.LENGTH_LONG).show();
	            		//Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
	            	    clickeadoElementosLista();
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
	    	String cat_cliente="";
	    	try{
	    		cat_cliente= intent.getStringExtra(SubcategoriasActivity.EXTRA_MESSAGE_CAT_CLIENTE);
	    	}
	    	catch(Exception e){}
			//Toast.makeText(DisplayListActivity.this, cat_cliente, Toast.LENGTH_SHORT).show();
			if(cat_cliente==null){
				cat_cliente="Varias";
			}
			if(length==0){
				cat_cliente="Ninguna";
			}
			Button button_cats_client=(Button)findViewById(R.id.buttonlabelcatclientes);
			button_cats_client.setText(cat_cliente);
	    }
	    catch(Exception e){}
	}

	
	
	//------------------------------------------------------------------------
    /** Called when the user clicks the Send button */
    public void haciaGoogleMap() {
    	if(Integer.parseInt(tipo_plan_cliente)==1 || Integer.parseInt(tipo_plan_cliente)==2){
				Intent intent = new Intent(this, DecripcionEmpresaActivity.class);
        		//String latitud="9.23435";
        		//String longitud="-84.23435";
        		intent.putExtra(EXTRA_MESSAGE_LAT, latitud_cliente);
        		intent.putExtra(EXTRA_MESSAGE_LNG, longitud_cliente);
        		intent.putExtra(EXTRA_MESSAGE_NAME_CLIENT, nombre_cliente);
        		intent.putExtra(EXTRA_MESSAGE_LOGO_CLIENT, logo_cliente);
        		intent.putExtra(EXTRA_MESSAGE_DESCRIPCION_CLIENTE, descripcion_cliente);
        		intent.putExtra(EXTRA_MESSAGE_BANNER_CLIENTE, banner_cliente);
        		EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
            	String message = editText.getText().toString();
            	intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
        		startActivity(intent);
        		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    	}
    	else{
    		//String cosa="cosa";
    	    //ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
    		//pb.setVisibility(View.GONE);
    	}
    }


    
    private class MyAsyncTaskClickList extends AsyncTask<String, Integer, Double>{
    	@Override
    	protected Double doInBackground(String... params) {
    		// TODO Auto-generated method stub
    	    //ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
    		//pb.setVisibility(View.GONE);
    	    haciaGoogleMap();
    	    return null;
    	}
    	protected void onPostExecute(Double result){
    	    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
    		pb.setVisibility(View.GONE);
    	    /*
    	    final int welcomeScreenDisplay = 4000;
    	    Thread welcomeThread = new Thread() {
    	        int wait = 0;
    	        @Override
    	        public void run() {
    	        	try {
    	        		super.run();
    	        		while (wait < welcomeScreenDisplay) {
    	        			sleep(100);
    	        			wait += 100;
    	        			}
    	        		} catch (Exception e) {
    	        			//System.out.println("EXc=" + e);
    	        		} finally {
   	        				//startActivity(new Intent(DisplayListActivity.this, NextActivity.class));
   	        				ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
   	        				pb.setVisibility(View.GONE);
   	        				finish();
   	        			}
    	        	}
    	        };
    	        welcomeThread.start();
    	        */
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
	//------------------------------------------------------------------------
	
	
	
	
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
    		Toast.makeText(DisplayListActivity.this, "error alguno...", Toast.LENGTH_SHORT).show();
    	    // Oops
    	}
    	finally {
    		//Toast.makeText(MainActivity.this, "entro a finally...", Toast.LENGTH_SHORT).show();
    	    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
    	}
    	
    	//-------------------------------------------------------------------------------
    	/*
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
	            listContents.add("NOMBRE: "+jsonArray.getJSONObject(i).get("nombre").toString()+"\n"+"DESCRIPCION: "+"\n"+"TELEFONO: "+jsonArray.getJSONObject(i).get("telefono").toString()+"\n"+"EMAIL: "+jsonArray.getJSONObject(i).get("email").toString());
	            ListView myListView = (ListView) findViewById(R.id.lista_despliegue_search);
	            myListView.setOnItemClickListener(new OnItemClickListener() {
	            	@Override
	            	public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
	            		//String tipoprod="";
	            		try{
	            			latitud_cliente=jArray.getJSONObject(position).get("latitud").toString();
	            			longitud_cliente=jArray.getJSONObject(position).get("longitud").toString();
	            			nombre_cliente=jArray.getJSONObject(position).get("nombre").toString();
	            			logo_cliente=jArray.getJSONObject(position).get("logo").toString();
	            			descripcion_cliente=jArray.getJSONObject(position).get("descripcion").toString();
	            			banner_cliente=jArray.getJSONObject(position).get("banner").toString();
	            		}
	            		catch(Exception e){
	            			latitud_cliente="0";
	            			longitud_cliente="0";
	            			nombre_cliente="";
	            		}
	            		///Toast.makeText(getApplicationContext(), "Tipo producto " + tipoprod, Toast.LENGTH_LONG).show();
	            	    //Toast.makeText(getApplicationContext(), "Click ListItem Number " + jsonArray.getJSONObject(i).get("tipoProducto").toString(), Toast.LENGTH_LONG).show();
	            		//Toast.makeText(getApplicationContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
	            	    clickeadoElementosLista();
	                }
	            });
	            myListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContents));
	            //.notifyDataSetChanged();
	            //myListView.getAdapter().notifyDataSetChanged();
	            myListView.invalidateViews();
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
			Button button_cats_client=(Button)findViewById(R.id.buttonlabelcatclientes);
			button_cats_client.setText("Varias");
	    }
	    catch(Exception e){}
	    */
    	//-------------------------------------------------------------------------------

    	
    	
    	//DELETE ITEMS FROM LIST
    	//
    	int counter;
    	ListView listViewBuffer;
    	ArrayAdapter <String> adapterBuffer;
    	//ArrayList <String>listItemsBuffer=new ArrayList<String>();
    	ArrayList<String> listItemsBuffer = new ArrayList<String>(0);
    	//
    	counter=0;
    	listViewBuffer=(ListView) findViewById(R.id.lista_despliegue_search);
    	//adapterBuffer= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItemsBuffer);
    	listViewBuffer.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	//listViewBuffer.setAdapter(adapterBuffer);
    	listViewBuffer.getBottom();
    	//Toast.makeText(DisplayListActivity.this, "sin error...? "+listViewBuffer, Toast.LENGTH_SHORT).show();
    	
    	listViewBuffer.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,listItemsBuffer));
    	
    	//listViewBuffer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, {"test","test","test"}));
    	//listItemsBuffer.remove(counter);
    	//adapterBuffer.notifyDataSetChanged();
    	
    	
    	
    	
    	
    	
    	//Delete all items from list
    	/*
    	ListView listViewBuffer;
    	ArrayAdapter <String> adapterBuffer;
    	ArrayList <String> listItemsBuffer = new ArrayList<String>();
    	listViewBuffer=(ListView) findViewById(R.id.lista_despliegue_search);
    	adapterBuffer= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,listItemsBuffer);
    	listViewBuffer.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	listViewBuffer.setAdapter(adapterBuffer);
    	int size = listItemsBuffer.size() - 1;
        for (int i = size; i > -1; i--)
        {
        	//listItemsBuffer.remove(i);
        }
        adapterBuffer.notifyDataSetChanged();
        */
  
    	
    	//myListViewBuffer.getAdapter().clear();
    	//List<String> listContentsBuffer = new ArrayList<String>(0);
    	//myListViewBuffer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContentsBuffer));
    	
    	//myListView.remove(0);
    	//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myListViewBuffer);
    	//ArrayAdapter<String> adapter = myListView.getAdapter();
    	
    	
    	
    	///Intent intent = new Intent(this, DisplayListActivity.class);
    	//string msg
        ///intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
    	///intent.putExtra(EXTRA_MESSAGE_RESULT_SEARCH, result);
    	///startActivity(intent);
    	///overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    	
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
    	Toast.makeText(DisplayListActivity.this, "Por implementar...", Toast.LENGTH_SHORT).show();
    	ListView myListView = (ListView) findViewById(R.id.lista_despliegue_search);
    	myListView.getAdapter();
    	
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
		getMenuInflater().inflate(R.menu.display_list, menu);
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
}
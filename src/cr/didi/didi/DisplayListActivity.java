package cr.didi.didi;

import java.util.ArrayList;
import java.util.List;

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
	private static String nombre_cliente = "";
	private static String logo_cliente = "";
	private static JSONArray jArray;
	
	public final static String EXTRA_MESSAGE_EDIT_TEXT = "cr.didi.didi.MESSAGE_EDIT_TEXT";

	//Creacion e iniciacion de las dos constantes utilizadas aqui para el paso
	//hacia el view de GoogleMap
	public final static String EXTRA_MESSAGE_LAT = "com.example.myfirstapp.MESSAGE_LAT";
	public final static String EXTRA_MESSAGE_LNG = "com.example.myfirstapp.MESSAGE_LNG";
	//EXTRA_MESSAGE_NAME_CLIENT
	public final static String EXTRA_MESSAGE_NAME_CLIENT = "com.example.myfirstapp.MESSAGE_NAME_CLIENT";
	public final static String EXTRA_MESSAGE_LOGO_CLIENT = "com.example.myfirstapp.MESSAGE_LOGO_CLIENT";
	
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
    	
    	Intent intent = new Intent(this, DecripcionEmpresaActivity.class);
    	//String latitud="9.23435";
    	//String longitud="-84.23435";
    	intent.putExtra(EXTRA_MESSAGE_LAT, latitud_cliente);
    	intent.putExtra(EXTRA_MESSAGE_LNG, longitud_cliente);
    	intent.putExtra(EXTRA_MESSAGE_NAME_CLIENT, nombre_cliente);
    	intent.putExtra(EXTRA_MESSAGE_LOGO_CLIENT, logo_cliente);
    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
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
    	    haciaGoogleMap();
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
	//------------------------------------------------------------------------
	
	
	
	
	
    
    
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
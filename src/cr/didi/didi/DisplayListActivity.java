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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayListActivity extends Activity {

	//Creacion e iniciacion de las dos constantes utilizadas aqui para el paso
	//hacia el view de GoogleMap
	public final static String EXTRA_MESSAGE_LAT = "com.example.myfirstapp.MESSAGE_LAT";
	public final static String EXTRA_MESSAGE_LNG = "com.example.myfirstapp.MESSAGE_LNG";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_list);
		
        //Se mantiene oculta la barra de progreso desde el inicio
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBarOverList);
        pb.setVisibility(View.GONE);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Get intent
		Intent intent = getIntent();
		String result= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_RESULT_SEARCH);
		
        
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
	    	int length = jsonArray.length();
	    	List<String> listContents = new ArrayList<String>(length);
	    	for (int i = 0; i < length; i++)
	        {
	    		//Toast.makeText(DisplayListActivity.this, "no problem...", Toast.LENGTH_SHORT).show();
	    		//Toast.makeText(DisplayListActivity.this, jsonArray.getJSONObject(i).get("nombreProducto").toString(), Toast.LENGTH_SHORT).show();
	    		//jsonArray.getJSONObject(i);
	            listContents.add("TP: "+jsonArray.getJSONObject(i).get("tipoProducto").toString()+"\n"+"NP: "+jsonArray.getJSONObject(i).get("nombreProducto").toString());
	            ListView myListView = (ListView) findViewById(R.id.lista_despliegue_search);
	            myListView.setOnItemClickListener(new OnItemClickListener() {
	            	@Override
	            	public void onItemClick(AdapterView<?> parent, View view,	int position, long id) {
	            	    //Toast.makeText(getApplicationContext(), "Click ListItem Number " + jsonArray.getJSONObject(i).get("tipoProducto").toString(), Toast.LENGTH_LONG).show();
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
	    	
	    }
	    catch(Exception e){}
	}

	
	
	//------------------------------------------------------------------------
    /** Called when the user clicks the Send button */
    public void haciaGoogleMap() {
    	
    	Intent intent = new Intent(this, GoogleMapActivity.class);
    	String latitud="9.23435";
    	String longitud="-84.23435";
    	intent.putExtra(EXTRA_MESSAGE_LAT, latitud);
    	intent.putExtra(EXTRA_MESSAGE_LNG, longitud);
    	startActivity(intent);
    	
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
    	    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBarOverList);
    		pb.setVisibility(View.GONE);
    		Toast.makeText(getApplicationContext(), "Listo", Toast.LENGTH_LONG).show();
    	}
    	protected void onProgressUpdate(Integer... progress){
    	    ProgressBar pb=(ProgressBar)findViewById(R.id.progressBarOverList);
    	    pb.setProgress(progress[0]);
    	}
    }
    
    public void clickeadoElementosLista() {
    	//Toast.makeText(getApplicationContext(), "Previous...", Toast.LENGTH_LONG).show();
		ProgressBar pb=(ProgressBar)findViewById(R.id.progressBarOverList);
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

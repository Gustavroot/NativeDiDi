package cr.didi.didi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class SubcategoriasActivity extends Activity {
	
	private static JSONArray jArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subcategorias);
		// Show the Up button in the action bar.
		setupActionBar();
		
        //Se mantiene oculta la barra de progreso desde el inicio
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
		
		//Toast test
		//Get intent
		Intent intent = getIntent();
		String result= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_RESULT_SEARCH);
		//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		
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
	            listContents.add("Subcat.: "+jsonArray.getJSONObject(i).get("nombre").toString());
	            ListView myListView = (ListView) findViewById(R.id.lista_despliegue_search);
	            /*
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
	            */
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
		getMenuInflater().inflate(R.menu.subcategorias, menu);
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

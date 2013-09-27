package cr.didi.didi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PedirTaxiActivity extends FragmentActivity implements LocationListener, LocationSource{

	private GoogleMap mMap2;
    //private LocationClient mLocationClient;
    //private Location mCurrentLocation;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;
    //private CameraUpdate cu;
    private String latActual="0";
    private String lngActual="0";
    
	public final static String EXTRA_MESSAGE_RESULT_SEARCH = "cr.didi.didi.MESSAGE";
	public final static String EXTRA_MESSAGE_EDIT_TEXT = "cr.didi.didi.MESSAGE_EDIT_TEXT";
	public final static String EXTRA_MESSAGE_CAT_REQUEST = "cr.didi.didi.MESSAGE_CAT_REQUEST";
	private static float init_x = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedir_taxi);
		// Show the Up button in the action bar.
		setupActionBar();
		
        //Se mantiene oculta la barra de progreso desde el inicio
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        
		//Get intent
		Intent intent = getIntent();
		String result= intent.getStringExtra(SecondActivity.EXTRA_MESSAGE_EDIT_TEXT);
        EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
        editText.setText(result);
        
        try{
        	//Coding for map center
    		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(locationManager != null)
            {
                boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(gpsIsEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
                    //obtencion forzada de location
                    latActual=String.valueOf(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude());
                    lngActual=String.valueOf(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
                }
                else if(networkIsEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
                    //obtencion forzada de location
                    latActual=String.valueOf(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude());
                    lngActual=String.valueOf(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude());
                }
                else
                {
                    //Show an error dialog that GPS is disabled...
                	Toast.makeText(this, "GPS deshabilitado.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                //Show some generic error dialog because something must have gone wrong with location manager.
            	mMap2.setLocationSource(this);
            }
    		//Toast.makeText(this, "Ajua.", Toast.LENGTH_SHORT).show();
    		//Toast.makeText(this, latActual, Toast.LENGTH_SHORT).show();
    		//Toast.makeText(this, lngActual, Toast.LENGTH_SHORT).show();
    		
    		mMap2 = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapclienttaxi)).getMap();
        	try{
        		Marker markerPosActual2=mMap2.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latActual), Double.parseDouble(lngActual))).title("Pos. actual"));
        	}
        	catch(Exception e){
        		Context context = getApplicationContext();
        		Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
        	}
        	//Center and zoom
    		mMap2.animateCamera(CameraUpdateFactory.zoomTo(14.0f), new CancelableCallback(){
    	        @Override
    	        public void onFinish() {
    	            //Toast.makeText(getBaseContext(), "Centrado de mapa completado.", Toast.LENGTH_SHORT).show();
    	    		mMap2.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(latActual), Double.parseDouble(lngActual))),new CancelableCallback(){
    	    	        @Override
    	    	        public void onFinish() {
    	    	            //Toast.makeText(getBaseContext(), "Ajuste de zoom de mapa completado.", Toast.LENGTH_SHORT).show();
    	    	        }
    	    	        @Override
    	    	        public void onCancel() {
    	    	            Toast.makeText(getBaseContext(), "Ajuste de zoom de mapa cancelado.", Toast.LENGTH_SHORT).show();
    	    	        }
    	    		});
    	        }
    	        @Override
    	        public void onCancel() {
    	            Toast.makeText(getBaseContext(), "Centrado de mapa cancelado.", Toast.LENGTH_SHORT).show();
    	        }
    		});
    		setUpMapIfNeeded();
        }
        catch(Exception e){
        	Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        
        
		
	}
		//0000000000000000000000000000000000000000000000000000000000000000000000
		@Override
	    public void onPause()
	    {
	        if(locationManager != null)
	        {
	            locationManager.removeUpdates(this);
	        }
	        super.onPause();
	    }
	    @Override
	    public void onResume()
	    {
	        super.onResume();
	        setUpMapIfNeeded();
	        if(locationManager != null)
	        {
	            mMap2.setMyLocationEnabled(true);
	        }
	    }
	    /**
	     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
	     * installed) and the map has not already been instantiated.. This will ensure that we only ever
	     * call {@link #setUpMap()} once when {@link #mMap} is not null.
	     * <p>
	     * If it isn't installed {@link SupportMapFragment} (and
	     * {@link com.google.android.gms.maps.MapView
	     * MapView}) will show a prompt for the user to install/update the Google Play services APK on
	     * their device.
	     * <p>
	     * A user can return to this Activity after following the prompt and correctly
	     * installing/updating/enabling the Google Play services. Since the Activity may not have been
	     * completely destroyed during this process (it is likely that it would only be stopped or
	     * paused), {@link #onCreate(Bundle)} may not be called again so we should call this method in
	     * {@link #onResume()} to guarantee that it will be called.
	     */
	    private void setUpMapIfNeeded() {
	        // Do a null check to confirm that we have not already instantiated the map.
	        if (mMap2 == null)
	        {
	            // Try to obtain the map from the SupportMapFragment.
	            mMap2 = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapclienttaxi)).getMap();
	            // Check if we were successful in obtaining the map.
	            if (mMap2 != null) 
	            {
	                setUpMap();
	            }
	            //This is how you register the LocationSource
	            mMap2.setLocationSource(this);
	        }
	    }
	    /**
	     * This is where we can add markers or lines, add listeners or move the camera.
	     * <p>
	     * This should only be called once and when we are sure that {@link #mMap} is not null.
	     */
	    private void setUpMap()
	    {
	        mMap2.setMyLocationEnabled(true);
	    }
	    @Override
	    public void activate(OnLocationChangedListener listener) 
	    {
	        mListener = listener;
	    }
	    @Override
	    public void deactivate() 
	    {
	        mListener = null;
	    }
	    @Override
	    public void onLocationChanged(Location location) 
	    {
	        if( mListener != null )
	        {
	            mListener.onLocationChanged(location);
	            //Toast.makeText(getApplicationContext(), "Lat mia: "+location.getLatitude(), Toast.LENGTH_LONG).show();
	            mMap2.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
	        }
	    }
	    @Override
	    public void onProviderDisabled(String provider) 
	    {
	        // TODO Auto-generated method stub
	        Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	    }
	    @Override
	    public void onProviderEnabled(String provider) 
	    {
	        // TODO Auto-generated method stub
	        Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	    }
	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) 
	    {
	        // TODO Auto-generated method stub
	        Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
	    }
		//0000000000000000000000000000000000000000000000000000000000000000000000

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
	    	    }
	    	    result = sb.toString();
	    	    //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
	    	} catch (Exception e) {
	    		Toast.makeText(PedirTaxiActivity.this, "error alguno...", Toast.LENGTH_SHORT).show();
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
	    
	    private class MyAsyncTask2 extends AsyncTask<String, Integer, Double>{
	    	@Override
	    	protected Double doInBackground(String... params) {
	    		// TODO Auto-generated method stub
	    	    requestCategorias();
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

	    //Ejecucion que se le asigna al boton de Eventos para el menu principal
	    public void clickeadoAlBotonDirectorio(View view) {
	    	//888888888888888888888888888888888888888888888888888888888888888888888888
			ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
			pb.setVisibility(View.VISIBLE);
			new MyAsyncTask2().execute();
	    	//888888888888888888888888888888888888888888888888888888888888888888888888
	    }
	    
	    //Request de categorias
	    public void requestCategorias() {
	    	DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
	    	HttpGet httpget = new HttpGet("http://tecmo.webfactional.com/didi/categorias");
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
	    		Toast.makeText(PedirTaxiActivity.this, "error alguno...", Toast.LENGTH_SHORT).show();
	    	    // Oops
	    	}
	    	finally {
	    		//Toast.makeText(MainActivity.this, "entro a finally...", Toast.LENGTH_SHORT).show();
	    	    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
	    	}
	    	Intent intent = new Intent(this, CategoriasActivity.class);
	    	//EditText editText = (EditText) findViewById(R.id.edit_message);
	    	//String message = editText.getText().toString();
	    	EditText editText = (EditText) findViewById(R.id.text_field_busqueda_inicio);
	        String message = editText.getText().toString();
	        intent.putExtra(EXTRA_MESSAGE_EDIT_TEXT, message);
	    	intent.putExtra(EXTRA_MESSAGE_CAT_REQUEST, result);
	    	startActivity(intent);
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
		getMenuInflater().inflate(R.menu.pedir_taxi, menu);
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
package cr.didi.didi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedir_taxi);
		// Show the Up button in the action bar.
		setupActionBar();
		
        //Se mantiene oculta la barra de progreso desde el inicio
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        
        
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
		mMap2.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(latActual), Double.parseDouble(lngActual))),new CancelableCallback(){
	        @Override
	        public void onFinish() {
	            //Toast.makeText(getBaseContext(), "Centrado de mapa completado.", Toast.LENGTH_SHORT).show();
	    		mMap2.animateCamera(CameraUpdateFactory.zoomTo(14.0f), new CancelableCallback(){
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
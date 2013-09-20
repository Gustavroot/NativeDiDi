package cr.didi.didi;

import android.annotation.TargetApi;
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
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends FragmentActivity implements LocationListener, LocationSource{
	
	//implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener

	private GoogleMap mMap;
    //private LocationClient mLocationClient;
    //private Location mCurrentLocation;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;
    
    private CameraUpdate cu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Get intent
		Intent intent = getIntent();
		String latitud= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_LAT);
		String longitud= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_LNG);
		String nombreCliente= intent.getStringExtra(DisplayListActivity.EXTRA_MESSAGE_NAME_CLIENT);
		
		//Display of map's pin for client
		//Toast.makeText(getApplicationContext(), "Lat: "+latitud, Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(), "Lat: "+longitud, Toast.LENGTH_LONG).show();
		
		//pin over map
		
		//Display of the intent's content
	    // Create the text view
	    ///TextView textView = new TextView(this);
	    ///textView.setTextSize(40);
	    //textView.setText(latitud+" "+longitud);
	    ///textView.setText("Latitud: "+latitud+"\n"+"Longitud: "+longitud);
	    // Set the text view as the activity layout
	    ///setContentView(textView);
		//private GoogleMap mMap;
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapclient)).getMap();
		Marker markerCliente=mMap.addMarker(new MarkerOptions()
		.position(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)))
		.title(nombreCliente));
		//mMap.setMyLocationEnabled(true);
		//mMap.setLocationSource(this);
		//Set map center in current location:
		
		//LocationClient.getLastLocation();
		
	    
	    //Aqui va el codigo de Google Maps, para ubicar un pin con la latitud
	    //y longitud que se pasaron
		
		//Coding for map center
    	String latActual="";
    	String lngActual="";
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
        	mMap.setLocationSource(this);
        }
		//Toast.makeText(this, "Ajua.", Toast.LENGTH_SHORT).show();
		//Toast.makeText(this, latActual, Toast.LENGTH_SHORT).show();
		//Toast.makeText(this, lngActual, Toast.LENGTH_SHORT).show();
		Marker markerPosActual=mMap.addMarker(new MarkerOptions()
		.position(new LatLng(Double.parseDouble(latActual), Double.parseDouble(lngActual)))
		.title("Pos. actual"));
		
		//Se crea bounds y latlngbounds
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
	    builder.include(markerCliente.getPosition());
	    builder.include(markerPosActual.getPosition());
		LatLngBounds bounds = builder.build();
		//Se hace un update de la camera
		int padding = 50; // offset from edges of the map in pixels
		cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
		//Y finalmente se procede a mover el mapa

		//Se debe agregar un metodo, tal que se ejecute cuando el mapa ya este
		//pasado por el layout, pues si no hay problemas
		View mapView = getSupportFragmentManager().findFragmentById(R.id.mapclient).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
		    mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		        @Override
		        public void onGlobalLayout() {
		    		mMap.moveCamera(cu);
		        }
		    });
		}
        /*
		mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(latActual), Double.parseDouble(lngActual))),new CancelableCallback(){
	        @Override
	        public void onFinish() {
	            //Toast.makeText(getBaseContext(), "Centrado de mapa completado.", Toast.LENGTH_SHORT).show();
	    		mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f), new CancelableCallback(){
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
		*/
		setUpMapIfNeeded();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	
	//Some methods for connection and getting current location
	//----------------------------------------------------------------------
	/*
    @Override
    protected void onStart() {
    	try{
            super.onStart();
            mLocationClient.connect();
    	}
    	catch(Exception e){
    		Toast.makeText(this, "Error conectando: "+e, Toast.LENGTH_SHORT).show();
    	}
    }
    @Override
    protected void onStop() {
    	try{
            mLocationClient.disconnect();
            super.onStop();
    	}
    	catch(Exception e){
    		Toast.makeText(this, "Error desconectando: "+e, Toast.LENGTH_SHORT).show();
    	}
    }
    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        //TODO Auto-generated method stub
    }

    @Override
    public void onConnected(Bundle arg0) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
            @Override
            public void onLocationChanged(final Location location) {
            }
        });
        mCurrentLocation = mLocationClient.getLastLocation();

        Toast.makeText(this, "Lat: "+mCurrentLocation.getLatitude(), Toast.LENGTH_SHORT).show();
        //TextView latTextView = (TextView) findViewById(R.id.latitude_text);
        //latTextView.setText(Double.toString(mCurrentLocation.getLatitude()));
        Toast.makeText(this, "Lng: "+mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        //TextView longTextView = (TextView) findViewById(R.id.longitude_text);
        //longTextView.setText(Double.toString(mCurrentLocation.getLongitude()));
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }*/
	//----------------------------------------------------------------------
	
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
            mMap.setMyLocationEnabled(true);
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
        if (mMap == null)
        {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapclient)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) 
            {
                setUpMap();
            }
            //This is how you register the LocationSource
            mMap.setLocationSource(this);
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() 
    {
        mMap.setMyLocationEnabled(true);
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
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
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
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.google_map, menu);
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
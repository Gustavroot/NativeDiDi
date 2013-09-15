package cr.didi.didi;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class GoogleMapActivity extends FragmentActivity {

	private GoogleMap mMap;
	
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
		
		//Display of map's pin for client
		Toast.makeText(getApplicationContext(), "Lat: "+latitud, Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(), "Lat: "+longitud, Toast.LENGTH_LONG).show();
		
		//pin over map
		
		//Display of the intent's content
	    // Create the text view
	    ///TextView textView = new TextView(this);
	    ///textView.setTextSize(40);
	    //textView.setText(latitud+" "+longitud);
	    ///textView.setText("Latitud: "+latitud+"\n"+"Longitud: "+longitud);
	    // Set the text view as the activity layout
	    ///setContentView(textView);
	    
	    //Aqui va el codigo de Google Maps, para ubicar un pin con la latitud
	    //y longitud que se pasaron
	    
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

package cr.didi.didi;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class DecripcionEmpresaActivity extends Activity {

	public final static String EXTRA_MESSAGE_LAT = "com.example.myfirstapp.MESSAGE_LAT";
	public final static String EXTRA_MESSAGE_LNG = "com.example.myfirstapp.MESSAGE_LNG";

	private static String latitud_cliente = "";
	private static String longitud_cliente = "";

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
}
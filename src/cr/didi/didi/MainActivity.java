package cr.didi.didi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
	
	private static float init_x = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewFlipper);
        vf.setOnTouchListener(new ListenerTouchViewFlipper());
        /**

        Button bt1 = (Button) findViewById(R.id.buttonUno);
        bt1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                vf.showNext();
            }
        });
 
        Button bt2 = (Button) findViewById(R.id.buttondos);
        bt2.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
 
                vf.showPrevious();
            }
        });
 
        vf.setOnTouchListener(new ListenerTouchViewFlipper());*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void clickeadoAlButtonInitial(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    
    private class ListenerTouchViewFlipper implements View.OnTouchListener{
    	
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewFlipper);

            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //Cuando el usuario toca la pantalla por primera vez
                init_x=(float)event.getX();
                //Toast.makeText(MainActivity.this, "X position: "+init_x, Toast.LENGTH_SHORT).show();
                return true;
            case MotionEvent.ACTION_UP: //Cuando el usuario deja de presionar
                float distance =init_x-(float)event.getX();
            	//Toast.makeText(MainActivity.this, "X position: "+event.getX(), Toast.LENGTH_SHORT).show();
            	//Toast.makeText(MainActivity.this, "X position-2: "+(float)event.getX(), Toast.LENGTH_SHORT).show();
            	//Toast.makeText(MainActivity.this, "X position: "+distance, Toast.LENGTH_SHORT).show();
                if(distance>0)
                {
                     vf.showNext();
                }
                if(distance<0)
                {
                     vf.showPrevious();
                }
            default:
                break;
            }
            return false;
        }
    }
}
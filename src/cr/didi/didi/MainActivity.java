package cr.didi.didi;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
	
	private static float init_x = 0;
	
	public final static String EXTRA_MESSAGE_RESULT_SEARCH = "cr.didi.didi.MESSAGE";
	public static JSONObject jObjectCategorias;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        ViewFlipper vf = (ViewFlipper) findViewById(R.id.viewFlipper);
        vf.setOnTouchListener(new ListenerTouchViewFlipper());
        
        //Toast.makeText(MainActivity.this, R.string.app_name, Toast.LENGTH_SHORT).show();
        
        //Se hace el llenado de las categorias, para tenerlas accesibles desde cualquier
        //lugar del app
        try{
        	jObjectCategorias=new JSONObject().put("1", "CARGA Y TRANSPORTE");
        	jObjectCategorias.put("2", "AUTOMOTRIZ");
        	jObjectCategorias.put("3", "ALIMENTOS Y BEBIDAS");
        	jObjectCategorias.put("4", "PROFESIONALES Y SERVICIOS");
        	jObjectCategorias.put("5", "VARIOS (DEFINIR)");
        	jObjectCategorias.put("6", "EDUCACION");
        	jObjectCategorias.put("7", "TECNOLOGIA");
        	jObjectCategorias.put("8", "INDUSTRIAL");
        	jObjectCategorias.put("9", "CONSTRUCTORAS");
        	jObjectCategorias.put("10", "BIENES RAICES E INMUEBLES");
        	jObjectCategorias.put("11", "SEGURIDAD");
        	jObjectCategorias.put("12", "FINANCIERO");
        	jObjectCategorias.put("13", "AGRICULTURA Y VIVEROS");
        	jObjectCategorias.put("14", "HOGAR Y MUEBLES");
        	jObjectCategorias.put("15", "MUSICA");
        	jObjectCategorias.put("16", "COMERCIO");
        	jObjectCategorias.put("17", "HOTELES");
        	jObjectCategorias.put("18", "CLINICAS Y HOSPITALES");
        	jObjectCategorias.put("19", "BELLEZA");
        	jObjectCategorias.put("20", "ENTRETENIMIENTO");
        	jObjectCategorias.put("21", "SUPERMERCADOS");
        	jObjectCategorias.put("22", "ANIMALES");
        	jObjectCategorias.put("23", "FARMACIA");
        	jObjectCategorias.put("24", "TURISMO");
        	jObjectCategorias.put("25", "FERRETERIAS");
        	jObjectCategorias.put("26", "BANCOS");
        	jObjectCategorias.put("27", "BARES");
        	jObjectCategorias.put("28", "RESTAURANTES");
        	jObjectCategorias.put("29", "GIMNASIOS Y DEPORTE");
        	jObjectCategorias.put("30", "SERVICIO EXPRES");
        	jObjectCategorias.put("31", "TALLERES");
        	jObjectCategorias.put("32", "SERVICIOS MEDICOS");
        }
        catch(Exception e){}

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
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
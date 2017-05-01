package example.com.investigacionoperativa2017;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ObjetosBD.Envio;
import ObjetosBD.ResultBaseDeDatosUpdates;

/**
 * Created by Franco on 11/4/2017.
 */

public class RecibirActivity extends Activity implements LocationListener{

    private TextView codProdRec, textDonante, textReceptor, descripcion;
    private String scanContent, nombre_user, nombre_donante, descripcion_envio;
    private int id_usuario;
    private Button buttonConfirm;
    private ProgressBar progressBar;
    MySql mySql;

    LocationManager lm;

    //Se obtiene la fecha
    Calendar cal = new GregorianCalendar();
    Date date = cal.getTime();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String current_date = df.format(date);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recibir);
        codProdRec = (TextView) findViewById(R.id.textCodProdRec);
        textDonante = (TextView) findViewById(R.id.textDonanteRec);
        textReceptor = (TextView) findViewById(R.id.textReceptorRec);
        descripcion = (TextView) findViewById(R.id.textDescProdRec);
        buttonConfirm = (Button) findViewById(R.id.btnConfirRec);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Login);

        Intent i = getIntent();
        if (i!=null){
            if (i.getAction().equals(ScanActivity.SEND_RECIBIR)){
                id_usuario = i.getIntExtra(ScanActivity.ID_USUARIO_LOGUEADO, 0);
                scanContent = i.getStringExtra(ScanActivity.NUMERO_SEGUIMIENTO);
                nombre_user = i.getStringExtra(ScanActivity.NOMBRE_USUARIO);
                nombre_donante = i.getStringExtra(ScanActivity.NOMBRE_DONANTE);
                descripcion_envio = i.getStringExtra(ScanActivity.DESCRIPCION_ENVIO);
            }
        }


        textDonante.setText(nombre_donante);
        descripcion.setText(descripcion_envio);
        textReceptor.setText(nombre_user);
        codProdRec.setText(scanContent);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(RecibirActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(RecibirActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    lm = (LocationManager) getSystemService(SendActivity.LOCATION_SERVICE);

                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    progressBar.setVisibility(ImageView.VISIBLE);
                    actualizarEnvio ();
                    progressBar.setVisibility(ImageView.VISIBLE);
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Necesita tener activado su GPS para poder confirmar la recepcion del envio", Toast.LENGTH_SHORT);
                    toast.show();
                }



            }
        });
    }


    public void actualizarEnvio() {

        if (ContextCompat.checkSelfPermission(RecibirActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(RecibirActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //lm = (LocationManager) getSystemService(SendActivity.LOCATION_SERVICE);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lm.removeUpdates(this);

        double longitud = 0;
        double latitud = 0;

        if (location != null) {
            longitud = location.getLongitude();
            latitud = location.getLatitude();
        }

        ArrayList<String> georeferencia = new ArrayList<>();
        georeferencia.add(String.valueOf(longitud));
        georeferencia.add(String.valueOf(latitud));

        new AsyncTask<ArrayList<String>,Void, ResultBaseDeDatosUpdates>(){
            @Override
            protected ResultBaseDeDatosUpdates doInBackground(ArrayList<String>... objects) {

                ArrayList<String> georeferencia = objects[0];

                double longitud = Double.valueOf(georeferencia.get(0));
                double latitud = Double.valueOf(georeferencia.get(1));

                mySql = new MySql();

                StringBuffer query_update = new StringBuffer();
                query_update.append("UPDATE envio SET entregado = true, fecha_recepcion = ");
                query_update.append("'" + current_date + "', ");
                query_update.append("latitude_receptor = ");
                query_update.append(latitud + ", ");
                query_update.append("longitude_receptor = ");
                query_update.append(longitud);
                query_update.append(" WHERE numero_seguimiento = ");
                query_update.append(scanContent);

                if (longitud != 0 && latitud != 0) {
                    ResultBaseDeDatosUpdates result = mySql.executeUpdate(query_update.toString());
                    return result;
                }
                else return null;
            }

            @Override
            protected void onPostExecute(ResultBaseDeDatosUpdates result) {
                super.onPostExecute(result);
                if (result != null){
                    if (result.Conecto()){
                        int rs = result.getResultado();
                        if (rs != -1){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Recepcion confirmada.", Toast.LENGTH_SHORT);
                            toast.show();
                            progressBar.setVisibility(ImageView.INVISIBLE);
                            RecibirActivity.this.finish();
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Fallo la confirmacion de la recepcion, verifique su conexión e intente nuevamente.", Toast.LENGTH_SHORT);
                            toast.show();
                            progressBar.setVisibility(ImageView.INVISIBLE);
                        }

                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No se pudo conectar, revise su conexion y vuelva a intentarlo", Toast.LENGTH_SHORT);
                        toast.show();
                        progressBar.setVisibility(ImageView.INVISIBLE);
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error de conexión o al intentar recuperar su ubicación. Verifique si su GPS esta activado", Toast.LENGTH_SHORT);
                    toast.show();
                    progressBar.setVisibility(ImageView.INVISIBLE);
                }
            }
        }.execute(georeferencia);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    }

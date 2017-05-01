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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


import ObjetosBD.ResultBaseDeDatos;
import ObjetosBD.ResultBaseDeDatosUpdates;
import ObjetosBD.Usuario;


/**
 * Created by Franco on 11/4/2017.
 */

public class SendActivity extends Activity {


    private Usuario usuario;
    private String scanContent;
    private TextView codProdEnv;
    private Spinner comboReceptores;
    private Button buttonConfirmar;
    private EditText descripcionEnvio;
    private ProgressBar progressBar;
    private MySql mySql;
    private String desc;
    private LocationManager lm;
    


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_enviar);
        usuario = new Usuario();
        codProdEnv = (TextView) findViewById(R.id.textCodProdEnv);
        comboReceptores = (Spinner) findViewById(R.id.comboBoxReceptores);
        buttonConfirmar = (Button) findViewById(R.id.btnConfirEnv);
        descripcionEnvio = (EditText) findViewById(R.id.descripcionProdEnv);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Login);
        scanContent = "";


        Intent i = getIntent();
        if (i!=null){
            if (i.getAction().equals(ScanActivity.SEND_ENVIAR)){
                usuario.setId(i.getIntExtra(ScanActivity.ID_USUARIO_LOGUEADO, 0));
                usuario.setAdress(i.getStringExtra(ScanActivity.ADRESS_USUARIO));
                usuario.setName(i.getStringExtra(ScanActivity.NOMBRE_USUARIO));
                scanContent = i.getStringExtra(ScanActivity.NUMERO_SEGUIMIENTO);
                desc = i.getStringExtra(ScanActivity.DESCRIPCION_ENVIO);

            }
        }

        if (desc != null){
            descripcionEnvio.setText(desc);
            descripcionEnvio.setEnabled(false);
        }

        ArrayList <String> beneficiarios = new ArrayList<>();
        llenarAdapter (beneficiarios);
        progressBar.setVisibility(ImageView.VISIBLE);

        codProdEnv.setText(scanContent);

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comboReceptores.getSelectedItem()==null){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Debe seleccionar un receptor.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }else if (!descripcionEnvio.getText().toString().matches("")){
                    String selected = (String) comboReceptores.getSelectedItem();
                    String [] split = selected.split(":");
                    int id_Receptor = Integer.parseInt(split[0]);
                    agregarAEnvio (id_Receptor, descripcionEnvio.getText().toString());
                    //progressBar.setVisibility(ImageView.VISIBLE);

                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "La descripcion no puede ser vacia", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
            }
        });

    }


    public void llenarAdapter (final ArrayList beneficiarios){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, ResultBaseDeDatos>() {

            @Override
            protected ResultBaseDeDatos doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id, name ");
                query.append("FROM users WHERE id <> ");
                query.append(usuario.getId());
                //Obtengo los resultados de la consulta

                ResultBaseDeDatos result = mySql.executeQuery(query.toString());

                return result;
            }

            @Override
            protected void onPostExecute(ResultBaseDeDatos result) {
                super.onPostExecute(result);

                if (result != null){
                    if (result.Conecto()){
                        ResultSet rs = result.getResultSet();
                        try {
                            if (rs.isBeforeFirst()) {
                                try {
                                    while (rs.next()) {
                                        //Obtengo el id
                                        String id_ben = rs.getString(1);
                                        id_ben = id_ben + ": ";
                                        //Concateno el nombre
                                        id_ben = id_ben + rs.getString(2);
                                        beneficiarios.add(id_ben);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SendActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, beneficiarios);
                        comboReceptores.setAdapter(adapter);
                        progressBar.setVisibility(ImageView.INVISIBLE);
                    }else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No se pudo conectar, revise su conexion y vuelva a intentarlo", Toast.LENGTH_SHORT);
                        toast.show();
                        progressBar.setVisibility(ImageView.INVISIBLE);
                        SendActivity.this.finish();
                        return;
                    }
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error de conexión, vuelva a intentarlo", Toast.LENGTH_SHORT);
                    toast.show();
                    progressBar.setVisibility(ImageView.INVISIBLE);
                    SendActivity.this.finish();
                    return;
                }
            }
        }.execute();
    };


    public void agregarAEnvio(final int id_Receptor, final String descripcion){
        //Se utiliza para agregar, eliminar o modificar informacion de la BD.
        ArrayList<String> list = new ArrayList<>();

        list.add(String.valueOf(id_Receptor));
        list.add(descripcion);
        list.add(usuario.getAdress());

        if (ContextCompat.checkSelfPermission(SendActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SendActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            lm = (LocationManager) getSystemService(SendActivity.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                progressBar.setVisibility(ImageView.VISIBLE);
                TaskExt tarea = new TaskExt();
                tarea.execute(list);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Necesita tener activado su GPS para poder realizar el envio", Toast.LENGTH_SHORT);
            toast.show();

        }

    };

    public class TaskExt extends AsyncTask<ArrayList<String>,Void, ResultBaseDeDatos> implements LocationListener{

        int id_Receptor;
        String descripcion;
        String nombre_receptor = "";
        String address_receptor = "";
        //LocationManager lm;
        ArrayList<String> strings;

        //Se obtiene la fecha
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String current_date = df.format(date);

        @Override
        protected ResultBaseDeDatos doInBackground(ArrayList<String>... objects) {
            strings = objects[0];
            id_Receptor = Integer.valueOf(objects[0].get(0));
            descripcion = objects[0].get(1);

            mySql = new MySql();

            StringBuffer query_datos_receptor = new StringBuffer();
            query_datos_receptor.append("SELECT name, address ");
            query_datos_receptor.append("FROM users ");
            query_datos_receptor.append("WHERE id = " + id_Receptor);

            ResultBaseDeDatos result = mySql.executeQuery(query_datos_receptor.toString());
            return result;
        }

        @Override
        protected void onPostExecute(ResultBaseDeDatos result) {
            super.onPostExecute(result);

            if (result != null){
                if (result.Conecto()){
                    ResultSet rs = result.getResultSet();
                    try {
                        if (rs.isBeforeFirst()) {
                            try {
                                while (rs.next()) {
                                    nombre_receptor = rs.getString(1);
                                    address_receptor = rs.getString(2);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (ContextCompat.checkSelfPermission(SendActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    || ContextCompat.checkSelfPermission(SendActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                //lm = (LocationManager) getSystemService(SendActivity.LOCATION_SERVICE);
                                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "No se pudo conectar, revise su conexion y vuelva a intentarlo", Toast.LENGTH_SHORT);
                    toast.show();
                    progressBar.setVisibility(ImageView.INVISIBLE);
                    return;
                }
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Error de conexión, intente nuevamente", Toast.LENGTH_SHORT);
                toast.show();
                progressBar.setVisibility(ImageView.INVISIBLE);
                return;
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

                    StringBuffer queryInsertEnvio = new StringBuffer();
                    queryInsertEnvio.append("INSERT INTO envio (donante, receptor, entregado, numero_seguimiento, nombre_donante, nombre_receptor, address_donante, latitude_donante, ");
                    queryInsertEnvio.append("longitude_donante, address_receptor, latitude_receptor, longitude_receptor, fecha_envio, fecha_recepcion, descripcion) ");
                    queryInsertEnvio.append("VALUES ( " + usuario.getId()+ ", " +  id_Receptor + "," + false + ", " );
                    queryInsertEnvio.append(scanContent + ", '" + usuario.getName() + "', " + "'" + nombre_receptor + "', " + "'" + usuario.getAdress() + "', " + latitud + ", " + longitud + ",'" );
                    queryInsertEnvio.append(address_receptor + "' , " +  0 + " , " +  0 + " , " + "' " +  current_date + "', "+ null + ", '" +descripcion + "'" +")");

                    if (longitud != 0 && latitud != 0) {
                        ResultBaseDeDatosUpdates result = mySql.executeUpdate(queryInsertEnvio.toString());
                        return result;
                    }
                    else return null;
                }

                @Override
                protected void onPostExecute(ResultBaseDeDatosUpdates result) {
                    super.onPostExecute(result);
                    if (result != null){
                        if (result.Conecto()){
                            int insert = result.getResultado();
                            if (insert != -1){
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Envio cargado.", Toast.LENGTH_SHORT);
                                toast.show();
                                progressBar.setVisibility(ImageView.INVISIBLE);
                                SendActivity.this.finish();
                            }
                            else {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Fallo la carga del envío, verifique su conexión e intente nuevamente.", Toast.LENGTH_SHORT);
                                toast.show();
                                progressBar.setVisibility(ImageView.INVISIBLE);
                            }
                        }else{ Toast toast = Toast.makeText(getApplicationContext(),
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





            /*new AsyncTask<ArrayList<String>,Void,Integer>(){
                @Override
                protected Integer doInBackground(ArrayList<String>... objects) {

                    ArrayList<String> georeferencia = objects[0];

                    double longitud = Double.valueOf(georeferencia.get(0));
                    double latitud = Double.valueOf(georeferencia.get(1));

                    StringBuffer queryInsertEnvio = new StringBuffer();
                    queryInsertEnvio.append("INSERT INTO envio (donante, receptor, entregado, numero_seguimiento, nombre_donante, nombre_receptor, address_donante, latitude_donante, ");
                    queryInsertEnvio.append("longitude_donante, address_receptor, latitude_receptor, longitude_receptor, fecha_envio, fecha_recepcion, descripcion) ");
                    queryInsertEnvio.append("VALUES ( " + usuario.getId()+ ", " +  id_Receptor + "," + false + ", " );
                    queryInsertEnvio.append(scanContent + ", '" + usuario.getName() + "', " + "'" + nombre_receptor + "', " + "'" + usuario.getAdress() + "', " + latitud + ", " + longitud + ",'" );
                    queryInsertEnvio.append(address_receptor + "' , " +  0 + " , " +  0 + " , " + "' " +  current_date + "', "+ null + ", '" +descripcion + "'" +")");

                    int insert;
                    if (longitud != 0 && latitud != 0) {
                        insert = mySql.executeUpdate(queryInsertEnvio.toString());
                        Log.d("RESULTADO DE INSERT", String.valueOf(insert));
                        return insert;
                    }
                    else return -1;
                }

                @Override
                protected void onPostExecute(Integer insert) {
                    super.onPostExecute(insert);

                    if (insert == 1) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Envio cargado.", Toast.LENGTH_SHORT);
                        toast.show();
                        progressBar.setVisibility(ImageView.INVISIBLE);
                        SendActivity.this.finish();
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Fallo la carga del envío, verifique su conexión e intente nuevamente.", Toast.LENGTH_SHORT);
                        toast.show();
                        progressBar.setVisibility(ImageView.INVISIBLE);
                    }
                }
            }.execute(georeferencia);*/
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
}

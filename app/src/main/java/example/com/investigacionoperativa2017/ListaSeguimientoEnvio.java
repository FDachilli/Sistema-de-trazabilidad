package example.com.investigacionoperativa2017;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ObjetosBD.Entrada_lista_envio;
import ObjetosBD.ResultBaseDeDatos;

/**
 * Created by Franco on 20/4/2017.
 */

public class ListaSeguimientoEnvio extends Activity {


    private String numero_seguimiento = "";
    private ListView lista;
    private MySql mySql;
    private ProgressBar progressBar;
    private TextView descripcion;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_envio);

        lista = (ListView) findViewById(R.id.list_envio);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Login);
        descripcion = (TextView) findViewById(R.id.descripcion_lista_envio);
        mySql = new MySql();
        Intent i = getIntent();
        if (i!=null){
            numero_seguimiento = i.getStringExtra(ScanActivity.NUMERO_SEGUIMIENTO);
        }

        llenarLista ();
        progressBar.setVisibility(ImageView.VISIBLE);
    }


    public void llenarLista (){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, ResultBaseDeDatos>(){

            @Override
            protected ResultBaseDeDatos doInBackground(Void... voids) {


                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio, nombre_donante, nombre_receptor, address_donante, address_receptor, fecha_envio, ");
                query.append("fecha_recepcion, latitude_donante, longitude_donante, latitude_receptor, longitude_receptor, descripcion ");
                query.append(" FROM envio WHERE numero_seguimiento=  ");
                query.append(numero_seguimiento);
                query.append(" ORDER BY id_envio");

                ResultBaseDeDatos result = mySql.executeQuery(query.toString());
                return result;

                //Obtengo los resultados de la consulta
                /*ResultSet rs;
                rs = mySql.executeQuery(query.toString());

                ArrayList<Entrada_lista_envio> envios = new ArrayList<>();

                if (rs != null) {
                    try {
                        //Muestro los resultados por Log.
                        while (rs.next()) {

                            Entrada_lista_envio entrada = new Entrada_lista_envio();

                            entrada.setDonante(rs.getString(2));
                            entrada.setReceptor(rs.getString(3));
                            entrada.setDire_donante(rs.getString(4));
                            entrada.setDire_receptor(rs.getString(5));
                            entrada.setFecha_envio(rs.getString(6));
                            if (rs.getString(7) != null) {
                                entrada.setFecha_recepcion(rs.getString(7));
                                entrada.setIdImagen(R.drawable.tick);
                            }

                            else {
                                entrada.setFecha_recepcion("No recibido");
                                entrada.setIdImagen(R.drawable.cruz);
                            }

                            entrada.setLatitud_donante(Double.valueOf(rs.getString(8)));
                            entrada.setLongitud_donante(Double.valueOf(rs.getString(9)));
                            entrada.setLatitud_receptor(Double.valueOf(rs.getString(10)));
                            entrada.setLongitud_receptor(Double.valueOf(rs.getString(11)));
                            entrada.setDescripcion(rs.getString(12));

                            envios.add(entrada);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return envios;*/
            }

            @Override
            protected void onPostExecute(ResultBaseDeDatos result) {
                super.onPostExecute(result);

                if (result != null){
                    if (result.Conecto()){
                        ResultSet rs = result.getResultSet();
                        ArrayList<Entrada_lista_envio> envios = new ArrayList<>();
                        try {
                            if (rs.isBeforeFirst()){
                                try {
                                    //Muestro los resultados por Log.
                                    while (rs.next()) {

                                        Entrada_lista_envio entrada = new Entrada_lista_envio();

                                        entrada.setDonante(rs.getString(2));
                                        entrada.setReceptor(rs.getString(3));
                                        entrada.setDire_donante(rs.getString(4));
                                        entrada.setDire_receptor(rs.getString(5));
                                        entrada.setFecha_envio(rs.getString(6));
                                        if (rs.getString(7) != null) {
                                            entrada.setFecha_recepcion(rs.getString(7));
                                            entrada.setIdImagen(R.drawable.tick);
                                        }

                                        else {
                                            entrada.setFecha_recepcion("No recibido");
                                            entrada.setIdImagen(R.drawable.cruz);
                                        }

                                        entrada.setLatitud_donante(Double.valueOf(rs.getString(8)));
                                        entrada.setLongitud_donante(Double.valueOf(rs.getString(9)));
                                        entrada.setLatitud_receptor(Double.valueOf(rs.getString(10)));
                                        entrada.setLongitud_receptor(Double.valueOf(rs.getString(11)));
                                        entrada.setDescripcion(rs.getString(12));

                                        envios.add(entrada);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        lista.setAdapter(new Lista_adaptador_envio(ListaSeguimientoEnvio.this, R.layout.item_lista, envios));
                        descripcion.setText("Seguimiento del envio numero: " + numero_seguimiento);
                        progressBar.setVisibility(ImageView.GONE);

                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No se pudo conectar, revise su conexion y vuelva a intentarlo", Toast.LENGTH_SHORT);
                        toast.show();
                        progressBar.setVisibility(ImageView.INVISIBLE);
                        return;
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error de conexión, vuelva a intentarlo", Toast.LENGTH_SHORT);
                    toast.show();
                    progressBar.setVisibility(ImageView.INVISIBLE);
                    return;
                }


            }
        }.execute();
    };



    /*public void llenarLista (){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, ArrayList<Entrada_lista_envio>>(){

            @Override
            protected ArrayList <Entrada_lista_envio> doInBackground(Void... voids) {


                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio, nombre_donante, nombre_receptor, address_donante, address_receptor, fecha_envio, ");
                query.append("fecha_recepcion, latitude_donante, longitude_donante, latitude_receptor, longitude_receptor, descripcion ");
                query.append(" FROM envio WHERE numero_seguimiento=  ");
                query.append(numero_seguimiento);
                query.append(" ORDER BY id_envio");

                //Obtengo los resultados de la consulta
                ResultSet rs;
                rs = mySql.executeQuery(query.toString());

                ArrayList<Entrada_lista_envio> envios = new ArrayList<>();

                if (rs != null) {
                    try {
                        //Muestro los resultados por Log.
                        while (rs.next()) {

                            Entrada_lista_envio entrada = new Entrada_lista_envio();

                            entrada.setDonante(rs.getString(2));
                            entrada.setReceptor(rs.getString(3));
                            entrada.setDire_donante(rs.getString(4));
                            entrada.setDire_receptor(rs.getString(5));
                            entrada.setFecha_envio(rs.getString(6));
                            if (rs.getString(7) != null) {
                                entrada.setFecha_recepcion(rs.getString(7));
                                entrada.setIdImagen(R.drawable.tick);
                            }

                            else {
                                entrada.setFecha_recepcion("No recibido");
                                entrada.setIdImagen(R.drawable.cruz);
                            }

                            entrada.setLatitud_donante(Double.valueOf(rs.getString(8)));
                            entrada.setLongitud_donante(Double.valueOf(rs.getString(9)));
                            entrada.setLatitud_receptor(Double.valueOf(rs.getString(10)));
                            entrada.setLongitud_receptor(Double.valueOf(rs.getString(11)));
                            entrada.setDescripcion(rs.getString(12));

                            envios.add(entrada);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return envios;
            }

            @Override
            protected void onPostExecute(ArrayList<Entrada_lista_envio> entrada_lista_envios) {
                super.onPostExecute(entrada_lista_envios);

                lista.setAdapter(new Lista_adaptador_envio(ListaSeguimientoEnvio.this, R.layout.item_lista, entrada_lista_envios));
                descripcion.setText("Seguimiento del envio numero: " + numero_seguimiento);
                progressBar.setVisibility(ImageView.GONE);

            }
        }.execute();
    };*/


}

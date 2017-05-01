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

import ObjetosBD.Entrada_notificacion;
import ObjetosBD.ResultBaseDeDatos;

/**
 * Created by Joaking on 4/27/2017.
 */

public class NotificacionActivity extends Activity {

    private ListView lista;
    private ProgressBar progressBar;
    private int usuario;
    private MySql mySql;
    private TextView descripcion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificacionenvios);

        mySql = new MySql();
        lista = (ListView) findViewById(R.id.list_notificaciones);
        descripcion = (TextView) findViewById(R.id.descripcion_lista_notificaciones);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Notificacion);
        Intent i = getIntent();
        if (i!=null){
            usuario = i.getIntExtra(ScanActivity.ID_USUARIO_LOGUEADO, 0);
        }

        llenarLista ();
        progressBar.setVisibility(ImageView.VISIBLE);
    }




    public void llenarLista() {
        new AsyncTask<Void, Void, ResultBaseDeDatos>() {
            @Override
            protected ResultBaseDeDatos doInBackground(Void... voids) {

                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio, nombre_donante, address_donante, fecha_envio, descripcion");
                query.append(" FROM envio WHERE receptor =");
                query.append(usuario);
                query.append(" AND entregado = false");
                query.append(" ORDER BY id_envio");

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
                        ArrayList<Entrada_notificacion> envios = new ArrayList<>();
                        try {
                            if (rs.isBeforeFirst()) {
                                try {
                                    while (rs.next()) {
                                        Entrada_notificacion entrada = new Entrada_notificacion();
                                        entrada.setNombre_donante(rs.getString(2));
                                        entrada.setDire_donante(rs.getString(3));
                                        entrada.setFecha_envio(rs.getString(4));
                                        entrada.setDescripcion(rs.getString(5));
                                        envios.add(entrada);
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        if (!envios.isEmpty()) {
                            lista.setAdapter(new Lista_adaptador_notificacion(NotificacionActivity.this, R.layout.item_lista_notificacion, envios));
                            descripcion.setText("Envios sin recepcion confirmada:");
                        }
                        else
                            descripcion.setText("Usted no tiene envios sin confirmar");
                        progressBar.setVisibility(ImageView.GONE);

                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No se pudo conectar, revise su conexion y vuelva a intentarlo", Toast.LENGTH_SHORT);
                        toast.show();
                        progressBar.setVisibility(ImageView.GONE);
                        return;
                    }

                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error de conexión, vuelva a intentarlo", Toast.LENGTH_SHORT);
                    toast.show();
                    progressBar.setVisibility(ImageView.GONE);
                    return;
                }
            }
        }.execute();
    };









    /*public void llenarLista() {
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, ArrayList<Entrada_notificacion>>() {

            @Override
            protected ArrayList<Entrada_notificacion> doInBackground(Void... voids) {

                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio, nombre_donante, address_donante, fecha_envio, descripcion");
                query.append(" FROM envio WHERE receptor =");
                query.append(usuario);
                query.append(" AND entregado = false");
                query.append(" ORDER BY id_envio");

                //Obtengo los resultados de la consulta
                ResultSet rs;
                rs = mySql.executeQuery(query.toString());

                ArrayList<Entrada_notificacion> envios = new ArrayList<>();

                if (rs != null) {
                    try {
                        //Muestro los resultados por Log.
                        while (rs.next()) {

                            Entrada_notificacion entrada = new Entrada_notificacion();
                            entrada.setNombre_donante(rs.getString(2));
                            entrada.setDire_donante(rs.getString(3));
                            entrada.setFecha_envio(rs.getString(4));
                            entrada.setDescripcion(rs.getString(5));
                            envios.add(entrada);
                        }
                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return envios;
            }

            @Override
            protected void onPostExecute(ArrayList<Entrada_notificacion> entrada_notificacion) {
                super.onPostExecute(entrada_notificacion);

                lista.setAdapter(new Lista_adaptador_notificacion(NotificacionActivity.this, R.layout.item_lista_notificacion, entrada_notificacion));
                if (!entrada_notificacion.isEmpty())
                        descripcion.setText("Envios sin recepcion confirmada:");
                else
                    descripcion.setText("Usted no tiene envios sin confirmar");
                progressBar.setVisibility(ImageView.GONE);
            }
        }.execute();
    };*/

}

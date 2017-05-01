package example.com.investigacionoperativa2017;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import ObjetosBD.Envio;
import ObjetosBD.ResultBaseDeDatos;
import ObjetosBD.Usuario;


public class ScanActivity extends Activity implements View.OnClickListener{

    private Button scanBtn, enviarBtn, recibirBtn, seguimBtn, notificacionBtn;
    private TextView nombre_bienvenido, textError;
    private EditText editTextNumSeg;
    private MySql mySql;
    private ProgressBar progressBar;
    private Usuario usuario;
    public static String SEND_RECIBIR = "Informacion a activity recibir";
    public static String SEND_ENVIAR = "Informacion a activity enviar";
    public static String NUMERO_SEGUIMIENTO = "Envia numero de seguimiento";
    public static String ID_USUARIO_LOGUEADO = "Envia identificador de usuario logueado";
    public static String ADRESS_USUARIO = "Direccion del usuario logueado";
    public static String NOMBRE_USUARIO = "Nombre del usuario logueado";
    public static String NOMBRE_DONANTE = "Nombre del donante en envio que se recibe";
    public static String DESCRIPCION_ENVIO = "Descripcion del producto escaneado";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scanBtn = (Button)findViewById(R.id.scan_button);

        nombre_bienvenido = (TextView) findViewById(R.id.nombre_bienvenido);
        textError = (TextView) findViewById(R.id.textErrorScan);
        recibirBtn = (Button)findViewById(R.id.boton_recibir);
        enviarBtn = (Button)findViewById(R.id.boton_enviar);
        seguimBtn = (Button) findViewById(R.id.boton_detalles);
        notificacionBtn = (Button) findViewById(R.id.notificacion_btn);

        notificacionBtn.setOnClickListener(this);
        seguimBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
        recibirBtn.setOnClickListener(this);
        enviarBtn.setOnClickListener(this);
        editTextNumSeg = (EditText) findViewById(R.id.editTextNumSeguimiento);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Login);



        usuario = new Usuario();

        Intent i = getIntent();
        if (i!=null){
            if (i.getAction().equals(Login.SEND_INFO_USER)){
                usuario.setId(i.getIntExtra(Login.ID_USUARIO, 0));
                usuario.setName(i.getStringExtra(Login.NAME_USUARIO));
                usuario.setAdress(i.getStringExtra(Login.ADRESS_USUARIO));
                usuario.setUserName(i.getStringExtra(Login.USERNAME_USUARIO));
            }
        }

        nombre_bienvenido.setText(usuario.getName());
    }


    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }else
            if (view.getId()==R.id.boton_recibir){

                if (!editTextNumSeg.getText().toString().matches("")) {

                    validarAperturaActivityRec (String.valueOf(editTextNumSeg.getText()));
                    progressBar.setVisibility(ImageView.VISIBLE);

                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Debe escanear el producto antes.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else
                if (view.getId()==R.id.boton_enviar){

                    if (!editTextNumSeg.getText().toString().matches("")) {
                        validarAperturaActivityEnviar(String.valueOf(editTextNumSeg.getText()));
                        progressBar.setVisibility(ImageView.VISIBLE);

                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Debe escanear el producto antes.", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }else
                if (view.getId()==R.id.boton_detalles){

                    if (!editTextNumSeg.getText().toString().matches("")) {

                        validarAperturaSeguimiento (String.valueOf(editTextNumSeg.getText()));
                        progressBar.setVisibility(ImageView.VISIBLE);

                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Debe escanear el producto antes.", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }else
                    if (view.getId()==R.id.notificacion_btn){

                        Intent myIntent = new Intent(ScanActivity.this, NotificacionActivity.class);
                        myIntent.putExtra(ID_USUARIO_LOGUEADO, usuario.getId());
                        ScanActivity.this.startActivity(myIntent);

                    }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //Se obtienen los resultados del scaneo

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            if (scanContent == null)
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No se recibio informacion del scan.", Toast.LENGTH_SHORT);
                toast.show();
                textError.setVisibility(View.INVISIBLE);
                textError.setText("Volver a escanear");
            }else {
                editTextNumSeg.setText(scanContent);
                textError.setVisibility(View.INVISIBLE);
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No se recibio informacion del scan.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }




    public void validarAperturaActivityRec (final String scanContent){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, ResultBaseDeDatos>(){

            @Override
            protected ResultBaseDeDatos doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio, receptor, nombre_donante, fecha_recepcion, descripcion \n" +
                        "FROM envio\n" +
                        "WHERE id_envio = (SELECT MAX(id_envio)\n" +
                        "\t\t\tFROM envio \n" +
                        "\t\t\tWHERE numero_seguimiento = ");
                query.append(scanContent + ")");
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
                            if (rs.isBeforeFirst()){
                                Envio envio = new Envio();
                                try {
                                    while (rs.next()) {
                                        envio.setId_envio(Integer.parseInt(rs.getString(1)));
                                        envio.setReceptor(Integer.parseInt(rs.getString(2)));
                                        envio.setNombre_donante(rs.getString(3));
                                        if (rs.getString(4) != null)
                                            envio.setFecha_recepcion(Date.valueOf(rs.getString(4)));
                                        envio.setDescripcion(rs.getString(5));
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if (envio.getReceptor() != usuario.getId()){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "El envio con ese numero de seguimiento no corresponde al usuario logueado.", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progressBar.setVisibility(ImageView.INVISIBLE);
                                    return;
                                }
                                else if (envio.getFecha_recepcion() != null){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "El producto con ese numero de seguimiento ya figura entregado.", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progressBar.setVisibility(ImageView.INVISIBLE);
                                    return;
                                }
                                else {
                                    Intent myIntent = new Intent(ScanActivity.this, RecibirActivity.class);
                                    myIntent.setAction(SEND_RECIBIR);
                                    myIntent.putExtra(NUMERO_SEGUIMIENTO, scanContent);
                                    myIntent.putExtra(ID_USUARIO_LOGUEADO, usuario.getId());
                                    myIntent.putExtra(NOMBRE_USUARIO, usuario.getName());
                                    myIntent.putExtra(NOMBRE_DONANTE, envio.getNombre_donante());
                                    myIntent.putExtra(DESCRIPCION_ENVIO, envio.getDescripcion());
                                    progressBar.setVisibility(ImageView.INVISIBLE);
                                    ScanActivity.this.startActivity(myIntent);
                                }
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "No hay ningun envio cargado con ese numero de seguimiento.", Toast.LENGTH_SHORT);
                                toast.show();
                                progressBar.setVisibility(ImageView.INVISIBLE);
                                return;
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



    public void validarAperturaActivityEnviar (final String scanContent){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, ResultBaseDeDatos>(){

            @Override
            protected ResultBaseDeDatos doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio, receptor, fecha_recepcion, descripcion \n" +
                        "FROM envio\n" +
                        "WHERE id_envio = (SELECT MAX(id_envio)\n" +
                        "\t\t\tFROM envio \n" +
                        "\t\t\tWHERE numero_seguimiento = ");
                query.append(scanContent + ")");

                ResultBaseDeDatos result = mySql.executeQuery(query.toString());
                return result;
            }

            @Override
            protected void onPostExecute(ResultBaseDeDatos result) {
                super.onPostExecute(result);


                if (result != null){
                    if (result.Conecto()){
                        ResultSet rs = result.getResultSet();
                        Envio envio = new Envio();
                        try {
                            if (rs.isBeforeFirst()){
                                try {
                                    //Muestro los resultados por Log.
                                    while (rs.next()) {
                                        envio.setId_envio(Integer.parseInt(rs.getString(1)));
                                        envio.setReceptor(Integer.parseInt(rs.getString(2)));
                                        if (rs.getString(3) != null)
                                            envio.setFecha_recepcion(Date.valueOf(rs.getString(3)));
                                        envio.setDescripcion(rs.getString(4));
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                if (envio.getReceptor() != usuario.getId()){
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "El ultimo receptor del envio no corresponde con el usuario logueado.", Toast.LENGTH_SHORT);
                                    toast.show();
                                    progressBar.setVisibility(ImageView.INVISIBLE);
                                    return;
                                }
                                else {
                                    if (envio.getFecha_recepcion() == null) {
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Debe confirmar la recepcion antes de volver a enviar el producto.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        progressBar.setVisibility(ImageView.INVISIBLE);
                                        return;
                                    }
                                    else {
                                        Intent myIntent = new Intent(ScanActivity.this, SendActivity.class);
                                        myIntent.setAction(SEND_ENVIAR);
                                        myIntent.putExtra(NUMERO_SEGUIMIENTO, scanContent);
                                        myIntent.putExtra(ID_USUARIO_LOGUEADO, usuario.getId());
                                        myIntent.putExtra(ADRESS_USUARIO, usuario.getAdress());
                                        myIntent.putExtra(NOMBRE_USUARIO, usuario.getName());
                                        myIntent.putExtra(DESCRIPCION_ENVIO, envio.getDescripcion());
                                        progressBar.setVisibility(ImageView.INVISIBLE);
                                        ScanActivity.this.startActivity(myIntent);
                                    }
                                }
                            }else{
                                Intent myIntent = new Intent(ScanActivity.this, SendActivity.class);
                                myIntent.setAction(SEND_ENVIAR);
                                myIntent.putExtra(NUMERO_SEGUIMIENTO, scanContent);
                                myIntent.putExtra(ID_USUARIO_LOGUEADO, usuario.getId());
                                myIntent.putExtra(ADRESS_USUARIO, usuario.getAdress());
                                myIntent.putExtra(NOMBRE_USUARIO, usuario.getName());
                                myIntent.putExtra(DESCRIPCION_ENVIO, envio.getDescripcion());
                                progressBar.setVisibility(ImageView.INVISIBLE);
                                ScanActivity.this.startActivity(myIntent);
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

    public void validarAperturaSeguimiento (final String scanContent){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, ResultBaseDeDatos>(){

            @Override
            protected ResultBaseDeDatos doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio \n" +
                        "FROM envio\n" +
                        "WHERE id_envio = (SELECT MAX(id_envio)\n" +
                        "\t\t\tFROM envio \n" +
                        "\t\t\tWHERE numero_seguimiento = ");
                query.append(scanContent + ")");

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
                            if (rs.isBeforeFirst()){
                                Intent myIntent = new Intent(ScanActivity.this, ListaSeguimientoEnvio.class);
                                myIntent.putExtra(NUMERO_SEGUIMIENTO, String.valueOf(editTextNumSeg.getText()));
                                ScanActivity.this.startActivity(myIntent);
                                progressBar.setVisibility(ImageView.INVISIBLE);
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "No hay ningun envio cargado con ese numero de seguimiento.", Toast.LENGTH_SHORT);
                                toast.show();
                                progressBar.setVisibility(ImageView.INVISIBLE);
                                return;
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






    /*public void validarAperturaActivityRec (final String scanContent){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, Envio>(){

            @Override
            protected Envio doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio, receptor, nombre_donante, fecha_recepcion, descripcion \n" +
                        "FROM envio\n" +
                        "WHERE id_envio = (SELECT MAX(id_envio)\n" +
                        "\t\t\tFROM envio \n" +
                        "\t\t\tWHERE numero_seguimiento = ");
                query.append(scanContent + ")");

                //Obtengo los resultados de la consulta
                ResultSet rs;
                rs = mySql.executeQuery(query.toString());
                Envio envio = new Envio();
                envio.setId_envio(-1);
                if (rs != null) {
                try {
                    //Muestro los resultados por Log.
                    while (rs.next()) {

                        envio.setId_envio(Integer.parseInt(rs.getString(1)));
                        envio.setReceptor(Integer.parseInt(rs.getString(2)));
                        envio.setNombre_donante(rs.getString(3));
                        if (rs.getString(4) != null)
                            envio.setFecha_recepcion(Date.valueOf(rs.getString(4)));
                        envio.setDescripcion(rs.getString(5));



                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

                return envio;
            }

            @Override
            protected void onPostExecute(Envio envio) {
                super.onPostExecute(envio);

                 if (envio.getId_envio() == -1){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "No hay ningun envio cargado con ese numero de seguimiento.", Toast.LENGTH_SHORT);
                    toast.show();
                     progressBar.setVisibility(ImageView.INVISIBLE);
                    return;
                }
                else if (envio.getReceptor() != usuario.getId()){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "El envio con ese numero de seguimiento no corresponde al usuario logueado.", Toast.LENGTH_SHORT);
                    toast.show();
                     progressBar.setVisibility(ImageView.INVISIBLE);
                    return;
                }
                else if (envio.getFecha_recepcion() != null){
                     Toast toast = Toast.makeText(getApplicationContext(),
                             "El producto con ese numero de seguimiento ya figura entregado.", Toast.LENGTH_SHORT);
                     toast.show();
                     progressBar.setVisibility(ImageView.INVISIBLE);
                     return;
                 }

                 else {
                    Intent myIntent = new Intent(ScanActivity.this, RecibirActivity.class);
                    myIntent.setAction(SEND_RECIBIR);
                    myIntent.putExtra(NUMERO_SEGUIMIENTO, scanContent);
                    myIntent.putExtra(ID_USUARIO_LOGUEADO, usuario.getId());
                    myIntent.putExtra(NOMBRE_USUARIO, usuario.getName());
                     myIntent.putExtra(NOMBRE_DONANTE, envio.getNombre_donante());
                     myIntent.putExtra(DESCRIPCION_ENVIO, envio.getDescripcion());
                     progressBar.setVisibility(ImageView.INVISIBLE);
                    ScanActivity.this.startActivity(myIntent);
                }


            }
        }.execute();
    };



    public void validarAperturaActivityEnviar (final String scanContent){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, Envio>(){

            @Override
            protected Envio doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio, receptor, fecha_recepcion, descripcion \n" +
                        "FROM envio\n" +
                        "WHERE id_envio = (SELECT MAX(id_envio)\n" +
                        "\t\t\tFROM envio \n" +
                        "\t\t\tWHERE numero_seguimiento = ");
                query.append(scanContent + ")");

                //Obtengo los resultados de la consulta
                ResultSet rs;
                rs = mySql.executeQuery(query.toString());
                Envio envio = new Envio();
                envio.setId_envio(-1);
                if (rs != null) {
                    try {
                        //Muestro los resultados por Log.
                        while (rs.next()) {

                            envio.setId_envio(Integer.parseInt(rs.getString(1)));
                            envio.setReceptor(Integer.parseInt(rs.getString(2)));
                            if (rs.getString(3) != null)
                                envio.setFecha_recepcion(Date.valueOf(rs.getString(3)));
                            envio.setDescripcion(rs.getString(4));

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                return envio;
            }

            @Override
            protected void onPostExecute(Envio envio) {
                super.onPostExecute(envio);

                if (envio.getId_envio() != -1) {

                    if (envio.getReceptor() != usuario.getId()){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "El ultimo receptor del envio no corresponde con el usuario logueado.", Toast.LENGTH_SHORT);
                        toast.show();
                        progressBar.setVisibility(ImageView.INVISIBLE);
                        return;
                    }
                    else {
                        if (envio.getFecha_recepcion() == null) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Debe confirmar la recepcion antes de volver a enviar el producto.", Toast.LENGTH_SHORT);
                            toast.show();
                            progressBar.setVisibility(ImageView.INVISIBLE);
                            return;
                        }
                        else {
                            Intent myIntent = new Intent(ScanActivity.this, SendActivity.class);
                            myIntent.setAction(SEND_ENVIAR);
                            myIntent.putExtra(NUMERO_SEGUIMIENTO, scanContent);
                            myIntent.putExtra(ID_USUARIO_LOGUEADO, usuario.getId());
                            myIntent.putExtra(ADRESS_USUARIO, usuario.getAdress());
                            myIntent.putExtra(NOMBRE_USUARIO, usuario.getName());
                            myIntent.putExtra(DESCRIPCION_ENVIO, envio.getDescripcion());
                            progressBar.setVisibility(ImageView.INVISIBLE);
                            ScanActivity.this.startActivity(myIntent);
                        }
                    }

                }

                else {
                    Intent myIntent = new Intent(ScanActivity.this, SendActivity.class);
                    myIntent.setAction(SEND_ENVIAR);
                    myIntent.putExtra(NUMERO_SEGUIMIENTO, scanContent);
                    myIntent.putExtra(ID_USUARIO_LOGUEADO, usuario.getId());
                    myIntent.putExtra(ADRESS_USUARIO, usuario.getAdress());
                    myIntent.putExtra(NOMBRE_USUARIO, usuario.getName());
                    myIntent.putExtra(DESCRIPCION_ENVIO, envio.getDescripcion());
                    progressBar.setVisibility(ImageView.INVISIBLE);
                    ScanActivity.this.startActivity(myIntent);
                }

            }
        }.execute();
    };

    public void validarAperturaSeguimiento (final String scanContent){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, Integer>(){

            @Override
            protected Integer doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id_envio \n" +
                        "FROM envio\n" +
                        "WHERE id_envio = (SELECT MAX(id_envio)\n" +
                        "\t\t\tFROM envio \n" +
                        "\t\t\tWHERE numero_seguimiento = ");
                query.append(scanContent + ")");

                //Obtengo los resultados de la consulta
                ResultSet rs;
                rs = mySql.executeQuery(query.toString());
                int id_envio = -1;
                if (rs != null) {
                    try {
                        //Muestro los resultados por Log.
                        while (rs.next()) {
                            id_envio = Integer.parseInt(rs.getString(1));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                return id_envio;
            }

            @Override
            protected void onPostExecute(Integer id_envio) {
                super.onPostExecute(id_envio);

                if (id_envio == -1){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "No hay ningun envio cargado con ese numero de seguimiento.", Toast.LENGTH_SHORT);
                    toast.show();
                    progressBar.setVisibility(ImageView.INVISIBLE);
                    return;
                }
                else {
                    Intent myIntent = new Intent(ScanActivity.this, ListaSeguimientoEnvio.class);
                    myIntent.putExtra(NUMERO_SEGUIMIENTO, String.valueOf(editTextNumSeg.getText()));
                    ScanActivity.this.startActivity(myIntent);
                    progressBar.setVisibility(ImageView.INVISIBLE);
                }

            }
        }.execute();
    };
*/

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(" Cerrar sesion ?")
                .setCancelable(false)
                .setNegativeButton("No", null)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ScanActivity.this, Login.class));
                        ScanActivity.this.finish();
                    }
                })
                .show();
    }



}

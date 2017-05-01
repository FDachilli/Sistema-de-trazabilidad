package example.com.investigacionoperativa2017;

/**
 * Created by Joaking on 3/23/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;

import ObjetosBD.ResultBaseDeDatos;
import ObjetosBD.ResultBaseDeDatosUpdates;
import ObjetosBD.Usuario;


import static android.R.attr.value;

public class Login extends Activity {


    private Button loginBtn;
    private EditText userEt, passwordEt;
    private TextView alertMsj;
    private MySql mySql;
    private ProgressBar progressBar;

    public static String SEND_INFO_USER = "Send info user";
    public static String ID_USUARIO = "Send ID";
    public static String USERNAME_USUARIO = "Send username";
    public static String NAME_USUARIO = "Send name";
    public static String ADRESS_USUARIO = "Send adress";




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login);

        //Seteo de variables
        userEt = (EditText) findViewById(R.id.et_user);
        passwordEt = (EditText) findViewById(R.id.et_password);
        alertMsj = (TextView) findViewById(R.id.alertMsjLogin);
        loginBtn = (Button) findViewById(R.id.btn_login);


        //QUERYS DE CREACION E INSERCION


        /*String queryDeleteTableEnvio = "DROP TABLE IF EXISTS envio";
        executeUpdate(queryDeleteTableEnvio);

        String queryDeleteTable = "DROP TABLE IF EXISTS users";
        executeUpdate(queryDeleteTable);

        String queryCreateTable = "CREATE TABLE IF NOT EXISTS users (\n" +
                "  id int NOT NULL,\n" +
                "  username varchar(60)  NOT NULL,\n" +
                "  password varchar(100) NOT NULL,\n" +
                "  name varchar(50) NOT NULL,\n" +
                "  address varchar(45) NOT NULL,\n" +
                "  CONSTRAINT pk_users PRIMARY KEY (id)\n" +
                ") \n";
        executeUpdate(queryCreateTable);

        String queryInsert = "INSERT INTO users (id, username, password, name, address) VALUES\n" +
                "(61,'moises', 'moises', 'Moisés Evaristo Bueno','Rodriguez 500'),\n" +
                "(142, 'oyhanart.matias.ezequiel', 'matias', 'oyhanart matias ezequiel', 'aristeguy 1346'),\n" +
                "(141, 'andrea.veronica.roldan', 'andrea', 'andrea veronica roldan', 'loberia 1214'),\n" +
                "(140, 'myrian.fernandez', 'myrian', 'Myrian Fernandez', 'Sinka 101'),\n" +
                "(119, 'fabián.alejandro.gerez', 'fabian', 'Fabián Alejandro Gerez', 'San Francisco 2148'),\n" +
                "(136, 'evaristo.bueno', 'evaristo', 'Evaristo Bueno', 'salceda 1995'),\n" +
                "(137, 'claudia.serafini', 'claudia', 'CLAUDIA SERAFINI', 'ARANA 22'),\n" +
                "(138, 'natalia.bellver', 'natalia', 'Natalia Bellver',  'mitre 1512'),\n" +
                "(139, 'maria.guillermina', 'guillermina', 'Maria Guillermina', 'Mosconi 438'),\n" +
                "(143, 'omar.alejandro', 'omarale', 'OMAR ALEJANDRO','Mosconi 438'),\n" +
                "(144, 'carlosalfredo', 'carlitos', 'CARLOS ALFREDO','Mosconi 438')";
        executeUpdate(queryInsert);



      /*  String queryCreateTableEnvio = "CREATE TABLE IF NOT EXISTS envio (\n" +
                " id_envio serial NOT NULL,\n" +
                " donante int NOT NULL,\n" +
                " receptor int NOT NULL,\n" +
                " entregado boolean NOT NULL,\n" +
                " numero_seguimiento varchar(45) NOT NULL, \n" +
                " nombre_donante varchar (45) NOT NULL, \n"+
                " nombre_receptor varchar (45) NOT NULL, \n"+
                " address_donante varchar(45) COLLATE utf8_unicode_ci NOT NULL,\n" +
                " latitude_donante float DEFAULT NULL,\n" +
                " longitude_donante float DEFAULT NULL,\n" +
                " address_receptor varchar(45) COLLATE utf8_unicode_ci NOT NULL,\n" +
                " latitude_receptor float DEFAULT NULL,\n" +
                " longitude_receptor float DEFAULT NULL,\n" +
                " fecha_envio date NOT NULL,\n" +
                " fecha_recepcion date, \n" +
                " descripcion varchar (200) NOT NULL, \n" +
                " CONSTRAINT pk_envio PRIMARY KEY (id_envio)\n" +
                ")";


        executeUpdate(queryCreateTableEnvio);


        String queryAlterTableEnvio =  "ALTER TABLE envio\n" +
                "  ADD CONSTRAINT fk_donante_id_ FOREIGN KEY (donante) REFERENCES users (id);";

        executeUpdate(queryAlterTableEnvio);

        queryAlterTableEnvio = "ALTER TABLE envio\n" +
                "  ADD CONSTRAINT fk_receptor_id FOREIGN KEY (receptor) REFERENCES users (id) ";
        executeUpdate(queryAlterTableEnvio);




*/


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = userEt.getText().toString();
                String password = passwordEt.getText().toString();
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Login);

                //Propiedades de usuario y password
                if (user.equals("")){
                    alertMsj.setVisibility(View.VISIBLE);
                    alertMsj.setText("El usuario no puede ser vacio");
                    return;
                }
                else if (password.equals("")){
                        alertMsj.setVisibility(View.VISIBLE);
                        alertMsj.setText("La contraseña no puede ser vacia");
                        return;
                    }

                else if (password.length()<=5){
                        alertMsj.setVisibility(View.VISIBLE);
                        alertMsj.setText("La contraseña debe ser mayor a cinco caracteres");
                        return;
                    }

                else{
                        alertMsj.setVisibility(View.INVISIBLE);
                        validarUsuario(user, password);
                        progressBar.setVisibility(ImageView.VISIBLE);
                    }
                    }




        });
    }

    /*public void validarUsuario (final String user, final String pass){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, Usuario>(){

            @Override
            protected Usuario doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id, username, name, address FROM users WHERE username = ");
                query.append("'" + user + "'");
                query.append(" AND password = ");
                query.append("'" + pass + "'");
                //Obtengo los resultados de la consulta
                ResultBaseDeDatos result = mySql.executeQuery(query.toString());

                Usuario us = new Usuario();
                if (result != null) {
                    try {
                        while (rs.next()) {

                            us.setId(Integer.parseInt(rs.getString(1)));
                            us.setUserName(rs.getString(2));
                            us.setName(rs.getString(3));
                            us.setAdress(rs.getString(4));

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return us;
            }

            @Override
            protected void onPostExecute(Usuario usuario) {
                super.onPostExecute(usuario);

                if (usuario.getUserName() != null)
                {
                    Intent myIntent = new Intent(Login.this, ScanActivity.class);
                    myIntent.setAction(SEND_INFO_USER);
                    myIntent.putExtra(ID_USUARIO, usuario.getId());
                    myIntent.putExtra(USERNAME_USUARIO, usuario.getUserName());
                    myIntent.putExtra(NAME_USUARIO, usuario.getName());
                    myIntent.putExtra(ADRESS_USUARIO, usuario.getAdress());
                    Login.this.startActivity(myIntent);
                    Login.this.finish();
                }

                else {

                    progressBar.setVisibility(ImageView.INVISIBLE);
                    alertMsj.setVisibility(View.VISIBLE);
                    alertMsj.setText("Usuario o contraseña incorrecto");
                    passwordEt.setText("");
                    return;
                }
            }
        }.execute();
    };

*/


    public void validarUsuario (final String user, final String pass){
        //Se utiliza para ejecutar busquedas dentro de la BD
        new AsyncTask<Void, Void, ResultBaseDeDatos>(){

            @Override
            protected ResultBaseDeDatos doInBackground(Void... voids) {

                mySql = new MySql();
                StringBuffer query = new StringBuffer();
                query.append("SELECT id, username, name, address FROM users WHERE username = ");
                query.append("'" + user + "'");
                query.append(" AND password = ");
                query.append("'" + pass + "'");
                //Obtengo los resultados de la consulta
                ResultBaseDeDatos result = mySql.executeQuery(query.toString());

                /*Usuario us = new Usuario();
                if (result != null) {
                    try {
                        while (rs.next()) {

                            us.setId(Integer.parseInt(rs.getString(1)));
                            us.setUserName(rs.getString(2));
                            us.setName(rs.getString(3));
                            us.setAdress(rs.getString(4));

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/
                return result;
            }

            @Override
            protected void onPostExecute(ResultBaseDeDatos result) {
                super.onPostExecute(result);

                if (result != null) {
                    if (result.Conecto()) {
                        ResultSet rs = result.getResultSet();
                        try {
                            if (rs.isBeforeFirst()) {
                                try {
                                    while (rs.next()) {
                                        Intent myIntent = new Intent(Login.this, ScanActivity.class);
                                        myIntent.setAction(SEND_INFO_USER);
                                        myIntent.putExtra(ID_USUARIO, Integer.parseInt(rs.getString(1)));
                                        myIntent.putExtra(USERNAME_USUARIO, rs.getString(2));
                                        myIntent.putExtra(NAME_USUARIO, rs.getString(3));
                                        myIntent.putExtra(ADRESS_USUARIO, rs.getString(4));
                                        Login.this.startActivity(myIntent);
                                        Login.this.finish();
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                progressBar.setVisibility(ImageView.INVISIBLE);
                                alertMsj.setVisibility(View.VISIBLE);
                                alertMsj.setText("Usuario o contraseña incorrecto");
                                passwordEt.setText("");
                                return;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No se pudo conectar, revise su conexion y vuelva a intentarlo", Toast.LENGTH_SHORT);
                        toast.show();
                        progressBar.setVisibility(ImageView.INVISIBLE);
                        passwordEt.setText("");
                        return;
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Error de conexión, vuelva a intentarlo", Toast.LENGTH_SHORT);
                    toast.show();
                    progressBar.setVisibility(ImageView.INVISIBLE);
                    passwordEt.setText("");
                    return;
                }


                /*if (usuario.getUserName() != null)
                {
                    Intent myIntent = new Intent(Login.this, ScanActivity.class);
                    myIntent.setAction(SEND_INFO_USER);
                    myIntent.putExtra(ID_USUARIO, usuario.getId());
                    myIntent.putExtra(USERNAME_USUARIO, usuario.getUserName());
                    myIntent.putExtra(NAME_USUARIO, usuario.getName());
                    myIntent.putExtra(ADRESS_USUARIO, usuario.getAdress());
                    Login.this.startActivity(myIntent);
                    Login.this.finish();
                }

                else {

                    progressBar.setVisibility(ImageView.INVISIBLE);
                    alertMsj.setVisibility(View.VISIBLE);
                    alertMsj.setText("Usuario o contraseña incorrecto");
                    passwordEt.setText("");
                    return;
                }*/
            }
        }.execute();
    };





    public void executeUpdate(final String s){
        //Se utiliza para agregar, eliminar o modificar informacion de la BD.

        new AsyncTask<Void,Void,Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                mySql = new MySql();
                //Obtengo los resultados de la consulta
                ResultBaseDeDatosUpdates result = mySql.executeUpdate(s);
                if (result != null) {
                    if (result.Conecto()) {
                        int rs = result.getResultado();
                        Log.v("MySql", String.valueOf(rs));
                    } else {
                        Log.v("MySql", "Error de conexión");
                    }
                }else {
                    Log.v("MySql", "Error de conexión");
                }



                return null;
            }
        }.execute();



    };





    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Esta seguro que desea salir ?")
                .setCancelable(false)
                .setNegativeButton("No", null)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Login.this.finish();

                    }
                })
                .show();
    }


}

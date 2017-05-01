package example.com.investigacionoperativa2017;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ObjetosBD.ResultBaseDeDatos;
import ObjetosBD.ResultBaseDeDatosUpdates;

/**
 * Created by Joaking on 4/4/2017.
 */

public class MySql {
    private Connection conn;
    private Statement st;

    public MySql(){
    }

    public void cerrarConexion(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement crearConn(){

        try {
            Class.forName("com.mysql.jdbc.Driver");
            // "jdbc:mysql://IP:PUERTO/DB", "USER", "PASSWORD");
            // Si estás utilizando el emulador de android y tenes el mysql en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
            conn = DriverManager.getConnection(
                    "jdbc:mysql://investigacionoperativa.cjwutp4sthue.us-west-2.rds.amazonaws.com:3306", "io", "moisesbueno");

            if (conn != null) {
                st = conn.createStatement();
                st.executeUpdate("USE investigacionoperativa");
                Log.v("MySql", "Conexion con BD establecida.");
            }
        } catch (SQLException se) {
            Log.v("MySql","oops! No se puede conectar. Error: " + se.toString());
        } catch (ClassNotFoundException e) {
            Log.v("MySql","oops! No se encuentra la clase. Error: " + e.getMessage());
        }

        return st;
    }

    public ResultBaseDeDatos executeQuery(String s){
        ResultBaseDeDatos result = new ResultBaseDeDatos();
        try {
            crearConn();
            if (st != null) {
                result.setConecto(true);
                ResultSet rs = st.executeQuery(s);
                result.setResultSet(rs);
                return result;
            }
            else{
                result.setConecto(false);
                result.setResultSet(null);
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.v("MySql","Error en executeQuery-MySql");
            return null;
        }
    }

    public ResultBaseDeDatosUpdates executeUpdate(String s){

        ResultBaseDeDatosUpdates result = new ResultBaseDeDatosUpdates();
        try {
            crearConn();
            if (st != null) {
                result.setConecto(true);
                int rs = st.executeUpdate(s);
                result.setResultado(rs);
                return result;
            }
            else{
                result.setConecto(false);
                result.setResultado(-1);
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

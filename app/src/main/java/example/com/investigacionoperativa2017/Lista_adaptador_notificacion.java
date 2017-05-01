package example.com.investigacionoperativa2017;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ObjetosBD.Entrada_notificacion;

/**
 * Created by Joaking on 4/27/2017.
 */

public class Lista_adaptador_notificacion extends ArrayAdapter<Entrada_notificacion>{

    private ArrayList<Entrada_notificacion> entradas;
    private int R_layout_IdView;
    private Context contexto;

    public Lista_adaptador_notificacion(Context contexto, int R_layout_IdView, ArrayList<Entrada_notificacion> entradas) {
        super(contexto, R_layout_IdView, entradas);
        this.contexto = contexto;
        this.entradas = entradas;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(final int posicion, View view, ViewGroup pariente) {

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }

        TextView dire_donante = (TextView) view.findViewById(R.id.tv_direccion_txt);
        dire_donante.setText(entradas.get(posicion).getDire_donante());

        TextView texto_donante = (TextView) view.findViewById(R.id.tv_donante_txt);
        texto_donante.setText(entradas.get(posicion).getNombre_donante());

        TextView texto_fecha_envio = (TextView) view.findViewById(R.id.tv_fecha_txt);
        texto_fecha_envio.setText(entradas.get(posicion).getFecha_envio());

        TextView texto_descripcion_envio = (TextView) view.findViewById(R.id.tv_descripcion_notif_txt);
        texto_descripcion_envio.setText(entradas.get(posicion).getDescripcion());

        return view;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Entrada_notificacion getItem(int i) {
        return entradas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}

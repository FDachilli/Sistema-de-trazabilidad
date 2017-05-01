package example.com.investigacionoperativa2017;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ObjetosBD.Entrada_lista_envio;

/**
 * Created by Franco on 20/4/2017.
 */


public class Lista_adaptador_envio extends ArrayAdapter<Entrada_lista_envio> {

    private ArrayList<Entrada_lista_envio> entradas;
    private int R_layout_IdView;
    private Context contexto;

    public Lista_adaptador_envio(Context contexto, int R_layout_IdView, ArrayList<Entrada_lista_envio> entradas) {
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

        TextView texto_donante = (TextView) view.findViewById(R.id.text_donante_lista);
        texto_donante.setText(entradas.get(posicion).getDonante());

        TextView texto_receptor = (TextView) view.findViewById(R.id.text_receptor_lista);
        texto_receptor.setText(entradas.get(posicion).getReceptor());

        TextView texto_descripcion = (TextView) view.findViewById(R.id.text_descripcion_lista);
        texto_descripcion.setText(entradas.get(posicion).getDescripcion());

        TextView texto_fecha_envio = (TextView) view.findViewById(R.id.text_fecha_envio_lista);
        texto_fecha_envio.setText(entradas.get(posicion).getFecha_envio());

        TextView texto_fecha_rec = (TextView) view.findViewById(R.id.text_fecha_recepcion_lista);
        texto_fecha_rec.setText(entradas.get(posicion).getFecha_recepcion());

        TextView dire_donante = (TextView) view.findViewById(R.id.text_dir_donante_lista);
        dire_donante.setText(entradas.get(posicion).getDire_donante());

        TextView dire_receptor = (TextView) view.findViewById(R.id.text_dir_rec_lista);
        dire_receptor.setText(entradas.get(posicion).getDire_receptor());

        TextView longitud_envio = (TextView) view.findViewById(R.id.text_long_envio_lista);
        longitud_envio.setText(String.valueOf(entradas.get(posicion).getLongitud_donante()));

        TextView latitud_envio = (TextView) view.findViewById(R.id.text_lat_envio_lista);
        latitud_envio.setText(String.valueOf(entradas.get(posicion).getLatitud_donante()));

        TextView longitud_rec = (TextView) view.findViewById(R.id.text_long_rec_lista);
        longitud_rec.setText(String.valueOf(entradas.get(posicion).getLongitud_receptor()));

        TextView latitud_rec = (TextView) view.findViewById(R.id.text_lat_rec_lista);
        latitud_rec.setText(String.valueOf(entradas.get(posicion).getLatitud_receptor()));


        ImageView imageView = (ImageView) view.findViewById(R.id.image_estado_envio);
        imageView.setImageResource(entradas.get(posicion).getIdImagen());

        Button mapDonanteButton = (Button) view.findViewById(R.id.button_map_donante);
        mapDonanteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + entradas.get(posicion).getLatitud_donante() + "," + entradas.get(posicion).getLongitud_donante() + " (" + "Ubicación de salida del producto"+ ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                contexto.startActivity(intent);
            }
        });

        Button mapReceptorButton = (Button) view.findViewById(R.id.button_map_receptor);
        mapReceptorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (entradas.get(posicion).getLatitud_receptor() != 0){
                    String geoUri = "http://maps.google.com/maps?q=loc:" + entradas.get(posicion).getLatitud_receptor() + "," + entradas.get(posicion).getLongitud_receptor() + " (" + "Ubicación de llegada del producto"+ ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    contexto.startActivity(intent);
                }
                else {
                    Toast toast = Toast.makeText(contexto,
                            "Aun no se ha recibido el producto", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Entrada_lista_envio getItem(int i) {
        return entradas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}

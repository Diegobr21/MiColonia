package com.micolonia.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentoNegSer extends Fragment {

    private NegocioSer servicio;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        servicio = getArguments().getParcelable("servicio");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Una vez se cargue todas las vistas del fragmento podemos utilizarlas.
        // Aquí envío un mensaje sencillo con un toast
        Toast.makeText(getActivity(), "Bienvenid@" , Toast.LENGTH_SHORT).show();
    }
    public FragmentoNegSer() {
        //Se necesita el constructor vacío
    };
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState){
        View view= inflater.inflate(R.layout.fragm_negoserv, container, false);

        //String [] nombre_servicio={"Revision General" , "Encerado", "Daño a tuberías", "Lavado de Autos"};

       // String [] nombre_servicio={servicio.getElemento1(), servicio.getElemento2(), servicio.getElemento3(),
       // servicio.getElemento4()};


        ArrayList<String> nombre_serv = new ArrayList<>();

        String elemento1 = servicio.getElemento1().trim();
        String elemento2 = servicio.getElemento2().trim();
        String elemento3 = servicio.getElemento3().trim();
        String elemento4 = servicio.getElemento4().trim();
        String elemento5 = servicio.getElemento5().trim();
        String elemento6 = servicio.getElemento6().trim();

        if(elemento1.length()>1){
            nombre_serv.add(elemento1);
        }
        if(elemento2.length()>1){
            nombre_serv.add(elemento2);
        }
        if(elemento3.length()>1){
            nombre_serv.add(elemento3);
        }
        if(elemento4.length()>1){
            nombre_serv.add(elemento4);
        }
        if(elemento5.length()>1){
            nombre_serv.add(elemento5);
        }
        if(elemento6.length()>1){
            nombre_serv.add(elemento6);
        }
       // nombre_serv.add("A");
        //nombre_serv.add("B");
       // nombre_serv.add("C");
        /*
        nombre_serv.add(servicio.getElemento1());
        if(servicio.getElemento2()!= null){
            nombre_serv.add(servicio.getElemento2());
        }
        if(servicio.getElemento3()!= null){
            nombre_serv.add(servicio.getElemento3());
        }
        if(servicio.getElemento4()!= null){
            nombre_serv.add(servicio.getElemento4());
        }

        if(servicio.getElemento5()!= null){
            nombre_serv.add(servicio.getElemento5());
        }
        if(servicio.getElemento6()!= null){
            nombre_serv.add(servicio.getElemento6());
        }

*/
        ListView listView = (ListView) view.findViewById(R.id.lista_servicios);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,nombre_serv);
        listView.setAdapter(arrayAdapter);


        return view;
    }
}


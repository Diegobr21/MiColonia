package com.micolonia.app;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Fragm_ayuda2 extends Fragment {

    public Fragm_ayuda2(){
        //constructor
    }
@Nullable
@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragm_ayuda2, container, false);

        ArrayList<String> usos = new ArrayList<>();
        usos.add("* Publicar a la comunidad, ya sean cotidianeidades, eventos, campañas, propuestas, encuestas, etc. \n");
        usos.add("* Dar a conocer negocios en crecimiento, ya sean de comida o servicios\n");
        usos.add("* Compra, venta o renta para particulares, en la seccion de avisos de ocasión\n");


        ListView listView = (ListView) view.findViewById(R.id.lista_usos);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, usos);
        listView.setAdapter(arrayAdapter2);


        return view;
    }

}

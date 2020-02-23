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

public class Fragm_ayuda1 extends Fragment {


    public Fragm_ayuda1(){
        //constructor
    }
@Nullable
@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragm_ayuda1, container, false);

        ArrayList<String> propositos = new ArrayList<>();
        propositos.add("* Crear una comunidad y un medio de contacto disponible para todos los residentes\n");
        propositos.add("* Establecer por medio de la tecnología un ambiente mas vigilado y por ende, seguro\n");
        propositos.add("* Brindar oportunidades de crecimiento a negocios ya existentes\n");
        propositos.add("* Crear oportunidades para nuevos negocios\n");
        propositos.add("* Beneficiar a la comunidad brindando la comodidad de tener información al alcance\n");
        propositos.add("* Y en base a la comunicación, tomar acciones para el progreso de nuestra comunidad\n");

    ListView listView = (ListView) view.findViewById(R.id.lista_propositos);
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, propositos);
        listView.setAdapter(arrayAdapter);

        return view;
}

}

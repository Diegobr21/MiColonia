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

public class FragmentoNegCom extends Fragment {

    // Agregamos el objeto comida públicamente
    private NegocioCom comida;

    // Agregamos el onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializamos el objeto comida, lo cargamos desde los argumentos
        // que enviamos en la activity Comida
        comida = getArguments().getParcelable("comida");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Una vez se cargue todas las vistas del fragmento podemos utilizarlas.
        // Aquí envío un mensaje sencillo con un toast
        Toast.makeText(getActivity(), "Comida: " + comida.getName(), Toast.LENGTH_SHORT).show();
    }

    public FragmentoNegCom() {
        //Se necesita el constructor vacío
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_negcomida, container, false);

       // String[] nombre_platillo = {"1) "+comida.getElemento1(),"2) "+ comida.getElemento2(), "3) "+comida.getElemento3(),
          //      "4) "+comida.getElemento4(), "5) "+comida.getElemento5()};
        /*String[] nombre_platillo = {comida.getElemento1(), comida.getElemento2(), comida.getElemento3(),
                comida.getElemento4(), comida.getElemento5()};
*/


        ArrayList<String> nombre_platillo = new ArrayList<>();

        String elemento1 = comida.getElemento1().toString().trim();
        String elemento2 = comida.getElemento2().toString().trim();
        String elemento3 = comida.getElemento3().toString().trim();
        String elemento4 = comida.getElemento4().toString().trim();
        String elemento5 = comida.getElemento5().toString().trim();
        String elemento6 = comida.getElemento6().toString().trim();


        if(elemento1.length()>1){
            nombre_platillo.add(elemento1);
        }
        if(elemento2.length()>1){
            nombre_platillo.add(elemento2);
        }
        if(elemento3.length()>1){
            nombre_platillo.add(elemento3);
        }
        if(elemento4.length()>1){
            nombre_platillo.add(elemento4);
        }
        if(elemento5.length()>1){
            nombre_platillo.add(elemento5);
        }
        if(elemento6.length()>1){
            nombre_platillo.add(elemento6);
        }





       /*
        nombre_platillo.add(comida.getElemento1());
        if(comida.getElemento2()!= null){
            nombre_platillo.add(comida.getElemento2());
        }
        if(comida.getElemento3()!= null){
            nombre_platillo.add(comida.getElemento3());
        }
        if(comida.getElemento4()!= null){
            nombre_platillo.add(comida.getElemento4());
        }

        if(comida.getElemento5()!= null){
            nombre_platillo.add(comida.getElemento5());
        }
        if(comida.getElemento6()!= null){
            nombre_platillo.add(comida.getElemento6());
        }

        */
        ListView listView = (ListView) view.findViewById(R.id.lista_platillos);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nombre_platillo);
        listView.setAdapter(arrayAdapter);

        return view;
    }
}

package com.micolonia.app;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class CustomOnItemSelectedSpinner implements AdapterView.OnItemSelectedListener {
 private String colonia_select;
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String colonia = parent.getItemAtPosition(pos).toString();
        Toast.makeText(parent.getContext(), colonia,
                Toast.LENGTH_SHORT).show();
        colonia_select = parent.getItemAtPosition(pos).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public String getColonia(){
        return colonia_select;
    }
}

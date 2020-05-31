package com.micolonia.app;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;


public class Drawer_Inicio extends AppCompatActivity {

    private DrawerLayout drawerini;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_inicio);

        getSupportActionBar().hide();

        Toolbar toolbar = findViewById(R.id.inicio_toolbar2);
        drawerini = findViewById(R.id.nav_drawerlayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerini, toolbar,
                R.string.drawer_abrir, R.string.drawer_cierre);
        drawerini.addDrawerListener(toggle);
        toggle.syncState();
    }


}

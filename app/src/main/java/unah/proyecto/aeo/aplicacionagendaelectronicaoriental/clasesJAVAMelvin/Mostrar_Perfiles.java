package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;

public class Mostrar_Perfiles extends AppCompatActivity {
    ArrayList<Fuente_mostrarPerfiles> mostrar_perfiles;
    private ListView lista;
    private int perfilselecionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar__perfiles);

        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mostrar_perfiles= new ArrayList<Fuente_mostrarPerfiles>();
        lista=(ListView)findViewById(R.id.listviewperfiles);
       // llenarLista();
    }
}

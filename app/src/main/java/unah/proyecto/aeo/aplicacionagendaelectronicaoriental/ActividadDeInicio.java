package unah.proyecto.aeo.aplicacionagendaelectronicaoriental;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.ActivityCategorias;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.provider.AEODbHelper;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.sync.SyncAdapter;

/**
 * Created by melvinrivera on 22/4/18.
 */

public class ActividadDeInicio extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AEODbHelper    aeoDbHelper = new AEODbHelper(this);
        SyncAdapter.initializeSyncAdapter(this);


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if(sharedPref.getString("preferencia","ExistePreferencia").isEmpty()){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("preferencia","ExistePreferencia");
            editor.commit();

        }

        Intent i = new Intent(getApplicationContext(), ActivityCategorias.class);
        startActivity(i);
        finish();

    }
}

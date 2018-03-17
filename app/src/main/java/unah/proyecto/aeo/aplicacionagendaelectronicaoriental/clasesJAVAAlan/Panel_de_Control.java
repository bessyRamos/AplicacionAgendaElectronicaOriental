package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin.ListaDeContactos;

public class Panel_de_Control extends AppCompatActivity {
    ListView lista;
   public Fuente_Panel_de_control[] fuente_panel_de_control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_de__control);
        setContentView(R.layout.activity_panel_de__control);
        lista=(ListView)findViewById(R.id.listViewPneldeControl);



            Fuente_Panel_de_control fuente_panel_de_control[]={
                    new Fuente_Panel_de_control(R.drawable.administracioncuenta,"Administración de  Cuenta",0)
                    //new Fuente_Panel_de_control(R.drawable.administracionperfil,"Administración de Perfil",0)
                   // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Nuevas",0),
                   // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Aprovadas",0),
                   // new Fuente_Panel_de_control(R.drawable.celular,"Solicitudes Eliminadas",0)

            };
            final Adaptador_paneldeControl adapter  = new Adaptador_paneldeControl(this,fuente_panel_de_control);
            lista.setAdapter(adapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    if(position==0){
                       Intent intent = new Intent(view.getContext(),Mostrar_Usuarios.class);
                        startActivityForResult(intent,0);
                    }else if(position==1){
                        Intent intent = new Intent(view.getContext(),Mostrar_Usuarios.class);
                        startActivityForResult(intent,0);
                    }else if(position==2){
                        Intent intent = new Intent(view.getContext(),ListaDeContactos.class);
                        startActivityForResult(intent,0);
                    }

                }
            });
        }


    }








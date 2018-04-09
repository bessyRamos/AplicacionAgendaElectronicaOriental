package unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAMelvin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.R;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Editar_Usuarios;
import unah.proyecto.aeo.aplicacionagendaelectronicaoriental.clasesJAVAAlan.Mostrar_Usuarios;

public class EditarPerfil extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1 ;

    int id_perfilEditar;
    CircleImageView imagenOrg;
     Bitmap imagenBitmap;
    FloatingActionButton botonGuardar;
    TextInputEditText etnombreeorganizacion, etnumerofijo, etnumerocel, etdireccion, etemail, etdescripcion, etlatitud, etlongitud;
    Spinner spcategorias, spregiones;
    //variables que almacenaran los datos traidos del webservice
    String nomborg_rec ;
    String numtel_rec ;
    String numcel_rec ;
    String direccion_rec ;
    String email_rec ;
    String desc_rec ;
    String lati_rec ;
    String longitud_rec ;
    String imagen_rec ;
    int idregion_rec ;
    int idcategoria_rec ;
    //controla si existe imagen en el contacto traido desde el webservice
    boolean tieneImagen;
    //
    ArrayList listaCategorias, listaRegiones;

    int id_categoria, id_region;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    String encodeImagen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //imagenOrg = findViewById(R.id.imagenDeOrganizacion);
        botonGuardar= findViewById(R.id.botonGuardar);
        etnombreeorganizacion = findViewById(R.id.etnombreeorganizacion);
        etnumerofijo = findViewById(R.id.etnumerofijo);
        etnumerocel = findViewById(R.id.etnumerocel);
        etdireccion = findViewById(R.id.etdireccion);
        etemail = findViewById(R.id.etemail);
        etdescripcion = findViewById(R.id.etdescripcion);
        etlatitud = findViewById(R.id.etlatitud);
        etlongitud = findViewById(R.id.etlongitud);

        spcategorias = findViewById(R.id.spinercategoriaPerfil);
        spregiones = findViewById(R.id.spinerregionPerfil);

        listaCategorias=new ArrayList();
        listaRegiones=new ArrayList();


        Bundle a = getIntent().getExtras();
        id_perfilEditar=a.getInt("id");

        Toast.makeText(getApplicationContext(),"Cargando...",Toast.LENGTH_SHORT).show();

        new llenarEditTexEditarPerfil().execute();

        new llenarSpinnersPerfil().execute();



        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


        }
        //validar();
        /*imagenOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestRead();
            }
        });*/

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
                Toast.makeText(getApplicationContext(),"Procesando...",Toast.LENGTH_SHORT).show();
                switch (spcategorias.getSelectedItem().toString()){
                    case "Emergencia":
                        id_categoria=1;
                        break;
                    case "Educación":
                        id_categoria=2;
                        break;
                    case "Centros Asistenciales":
                        id_categoria=3;
                        break;
                    case "Bancos":
                        id_categoria=4;
                        break;
                    case "Hostelería y Turismo":
                        id_categoria=5;
                        break;
                    case "Instituciones Públicas":
                        id_categoria=6;
                        break;
                    case "Comercio de Bienes":
                        id_categoria=7;
                        break;
                    case "Comercio de Servicios":
                        id_categoria=8;
                        break;
                    case "Bienes y Raíces":
                        id_categoria=9;
                        break;
                    case "Asesoría Legal":
                        id_categoria=10;
                        break;
                    case "Funerarias":
                        id_categoria=11;
                        break;
                }

                switch (spregiones.getSelectedItem().toString()){
                    case "Danlí":
                        id_region=3;
                        break;
                    case "El Paraíso":
                        id_region=4;
                        break;
                }


//                imagenBitmap = ((BitmapDrawable)imagenOrg.getDrawable()).getBitmap();

               /* new AsyncTask<Void, Void, String>(){
                    @Override
                    protected String doInBackground(Void... voids) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imagenBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                        byte b []= baos.toByteArray();

                        encodeImagen = Base64.encodeToString(b,Base64.DEFAULT);

                        return null;
                    }
                }.execute();*/




                new actualizarPerfil().execute();

            }
        });

    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Permission Denied
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imagenOrg.setImageURI(imageUri);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    private void validar(){

        //id.setError(null);
        etnombreeorganizacion.setError(null);
        etnumerofijo.setError(null);
        etnumerocel.setError(null);
        etdireccion.setError(null);
        etemail.setError(null);
        etdescripcion.setError(null);
        etlatitud.setError(null);
        etlongitud.setError(null);


        // String idd = id.getText().toString();
        String nomborg = etnombreeorganizacion.getText().toString();
        String numtel = etnumerofijo.getText().toString();
        String numcel = etnumerocel.getText().toString();
        String direccion = etdireccion.getText().toString();
        String desc = etdescripcion.getText().toString();
        String lati = etlatitud.getText().toString();
        String longitud = etlongitud.getText().toString();


        if(TextUtils.isEmpty(nomborg)){
            etnombreeorganizacion.setError(getString(R.string.errNombreOrg));
            etnombreeorganizacion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(numtel) && TextUtils.isEmpty(numcel)){
            etnumerofijo.setError(getString(R.string.errNumero));
            etnumerofijo.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(direccion)){
            etdireccion.setError(getString(R.string.errDir));
            etdireccion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(desc)){
            etdescripcion.setError(getString(R.string.errDesc));
            etdescripcion.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(lati)){
            etlatitud.setError(getString(R.string.errLat));
            etlatitud.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(longitud)){
            etlongitud.setError(getString(R.string.errLong));
            etlongitud.requestFocus();
            return;
        }

    }

    private class llenarEditTexEditarPerfil extends AsyncTask<String, Integer, Boolean> {
        private llenarEditTexEditarPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                JSONObject respJSON = new JSONObject(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/consultarDatosDePerfilParaEditar.php?id_contacto="+id_perfilEditar)).getEntity()));
                JSONArray jsonArray = respJSON.getJSONArray("perfiles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    nomborg_rec = jsonArray.getJSONObject(i).getString("nombre_organizacion");
                    numtel_rec = jsonArray.getJSONObject(i).getString("numero_fijo");
                    numcel_rec = jsonArray.getJSONObject(i).getString("numero_movil");
                    direccion_rec = jsonArray.getJSONObject(i).getString("direccion");
                    if(jsonArray.getJSONObject(i).getString("imagen").isEmpty()){
                        tieneImagen=false;
                    }else{
                        imagen_rec = jsonArray.getJSONObject(i).getString("imagen");
                        tieneImagen=true;
                    }

                    email_rec = jsonArray.getJSONObject(i).getString("e_mail");
                    desc_rec = jsonArray.getJSONObject(i).getString("descripcion_organizacion");
                    lati_rec = jsonArray.getJSONObject(i).getString("latitud");
                    longitud_rec = jsonArray.getJSONObject(i).getString("longitud");
                    idregion_rec = jsonArray.getJSONObject(i).getInt("id_region");
                    idcategoria_rec = jsonArray.getJSONObject(i).getInt("id_categoria");
                }

                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {

            if (resul) {
                /*if(tieneImagen==true){
                    byte[] byteCode = Base64.decode(imagen_rec, Base64.DEFAULT);
                    imagenOrg.setImageBitmap(BitmapFactory.decodeByteArray(byteCode,0,byteCode.length));
                }else {
                    imagenOrg.setImageResource(R.drawable.iconocontactowhite);
                }*/

                etnombreeorganizacion.setText(nomborg_rec);
                etnumerofijo.setText(numtel_rec);
                etnumerocel.setText(numcel_rec);
                etdireccion.setText(direccion_rec);
                etemail.setText(email_rec);
                etdescripcion.setText(desc_rec);
                etlatitud.setText(lati_rec);
                etlongitud.setText(longitud_rec);



            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private class actualizarPerfil extends AsyncTask<String, Integer, Boolean> {
        private actualizarPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                HttpClient httpclient;
                HttpPost httppost;
                ArrayList<NameValuePair> parametros;
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost("https://shessag.000webhostapp.com/actualizarPerfil.php");
                parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("id_contacto", String.valueOf(id_perfilEditar)));
                parametros.add(new BasicNameValuePair("nomborg_rec",etnombreeorganizacion.getText().toString()));
                parametros.add(new BasicNameValuePair("numtel_rec",etnumerofijo.getText().toString()));
                parametros.add(new BasicNameValuePair("numcel_rec",etnumerocel.getText().toString()));
                parametros.add(new BasicNameValuePair("direccion_rec",etdireccion.getText().toString()));
                parametros.add(new BasicNameValuePair("email_rec",etemail.getText().toString()));
                parametros.add(new BasicNameValuePair("desc_rec",etdescripcion.getText().toString()));
                parametros.add(new BasicNameValuePair("lat_rec",etlatitud.getText().toString()));
                parametros.add(new BasicNameValuePair("longitud_rec",etlongitud.getText().toString()));
                parametros.add(new BasicNameValuePair("id_categoria",String.valueOf(id_categoria)));
                parametros.add(new BasicNameValuePair("id_region",String.valueOf(id_region)));
                parametros.add(new BasicNameValuePair("imagen_rec",encodeImagen));

                httppost.setEntity(new UrlEncodedFormEntity(parametros, "UTF-8"));

                httpclient.execute(httppost);

               /* EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("?id_contacto="+id_perfilEditar
                        +"&nomborg_rec="+etnombreeorganizacion.getText().toString().replace(" ","%20")+
                        "&numtel_rec="+etnumerofijo.getText().toString()+"&numcel_rec="+etnumerocel.getText().toString()+"&direccion_rec="+
                        etdireccion.getText().toString().replace(" ","%20")+"&email_rec="+etemail.getText().toString()+"&desc_rec="
                        +etdescripcion.getText().toString().replace(" ","%20")+"&lat_rec="+etlatitud.getText().toString()+
                        "&longitud_rec="+etlongitud.getText().toString().replace("-","%2D")+"&id_categoria="+id_categoria+"&id_region="+id_region+"&imagen_rec="+  encodeImagen.toString())).getEntity());
                */
                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }


        protected void onPostExecute(Boolean result) {
            if (resul) {
                if (etnombreeorganizacion.getError()==null &&
                etnumerofijo.getError()==null &&
                etnumerocel.getError()==null &&
                etdireccion.getError()==null &&
                etemail.getError()==null &&
                etdescripcion.getError()==null &&
                etlatitud.getError()==null &&
                etlongitud.getError()==null){
                    Toast.makeText(getApplicationContext(),"Perfil Actualizado Correctamente",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AdministracionDePerfiles.class));
                    finish();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private class llenarSpinnersPerfil extends AsyncTask<String, Integer, Boolean> {
        private llenarSpinnersPerfil(){}
        boolean resul = true;

        @Override
        protected Boolean doInBackground(String... strings) {

            try {

                JSONArray regionesWS = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/consultarRegiones.php")).getEntity()));
                JSONArray categoriasWS = new JSONArray(EntityUtils.toString(new DefaultHttpClient().execute(new HttpPost("https://shessag.000webhostapp.com/consultarCategorias.php")).getEntity()));

                for (int i = 0; i < regionesWS.length(); i++) {
                    listaRegiones.add(regionesWS.getJSONObject(i).getString("nombre_region"));
                }
                for (int i=0;i<categoriasWS.length();i++){
                    listaCategorias.add(categoriasWS.getJSONObject(i).getString("nombre_categoria"));
                }

                resul = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }
            return resul;

        }

        protected void onPostExecute(Boolean result) {
            if (resul) {
                ArrayAdapter adaptadorCategorias = new ArrayAdapter<>(getApplicationContext(),R.layout.adaptacion_spinner,listaCategorias);
                ArrayAdapter adaptadorRegiones = new ArrayAdapter<>(getApplicationContext(),R.layout.adaptacion_spinner,listaRegiones);
                spcategorias.setAdapter(adaptadorCategorias);
                spregiones.setAdapter(adaptadorRegiones);
                if(idregion_rec==3){
                    spregiones.setSelection(0);
                }else if(idregion_rec==4){
                    spregiones.setSelection(1);
                }

                switch (idcategoria_rec){
                    case 1:
                        spcategorias.setSelection(0);
                        break;
                    case 2:
                        spcategorias.setSelection(1);
                        break;
                    case 3:
                        spcategorias.setSelection(2);
                        break;
                    case 4:
                        spcategorias.setSelection(3);
                        break;
                    case 5:
                        spcategorias.setSelection(4);
                        break;
                    case 6:
                        spcategorias.setSelection(5);
                        break;
                    case 7:
                        spcategorias.setSelection(6);
                        break;
                    case 8:
                        spcategorias.setSelection(7);
                        break;
                    case 9:
                        spcategorias.setSelection(8);
                        break;
                    case 10:
                        spcategorias.setSelection(9);
                        break;
                    case 11:
                        spcategorias.setSelection(10);
                        break;
                }

            }else {
                Toast.makeText(getApplicationContext(), "Problemas de conexión", Toast.LENGTH_SHORT).show();
            }
        }


    }
}

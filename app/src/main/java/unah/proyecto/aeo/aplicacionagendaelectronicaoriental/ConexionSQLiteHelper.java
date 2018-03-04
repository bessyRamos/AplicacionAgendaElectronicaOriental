package unah.proyecto.aeo.aplicacionagendaelectronicaoriental;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by melvinrivera on 22/2/18.
 */

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    private int imagenPredeterminada=R.drawable.iconocontactowhite;

    final String CREAR_TABLA_ROLES = "create table Roles(id_rol INTEGER PRIMARY KEY, descripcion_rol TEXT)";
    final String CREAR_TABLA_USUARIOS = "create table Usuarios(id_usuario INTEGER PRIMARY KEY, nombre_usuario TEXT, nombre_propio TEXT, contrasena TEXT, rol INTEGER, estado_usuario INTEGER, FOREIGN KEY(rol) REFERENCES Roles(id_rol))";
    final String CREAR_TABLA_CATEGORIAS = "create table Categorias(id_categoria INTEGER PRIMARY KEY, nombre_categoria TEXT NOT NULL, imagen_categoria INT)";
    final String CREAR_TABLA_REGIONES = "create table Regiones(id_region INTEGER PRIMARY KEY, nombre_region TEXT NOT NULL)";
    final String CREAR_TABLA_ESTADO_CONTACTOS = "create table Estado_Contactos(id_estado INTEGER PRIMARY KEY, descripcion_estado_contactos TEXT NOT NULL)";

    final String CREAR_TABLA_CONTACTOS = "create table Contactos(id_contacto INTEGER PRIMARY KEY, nombre_organizacion TEXT NOT NULL, numero_fijo TEXT," +
            " numero_movil TEXT, direccion TEXT NOT NULL, imagen INTEGER DEFAULT "+imagenPredeterminada+", e_mail TEXT, descripcion_organizacion TEXT NOT NULL, latitud DOUBLE, " +
            "longitud DOUBLE, id_usuario INTEGER, id_categoria INTEGER, id_estado INTEGER, id_region INTEGER, FOREIGN KEY(id_usuario) REFERENCES Usuarios(id_usuario)," +
            "  FOREIGN KEY(id_categoria) REFERENCES Categoria(id_categoria) , FOREIGN KEY(id_estado) REFERENCES Estado_Contactos(id_estado),  FOREIGN KEY(id_region) REFERENCES Regiones(id_region))";

    final String INSERTAR_ROL="INSERT INTO Roles values(1,'Administrador'), " +
            "(2,'Cliente')";
//99-45-02-13
    final String INSERTAR_USUARIOS="INSERT INTO Usuarios values(1,'Admin','Administrador','admin123',1,1), (2,'cliente1','nombreCliente','cliente123',2,1), (3,'cliente2','nombreCliente2','cliente123',2,1)";

    final String INSERTAR_CATEGORIAS = "INSERT INTO Categorias values(1,'Emergencia',"+R.drawable.emergencia+")" +
            ",(2,'Educación',"+R.drawable.educacion +"),(3,'Centros Asistenciales',"+R.drawable.centros_asistenciales+")" +
            ",(4,'Bancos',"+R.drawable.bancos+"),(5,'Hoteleria y Turismo',"+R.drawable.hoteleria_y_turismo+")," +
            "(6,'Instituciones Públicas',"+R.drawable.gobierno+"),(7,'Comercio de Bienes',"+R.drawable.comercio_de_bienes+")," +
            "(8,'Comercio de Servicios',"+R.drawable.comercio_de_servicios+"),(9,'Bienes y Raises',"+R.drawable.biene_y_raises+")," +
            "(10,'Asesoria Legal',"+R.drawable.asesoria_legal+"),(11,'Funerarias',"+R.drawable.funeraria+")";

    final String INSERTAR_REGIONES = "INSERT INTO Regiones values(0703,'Danlí'),(0704,'El Paraíso')";

    final String INSERTAR_ESTADO_CONTACTOS = "INSERT INTO Estado_Contactos values(1,'Solicitado'), (2,'Aprobado'),(3,'Rechazado'), (4,'Eliminado')";

    final String INSERTAR_CONTACTOS = "INSERT INTO Contactos(id_contacto, nombre_organizacion, numero_fijo, numero_movil, direccion, e_mail, descripcion_organizacion, latitud, " +
            " longitud, id_usuario, id_categoria, id_estado, id_region) values(1,'Hotel La Esperanza','2763-0101','9965-2400', 'Barrio El centro', 'unah@edu.hn', 'Servicio de hotel en la ciudad de Danlí con precios cómodos y servicio de calidad', 100, 100," +
            "1, 1, 2, 0703), (2,'Restaurante Paraiso','2763-0101','9965-2400', 'Barrio El centro', 'unah@edu.hn', 'Comida de calidad', 100, 100," +
            "2, 2, 2, 0704)";

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREAR_TABLA_ROLES);
        db.execSQL(CREAR_TABLA_USUARIOS);
        db.execSQL(CREAR_TABLA_CATEGORIAS);
        db.execSQL(CREAR_TABLA_REGIONES);
        db.execSQL(CREAR_TABLA_ESTADO_CONTACTOS);
        db.execSQL(CREAR_TABLA_CONTACTOS);
        db.execSQL(INSERTAR_ROL);
        db.execSQL(INSERTAR_USUARIOS);
        db.execSQL(INSERTAR_CATEGORIAS);
        db.execSQL(INSERTAR_REGIONES);
        db.execSQL(INSERTAR_ESTADO_CONTACTOS);
        db.execSQL(INSERTAR_CONTACTOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Roles");
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL("DROP TABLE IF EXISTS Categorias");
        db.execSQL("DROP TABLE IF EXISTS Regiones");
        db.execSQL("DROP TABLE IF EXISTS Estado_Contactos");
        db.execSQL("DROP TABLE IF EXISTS Contactos");
        onCreate(db);

    }

}

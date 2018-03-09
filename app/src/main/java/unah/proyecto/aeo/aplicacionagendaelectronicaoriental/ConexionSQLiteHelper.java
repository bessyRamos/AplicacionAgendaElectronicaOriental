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
    final String CREAR_TABLA_USUARIOS = "create table Usuarios(id_usuario INTEGER  PRIMARY KEY, nombre_usuario TEXT, nombre_propio TEXT, contrasena TEXT, rol INTEGER, estado_usuario INTEGER, FOREIGN KEY(rol) REFERENCES Roles(id_rol))";
    final String CREAR_TABLA_CATEGORIAS = "create table Categorias(id_categoria INTEGER  PRIMARY KEY  , nombre_categoria TEXT NOT NULL, imagen_categoria INT)";
    final String CREAR_TABLA_REGIONES = "create table Regiones(id_region INTEGER PRIMARY KEY, nombre_region TEXT NOT NULL)";
    final String CREAR_TABLA_ESTADO_CONTACTOS = "create table Estado_Contactos(id_estado INTEGER PRIMARY KEY, descripcion_estado_contactos TEXT NOT NULL)";

    final String CREAR_TABLA_CONTACTOS = "create table Contactos(id_contacto INTEGER PRIMARY KEY  , nombre_organizacion TEXT NOT NULL, numero_fijo TEXT," +
            " numero_movil TEXT, direccion TEXT NOT NULL, imagen INTEGER DEFAULT "+imagenPredeterminada+", e_mail TEXT DEFAULT 'No Disponible', descripcion_organizacion TEXT NOT NULL, latitud DOUBLE, " +
            "longitud DOUBLE, id_usuario INTEGER, id_categoria INTEGER, id_estado INTEGER, id_region INTEGER, FOREIGN KEY(id_usuario) REFERENCES Usuarios(id_usuario)," +
            "  FOREIGN KEY(id_categoria) REFERENCES Categoria(id_categoria) , FOREIGN KEY(id_estado) REFERENCES Estado_Contactos(id_estado),  FOREIGN KEY(id_region) REFERENCES Regiones(id_region))";

    final String INSERTAR_ROL="INSERT INTO Roles values(1,'Administrador'), " +
            "(2,'Cliente')";
//99-45-02-13
    final String INSERTAR_USUARIOS="INSERT INTO Usuarios values(1,'Admin','Administrador','admin123',1,1), (2,'cliente1','nombreCliente','cliente123',2,1), (3,'cliente2','nombreCliente2','cliente123',2,1)";

    final String INSERTAR_CATEGORIAS = "INSERT INTO Categorias" +
            "(id_categoria,nombre_categoria,imagen_categoria) values" +
            "(1,'Emergencia',"+R.drawable.emergencia+")" +
            ",(2,'Educación',"+R.drawable.educacion +")," +
            "(3,'Centros Asistenciales',"+R.drawable.centrosasistenciales +")" +
            ",(4,'Bancos',"+R.drawable.bancos+")," +
            "(5,'Hoteleria y Turismo',"+R.drawable.hoteleria_y_turismo+")," +
            "(6,'Instituciones Públicas',"+R.drawable.gobierno+")," +
            "(7,'Comercio de Bienes',"+R.drawable.comercio_de_bienes+")," +
            "(8,'Comercio de Servicios',"+R.drawable.comercio_de_servicios+")," +
            "(9,'Bienes y Raises',"+R.drawable.biene_y_raises+")," +
            "(10,'Asesoria Legal',"+R.drawable.asesoria_legal+")," +
            "(11,'Funerarias',"+R.drawable.funeraria+")";

    final String INSERTAR_REGIONES = "INSERT INTO Regiones values(0703,'Danlí'),(0704,'El Paraíso')";

    final String INSERTAR_ESTADO_CONTACTOS = "INSERT INTO Estado_Contactos values(1,'Solicitado'), (2,'Aprobado'),(3,'Rechazado'), (4,'Eliminado')";

    final String INSERTAR_CONTACTOS = "INSERT INTO Contactos" +
            "(id_contacto, nombre_organizacion, numero_fijo, numero_movil, direccion, e_mail, descripcion_organizacion, latitud, " +
            " longitud, id_usuario, id_categoria, id_estado, id_region) values" +
            "(1,'Policía','2763-2063','', 'Barrio Las Colinas Segunda Etapa dos cuadras arriba de la escuela Gabriela Albarado  a mano derecha', 'redes.sociales@seguridad.gob.hn', 'Nos pueden contactar las 24 horas y estamos para servir y proteger ',100,100," +
            " 1, 1,2, 0703)," + "(2,'Policía','2793-0286','', 'Barrio El Calvario cuadra y media de agro comercial Gaitan contiguo el cementerio', 'redes.sociales@seguridad.gob.hn', 'Nos pueden contactar las 24 horas y estamos para servir y proteger',100,100," +
            " 1, 1,2, 0704)," + "(3,'Instituto Teodoro Rodas Valle','2763-3050','3219-6497', 'Barrio las Flores', 'loborams@yahoo.com', 'Instituto privado',100,100," +
            " 1, 2,2, 0703),"+ "(4,'Centro Tecnico Vocacional Pedro Nufio','2763-3195','', 'Barrio El Quiquisque', '', 'Instituto Público',100,100," + " 1, 2,2, 0703)," +
            "(5,'Instituto Teodoro Rodas Valle','2763-3050','3219-6497', 'Barrio las Flores', 'loborams@yahoo.com', 'Instituto privado',100,100," +
            " 1, 2,2, 0703),"+
            "(6,'Instituto Técnico Alegandro Flores','2793-4123','', 'Col.Los Montesitos, Salida Danlí', '', 'Instituto Público',100,100," +
            " 1, 2,2, 0704),"+
            "(7,'Farmacia Cimar','2763-2115','', 'Barrio San Sebastian', '', 'Cuenta con servicio a domicilio',100,100," +
            " 1, 3,2, 0704),"+
            "(8,'Farmacia del Ahorro','2763-2787','', 'Barrio El Centro Edificio Astrovisión, Salida Danlí', '', 'Cuenta con servicio a domicilio',100,100," +
            " 1, 3,2, 0703),"+
            "(9,'Hospital Gabriela Alvarado','2763-5824','', 'San Marcos abajo, calle panamericana', '', 'Trabajamos 24 hrs',100,100," +
            " 1, 3,2, 0703),"+
            "(10,'BANHCAFE','2763-2402','', 'Barrio Tierra Blanca', '', 'Servicios financieros',100,100," +
            " 1, 4, 2, 0703),"+
            "(11,'Banco Atlantida','2793-4200','', 'El paraiso frente a plaza Ramón Ignacio Díaz, Bo.San Isidro', '', 'Servicios financieros',100,100," +
            " 1, 4, 2, 0704),"+
            "(12,'Hotel La Esperanza','2763-2106','', 'Bo.Tierra Santa', '', 'Servicios de hoteleria',100,100," +
            " 1, 5, 2, 0703),"+
            "(13,'Hotel y Restaurante Mario Chávez','2793-4345','', 'Calle principal quinta avenida, El Paraíso', '', 'Servicios de hoteleria y comida',100,100," +
            " 1, 5, 2, 0704),"+
            "(14,'Finca Santa Emilia','','9842-7586', 'Siete km del parque central de Danlí, una reserva natural', '', 'turismo',100,100," +
            " 1, 5, 2, 0703),"+
            "(15,'Instituto de la Propiedad','2763-0473','', 'Bo.Oriental', '', 'Registro de bienes',100,100," +
            " 1, 6, 2, 0703),"+
            "(16,'Secretaria Agricultura Ganaderia','2763-2112','98543113', 'Salida El Paraiso frente al intituto Cosme Garcia', 'valdivita_10@yahoo.com', ' asesoria ',100,100," +
            " 1, 6, 2, 0703),"+
            "(17,'Plaza Unicentro','2763-3552','', 'Bo.el centro calle del comerciofrente a hondutel', 'jeany_salinas@hotmail.com', 'adquiere un bien',100,100," +
            " 1, 7, 2, 0703),"+
            "(18,'Carrión','2763-2331','', 'frente a Casa de la Cultura', '', 'servio de bienes',100,100," +
            " 1, 7, 2, 0703),"+
            "(19,'Oficina Postal Danlí Paraíso','2763-2036','', 'Bo. El Centro frente al Parque Centenario', '', ' envio',100,100," +
            " 1, 8, 2, 0703),"+
            "(20,'Cámara de Comercio E Industrias de Danlí (CCID)','2763-2061','', 'Colonia Gualiquemes por terminal de buses contiguo ala Plaza San Miguel en Boulevard La Democracia.', 'cidedanli@yahoo.com cidedanli@hotmail.com', ' Comercio',100,100," +
            " 1, 8, 2, 0703),"+
            "(21,'Inmobiliaria y Constructora El Protector','27634746','', 'Media cuadra al norte del parque central entre banco Los Trabajadores y La Despensa (72,02 km)', '','Agente de bienes raíces',100,100," +
            " 1, 9, 2, 0703),"+
            "(22,'Despacho Legal Lic. Hector Paguaga','','9767-3875', 'Barrio Tierra Blanca', '', ' Abogado',100,100," +
            " 1, 10, 2, 0703),"+
            "(23,'Colegio de Abogados Capitulo Danli','','', 'Frente al Triangulo salida al paraiso a la par de Instituto Técnico Pedro Nufio', '', '',100,100," +
            " 1, 10, 2, 0703),"+
            "(24,'FUNERALES DIVINA MISERICORDIA','2763-6798','', 'V-216  Danlí', '', 'disponibilidad de local',100,100," +
            " 1, 11, 2, 0703),"+
            "(25,'Funerales Puerta Al Cielo','2763-2588','', 'Danlí', '', 'disponibilidad de local',100,100," +
            " 1, 11, 2, 0703)";



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

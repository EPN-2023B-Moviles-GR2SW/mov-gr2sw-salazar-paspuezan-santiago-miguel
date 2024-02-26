import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DeberDatabase.db"
        private const val TABLE_PROYECTO = "Proyecto"
        private const val TABLE_TAREA = "Tarea"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_DESCRIPCION = "descripcion"
        private const val COLUMN_FECHA_CREACION = "fechaCreacion"
        private const val COLUMN_PROYECTO_ID = "proyectoId"

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableProyecto = "CREATE TABLE $TABLE_PROYECTO (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NOMBRE TEXT," +
                "$COLUMN_DESCRIPCION TEXT)"

        val createTableTarea = "CREATE TABLE $TABLE_TAREA (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NOMBRE TEXT," +
                "$COLUMN_FECHA_CREACION TEXT," +
                "$COLUMN_PROYECTO_ID INTEGER," +
                "FOREIGN KEY($COLUMN_PROYECTO_ID) REFERENCES $TABLE_PROYECTO($COLUMN_ID))"

        db.execSQL(createTableProyecto)
        db.execSQL(createTableTarea)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí manejas la actualización de la base de datos
    }

    fun addProyecto(nombreProyecto: String, descripcion: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE, nombreProyecto)
        values.put(COLUMN_DESCRIPCION, descripcion)

        db.insert(TABLE_PROYECTO, null, values)
        db.close()
    }

    fun updateProyecto(id: Int, nombreProyecto: String, descripcion: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOMBRE, nombreProyecto)
        values.put(COLUMN_DESCRIPCION, descripcion)

        db.update(TABLE_PROYECTO, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }
    fun getTareasPorProyectoId(proyectoId: Int): List<BTarea> {
        val listaTareas = mutableListOf<BTarea>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TAREA, null, "$COLUMN_PROYECTO_ID=?", arrayOf(proyectoId.toString()),
            null, null, null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COLUMN_ID)
            val nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE)
            val fechaCreacionIndex = cursor.getColumnIndex(COLUMN_FECHA_CREACION)

            // Verifica que todos los índices de las columnas sean válidos
            if (idIndex != -1 && nombreIndex != -1 && fechaCreacionIndex != -1) {
                do {
                    val id = cursor.getInt(idIndex)
                    val nombre = cursor.getString(nombreIndex)
                    val fechaCreacionStr = cursor.getString(fechaCreacionIndex)
                    val fechaCreacion = sdf.parse(fechaCreacionStr)
                    listaTareas.add(BTarea(id, nombre, fechaCreacion?: Date(), proyectoId))
                } while (cursor.moveToNext())
            } else {
                // Manejar el caso donde uno o más índices no son válidos
                // Por ejemplo, puedes lanzar una excepción o registrar un error
            }
        }
        cursor.close()
        db.close()
        return listaTareas
    }


    fun eliminarTareaPorId(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TAREA, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }
    @SuppressLint("Range")
    fun getAllProyectos(): List<BProyecto> {
        val listaProyectos = mutableListOf<BProyecto>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PROYECTO,
            null,
            null,
            null,
            null,
            null ,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
                val descripcion = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPCION))

                listaProyectos.add(BProyecto(id, nombre, descripcion))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return listaProyectos
    }
    fun eliminarProyectoPorId(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TAREA, "$COLUMN_PROYECTO_ID=?", arrayOf(id.toString()))
        db.delete(TABLE_PROYECTO, "$COLUMN_ID=?", arrayOf(id.toString()))

        db.close()
    }


}

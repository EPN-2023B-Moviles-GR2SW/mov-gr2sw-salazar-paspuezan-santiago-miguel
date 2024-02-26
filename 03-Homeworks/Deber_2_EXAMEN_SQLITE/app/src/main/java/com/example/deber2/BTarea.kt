import java.util.Date

class BTarea (
    var id: Int,
    var nombreTarea: String,
    var fechaCreacion: Date = Date(),
    var proyectoId: Int  // Nuevo campo para asociar tarea con proyecto
) {
    override fun toString(): String {
        // Formato de fecha actualizado para coincidir con SQLite
        val fechaFormateada = DatabaseHelper.sdf.format(fechaCreacion)
        return "[$id]\t Tarea: $nombreTarea \n Fecha Creación: $fechaFormateada"
    }
}
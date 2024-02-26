class BProyecto(
    val id: Int,
    var nombreProyecto: String,
    var descripcion: String
) {
    override fun toString(): String {
        return "Proyecto: $nombreProyecto \n\tDescripción: $descripcion"
    }
}
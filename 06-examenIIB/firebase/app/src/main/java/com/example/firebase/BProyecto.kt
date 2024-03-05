import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference

class BProyecto(
    var id: String = "",
    var nombreProyecto: String = "",
    var descripcion: String = ""
) {
    fun getTareasCollectionReference(): CollectionReference? {
        if (id.isEmpty()) {
            // Considera manejar este caso adecuadamente, por ejemplo, lanzando una excepción o devolviendo null.
            println("El ID del proyecto está vacío.")
            return null
        }
        val db = FirebaseFirestore.getInstance()
        return db.collection("proyectos").document(id).collection("ListaTareas")
    }
    override fun toString(): String {
        return nombreProyecto
    }
}

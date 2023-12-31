package models

data class Project(
    val nombre: String,
    val descripcion: String,
    val tareas: MutableList<Task> = mutableListOf()
) {
    fun addTask(tarea: Task) {
        tareas.add(tarea)
    }

    fun removeTask(tarea: Task) {
        tareas.remove(tarea)
    }

    fun editTask(taskId: Int, newTask: Task): Boolean {
        val existingTask = tareas.find { it.id == taskId }
        return if (existingTask != null) {
            existingTask.apply {
                nombreTarea = newTask.nombreTarea
                descripcionTarea = newTask.descripcionTarea
                completada = newTask.completada
            }
            true
        } else {
            false
        }
    }

    fun getAllTasks(): List<Task> {
        return tareas.toList()
    }

    fun getCompletedTasks(): List<Task> {
        return tareas.filter { it.completada }
    }

    fun getPendingTasks(): List<Task> {
        return tareas.filter { !it.completada }
    }

    override fun toString(): String {
        return "Proyecto: $nombre \n\tDescripción: $descripcion \n\tTotal de tareas: ${tareas.size}"
    }
}

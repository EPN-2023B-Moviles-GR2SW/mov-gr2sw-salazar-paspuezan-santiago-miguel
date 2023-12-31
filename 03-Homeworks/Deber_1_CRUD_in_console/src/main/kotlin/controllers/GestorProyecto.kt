package controllers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import models.Project
import models.Task
import java.io.File
import java.lang.reflect.Type

class GestorProyecto {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val url="C:\\Develop\\Mobile_apps\\mov-gr2sw-salazar-paspuezan-santiago-miguel\\03-Homeworks\\Deber_1_CRUD_in_console\\src\\main\\resources\\project-database.json"
            val projectManager = GestorProyecto()
            val projectList = projectManager.cargarJSON(url)

            var continuar = true
            while (continuar) {
                println("TODO - tu app de tareas rápida ")
                println("1. Ver todos los proyectos y tareas")
                println("2. Ingresar una nueva tarea")
                println("3. Ingresar un nuevo proyecto")
                println("4. Editar una tarea")
                println("5. Completar tareas")
                println("6. eliminar tareas")
                println("7. Salir")
                print("Ingrese su opción: ")

                when (readLine()) {
                    "1" -> {
                        try {
                            projectList.forEach { proyecto ->
                                println("Proyecto: ${proyecto.nombre}")
                                proyecto.tareas.forEach { tarea ->
                                    println("  Tarea: ${tarea.nombreTarea}")
                                    println("  Tarea: ${if (tarea.completada) "Completada" else "Pendiente"}")
                                }
                            }
                        } catch (e: Exception) {
                            println("Error al cargar los proyectos: ${e.message}")
                            e.printStackTrace()
                        }
                    }

                    "2" -> {
                        println("Seleccione el proyecto")
                        val selectedProject = projectManager.seleccionarProyecto(projectList)

                        println("ID de la tarea: ")
                        val taskId = readLine()?.toIntOrNull() ?: 0
                        println("Nombre de la tarea: ")
                        val taskName = readLine() ?: ""
                        println("Descripción de la tarea: ")
                        val taskDescription = readLine() ?: ""
                        val newTask = Task(
                            id = taskId,
                            nombreTarea = taskName,
                            descripcionTarea = taskDescription,
                            completada = false
                        )
                        selectedProject?.let {
                            it.addTask(newTask)
                            println("Tarea ingresada:\n${newTask.toString()} en el proyecto '${it.nombre}'")
                        } ?: run {
                            println("No se seleccionó ningún proyecto.")
                        }
                    }

                    "3" -> {
                        println("Nombre del proyecto: ")
                        val projName = readLine() ?: ""
                        println("Descripción del proyecto: ")
                        val projDescription = readLine() ?: ""
                        val listaVacia: MutableList<Task> = mutableListOf()
                        val newProject = Project(projName, projDescription, listaVacia)
                        projectList.add(newProject);
                        try {
                            projectList.forEach { proyecto ->
                                println("Proyecto: ${proyecto.nombre}")
                                proyecto.tareas.forEach { tarea ->
                                    println("  Tarea: ${tarea.nombreTarea}")
                                    println("  Tarea: ${if (tarea.completada) "Completada" else "Pendiente"}")
                                }
                            }
                        } catch (e: Exception) {
                            println("Error al cargar los proyectos: ${e.message}")
                            e.printStackTrace()
                        }

                    }
                    "4"-> {
                        println("Seleccione el proyecto")
                        val selectedProject = projectManager.seleccionarProyecto(projectList)
                        println("seleccione la tarea")
                        val selectedTask = projectManager.seleccionarTarea(selectedProject?.tareas!!)
                        println("Nombre de la tarea: ")
                        val taskName = readLine() ?: ""
                        println("Descripción de la tarea: ")
                        val taskDescription = readLine() ?: ""
                        val newTask = Task(
                            id = selectedTask?.id!!,
                            nombreTarea = taskName,
                            descripcionTarea = taskDescription,
                            completada = selectedTask.completada
                        )
                        selectedProject?.let {
                            it.editTask(selectedTask.id,newTask)
                            println("Tarea ingresada:\n${newTask.toString()} en el proyecto '${it.nombre}'")
                        } ?: run {
                            println("No se seleccionó ningún proyecto.")
                        }

                    }
                    "5"->{
                        println("Seleccione el proyecto")
                        val selectedProject = projectManager.seleccionarProyecto(projectList)
                        println("seleccione la tarea")
                        val selectedTask = projectManager.seleccionarTarea(selectedProject?.tareas!!)
                        selectedProject?.let {
                            it.editTask(selectedTask?.id!!,selectedTask.apply { completada=true })
                            println("Tarea Actualizada:\n${selectedTask.toString()} en el proyecto '${it.nombre}'")
                        } ?: run {
                            println("No se seleccionó ningún proyecto.")
                        }
                    }
                    "6"->{
                        println("Seleccione el proyecto")
                        val selectedProject = projectManager.seleccionarProyecto(projectList)
                        println("seleccione la tarea")
                        val selectedTask = projectManager.seleccionarTarea(selectedProject?.tareas!!)
                        selectedProject?.let {
                            it.removeTask(selectedTask!!)
                            println("Tarea Eliminada:\n${selectedTask.toString()} en el proyecto '${it.nombre}'")
                        } ?: run {
                            println("No se seleccionó ningún proyecto.")
                        }
                    }

                    "7" -> {
                        println("Saliendo del programa...")
                        continuar = false
                    }

                    else -> println("Opción inválida")
                }
                println("Presiona una tecla para continuar...")
                readLine()
            }
            projectManager.guardarJSON(projectList,url);
        }

    }

    fun cargarJSON(filePath: String): MutableList<Project> {
        val gson = Gson()
        val jsonFile = File(filePath)
        if (!jsonFile.exists()) {
            throw IllegalArgumentException("El archivo no existe en la ruta proporcionada")
        }

        val tipoListaProyectos: Type = object : TypeToken<List<Project>>() {}.type
        return gson.fromJson(jsonFile.readText(), tipoListaProyectos)
    }
    fun guardarJSON(projectList: List<Project>, jsonFilePath: String) {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val jsonProyectos: String = gson.toJson(projectList)

        try {
            val file = File(jsonFilePath)
            file.writeText(jsonProyectos)
            println("Estado actual guardado en $jsonFilePath")
        } catch (e: Exception) {
            println("Error al guardar el estado actual en $jsonFilePath: ${e.message}")
        }
    }
    fun seleccionarProyecto(listaProyecto: List<Project>): Project ? {
        var selectedIndex = 0

        while (true) {
            print("\u001b[H\u001b[2J")

            for (i in listaProyecto.indices) {
                if (i == selectedIndex) {
                    println("--> ${listaProyecto[i].nombre}")
                } else {
                    println("   ${listaProyecto[i].nombre}")
                }
            }

            println("Ingrese el número del proyecto (0-${listaProyecto.size - 1}): ")
            val input = readLine()

            input?.toIntOrNull()?.let { index ->
                if (index in 0 until listaProyecto.size) {
                    return listaProyecto[index]
                }
            }
        }
        return null
    }
    fun seleccionarTarea(tarea: List<Task>): Task ? {
        var selectedIndex = 0

        while (true) {
            print("\u001b[H\u001b[2J")

            for (i in tarea.indices) {
                if (i == selectedIndex) {
                    println("--> ${tarea[i].nombreTarea}")
                } else {
                    println("   ${tarea[i].nombreTarea}")
                }
            }

            println("Ingrese el número del proyecto (0-${tarea.size - 1}): ")
            val input = readLine()

            input?.toIntOrNull()?.let { index ->
                if (index in 0 until tarea.size) {
                    return tarea[index]
                }
            }
        }
        return null
    }
}

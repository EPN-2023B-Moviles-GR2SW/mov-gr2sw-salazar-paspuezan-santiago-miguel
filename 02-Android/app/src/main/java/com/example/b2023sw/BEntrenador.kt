package com.example.b2023sw

class BEntrenador(
    var id: Int,
    var nombre: String?,
    var description: String?
) {
    override fun toString(): String {
        return "${nombre} - ${description}"
    }
}
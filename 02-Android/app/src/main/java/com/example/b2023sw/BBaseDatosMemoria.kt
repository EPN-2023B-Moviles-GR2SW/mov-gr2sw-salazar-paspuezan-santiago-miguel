package com.example.b2023sw

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init{
            arregloBEntrenador
            .add(
                BEntrenador(1,"Adrian","a@a.com")
            )
            arregloBEntrenador
                .add(
                    BEntrenador(2,"Vicente","v@a.com")
                )
        }
    }
}
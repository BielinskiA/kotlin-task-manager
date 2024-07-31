package com.example.springboot.test

class Klasa1 (var name:String): KoloryInterface{
    override fun getColor(): Kolory {
    return Kolory.BIALY
    }
}

class Klasa2 {
    var name: String
    constructor(name:String){
        this.name = name
    }
}

data class DataClass1 (var kolor:Kolory){

}

enum class Kolory {
    BIALY, CZERWONY, ZOLTY
}

interface KoloryInterface {
    fun getColor (): Kolory
}

object PamiecKolorow {
    var kolor = Kolory.BIALY
}
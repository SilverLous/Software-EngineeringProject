package de.hbrs.team7.se1_starter_repo

class Car(var params: Array<String>) : CarIF {

    override val nr: Int
        get() = 0

    override val begin: Long
        get() = 0

    override val end: Long
        get() = 0

    override val duration: Int
        get() = 0


    override var price: Int = 0

    override var type: String = ""


    override fun toString(): String {
        return params.contentToString()
    }

    override fun paramsToArray(): Array<String>? {
        return params
    }

}

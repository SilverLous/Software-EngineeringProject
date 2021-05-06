package de.hbrs.team7.se1_starter_repo


class ParkhausService : ParkhausServiceIF {

    public override var init = 0

    init {
        init++
        print(init)
        println("hello service")
    }

    public override fun iterInit() = init++;



}
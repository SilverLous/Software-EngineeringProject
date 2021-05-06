package de.hbrs.team7.se1_starter_repo

public interface ParkhausServiceIF {

    var init: Int
        get() = 0
        set(value) {}

    public fun iterInit() = init++;
}
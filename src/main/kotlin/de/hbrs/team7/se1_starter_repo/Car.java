package de.hbrs.team7.se1_starter_repo;

import java.util.Arrays;

public class Car implements CarIF {
    String[] params;
    public Car( String[] params ){
        this.params = params;
    }

    @Override
    public int nr() {
        return 0;
    }

    @Override
    public long begin() {
        return 0;
    }

    @Override
    public long end() {
        return 0;
    }

    @Override
    public int duration() {
        return 0;
    }

    @Override
    public int price() {
        return 0;
    }

    @Override
    public String toString(){
        return Arrays.toString( params );
    }
}

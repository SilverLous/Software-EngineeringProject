package de.hbrs.team7.se1_starter_repo;


import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import jakarta.inject.Singleton;

@Singleton
public class ParkhausServiceGlobalJava implements Serializable {
    private int initNumber;

    public int getInitNumber() {
        return this.initNumber;
    }

    public void setInitNumber(int var1) {
        this.initNumber = var1;
    }

    public int iterInit() {
        int var1;
        this.setInitNumber((var1 = this.getInitNumber()) + 1);
        return var1;
    }


}

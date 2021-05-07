package de.hbrs.team7.se1_starter_repo;


import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;

@SessionScoped
public class ParkhausServiceSessionJava implements Serializable {
    private int initNumber;

    @Inject
    private ParkhausServiceGlobal ps;

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

package com.example.lab06_gui.domain;

public class Entity<ID> {
    private ID id;

    public ID getID(){
        return id;
    }

    public void setID(ID id){ this.id = id; }
}

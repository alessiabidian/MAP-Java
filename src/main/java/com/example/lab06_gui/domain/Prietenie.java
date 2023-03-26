package com.example.lab06_gui.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<String> {
    //Utilizator u1, u2;
    private final Long ID1, ID2;
    private LocalDateTime friendsFrom;
    private boolean acceptat;

    public Prietenie(Long IDprieten1, Long IDprieten2, LocalDateTime friendsFrom)
    {
        this.ID1 = IDprieten1;
        this.ID2 = IDprieten2;
        this.friendsFrom=friendsFrom;
        this.acceptat = false;

        String ids1, ids2, id;
        ids1 = String.valueOf(this.ID1);
        ids2 = String.valueOf(this.ID2);
        if(this.ID1<this.ID2)
            id = ids1 + "_" + ids2;
        else id = ids2 + "_" + ids1;

        setID(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Prietenie prietenie = (Prietenie) o;
        return Objects.equals(ID1, prietenie.ID1) && Objects.equals(ID2, prietenie.ID2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ID1, ID2);
    }

    @Override
    public String toString() {
        return "Prietenie {" + "ID1=" + ID1 + ", id2=" + ID2 + '}';
    }

    public Long getID1(){
        return this.ID1;
    }

    public Long getID2(){
        return this.ID2;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public boolean isAcceptat() {
        return acceptat;
    }

    public void setAcceptat(boolean acceptat) {
        this.acceptat = acceptat;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    /*public void addPrietenie(Utilizator u1, Utilizator u2){
        u1.getListaPrieteni().add(u2);
        u2.getListaPrieteni().add(u1);
    }*/


}
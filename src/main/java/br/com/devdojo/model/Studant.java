package br.com.devdojo.model;

import javax.persistence.Entity;

@Entity
public class Studant extends AbstractEntity{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

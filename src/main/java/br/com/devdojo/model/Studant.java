package br.com.devdojo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class Studant {
    private int id;
    private String name;
    public static List<Studant> studantList;

    static{
        studentRepository();
    }

    public Studant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Studant(String name) {
        this.name = name;
    }

    public Studant() {
    }

    private static void studentRepository() {
        studantList = new ArrayList<>(asList(new Studant(1, "Deku"), new Studant(2, "Todoroki")));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Studant studant = (Studant) o;
        return id == studant.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Studant> getStudantList() {
        return studantList;
    }

    public static void setStudantList(List<Studant> studantList) {
        Studant.studantList = studantList;
    }
}

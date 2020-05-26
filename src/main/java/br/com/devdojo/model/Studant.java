package br.com.devdojo.model;

import java.util.ArrayList;
import java.util.List;

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
        studantList = new ArrayList<>(asList(new Studant("Deku"), new Studant("Todoroki")));
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

package com.belajar.newportofolio;

public class Biodata {
    private int id;
    private String name, email, phone, ket;

    public Biodata (int id, String name, String email, String phone, String ket) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.ket = ket;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getKet() {
        return ket;
    }
}

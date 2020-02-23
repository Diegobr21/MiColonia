package com.micolonia.app;

public class Imagen{
    int id;
    String url;
    int idType;

    Imagen(String url, int idType) {
        this.id =id;
        this.url = url;
        this.idType = idType;

    }

    public Imagen(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}



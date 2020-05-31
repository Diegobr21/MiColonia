package com.micolonia.app;

class Comentario {
    String comentario;
    String nombre_usu;
    String post_id;
    String user_id;
    double timestamp;

    public Comentario(){
    }

    Comentario(String comentario, String nombre_usu, String post_id, String user_id, double timestamp) {
        this.comentario = comentario;
        this.nombre_usu = nombre_usu;
        this.post_id = post_id;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNombre_usu() {
        return nombre_usu;
    }

    public void setNombre_usu(String nombre_usu) {
        this.nombre_usu = nombre_usu;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
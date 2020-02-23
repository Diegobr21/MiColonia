package com.micolonia.app;

public class Usuario {

    private  int id;
    private  String nombre;
    private   String email;
    private  String contras;
    private  int id_colonia;
    private int tipo;
    private double num_telefono;


    public Usuario(int id, String nombre, String email,  int id_colonia, int tipo, String contras, double num_telefono) {
        this.id = id;
        this.email = email;
        this.contras = contras;
        this.nombre = nombre;
        this.id_colonia = id_colonia;
        this.tipo = tipo;
        this.num_telefono= num_telefono;

    }

/* Usuario(ResultSet resultSet) throws SQLException {
		this.id = resultSet.getInt("id_usu");
		this.cor_usu = resultSet.getString("cor_usu");
		this.pas_usu = resultSet.getString("pas_usu");

		this.nom_usu = resultSet.getString("nom_usu");


	}*/

    public String toString() {
        return "Usuario id=" + id + ", cor_usu=" + email + ", contra_usu=" + contras  + ", nom_usu=" + nombre
                + ", id_colonia=" + id_colonia  + ", tipo usuario=" + tipo +",celular=" + num_telefono ;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getContras() {
        return contras;
    }
    public void setContras(String contras) {
        this.contras = contras;
    }

    public int getId_colonia() { return id_colonia; }
    public void setId_colonia(int id_colonia) { this.id_colonia = id_colonia; }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getNum_telefono() { return num_telefono; }
    public void setNum_telefono(double num_telefono) { this.num_telefono = num_telefono; }
}


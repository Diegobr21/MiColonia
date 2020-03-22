package com.micolonia.app;

import android.os.Parcel;
import android.os.Parcelable;

class Usuario implements Parcelable {

    String id;
    String nombre;
    String email;
    String nombre_calle;
    String num_calle;
    String status;
    String contras;
    String colonia;
    int tipo;


    public Usuario() {
    }


    Usuario(String id, String nombre, String status, String email, String colonia, int tipo, String contras, String nombre_calle, String num_calle) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.nombre_calle = nombre_calle;
        this.num_calle = num_calle;
        this.status = status;
        this.contras = contras;
        this.colonia = colonia;
        this.tipo = tipo;



    }

    protected Usuario(Parcel in) {
        nombre = in.readString();
        email = in.readString();
        nombre_calle = in.readString();
        num_calle = in.readString();
        status = in.readString();
        colonia = in.readString();
        tipo = in.readInt();
        id = in.readString();


    }
    //Order of writing
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(email);
        dest.writeString(nombre_calle);
        dest.writeString(num_calle);
        dest.writeString(status);
        dest.writeString(colonia);
        dest.writeInt(tipo);
        dest.writeString(id);

    }
    @Override
    public int describeContents() { return 0; }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };


/*
    public String toString() {
        return "Usuario id=" + id + ", cor_usu=" + email + ", contra_usu=" + contras  + ", nom_usu=" + nombre
                + ", id_colonia=" + id_colonia  +  ", nombre calle=" + nombre_Calle + " # "+ numero_Calle + ", tipo usuario=" + tipo ;
    }
*/
    public String getNombre() {
    return nombre;
}
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre_calle() {
        return nombre_calle;
    }

    public void setNombre_calle(String nombre_calle) {
        this.nombre_calle = nombre_calle;
    }

    public String getNum_calle() {
        return num_calle;
    }

    public void setNum_calle(String num_calle) {
        this.num_calle = num_calle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContras() {
        return contras;
    }
    public void setContras(String contras) {
        this.contras = contras;
    }

     public String getColonia() {
         return colonia;
     }

     public void setColonia(String colonia) {
         this.colonia = colonia;
     }

     public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }






}


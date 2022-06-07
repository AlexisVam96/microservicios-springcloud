package com.alexis.pruebajjwt.entity;

import javax.persistence.*;

@Entity
@Table(name = "tb_tipo_contribuyente")
public class Contribuyente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_tipo_contribuyente;

    private String nombre;
    private Boolean estado;

    public Integer getId_tipo_contribuyente() {
        return id_tipo_contribuyente;
    }

    public void setId_tipo_contribuyente(Integer id_tipo_contribuyente) {
        this.id_tipo_contribuyente = id_tipo_contribuyente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}

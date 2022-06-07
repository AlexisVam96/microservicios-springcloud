package com.alexis.pruebajjwt.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_entidad")
public class Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_entidad;

    @ManyToOne
    @JoinColumn(name = "id_tipo_documento")
    @NotNull(message = "no puede estar vacío")
    private Documento documento;

    @NotEmpty
    @Column(unique = true)
    private String nro_documento;

    @NotEmpty
    private String razon_social;

    private String nombre_comercial;

    @ManyToOne
    @JoinColumn(name = "id_tipo_contribuyente")
    private Contribuyente contribuyente;

    private String direccion;

    private String telefono;

    @NotNull
    private Boolean estado = true;

    public Integer getId_entidad() {
        return id_entidad;
    }

    public void setId_entidad(Integer id_entidad) {
        this.id_entidad = id_entidad;
    }

    public String getNro_documento() {
        return nro_documento;
    }

    public void setNro_documento(String nro_documento) {
        this.nro_documento = nro_documento;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }

    public void setNombre_comercial(String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Contribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }
}

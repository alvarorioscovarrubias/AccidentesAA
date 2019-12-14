/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ivans
 */
@Entity
@Table(name = "ESTADO_SERVICIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoServicio.findAll", query = "SELECT e FROM EstadoServicio e")
    , @NamedQuery(name = "EstadoServicio.findById", query = "SELECT e FROM EstadoServicio e WHERE e.id = :id")
    , @NamedQuery(name = "EstadoServicio.findByDescripcion", query = "SELECT e FROM EstadoServicio e WHERE e.descripcion = :descripcion")})
public class EstadoServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "estadoServicioIdEstServ")
    private Servicio servicio;

    public EstadoServicio() {
    }

    public EstadoServicio(BigDecimal id) {
        this.id = id;
    }

    public EstadoServicio(BigDecimal id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoServicio)) {
            return false;
        }
        EstadoServicio other = (EstadoServicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.EstadoServicio[ id=" + id + " ]";
    }
    
}

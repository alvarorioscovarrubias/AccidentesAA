/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ivans
 */
@Entity
@Table(name = "ESTADO_CONTRATO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoContrato.findAll", query = "SELECT e FROM EstadoContrato e")
    , @NamedQuery(name = "EstadoContrato.findByIdEstCon", query = "SELECT e FROM EstadoContrato e WHERE e.idEstCon = :idEstCon")
    , @NamedQuery(name = "EstadoContrato.findByDescripcion", query = "SELECT e FROM EstadoContrato e WHERE e.descripcion = :descripcion")})
public class EstadoContrato implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_EST_CON")
    private BigDecimal idEstCon;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoContratoIdEstCon")
    private Collection<Contrato> contratoCollection;

    public EstadoContrato() {
    }

    public EstadoContrato(BigDecimal idEstCon) {
        this.idEstCon = idEstCon;
    }

    public EstadoContrato(BigDecimal idEstCon, String descripcion) {
        this.idEstCon = idEstCon;
        this.descripcion = descripcion;
    }

    public BigDecimal getIdEstCon() {
        return idEstCon;
    }

    public void setIdEstCon(BigDecimal idEstCon) {
        this.idEstCon = idEstCon;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Contrato> getContratoCollection() {
        return contratoCollection;
    }

    public void setContratoCollection(Collection<Contrato> contratoCollection) {
        this.contratoCollection = contratoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstCon != null ? idEstCon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoContrato)) {
            return false;
        }
        EstadoContrato other = (EstadoContrato) object;
        if ((this.idEstCon == null && other.idEstCon != null) || (this.idEstCon != null && !this.idEstCon.equals(other.idEstCon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.EstadoContrato[ idEstCon=" + idEstCon + " ]";
    }
    
}

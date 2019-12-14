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
@Table(name = "ESTADO_PAGO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoPago.findAll", query = "SELECT e FROM EstadoPago e")
    , @NamedQuery(name = "EstadoPago.findByIdEstadoPago", query = "SELECT e FROM EstadoPago e WHERE e.idEstadoPago = :idEstadoPago")
    , @NamedQuery(name = "EstadoPago.findByDescripcion", query = "SELECT e FROM EstadoPago e WHERE e.descripcion = :descripcion")})
public class EstadoPago implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ESTADO_PAGO")
    private BigDecimal idEstadoPago;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoPagoIdEstadoPago")
    private Collection<Pago> pagoCollection;

    public EstadoPago() {
    }

    public EstadoPago(BigDecimal idEstadoPago) {
        this.idEstadoPago = idEstadoPago;
    }

    public EstadoPago(BigDecimal idEstadoPago, String descripcion) {
        this.idEstadoPago = idEstadoPago;
        this.descripcion = descripcion;
    }

    public BigDecimal getIdEstadoPago() {
        return idEstadoPago;
    }

    public void setIdEstadoPago(BigDecimal idEstadoPago) {
        this.idEstadoPago = idEstadoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public Collection<Pago> getPagoCollection() {
        return pagoCollection;
    }

    public void setPagoCollection(Collection<Pago> pagoCollection) {
        this.pagoCollection = pagoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstadoPago != null ? idEstadoPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoPago)) {
            return false;
        }
        EstadoPago other = (EstadoPago) object;
        if ((this.idEstadoPago == null && other.idEstadoPago != null) || (this.idEstadoPago != null && !this.idEstadoPago.equals(other.idEstadoPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.EstadoPago[ idEstadoPago=" + idEstadoPago + " ]";
    }
    
}

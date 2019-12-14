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
@Table(name = "TIPO_PAGO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPago.findAll", query = "SELECT t FROM TipoPago t")
    , @NamedQuery(name = "TipoPago.findByIdTipoPago", query = "SELECT t FROM TipoPago t WHERE t.idTipoPago = :idTipoPago")
    , @NamedQuery(name = "TipoPago.findByDescripcion", query = "SELECT t FROM TipoPago t WHERE t.descripcion = :descripcion")})
public class TipoPago implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_TIPO_PAGO")
    private BigDecimal idTipoPago;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoPagoIdTipoPago")
    private Collection<Pago> pagoCollection;

    public TipoPago() {
    }

    public TipoPago(BigDecimal idTipoPago) {
        this.idTipoPago = idTipoPago;
    }

    public TipoPago(BigDecimal idTipoPago, String descripcion) {
        this.idTipoPago = idTipoPago;
        this.descripcion = descripcion;
    }

    public BigDecimal getIdTipoPago() {
        return idTipoPago;
    }

    public void setIdTipoPago(BigDecimal idTipoPago) {
        this.idTipoPago = idTipoPago;
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
        hash += (idTipoPago != null ? idTipoPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPago)) {
            return false;
        }
        TipoPago other = (TipoPago) object;
        if ((this.idTipoPago == null && other.idTipoPago != null) || (this.idTipoPago != null && !this.idTipoPago.equals(other.idTipoPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.TipoPago[ idTipoPago=" + idTipoPago + " ]";
    }
    
}

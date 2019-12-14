/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ivans
 */
@Entity
@Table(name = "CARGO_EXTRA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CargoExtra.findAll", query = "SELECT c FROM CargoExtra c")
    , @NamedQuery(name = "CargoExtra.findByIdCargoExtra", query = "SELECT c FROM CargoExtra c WHERE c.idCargoExtra = :idCargoExtra")
    , @NamedQuery(name = "CargoExtra.findByMonto", query = "SELECT c FROM CargoExtra c WHERE c.monto = :monto")
    , @NamedQuery(name = "CargoExtra.findByDescripcion", query = "SELECT c FROM CargoExtra c WHERE c.descripcion = :descripcion")})
public class CargoExtra implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CARGO_EXTRA")
    private BigDecimal idCargoExtra;
    @Basic(optional = false)
    @Column(name = "MONTO")
    private BigInteger monto;
    @Basic(optional = false)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "CUOTA_ID_CUOTA", referencedColumnName = "ID_CUOTA")
    @ManyToOne(optional = false)
    private Cuota cuotaIdCuota;

    public CargoExtra() {
    }

    public CargoExtra(BigDecimal idCargoExtra) {
        this.idCargoExtra = idCargoExtra;
    }

    public CargoExtra(BigDecimal idCargoExtra, BigInteger monto, String descripcion) {
        this.idCargoExtra = idCargoExtra;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    public BigDecimal getIdCargoExtra() {
        return idCargoExtra;
    }

    public void setIdCargoExtra(BigDecimal idCargoExtra) {
        this.idCargoExtra = idCargoExtra;
    }

    public BigInteger getMonto() {
        return monto;
    }

    public void setMonto(BigInteger monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cuota getCuotaIdCuota() {
        return cuotaIdCuota;
    }

    public void setCuotaIdCuota(Cuota cuotaIdCuota) {
        this.cuotaIdCuota = cuotaIdCuota;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCargoExtra != null ? idCargoExtra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargoExtra)) {
            return false;
        }
        CargoExtra other = (CargoExtra) object;
        if ((this.idCargoExtra == null && other.idCargoExtra != null) || (this.idCargoExtra != null && !this.idCargoExtra.equals(other.idCargoExtra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.CargoExtra[ idCargoExtra=" + idCargoExtra + " ]";
    }
    
}

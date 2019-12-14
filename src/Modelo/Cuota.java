/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ivans
 */
@Entity
@Table(name = "CUOTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuota.findAll", query = "SELECT c FROM Cuota c")
    , @NamedQuery(name = "Cuota.findByIdCuota", query = "SELECT c FROM Cuota c WHERE c.idCuota = :idCuota")
    , @NamedQuery(name = "Cuota.findByMonto", query = "SELECT c FROM Cuota c WHERE c.monto = :monto")
    , @NamedQuery(name = "Cuota.findByFechaPago", query = "SELECT c FROM Cuota c WHERE c.fechaPago = :fechaPago")
    , @NamedQuery(name = "Cuota.findByFechaVencimiento", query = "SELECT c FROM Cuota c WHERE c.fechaVencimiento = :fechaVencimiento")})
public class Cuota implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CUOTA")
    private BigDecimal idCuota;
    @Basic(optional = false)
    @Column(name = "MONTO")
    private BigInteger monto;
    @Basic(optional = false)
    @Column(name = "FECHA_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @Basic(optional = false)
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuotaIdCuota")
    private Collection<CargoExtra> cargoExtraCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cuotaIdCuota")
    private Collection<Pago> pagoCollection;
    @JoinColumn(name = "CONTRATO_ID_CONTRATO", referencedColumnName = "ID_CONTRATO")
    @ManyToOne(optional = false)
    private Contrato contratoIdContrato;

    public Cuota() {
    }

    public Cuota(BigDecimal idCuota) {
        this.idCuota = idCuota;
    }

    public Cuota(BigDecimal idCuota, BigInteger monto, Date fechaPago, Date fechaVencimiento) {
        this.idCuota = idCuota;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(BigDecimal idCuota) {
        this.idCuota = idCuota;
    }

    public BigInteger getMonto() {
        return monto;
    }

    public void setMonto(BigInteger monto) {
        this.monto = monto;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @XmlTransient
    public Collection<CargoExtra> getCargoExtraCollection() {
        return cargoExtraCollection;
    }

    public void setCargoExtraCollection(Collection<CargoExtra> cargoExtraCollection) {
        this.cargoExtraCollection = cargoExtraCollection;
    }

    @XmlTransient
    public Collection<Pago> getPagoCollection() {
        return pagoCollection;
    }

    public void setPagoCollection(Collection<Pago> pagoCollection) {
        this.pagoCollection = pagoCollection;
    }

    public Contrato getContratoIdContrato() {
        return contratoIdContrato;
    }

    public void setContratoIdContrato(Contrato contratoIdContrato) {
        this.contratoIdContrato = contratoIdContrato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuota != null ? idCuota.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuota)) {
            return false;
        }
        Cuota other = (Cuota) object;
        if ((this.idCuota == null && other.idCuota != null) || (this.idCuota != null && !this.idCuota.equals(other.idCuota))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Cuota[ idCuota=" + idCuota + " ]";
    }
    
}

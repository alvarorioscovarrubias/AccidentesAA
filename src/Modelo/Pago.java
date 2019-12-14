/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ivans
 */
@Entity
@Table(name = "PAGO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p")
    , @NamedQuery(name = "Pago.findByIdPago", query = "SELECT p FROM Pago p WHERE p.idPago = :idPago")
    , @NamedQuery(name = "Pago.findByFechaPago", query = "SELECT p FROM Pago p WHERE p.fechaPago = :fechaPago")})
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PAGO")
    private BigDecimal idPago;
    @Column(name = "FECHA_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @JoinColumn(name = "CUOTA_ID_CUOTA", referencedColumnName = "ID_CUOTA")
    @ManyToOne(optional = false)
    private Cuota cuotaIdCuota;
    @JoinColumn(name = "ESTADO_PAGO_ID_ESTADO_PAGO", referencedColumnName = "ID_ESTADO_PAGO")
    @ManyToOne(optional = false)
    private EstadoPago estadoPagoIdEstadoPago;
    @JoinColumn(name = "TIPO_PAGO_ID_TIPO_PAGO", referencedColumnName = "ID_TIPO_PAGO")
    @ManyToOne(optional = false)
    private TipoPago tipoPagoIdTipoPago;

    public Pago() {
    }

    public Pago(BigDecimal idPago) {
        this.idPago = idPago;
    }

    public BigDecimal getIdPago() {
        return idPago;
    }

    public void setIdPago(BigDecimal idPago) {
        this.idPago = idPago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Cuota getCuotaIdCuota() {
        return cuotaIdCuota;
    }

    public void setCuotaIdCuota(Cuota cuotaIdCuota) {
        this.cuotaIdCuota = cuotaIdCuota;
    }

    public EstadoPago getEstadoPagoIdEstadoPago() {
        return estadoPagoIdEstadoPago;
    }

    public void setEstadoPagoIdEstadoPago(EstadoPago estadoPagoIdEstadoPago) {
        this.estadoPagoIdEstadoPago = estadoPagoIdEstadoPago;
    }

    public TipoPago getTipoPagoIdTipoPago() {
        return tipoPagoIdTipoPago;
    }

    public void setTipoPagoIdTipoPago(TipoPago tipoPagoIdTipoPago) {
        this.tipoPagoIdTipoPago = tipoPagoIdTipoPago;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPago != null ? idPago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.idPago == null && other.idPago != null) || (this.idPago != null && !this.idPago.equals(other.idPago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Pago[ idPago=" + idPago + " ]";
    }
    
}

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ivans
 */
@Entity
@Table(name = "SERVICIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicio.findAll", query = "SELECT s FROM Servicio s")
    , @NamedQuery(name = "Servicio.findByIdServicio", query = "SELECT s FROM Servicio s WHERE s.idServicio = :idServicio")
    , @NamedQuery(name = "Servicio.findByFechaServicio", query = "SELECT s FROM Servicio s WHERE s.fechaServicio = :fechaServicio")})
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_SERVICIO")
    private BigDecimal idServicio;
    @Basic(optional = false)
    @Column(name = "FECHA_SERVICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaServicio;
    @JoinColumn(name = "CONTRATO_ID_CONTRATO", referencedColumnName = "ID_CONTRATO")
    @ManyToOne(optional = false)
    private Contrato contratoIdContrato;
    @JoinColumn(name = "ESTADO_SERVICIO_ID_EST_SERV", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EstadoServicio estadoServicioIdEstServ;
    @JoinColumn(name = "TIPO_SERVICIO_ID_TIPO_SERVICIO", referencedColumnName = "ID_TIPO_SERVICIO")
    @OneToOne(optional = false)
    private TipoServicio tipoServicioIdTipoServicio;
    @JoinColumn(name = "USUARIO_ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Usuario usuarioIdUsuario;

    public Servicio() {
    }

    public Servicio(BigDecimal idServicio) {
        this.idServicio = idServicio;
    }

    public Servicio(BigDecimal idServicio, Date fechaServicio) {
        this.idServicio = idServicio;
        this.fechaServicio = fechaServicio;
    }

    public BigDecimal getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(BigDecimal idServicio) {
        this.idServicio = idServicio;
    }

    public Date getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(Date fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public Contrato getContratoIdContrato() {
        return contratoIdContrato;
    }

    public void setContratoIdContrato(Contrato contratoIdContrato) {
        this.contratoIdContrato = contratoIdContrato;
    }

    public EstadoServicio getEstadoServicioIdEstServ() {
        return estadoServicioIdEstServ;
    }

    public void setEstadoServicioIdEstServ(EstadoServicio estadoServicioIdEstServ) {
        this.estadoServicioIdEstServ = estadoServicioIdEstServ;
    }

    public TipoServicio getTipoServicioIdTipoServicio() {
        return tipoServicioIdTipoServicio;
    }

    public void setTipoServicioIdTipoServicio(TipoServicio tipoServicioIdTipoServicio) {
        this.tipoServicioIdTipoServicio = tipoServicioIdTipoServicio;
    }

    public Usuario getUsuarioIdUsuario() {
        return usuarioIdUsuario;
    }

    public void setUsuarioIdUsuario(Usuario usuarioIdUsuario) {
        this.usuarioIdUsuario = usuarioIdUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idServicio != null ? idServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        if ((this.idServicio == null && other.idServicio != null) || (this.idServicio != null && !this.idServicio.equals(other.idServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Servicio[ idServicio=" + idServicio + " ]";
    }
    
}

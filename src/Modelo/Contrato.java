/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "CONTRATO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contrato.findAll", query = "SELECT c FROM Contrato c")
    , @NamedQuery(name = "Contrato.findByIdContrato", query = "SELECT c FROM Contrato c WHERE c.idContrato = :idContrato")
    , @NamedQuery(name = "Contrato.findByFechaInicio", query = "SELECT c FROM Contrato c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Contrato.findByFechaTermino", query = "SELECT c FROM Contrato c WHERE c.fechaTermino = :fechaTermino")})
public class Contrato implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CONTRATO")
    private BigDecimal idContrato;
    @Basic(optional = false)
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "FECHA_TERMINO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTermino;
    @JoinColumn(name = "EMPRESA_ID_EMPRESA", referencedColumnName = "ID_EMPRESA")
    @ManyToOne(optional = false)
    private Empresa empresaIdEmpresa;
    @JoinColumn(name = "ESTADO_CONTRATO_ID_EST_CON", referencedColumnName = "ID_EST_CON")
    @ManyToOne(optional = false)
    private EstadoContrato estadoContratoIdEstCon;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contratoIdContrato")
    private Collection<Servicio> servicioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contratoIdContrato")
    private Collection<Cuota> cuotaCollection;

    public Contrato() {
    }

    public Contrato(BigDecimal idContrato) {
        this.idContrato = idContrato;
    }

    public Contrato(BigDecimal idContrato, Date fechaInicio, Date fechaTermino) {
        this.idContrato = idContrato;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
    }

    public BigDecimal getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(BigDecimal idContrato) {
        this.idContrato = idContrato;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public Empresa getEmpresaIdEmpresa() {
        return empresaIdEmpresa;
    }

    public void setEmpresaIdEmpresa(Empresa empresaIdEmpresa) {
        this.empresaIdEmpresa = empresaIdEmpresa;
    }

    public EstadoContrato getEstadoContratoIdEstCon() {
        return estadoContratoIdEstCon;
    }

    public void setEstadoContratoIdEstCon(EstadoContrato estadoContratoIdEstCon) {
        this.estadoContratoIdEstCon = estadoContratoIdEstCon;
    }

    @XmlTransient
    public Collection<Servicio> getServicioCollection() {
        return servicioCollection;
    }

    public void setServicioCollection(Collection<Servicio> servicioCollection) {
        this.servicioCollection = servicioCollection;
    }

    @XmlTransient
    public Collection<Cuota> getCuotaCollection() {
        return cuotaCollection;
    }

    public void setCuotaCollection(Collection<Cuota> cuotaCollection) {
        this.cuotaCollection = cuotaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContrato != null ? idContrato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contrato)) {
            return false;
        }
        Contrato other = (Contrato) object;
        if ((this.idContrato == null && other.idContrato != null) || (this.idContrato != null && !this.idContrato.equals(other.idContrato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Contrato[ idContrato=" + idContrato + " ]";
    }
    
}

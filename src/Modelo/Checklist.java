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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "CHECKLIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Checklist.findAll", query = "SELECT c FROM Checklist c")
    , @NamedQuery(name = "Checklist.findByIdChecklist", query = "SELECT c FROM Checklist c WHERE c.idChecklist = :idChecklist")
    , @NamedQuery(name = "Checklist.findByNombre", query = "SELECT c FROM Checklist c WHERE c.nombre = :nombre")})
public class Checklist implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CHECKLIST")
    private BigDecimal idChecklist;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @JoinColumn(name = "USUARIO_ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Usuario usuarioIdUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistIdChecklist")
    private Collection<ChecklistItem> checklistItemCollection;

    public Checklist() {
    }

    public Checklist(BigDecimal idChecklist) {
        this.idChecklist = idChecklist;
    }

    public Checklist(BigDecimal idChecklist, String nombre) {
        this.idChecklist = idChecklist;
        this.nombre = nombre;
    }

    public BigDecimal getIdChecklist() {
        return idChecklist;
    }

    public void setIdChecklist(BigDecimal idChecklist) {
        this.idChecklist = idChecklist;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getUsuarioIdUsuario() {
        return usuarioIdUsuario;
    }

    public void setUsuarioIdUsuario(Usuario usuarioIdUsuario) {
        this.usuarioIdUsuario = usuarioIdUsuario;
    }

    @XmlTransient
    public Collection<ChecklistItem> getChecklistItemCollection() {
        return checklistItemCollection;
    }

    public void setChecklistItemCollection(Collection<ChecklistItem> checklistItemCollection) {
        this.checklistItemCollection = checklistItemCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idChecklist != null ? idChecklist.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Checklist)) {
            return false;
        }
        Checklist other = (Checklist) object;
        if ((this.idChecklist == null && other.idChecklist != null) || (this.idChecklist != null && !this.idChecklist.equals(other.idChecklist))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Checklist[ idChecklist=" + idChecklist + " ]";
    }
    
}

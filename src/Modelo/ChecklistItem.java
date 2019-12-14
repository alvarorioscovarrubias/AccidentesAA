/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "CHECKLIST_ITEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChecklistItem.findAll", query = "SELECT c FROM ChecklistItem c")
    , @NamedQuery(name = "ChecklistItem.findByIdChecklistItem", query = "SELECT c FROM ChecklistItem c WHERE c.idChecklistItem = :idChecklistItem")
    , @NamedQuery(name = "ChecklistItem.findByPregunta", query = "SELECT c FROM ChecklistItem c WHERE c.pregunta = :pregunta")
    , @NamedQuery(name = "ChecklistItem.findByEstado", query = "SELECT c FROM ChecklistItem c WHERE c.estado = :estado")})
public class ChecklistItem implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CHECKLIST_ITEM")
    private BigDecimal idChecklistItem;
    @Basic(optional = false)
    @Column(name = "PREGUNTA")
    private String pregunta;
    @Basic(optional = false)
    @Column(name = "ESTADO")
    private Character estado;
    @JoinColumn(name = "CHECKLIST_ID_CHECKLIST", referencedColumnName = "ID_CHECKLIST")
    @ManyToOne(optional = false)
    private Checklist checklistIdChecklist;
    @JoinColumn(name = "EMPRESA_ID_EMPRESA", referencedColumnName = "ID_EMPRESA")
    @ManyToOne(optional = false)
    private Empresa empresaIdEmpresa;

    public ChecklistItem() {
    }

    public ChecklistItem(BigDecimal idChecklistItem) {
        this.idChecklistItem = idChecklistItem;
    }

    public ChecklistItem(BigDecimal idChecklistItem, String pregunta, Character estado) {
        this.idChecklistItem = idChecklistItem;
        this.pregunta = pregunta;
        this.estado = estado;
    }

    public BigDecimal getIdChecklistItem() {
        return idChecklistItem;
    }

    public void setIdChecklistItem(BigDecimal idChecklistItem) {
        this.idChecklistItem = idChecklistItem;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Checklist getChecklistIdChecklist() {
        return checklistIdChecklist;
    }

    public void setChecklistIdChecklist(Checklist checklistIdChecklist) {
        this.checklistIdChecklist = checklistIdChecklist;
    }

    public Empresa getEmpresaIdEmpresa() {
        return empresaIdEmpresa;
    }

    public void setEmpresaIdEmpresa(Empresa empresaIdEmpresa) {
        this.empresaIdEmpresa = empresaIdEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idChecklistItem != null ? idChecklistItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChecklistItem)) {
            return false;
        }
        ChecklistItem other = (ChecklistItem) object;
        if ((this.idChecklistItem == null && other.idChecklistItem != null) || (this.idChecklistItem != null && !this.idChecklistItem.equals(other.idChecklistItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.ChecklistItem[ idChecklistItem=" + idChecklistItem + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Checklist;
import Modelo.ChecklistItem;
import Modelo.Empresa;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ivans
 */
public class ChecklistItemJpaController implements Serializable {

    public ChecklistItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ChecklistItem checklistItem) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Checklist checklistIdChecklist = checklistItem.getChecklistIdChecklist();
            if (checklistIdChecklist != null) {
                checklistIdChecklist = em.getReference(checklistIdChecklist.getClass(), checklistIdChecklist.getIdChecklist());
                checklistItem.setChecklistIdChecklist(checklistIdChecklist);
            }
            Empresa empresaIdEmpresa = checklistItem.getEmpresaIdEmpresa();
            if (empresaIdEmpresa != null) {
                empresaIdEmpresa = em.getReference(empresaIdEmpresa.getClass(), empresaIdEmpresa.getIdEmpresa());
                checklistItem.setEmpresaIdEmpresa(empresaIdEmpresa);
            }
            em.persist(checklistItem);
            if (checklistIdChecklist != null) {
                checklistIdChecklist.getChecklistItemCollection().add(checklistItem);
                checklistIdChecklist = em.merge(checklistIdChecklist);
            }
            if (empresaIdEmpresa != null) {
                empresaIdEmpresa.getChecklistItemCollection().add(checklistItem);
                empresaIdEmpresa = em.merge(empresaIdEmpresa);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findChecklistItem(checklistItem.getIdChecklistItem()) != null) {
                throw new PreexistingEntityException("ChecklistItem " + checklistItem + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ChecklistItem checklistItem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ChecklistItem persistentChecklistItem = em.find(ChecklistItem.class, checklistItem.getIdChecklistItem());
            Checklist checklistIdChecklistOld = persistentChecklistItem.getChecklistIdChecklist();
            Checklist checklistIdChecklistNew = checklistItem.getChecklistIdChecklist();
            Empresa empresaIdEmpresaOld = persistentChecklistItem.getEmpresaIdEmpresa();
            Empresa empresaIdEmpresaNew = checklistItem.getEmpresaIdEmpresa();
            if (checklistIdChecklistNew != null) {
                checklistIdChecklistNew = em.getReference(checklistIdChecklistNew.getClass(), checklistIdChecklistNew.getIdChecklist());
                checklistItem.setChecklistIdChecklist(checklistIdChecklistNew);
            }
            if (empresaIdEmpresaNew != null) {
                empresaIdEmpresaNew = em.getReference(empresaIdEmpresaNew.getClass(), empresaIdEmpresaNew.getIdEmpresa());
                checklistItem.setEmpresaIdEmpresa(empresaIdEmpresaNew);
            }
            checklistItem = em.merge(checklistItem);
            if (checklistIdChecklistOld != null && !checklistIdChecklistOld.equals(checklistIdChecklistNew)) {
                checklistIdChecklistOld.getChecklistItemCollection().remove(checklistItem);
                checklistIdChecklistOld = em.merge(checklistIdChecklistOld);
            }
            if (checklistIdChecklistNew != null && !checklistIdChecklistNew.equals(checklistIdChecklistOld)) {
                checklistIdChecklistNew.getChecklistItemCollection().add(checklistItem);
                checklistIdChecklistNew = em.merge(checklistIdChecklistNew);
            }
            if (empresaIdEmpresaOld != null && !empresaIdEmpresaOld.equals(empresaIdEmpresaNew)) {
                empresaIdEmpresaOld.getChecklistItemCollection().remove(checklistItem);
                empresaIdEmpresaOld = em.merge(empresaIdEmpresaOld);
            }
            if (empresaIdEmpresaNew != null && !empresaIdEmpresaNew.equals(empresaIdEmpresaOld)) {
                empresaIdEmpresaNew.getChecklistItemCollection().add(checklistItem);
                empresaIdEmpresaNew = em.merge(empresaIdEmpresaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = checklistItem.getIdChecklistItem();
                if (findChecklistItem(id) == null) {
                    throw new NonexistentEntityException("The checklistItem with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ChecklistItem checklistItem;
            try {
                checklistItem = em.getReference(ChecklistItem.class, id);
                checklistItem.getIdChecklistItem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The checklistItem with id " + id + " no longer exists.", enfe);
            }
            Checklist checklistIdChecklist = checklistItem.getChecklistIdChecklist();
            if (checklistIdChecklist != null) {
                checklistIdChecklist.getChecklistItemCollection().remove(checklistItem);
                checklistIdChecklist = em.merge(checklistIdChecklist);
            }
            Empresa empresaIdEmpresa = checklistItem.getEmpresaIdEmpresa();
            if (empresaIdEmpresa != null) {
                empresaIdEmpresa.getChecklistItemCollection().remove(checklistItem);
                empresaIdEmpresa = em.merge(empresaIdEmpresa);
            }
            em.remove(checklistItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ChecklistItem> findChecklistItemEntities() {
        return findChecklistItemEntities(true, -1, -1);
    }

    public List<ChecklistItem> findChecklistItemEntities(int maxResults, int firstResult) {
        return findChecklistItemEntities(false, maxResults, firstResult);
    }

    private List<ChecklistItem> findChecklistItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ChecklistItem.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ChecklistItem findChecklistItem(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ChecklistItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getChecklistItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ChecklistItem> rt = cq.from(ChecklistItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

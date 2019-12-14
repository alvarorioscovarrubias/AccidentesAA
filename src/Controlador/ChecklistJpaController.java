/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.Checklist;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Usuario;
import Modelo.ChecklistItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ivans
 */
public class ChecklistJpaController implements Serializable {

    public ChecklistJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Checklist checklist) throws PreexistingEntityException, Exception {
        if (checklist.getChecklistItemCollection() == null) {
            checklist.setChecklistItemCollection(new ArrayList<ChecklistItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuarioIdUsuario = checklist.getUsuarioIdUsuario();
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario = em.getReference(usuarioIdUsuario.getClass(), usuarioIdUsuario.getIdUsuario());
                checklist.setUsuarioIdUsuario(usuarioIdUsuario);
            }
            Collection<ChecklistItem> attachedChecklistItemCollection = new ArrayList<ChecklistItem>();
            for (ChecklistItem checklistItemCollectionChecklistItemToAttach : checklist.getChecklistItemCollection()) {
                checklistItemCollectionChecklistItemToAttach = em.getReference(checklistItemCollectionChecklistItemToAttach.getClass(), checklistItemCollectionChecklistItemToAttach.getIdChecklistItem());
                attachedChecklistItemCollection.add(checklistItemCollectionChecklistItemToAttach);
            }
            checklist.setChecklistItemCollection(attachedChecklistItemCollection);
            em.persist(checklist);
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario.getChecklistCollection().add(checklist);
                usuarioIdUsuario = em.merge(usuarioIdUsuario);
            }
            for (ChecklistItem checklistItemCollectionChecklistItem : checklist.getChecklistItemCollection()) {
                Checklist oldChecklistIdChecklistOfChecklistItemCollectionChecklistItem = checklistItemCollectionChecklistItem.getChecklistIdChecklist();
                checklistItemCollectionChecklistItem.setChecklistIdChecklist(checklist);
                checklistItemCollectionChecklistItem = em.merge(checklistItemCollectionChecklistItem);
                if (oldChecklistIdChecklistOfChecklistItemCollectionChecklistItem != null) {
                    oldChecklistIdChecklistOfChecklistItemCollectionChecklistItem.getChecklistItemCollection().remove(checklistItemCollectionChecklistItem);
                    oldChecklistIdChecklistOfChecklistItemCollectionChecklistItem = em.merge(oldChecklistIdChecklistOfChecklistItemCollectionChecklistItem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findChecklist(checklist.getIdChecklist()) != null) {
                throw new PreexistingEntityException("Checklist " + checklist + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Checklist checklist) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Checklist persistentChecklist = em.find(Checklist.class, checklist.getIdChecklist());
            Usuario usuarioIdUsuarioOld = persistentChecklist.getUsuarioIdUsuario();
            Usuario usuarioIdUsuarioNew = checklist.getUsuarioIdUsuario();
            Collection<ChecklistItem> checklistItemCollectionOld = persistentChecklist.getChecklistItemCollection();
            Collection<ChecklistItem> checklistItemCollectionNew = checklist.getChecklistItemCollection();
            List<String> illegalOrphanMessages = null;
            for (ChecklistItem checklistItemCollectionOldChecklistItem : checklistItemCollectionOld) {
                if (!checklistItemCollectionNew.contains(checklistItemCollectionOldChecklistItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ChecklistItem " + checklistItemCollectionOldChecklistItem + " since its checklistIdChecklist field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioIdUsuarioNew != null) {
                usuarioIdUsuarioNew = em.getReference(usuarioIdUsuarioNew.getClass(), usuarioIdUsuarioNew.getIdUsuario());
                checklist.setUsuarioIdUsuario(usuarioIdUsuarioNew);
            }
            Collection<ChecklistItem> attachedChecklistItemCollectionNew = new ArrayList<ChecklistItem>();
            for (ChecklistItem checklistItemCollectionNewChecklistItemToAttach : checklistItemCollectionNew) {
                checklistItemCollectionNewChecklistItemToAttach = em.getReference(checklistItemCollectionNewChecklistItemToAttach.getClass(), checklistItemCollectionNewChecklistItemToAttach.getIdChecklistItem());
                attachedChecklistItemCollectionNew.add(checklistItemCollectionNewChecklistItemToAttach);
            }
            checklistItemCollectionNew = attachedChecklistItemCollectionNew;
            checklist.setChecklistItemCollection(checklistItemCollectionNew);
            checklist = em.merge(checklist);
            if (usuarioIdUsuarioOld != null && !usuarioIdUsuarioOld.equals(usuarioIdUsuarioNew)) {
                usuarioIdUsuarioOld.getChecklistCollection().remove(checklist);
                usuarioIdUsuarioOld = em.merge(usuarioIdUsuarioOld);
            }
            if (usuarioIdUsuarioNew != null && !usuarioIdUsuarioNew.equals(usuarioIdUsuarioOld)) {
                usuarioIdUsuarioNew.getChecklistCollection().add(checklist);
                usuarioIdUsuarioNew = em.merge(usuarioIdUsuarioNew);
            }
            for (ChecklistItem checklistItemCollectionNewChecklistItem : checklistItemCollectionNew) {
                if (!checklistItemCollectionOld.contains(checklistItemCollectionNewChecklistItem)) {
                    Checklist oldChecklistIdChecklistOfChecklistItemCollectionNewChecklistItem = checklistItemCollectionNewChecklistItem.getChecklistIdChecklist();
                    checklistItemCollectionNewChecklistItem.setChecklistIdChecklist(checklist);
                    checklistItemCollectionNewChecklistItem = em.merge(checklistItemCollectionNewChecklistItem);
                    if (oldChecklistIdChecklistOfChecklistItemCollectionNewChecklistItem != null && !oldChecklistIdChecklistOfChecklistItemCollectionNewChecklistItem.equals(checklist)) {
                        oldChecklistIdChecklistOfChecklistItemCollectionNewChecklistItem.getChecklistItemCollection().remove(checklistItemCollectionNewChecklistItem);
                        oldChecklistIdChecklistOfChecklistItemCollectionNewChecklistItem = em.merge(oldChecklistIdChecklistOfChecklistItemCollectionNewChecklistItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = checklist.getIdChecklist();
                if (findChecklist(id) == null) {
                    throw new NonexistentEntityException("The checklist with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Checklist checklist;
            try {
                checklist = em.getReference(Checklist.class, id);
                checklist.getIdChecklist();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The checklist with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ChecklistItem> checklistItemCollectionOrphanCheck = checklist.getChecklistItemCollection();
            for (ChecklistItem checklistItemCollectionOrphanCheckChecklistItem : checklistItemCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Checklist (" + checklist + ") cannot be destroyed since the ChecklistItem " + checklistItemCollectionOrphanCheckChecklistItem + " in its checklistItemCollection field has a non-nullable checklistIdChecklist field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuarioIdUsuario = checklist.getUsuarioIdUsuario();
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario.getChecklistCollection().remove(checklist);
                usuarioIdUsuario = em.merge(usuarioIdUsuario);
            }
            em.remove(checklist);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Checklist> findChecklistEntities() {
        return findChecklistEntities(true, -1, -1);
    }

    public List<Checklist> findChecklistEntities(int maxResults, int firstResult) {
        return findChecklistEntities(false, maxResults, firstResult);
    }

    private List<Checklist> findChecklistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Checklist.class));
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

    public Checklist findChecklist(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Checklist.class, id);
        } finally {
            em.close();
        }
    }

    public int getChecklistCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Checklist> rt = cq.from(Checklist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Usuario;
import Modelo.Contrato;
import java.util.ArrayList;
import java.util.Collection;
import Modelo.ChecklistItem;
import Modelo.Empresa;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ivans
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("AccidentesAAPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) throws PreexistingEntityException, Exception {
        if (empresa.getContratoCollection() == null) {
            empresa.setContratoCollection(new ArrayList<Contrato>());
        }
        if (empresa.getChecklistItemCollection() == null) {
            empresa.setChecklistItemCollection(new ArrayList<ChecklistItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuarioIdUsuario = empresa.getUsuarioIdUsuario();
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario = em.getReference(usuarioIdUsuario.getClass(), usuarioIdUsuario.getIdUsuario());
                empresa.setUsuarioIdUsuario(usuarioIdUsuario);
            }
            Collection<Contrato> attachedContratoCollection = new ArrayList<Contrato>();
            for (Contrato contratoCollectionContratoToAttach : empresa.getContratoCollection()) {
                contratoCollectionContratoToAttach = em.getReference(contratoCollectionContratoToAttach.getClass(), contratoCollectionContratoToAttach.getIdContrato());
                attachedContratoCollection.add(contratoCollectionContratoToAttach);
            }
            empresa.setContratoCollection(attachedContratoCollection);
            Collection<ChecklistItem> attachedChecklistItemCollection = new ArrayList<ChecklistItem>();
            for (ChecklistItem checklistItemCollectionChecklistItemToAttach : empresa.getChecklistItemCollection()) {
                checklistItemCollectionChecklistItemToAttach = em.getReference(checklistItemCollectionChecklistItemToAttach.getClass(), checklistItemCollectionChecklistItemToAttach.getIdChecklistItem());
                attachedChecklistItemCollection.add(checklistItemCollectionChecklistItemToAttach);
            }
            empresa.setChecklistItemCollection(attachedChecklistItemCollection);
            em.persist(empresa);
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario.getEmpresaCollection().add(empresa);
                usuarioIdUsuario = em.merge(usuarioIdUsuario);
            }
            for (Contrato contratoCollectionContrato : empresa.getContratoCollection()) {
                Empresa oldEmpresaIdEmpresaOfContratoCollectionContrato = contratoCollectionContrato.getEmpresaIdEmpresa();
                contratoCollectionContrato.setEmpresaIdEmpresa(empresa);
                contratoCollectionContrato = em.merge(contratoCollectionContrato);
                if (oldEmpresaIdEmpresaOfContratoCollectionContrato != null) {
                    oldEmpresaIdEmpresaOfContratoCollectionContrato.getContratoCollection().remove(contratoCollectionContrato);
                    oldEmpresaIdEmpresaOfContratoCollectionContrato = em.merge(oldEmpresaIdEmpresaOfContratoCollectionContrato);
                }
            }
            for (ChecklistItem checklistItemCollectionChecklistItem : empresa.getChecklistItemCollection()) {
                Empresa oldEmpresaIdEmpresaOfChecklistItemCollectionChecklistItem = checklistItemCollectionChecklistItem.getEmpresaIdEmpresa();
                checklistItemCollectionChecklistItem.setEmpresaIdEmpresa(empresa);
                checklistItemCollectionChecklistItem = em.merge(checklistItemCollectionChecklistItem);
                if (oldEmpresaIdEmpresaOfChecklistItemCollectionChecklistItem != null) {
                    oldEmpresaIdEmpresaOfChecklistItemCollectionChecklistItem.getChecklistItemCollection().remove(checklistItemCollectionChecklistItem);
                    oldEmpresaIdEmpresaOfChecklistItemCollectionChecklistItem = em.merge(oldEmpresaIdEmpresaOfChecklistItemCollectionChecklistItem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpresa(empresa.getIdEmpresa()) != null) {
                throw new PreexistingEntityException("Empresa " + empresa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            Usuario usuarioIdUsuarioOld = persistentEmpresa.getUsuarioIdUsuario();
            Usuario usuarioIdUsuarioNew = empresa.getUsuarioIdUsuario();
            Collection<Contrato> contratoCollectionOld = persistentEmpresa.getContratoCollection();
            Collection<Contrato> contratoCollectionNew = empresa.getContratoCollection();
            Collection<ChecklistItem> checklistItemCollectionOld = persistentEmpresa.getChecklistItemCollection();
            Collection<ChecklistItem> checklistItemCollectionNew = empresa.getChecklistItemCollection();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoCollectionOldContrato : contratoCollectionOld) {
                if (!contratoCollectionNew.contains(contratoCollectionOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contrato " + contratoCollectionOldContrato + " since its empresaIdEmpresa field is not nullable.");
                }
            }
            for (ChecklistItem checklistItemCollectionOldChecklistItem : checklistItemCollectionOld) {
                if (!checklistItemCollectionNew.contains(checklistItemCollectionOldChecklistItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ChecklistItem " + checklistItemCollectionOldChecklistItem + " since its empresaIdEmpresa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioIdUsuarioNew != null) {
                usuarioIdUsuarioNew = em.getReference(usuarioIdUsuarioNew.getClass(), usuarioIdUsuarioNew.getIdUsuario());
                empresa.setUsuarioIdUsuario(usuarioIdUsuarioNew);
            }
            Collection<Contrato> attachedContratoCollectionNew = new ArrayList<Contrato>();
            for (Contrato contratoCollectionNewContratoToAttach : contratoCollectionNew) {
                contratoCollectionNewContratoToAttach = em.getReference(contratoCollectionNewContratoToAttach.getClass(), contratoCollectionNewContratoToAttach.getIdContrato());
                attachedContratoCollectionNew.add(contratoCollectionNewContratoToAttach);
            }
            contratoCollectionNew = attachedContratoCollectionNew;
            empresa.setContratoCollection(contratoCollectionNew);
            Collection<ChecklistItem> attachedChecklistItemCollectionNew = new ArrayList<ChecklistItem>();
            for (ChecklistItem checklistItemCollectionNewChecklistItemToAttach : checklistItemCollectionNew) {
                checklistItemCollectionNewChecklistItemToAttach = em.getReference(checklistItemCollectionNewChecklistItemToAttach.getClass(), checklistItemCollectionNewChecklistItemToAttach.getIdChecklistItem());
                attachedChecklistItemCollectionNew.add(checklistItemCollectionNewChecklistItemToAttach);
            }
            checklistItemCollectionNew = attachedChecklistItemCollectionNew;
            empresa.setChecklistItemCollection(checklistItemCollectionNew);
            empresa = em.merge(empresa);
            if (usuarioIdUsuarioOld != null && !usuarioIdUsuarioOld.equals(usuarioIdUsuarioNew)) {
                usuarioIdUsuarioOld.getEmpresaCollection().remove(empresa);
                usuarioIdUsuarioOld = em.merge(usuarioIdUsuarioOld);
            }
            if (usuarioIdUsuarioNew != null && !usuarioIdUsuarioNew.equals(usuarioIdUsuarioOld)) {
                usuarioIdUsuarioNew.getEmpresaCollection().add(empresa);
                usuarioIdUsuarioNew = em.merge(usuarioIdUsuarioNew);
            }
            for (Contrato contratoCollectionNewContrato : contratoCollectionNew) {
                if (!contratoCollectionOld.contains(contratoCollectionNewContrato)) {
                    Empresa oldEmpresaIdEmpresaOfContratoCollectionNewContrato = contratoCollectionNewContrato.getEmpresaIdEmpresa();
                    contratoCollectionNewContrato.setEmpresaIdEmpresa(empresa);
                    contratoCollectionNewContrato = em.merge(contratoCollectionNewContrato);
                    if (oldEmpresaIdEmpresaOfContratoCollectionNewContrato != null && !oldEmpresaIdEmpresaOfContratoCollectionNewContrato.equals(empresa)) {
                        oldEmpresaIdEmpresaOfContratoCollectionNewContrato.getContratoCollection().remove(contratoCollectionNewContrato);
                        oldEmpresaIdEmpresaOfContratoCollectionNewContrato = em.merge(oldEmpresaIdEmpresaOfContratoCollectionNewContrato);
                    }
                }
            }
            for (ChecklistItem checklistItemCollectionNewChecklistItem : checklistItemCollectionNew) {
                if (!checklistItemCollectionOld.contains(checklistItemCollectionNewChecklistItem)) {
                    Empresa oldEmpresaIdEmpresaOfChecklistItemCollectionNewChecklistItem = checklistItemCollectionNewChecklistItem.getEmpresaIdEmpresa();
                    checklistItemCollectionNewChecklistItem.setEmpresaIdEmpresa(empresa);
                    checklistItemCollectionNewChecklistItem = em.merge(checklistItemCollectionNewChecklistItem);
                    if (oldEmpresaIdEmpresaOfChecklistItemCollectionNewChecklistItem != null && !oldEmpresaIdEmpresaOfChecklistItemCollectionNewChecklistItem.equals(empresa)) {
                        oldEmpresaIdEmpresaOfChecklistItemCollectionNewChecklistItem.getChecklistItemCollection().remove(checklistItemCollectionNewChecklistItem);
                        oldEmpresaIdEmpresaOfChecklistItemCollectionNewChecklistItem = em.merge(oldEmpresaIdEmpresaOfChecklistItemCollectionNewChecklistItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = empresa.getIdEmpresa();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contrato> contratoCollectionOrphanCheck = empresa.getContratoCollection();
            for (Contrato contratoCollectionOrphanCheckContrato : contratoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Contrato " + contratoCollectionOrphanCheckContrato + " in its contratoCollection field has a non-nullable empresaIdEmpresa field.");
            }
            Collection<ChecklistItem> checklistItemCollectionOrphanCheck = empresa.getChecklistItemCollection();
            for (ChecklistItem checklistItemCollectionOrphanCheckChecklistItem : checklistItemCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the ChecklistItem " + checklistItemCollectionOrphanCheckChecklistItem + " in its checklistItemCollection field has a non-nullable empresaIdEmpresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuarioIdUsuario = empresa.getUsuarioIdUsuario();
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario.getEmpresaCollection().remove(empresa);
                usuarioIdUsuario = em.merge(usuarioIdUsuario);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

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
import Modelo.Contrato;
import Modelo.EstadoContrato;
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
public class EstadoContratoJpaController implements Serializable {

    public EstadoContratoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoContrato estadoContrato) throws PreexistingEntityException, Exception {
        if (estadoContrato.getContratoCollection() == null) {
            estadoContrato.setContratoCollection(new ArrayList<Contrato>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Contrato> attachedContratoCollection = new ArrayList<Contrato>();
            for (Contrato contratoCollectionContratoToAttach : estadoContrato.getContratoCollection()) {
                contratoCollectionContratoToAttach = em.getReference(contratoCollectionContratoToAttach.getClass(), contratoCollectionContratoToAttach.getIdContrato());
                attachedContratoCollection.add(contratoCollectionContratoToAttach);
            }
            estadoContrato.setContratoCollection(attachedContratoCollection);
            em.persist(estadoContrato);
            for (Contrato contratoCollectionContrato : estadoContrato.getContratoCollection()) {
                EstadoContrato oldEstadoContratoIdEstConOfContratoCollectionContrato = contratoCollectionContrato.getEstadoContratoIdEstCon();
                contratoCollectionContrato.setEstadoContratoIdEstCon(estadoContrato);
                contratoCollectionContrato = em.merge(contratoCollectionContrato);
                if (oldEstadoContratoIdEstConOfContratoCollectionContrato != null) {
                    oldEstadoContratoIdEstConOfContratoCollectionContrato.getContratoCollection().remove(contratoCollectionContrato);
                    oldEstadoContratoIdEstConOfContratoCollectionContrato = em.merge(oldEstadoContratoIdEstConOfContratoCollectionContrato);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadoContrato(estadoContrato.getIdEstCon()) != null) {
                throw new PreexistingEntityException("EstadoContrato " + estadoContrato + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoContrato estadoContrato) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoContrato persistentEstadoContrato = em.find(EstadoContrato.class, estadoContrato.getIdEstCon());
            Collection<Contrato> contratoCollectionOld = persistentEstadoContrato.getContratoCollection();
            Collection<Contrato> contratoCollectionNew = estadoContrato.getContratoCollection();
            List<String> illegalOrphanMessages = null;
            for (Contrato contratoCollectionOldContrato : contratoCollectionOld) {
                if (!contratoCollectionNew.contains(contratoCollectionOldContrato)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contrato " + contratoCollectionOldContrato + " since its estadoContratoIdEstCon field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Contrato> attachedContratoCollectionNew = new ArrayList<Contrato>();
            for (Contrato contratoCollectionNewContratoToAttach : contratoCollectionNew) {
                contratoCollectionNewContratoToAttach = em.getReference(contratoCollectionNewContratoToAttach.getClass(), contratoCollectionNewContratoToAttach.getIdContrato());
                attachedContratoCollectionNew.add(contratoCollectionNewContratoToAttach);
            }
            contratoCollectionNew = attachedContratoCollectionNew;
            estadoContrato.setContratoCollection(contratoCollectionNew);
            estadoContrato = em.merge(estadoContrato);
            for (Contrato contratoCollectionNewContrato : contratoCollectionNew) {
                if (!contratoCollectionOld.contains(contratoCollectionNewContrato)) {
                    EstadoContrato oldEstadoContratoIdEstConOfContratoCollectionNewContrato = contratoCollectionNewContrato.getEstadoContratoIdEstCon();
                    contratoCollectionNewContrato.setEstadoContratoIdEstCon(estadoContrato);
                    contratoCollectionNewContrato = em.merge(contratoCollectionNewContrato);
                    if (oldEstadoContratoIdEstConOfContratoCollectionNewContrato != null && !oldEstadoContratoIdEstConOfContratoCollectionNewContrato.equals(estadoContrato)) {
                        oldEstadoContratoIdEstConOfContratoCollectionNewContrato.getContratoCollection().remove(contratoCollectionNewContrato);
                        oldEstadoContratoIdEstConOfContratoCollectionNewContrato = em.merge(oldEstadoContratoIdEstConOfContratoCollectionNewContrato);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = estadoContrato.getIdEstCon();
                if (findEstadoContrato(id) == null) {
                    throw new NonexistentEntityException("The estadoContrato with id " + id + " no longer exists.");
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
            EstadoContrato estadoContrato;
            try {
                estadoContrato = em.getReference(EstadoContrato.class, id);
                estadoContrato.getIdEstCon();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoContrato with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contrato> contratoCollectionOrphanCheck = estadoContrato.getContratoCollection();
            for (Contrato contratoCollectionOrphanCheckContrato : contratoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoContrato (" + estadoContrato + ") cannot be destroyed since the Contrato " + contratoCollectionOrphanCheckContrato + " in its contratoCollection field has a non-nullable estadoContratoIdEstCon field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoContrato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoContrato> findEstadoContratoEntities() {
        return findEstadoContratoEntities(true, -1, -1);
    }

    public List<EstadoContrato> findEstadoContratoEntities(int maxResults, int firstResult) {
        return findEstadoContratoEntities(false, maxResults, firstResult);
    }

    private List<EstadoContrato> findEstadoContratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoContrato.class));
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

    public EstadoContrato findEstadoContrato(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoContrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoContratoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoContrato> rt = cq.from(EstadoContrato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

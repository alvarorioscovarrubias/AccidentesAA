/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.EstadoPago;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Pago;
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
public class EstadoPagoJpaController implements Serializable {

    public EstadoPagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoPago estadoPago) throws PreexistingEntityException, Exception {
        if (estadoPago.getPagoCollection() == null) {
            estadoPago.setPagoCollection(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : estadoPago.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getIdPago());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            estadoPago.setPagoCollection(attachedPagoCollection);
            em.persist(estadoPago);
            for (Pago pagoCollectionPago : estadoPago.getPagoCollection()) {
                EstadoPago oldEstadoPagoIdEstadoPagoOfPagoCollectionPago = pagoCollectionPago.getEstadoPagoIdEstadoPago();
                pagoCollectionPago.setEstadoPagoIdEstadoPago(estadoPago);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldEstadoPagoIdEstadoPagoOfPagoCollectionPago != null) {
                    oldEstadoPagoIdEstadoPagoOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldEstadoPagoIdEstadoPagoOfPagoCollectionPago = em.merge(oldEstadoPagoIdEstadoPagoOfPagoCollectionPago);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadoPago(estadoPago.getIdEstadoPago()) != null) {
                throw new PreexistingEntityException("EstadoPago " + estadoPago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoPago estadoPago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoPago persistentEstadoPago = em.find(EstadoPago.class, estadoPago.getIdEstadoPago());
            Collection<Pago> pagoCollectionOld = persistentEstadoPago.getPagoCollection();
            Collection<Pago> pagoCollectionNew = estadoPago.getPagoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoCollectionOldPago + " since its estadoPagoIdEstadoPago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Pago> attachedPagoCollectionNew = new ArrayList<Pago>();
            for (Pago pagoCollectionNewPagoToAttach : pagoCollectionNew) {
                pagoCollectionNewPagoToAttach = em.getReference(pagoCollectionNewPagoToAttach.getClass(), pagoCollectionNewPagoToAttach.getIdPago());
                attachedPagoCollectionNew.add(pagoCollectionNewPagoToAttach);
            }
            pagoCollectionNew = attachedPagoCollectionNew;
            estadoPago.setPagoCollection(pagoCollectionNew);
            estadoPago = em.merge(estadoPago);
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    EstadoPago oldEstadoPagoIdEstadoPagoOfPagoCollectionNewPago = pagoCollectionNewPago.getEstadoPagoIdEstadoPago();
                    pagoCollectionNewPago.setEstadoPagoIdEstadoPago(estadoPago);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldEstadoPagoIdEstadoPagoOfPagoCollectionNewPago != null && !oldEstadoPagoIdEstadoPagoOfPagoCollectionNewPago.equals(estadoPago)) {
                        oldEstadoPagoIdEstadoPagoOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldEstadoPagoIdEstadoPagoOfPagoCollectionNewPago = em.merge(oldEstadoPagoIdEstadoPagoOfPagoCollectionNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = estadoPago.getIdEstadoPago();
                if (findEstadoPago(id) == null) {
                    throw new NonexistentEntityException("The estadoPago with id " + id + " no longer exists.");
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
            EstadoPago estadoPago;
            try {
                estadoPago = em.getReference(EstadoPago.class, id);
                estadoPago.getIdEstadoPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoPago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pago> pagoCollectionOrphanCheck = estadoPago.getPagoCollection();
            for (Pago pagoCollectionOrphanCheckPago : pagoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoPago (" + estadoPago + ") cannot be destroyed since the Pago " + pagoCollectionOrphanCheckPago + " in its pagoCollection field has a non-nullable estadoPagoIdEstadoPago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoPago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoPago> findEstadoPagoEntities() {
        return findEstadoPagoEntities(true, -1, -1);
    }

    public List<EstadoPago> findEstadoPagoEntities(int maxResults, int firstResult) {
        return findEstadoPagoEntities(false, maxResults, firstResult);
    }

    private List<EstadoPago> findEstadoPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoPago.class));
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

    public EstadoPago findEstadoPago(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoPago.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoPago> rt = cq.from(EstadoPago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

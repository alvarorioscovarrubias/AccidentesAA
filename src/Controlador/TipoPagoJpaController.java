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
import Modelo.Pago;
import Modelo.TipoPago;
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
public class TipoPagoJpaController implements Serializable {

    public TipoPagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoPago tipoPago) throws PreexistingEntityException, Exception {
        if (tipoPago.getPagoCollection() == null) {
            tipoPago.setPagoCollection(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : tipoPago.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getIdPago());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            tipoPago.setPagoCollection(attachedPagoCollection);
            em.persist(tipoPago);
            for (Pago pagoCollectionPago : tipoPago.getPagoCollection()) {
                TipoPago oldTipoPagoIdTipoPagoOfPagoCollectionPago = pagoCollectionPago.getTipoPagoIdTipoPago();
                pagoCollectionPago.setTipoPagoIdTipoPago(tipoPago);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldTipoPagoIdTipoPagoOfPagoCollectionPago != null) {
                    oldTipoPagoIdTipoPagoOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldTipoPagoIdTipoPagoOfPagoCollectionPago = em.merge(oldTipoPagoIdTipoPagoOfPagoCollectionPago);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoPago(tipoPago.getIdTipoPago()) != null) {
                throw new PreexistingEntityException("TipoPago " + tipoPago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoPago tipoPago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPago persistentTipoPago = em.find(TipoPago.class, tipoPago.getIdTipoPago());
            Collection<Pago> pagoCollectionOld = persistentTipoPago.getPagoCollection();
            Collection<Pago> pagoCollectionNew = tipoPago.getPagoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoCollectionOldPago + " since its tipoPagoIdTipoPago field is not nullable.");
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
            tipoPago.setPagoCollection(pagoCollectionNew);
            tipoPago = em.merge(tipoPago);
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    TipoPago oldTipoPagoIdTipoPagoOfPagoCollectionNewPago = pagoCollectionNewPago.getTipoPagoIdTipoPago();
                    pagoCollectionNewPago.setTipoPagoIdTipoPago(tipoPago);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldTipoPagoIdTipoPagoOfPagoCollectionNewPago != null && !oldTipoPagoIdTipoPagoOfPagoCollectionNewPago.equals(tipoPago)) {
                        oldTipoPagoIdTipoPagoOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldTipoPagoIdTipoPagoOfPagoCollectionNewPago = em.merge(oldTipoPagoIdTipoPagoOfPagoCollectionNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = tipoPago.getIdTipoPago();
                if (findTipoPago(id) == null) {
                    throw new NonexistentEntityException("The tipoPago with id " + id + " no longer exists.");
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
            TipoPago tipoPago;
            try {
                tipoPago = em.getReference(TipoPago.class, id);
                tipoPago.getIdTipoPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pago> pagoCollectionOrphanCheck = tipoPago.getPagoCollection();
            for (Pago pagoCollectionOrphanCheckPago : pagoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoPago (" + tipoPago + ") cannot be destroyed since the Pago " + pagoCollectionOrphanCheckPago + " in its pagoCollection field has a non-nullable tipoPagoIdTipoPago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoPago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoPago> findTipoPagoEntities() {
        return findTipoPagoEntities(true, -1, -1);
    }

    public List<TipoPago> findTipoPagoEntities(int maxResults, int firstResult) {
        return findTipoPagoEntities(false, maxResults, firstResult);
    }

    private List<TipoPago> findTipoPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPago.class));
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

    public TipoPago findTipoPago(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPago.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPago> rt = cq.from(TipoPago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

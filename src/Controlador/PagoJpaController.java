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
import Modelo.Cuota;
import Modelo.EstadoPago;
import Modelo.Pago;
import Modelo.TipoPago;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ivans
 */
public class PagoJpaController implements Serializable {

    public PagoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("AccidentesAAPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuota cuotaIdCuota = pago.getCuotaIdCuota();
            if (cuotaIdCuota != null) {
                cuotaIdCuota = em.getReference(cuotaIdCuota.getClass(), cuotaIdCuota.getIdCuota());
                pago.setCuotaIdCuota(cuotaIdCuota);
            }
            EstadoPago estadoPagoIdEstadoPago = pago.getEstadoPagoIdEstadoPago();
            if (estadoPagoIdEstadoPago != null) {
                estadoPagoIdEstadoPago = em.getReference(estadoPagoIdEstadoPago.getClass(), estadoPagoIdEstadoPago.getIdEstadoPago());
                pago.setEstadoPagoIdEstadoPago(estadoPagoIdEstadoPago);
            }
            TipoPago tipoPagoIdTipoPago = pago.getTipoPagoIdTipoPago();
            if (tipoPagoIdTipoPago != null) {
                tipoPagoIdTipoPago = em.getReference(tipoPagoIdTipoPago.getClass(), tipoPagoIdTipoPago.getIdTipoPago());
                pago.setTipoPagoIdTipoPago(tipoPagoIdTipoPago);
            }
            em.persist(pago);
            if (cuotaIdCuota != null) {
                cuotaIdCuota.getPagoCollection().add(pago);
                cuotaIdCuota = em.merge(cuotaIdCuota);
            }
            if (estadoPagoIdEstadoPago != null) {
                estadoPagoIdEstadoPago.getPagoCollection().add(pago);
                estadoPagoIdEstadoPago = em.merge(estadoPagoIdEstadoPago);
            }
            if (tipoPagoIdTipoPago != null) {
                tipoPagoIdTipoPago.getPagoCollection().add(pago);
                tipoPagoIdTipoPago = em.merge(tipoPagoIdTipoPago);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPago(pago.getIdPago()) != null) {
                throw new PreexistingEntityException("Pago " + pago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getIdPago());
            Cuota cuotaIdCuotaOld = persistentPago.getCuotaIdCuota();
            Cuota cuotaIdCuotaNew = pago.getCuotaIdCuota();
            EstadoPago estadoPagoIdEstadoPagoOld = persistentPago.getEstadoPagoIdEstadoPago();
            EstadoPago estadoPagoIdEstadoPagoNew = pago.getEstadoPagoIdEstadoPago();
            TipoPago tipoPagoIdTipoPagoOld = persistentPago.getTipoPagoIdTipoPago();
            TipoPago tipoPagoIdTipoPagoNew = pago.getTipoPagoIdTipoPago();
            if (cuotaIdCuotaNew != null) {
                cuotaIdCuotaNew = em.getReference(cuotaIdCuotaNew.getClass(), cuotaIdCuotaNew.getIdCuota());
                pago.setCuotaIdCuota(cuotaIdCuotaNew);
            }
            if (estadoPagoIdEstadoPagoNew != null) {
                estadoPagoIdEstadoPagoNew = em.getReference(estadoPagoIdEstadoPagoNew.getClass(), estadoPagoIdEstadoPagoNew.getIdEstadoPago());
                pago.setEstadoPagoIdEstadoPago(estadoPagoIdEstadoPagoNew);
            }
            if (tipoPagoIdTipoPagoNew != null) {
                tipoPagoIdTipoPagoNew = em.getReference(tipoPagoIdTipoPagoNew.getClass(), tipoPagoIdTipoPagoNew.getIdTipoPago());
                pago.setTipoPagoIdTipoPago(tipoPagoIdTipoPagoNew);
            }
            pago = em.merge(pago);
            if (cuotaIdCuotaOld != null && !cuotaIdCuotaOld.equals(cuotaIdCuotaNew)) {
                cuotaIdCuotaOld.getPagoCollection().remove(pago);
                cuotaIdCuotaOld = em.merge(cuotaIdCuotaOld);
            }
            if (cuotaIdCuotaNew != null && !cuotaIdCuotaNew.equals(cuotaIdCuotaOld)) {
                cuotaIdCuotaNew.getPagoCollection().add(pago);
                cuotaIdCuotaNew = em.merge(cuotaIdCuotaNew);
            }
            if (estadoPagoIdEstadoPagoOld != null && !estadoPagoIdEstadoPagoOld.equals(estadoPagoIdEstadoPagoNew)) {
                estadoPagoIdEstadoPagoOld.getPagoCollection().remove(pago);
                estadoPagoIdEstadoPagoOld = em.merge(estadoPagoIdEstadoPagoOld);
            }
            if (estadoPagoIdEstadoPagoNew != null && !estadoPagoIdEstadoPagoNew.equals(estadoPagoIdEstadoPagoOld)) {
                estadoPagoIdEstadoPagoNew.getPagoCollection().add(pago);
                estadoPagoIdEstadoPagoNew = em.merge(estadoPagoIdEstadoPagoNew);
            }
            if (tipoPagoIdTipoPagoOld != null && !tipoPagoIdTipoPagoOld.equals(tipoPagoIdTipoPagoNew)) {
                tipoPagoIdTipoPagoOld.getPagoCollection().remove(pago);
                tipoPagoIdTipoPagoOld = em.merge(tipoPagoIdTipoPagoOld);
            }
            if (tipoPagoIdTipoPagoNew != null && !tipoPagoIdTipoPagoNew.equals(tipoPagoIdTipoPagoOld)) {
                tipoPagoIdTipoPagoNew.getPagoCollection().add(pago);
                tipoPagoIdTipoPagoNew = em.merge(tipoPagoIdTipoPagoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = pago.getIdPago();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
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
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getIdPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            Cuota cuotaIdCuota = pago.getCuotaIdCuota();
            if (cuotaIdCuota != null) {
                cuotaIdCuota.getPagoCollection().remove(pago);
                cuotaIdCuota = em.merge(cuotaIdCuota);
            }
            EstadoPago estadoPagoIdEstadoPago = pago.getEstadoPagoIdEstadoPago();
            if (estadoPagoIdEstadoPago != null) {
                estadoPagoIdEstadoPago.getPagoCollection().remove(pago);
                estadoPagoIdEstadoPago = em.merge(estadoPagoIdEstadoPago);
            }
            TipoPago tipoPagoIdTipoPago = pago.getTipoPagoIdTipoPago();
            if (tipoPagoIdTipoPago != null) {
                tipoPagoIdTipoPago.getPagoCollection().remove(pago);
                tipoPagoIdTipoPago = em.merge(tipoPagoIdTipoPago);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public boolean buscarCodigo(BigDecimal idPago) {
        EntityManager em = getEntityManager();
        boolean valor;
        try {
            Query query = em.createNamedQuery("Pago.findByIdPago");
            query.setParameter("idPago", idPago);

            List rs = query.getResultList();
            if (!rs.isEmpty()) {
                valor = true;
            } else {
                valor = false;
            }
        } catch (Exception e) {
            valor = false;
            e.getMessage();
        }
        return valor;
    }
}

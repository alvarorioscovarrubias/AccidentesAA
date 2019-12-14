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
import Modelo.CargoExtra;
import Modelo.Cuota;
import java.util.ArrayList;
import java.util.Collection;
import Modelo.Pago;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ivans
 */
public class CuotaJpaController implements Serializable {

    public CuotaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("AccidentesAAPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuota cuota) throws PreexistingEntityException, Exception {
        if (cuota.getCargoExtraCollection() == null) {
            cuota.setCargoExtraCollection(new ArrayList<CargoExtra>());
        }
        if (cuota.getPagoCollection() == null) {
            cuota.setPagoCollection(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato contratoIdContrato = cuota.getContratoIdContrato();
            if (contratoIdContrato != null) {
                contratoIdContrato = em.getReference(contratoIdContrato.getClass(), contratoIdContrato.getIdContrato());
                cuota.setContratoIdContrato(contratoIdContrato);
            }
            Collection<CargoExtra> attachedCargoExtraCollection = new ArrayList<CargoExtra>();
            for (CargoExtra cargoExtraCollectionCargoExtraToAttach : cuota.getCargoExtraCollection()) {
                cargoExtraCollectionCargoExtraToAttach = em.getReference(cargoExtraCollectionCargoExtraToAttach.getClass(), cargoExtraCollectionCargoExtraToAttach.getIdCargoExtra());
                attachedCargoExtraCollection.add(cargoExtraCollectionCargoExtraToAttach);
            }
            cuota.setCargoExtraCollection(attachedCargoExtraCollection);
            Collection<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : cuota.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getIdPago());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            cuota.setPagoCollection(attachedPagoCollection);
            em.persist(cuota);
            if (contratoIdContrato != null) {
                contratoIdContrato.getCuotaCollection().add(cuota);
                contratoIdContrato = em.merge(contratoIdContrato);
            }
            for (CargoExtra cargoExtraCollectionCargoExtra : cuota.getCargoExtraCollection()) {
                Cuota oldCuotaIdCuotaOfCargoExtraCollectionCargoExtra = cargoExtraCollectionCargoExtra.getCuotaIdCuota();
                cargoExtraCollectionCargoExtra.setCuotaIdCuota(cuota);
                cargoExtraCollectionCargoExtra = em.merge(cargoExtraCollectionCargoExtra);
                if (oldCuotaIdCuotaOfCargoExtraCollectionCargoExtra != null) {
                    oldCuotaIdCuotaOfCargoExtraCollectionCargoExtra.getCargoExtraCollection().remove(cargoExtraCollectionCargoExtra);
                    oldCuotaIdCuotaOfCargoExtraCollectionCargoExtra = em.merge(oldCuotaIdCuotaOfCargoExtraCollectionCargoExtra);
                }
            }
            for (Pago pagoCollectionPago : cuota.getPagoCollection()) {
                Cuota oldCuotaIdCuotaOfPagoCollectionPago = pagoCollectionPago.getCuotaIdCuota();
                pagoCollectionPago.setCuotaIdCuota(cuota);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldCuotaIdCuotaOfPagoCollectionPago != null) {
                    oldCuotaIdCuotaOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldCuotaIdCuotaOfPagoCollectionPago = em.merge(oldCuotaIdCuotaOfPagoCollectionPago);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCuota(cuota.getIdCuota()) != null) {
                throw new PreexistingEntityException("Cuota " + cuota + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuota cuota) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuota persistentCuota = em.find(Cuota.class, cuota.getIdCuota());
            Contrato contratoIdContratoOld = persistentCuota.getContratoIdContrato();
            Contrato contratoIdContratoNew = cuota.getContratoIdContrato();
            Collection<CargoExtra> cargoExtraCollectionOld = persistentCuota.getCargoExtraCollection();
            Collection<CargoExtra> cargoExtraCollectionNew = cuota.getCargoExtraCollection();
            Collection<Pago> pagoCollectionOld = persistentCuota.getPagoCollection();
            Collection<Pago> pagoCollectionNew = cuota.getPagoCollection();
            List<String> illegalOrphanMessages = null;
            for (CargoExtra cargoExtraCollectionOldCargoExtra : cargoExtraCollectionOld) {
                if (!cargoExtraCollectionNew.contains(cargoExtraCollectionOldCargoExtra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CargoExtra " + cargoExtraCollectionOldCargoExtra + " since its cuotaIdCuota field is not nullable.");
                }
            }
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoCollectionOldPago + " since its cuotaIdCuota field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (contratoIdContratoNew != null) {
                contratoIdContratoNew = em.getReference(contratoIdContratoNew.getClass(), contratoIdContratoNew.getIdContrato());
                cuota.setContratoIdContrato(contratoIdContratoNew);
            }
            Collection<CargoExtra> attachedCargoExtraCollectionNew = new ArrayList<CargoExtra>();
            for (CargoExtra cargoExtraCollectionNewCargoExtraToAttach : cargoExtraCollectionNew) {
                cargoExtraCollectionNewCargoExtraToAttach = em.getReference(cargoExtraCollectionNewCargoExtraToAttach.getClass(), cargoExtraCollectionNewCargoExtraToAttach.getIdCargoExtra());
                attachedCargoExtraCollectionNew.add(cargoExtraCollectionNewCargoExtraToAttach);
            }
            cargoExtraCollectionNew = attachedCargoExtraCollectionNew;
            cuota.setCargoExtraCollection(cargoExtraCollectionNew);
            Collection<Pago> attachedPagoCollectionNew = new ArrayList<Pago>();
            for (Pago pagoCollectionNewPagoToAttach : pagoCollectionNew) {
                pagoCollectionNewPagoToAttach = em.getReference(pagoCollectionNewPagoToAttach.getClass(), pagoCollectionNewPagoToAttach.getIdPago());
                attachedPagoCollectionNew.add(pagoCollectionNewPagoToAttach);
            }
            pagoCollectionNew = attachedPagoCollectionNew;
            cuota.setPagoCollection(pagoCollectionNew);
            cuota = em.merge(cuota);
            if (contratoIdContratoOld != null && !contratoIdContratoOld.equals(contratoIdContratoNew)) {
                contratoIdContratoOld.getCuotaCollection().remove(cuota);
                contratoIdContratoOld = em.merge(contratoIdContratoOld);
            }
            if (contratoIdContratoNew != null && !contratoIdContratoNew.equals(contratoIdContratoOld)) {
                contratoIdContratoNew.getCuotaCollection().add(cuota);
                contratoIdContratoNew = em.merge(contratoIdContratoNew);
            }
            for (CargoExtra cargoExtraCollectionNewCargoExtra : cargoExtraCollectionNew) {
                if (!cargoExtraCollectionOld.contains(cargoExtraCollectionNewCargoExtra)) {
                    Cuota oldCuotaIdCuotaOfCargoExtraCollectionNewCargoExtra = cargoExtraCollectionNewCargoExtra.getCuotaIdCuota();
                    cargoExtraCollectionNewCargoExtra.setCuotaIdCuota(cuota);
                    cargoExtraCollectionNewCargoExtra = em.merge(cargoExtraCollectionNewCargoExtra);
                    if (oldCuotaIdCuotaOfCargoExtraCollectionNewCargoExtra != null && !oldCuotaIdCuotaOfCargoExtraCollectionNewCargoExtra.equals(cuota)) {
                        oldCuotaIdCuotaOfCargoExtraCollectionNewCargoExtra.getCargoExtraCollection().remove(cargoExtraCollectionNewCargoExtra);
                        oldCuotaIdCuotaOfCargoExtraCollectionNewCargoExtra = em.merge(oldCuotaIdCuotaOfCargoExtraCollectionNewCargoExtra);
                    }
                }
            }
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    Cuota oldCuotaIdCuotaOfPagoCollectionNewPago = pagoCollectionNewPago.getCuotaIdCuota();
                    pagoCollectionNewPago.setCuotaIdCuota(cuota);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldCuotaIdCuotaOfPagoCollectionNewPago != null && !oldCuotaIdCuotaOfPagoCollectionNewPago.equals(cuota)) {
                        oldCuotaIdCuotaOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldCuotaIdCuotaOfPagoCollectionNewPago = em.merge(oldCuotaIdCuotaOfPagoCollectionNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = cuota.getIdCuota();
                if (findCuota(id) == null) {
                    throw new NonexistentEntityException("The cuota with id " + id + " no longer exists.");
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
            Cuota cuota;
            try {
                cuota = em.getReference(Cuota.class, id);
                cuota.getIdCuota();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuota with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<CargoExtra> cargoExtraCollectionOrphanCheck = cuota.getCargoExtraCollection();
            for (CargoExtra cargoExtraCollectionOrphanCheckCargoExtra : cargoExtraCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuota (" + cuota + ") cannot be destroyed since the CargoExtra " + cargoExtraCollectionOrphanCheckCargoExtra + " in its cargoExtraCollection field has a non-nullable cuotaIdCuota field.");
            }
            Collection<Pago> pagoCollectionOrphanCheck = cuota.getPagoCollection();
            for (Pago pagoCollectionOrphanCheckPago : pagoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuota (" + cuota + ") cannot be destroyed since the Pago " + pagoCollectionOrphanCheckPago + " in its pagoCollection field has a non-nullable cuotaIdCuota field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Contrato contratoIdContrato = cuota.getContratoIdContrato();
            if (contratoIdContrato != null) {
                contratoIdContrato.getCuotaCollection().remove(cuota);
                contratoIdContrato = em.merge(contratoIdContrato);
            }
            em.remove(cuota);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cuota> findCuotaEntities() {
        return findCuotaEntities(true, -1, -1);
    }

    public List<Cuota> findCuotaEntities(int maxResults, int firstResult) {
        return findCuotaEntities(false, maxResults, firstResult);
    }

    private List<Cuota> findCuotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuota.class));
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

    public Cuota findCuota(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuota.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuotaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuota> rt = cq.from(Cuota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public boolean buscarCodigo(BigDecimal idCuota) {
        EntityManager em = getEntityManager();
        boolean valor;
        try {
            Query query = em.createNamedQuery("Cuota.findByIdCuota");
            query.setParameter("idCuota", idCuota);

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

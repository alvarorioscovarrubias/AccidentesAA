/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.Contrato;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Empresa;
import Modelo.EstadoContrato;
import Modelo.Servicio;
import java.util.ArrayList;
import java.util.Collection;
import Modelo.Cuota;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ivans
 */
public class ContratoJpaController implements Serializable {

    public ContratoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("AccidentesAAPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contrato contrato) throws PreexistingEntityException, Exception {
        if (contrato.getServicioCollection() == null) {
            contrato.setServicioCollection(new ArrayList<Servicio>());
        }
        if (contrato.getCuotaCollection() == null) {
            contrato.setCuotaCollection(new ArrayList<Cuota>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresaIdEmpresa = contrato.getEmpresaIdEmpresa();
            if (empresaIdEmpresa != null) {
                empresaIdEmpresa = em.getReference(empresaIdEmpresa.getClass(), empresaIdEmpresa.getIdEmpresa());
                contrato.setEmpresaIdEmpresa(empresaIdEmpresa);
            }
            EstadoContrato estadoContratoIdEstCon = contrato.getEstadoContratoIdEstCon();
            if (estadoContratoIdEstCon != null) {
                estadoContratoIdEstCon = em.getReference(estadoContratoIdEstCon.getClass(), estadoContratoIdEstCon.getIdEstCon());
                contrato.setEstadoContratoIdEstCon(estadoContratoIdEstCon);
            }
            Collection<Servicio> attachedServicioCollection = new ArrayList<Servicio>();
            for (Servicio servicioCollectionServicioToAttach : contrato.getServicioCollection()) {
                servicioCollectionServicioToAttach = em.getReference(servicioCollectionServicioToAttach.getClass(), servicioCollectionServicioToAttach.getIdServicio());
                attachedServicioCollection.add(servicioCollectionServicioToAttach);
            }
            contrato.setServicioCollection(attachedServicioCollection);
            Collection<Cuota> attachedCuotaCollection = new ArrayList<Cuota>();
            for (Cuota cuotaCollectionCuotaToAttach : contrato.getCuotaCollection()) {
                cuotaCollectionCuotaToAttach = em.getReference(cuotaCollectionCuotaToAttach.getClass(), cuotaCollectionCuotaToAttach.getIdCuota());
                attachedCuotaCollection.add(cuotaCollectionCuotaToAttach);
            }
            contrato.setCuotaCollection(attachedCuotaCollection);
            em.persist(contrato);
            if (empresaIdEmpresa != null) {
                empresaIdEmpresa.getContratoCollection().add(contrato);
                empresaIdEmpresa = em.merge(empresaIdEmpresa);
            }
            if (estadoContratoIdEstCon != null) {
                estadoContratoIdEstCon.getContratoCollection().add(contrato);
                estadoContratoIdEstCon = em.merge(estadoContratoIdEstCon);
            }
            for (Servicio servicioCollectionServicio : contrato.getServicioCollection()) {
                Contrato oldContratoIdContratoOfServicioCollectionServicio = servicioCollectionServicio.getContratoIdContrato();
                servicioCollectionServicio.setContratoIdContrato(contrato);
                servicioCollectionServicio = em.merge(servicioCollectionServicio);
                if (oldContratoIdContratoOfServicioCollectionServicio != null) {
                    oldContratoIdContratoOfServicioCollectionServicio.getServicioCollection().remove(servicioCollectionServicio);
                    oldContratoIdContratoOfServicioCollectionServicio = em.merge(oldContratoIdContratoOfServicioCollectionServicio);
                }
            }
            for (Cuota cuotaCollectionCuota : contrato.getCuotaCollection()) {
                Contrato oldContratoIdContratoOfCuotaCollectionCuota = cuotaCollectionCuota.getContratoIdContrato();
                cuotaCollectionCuota.setContratoIdContrato(contrato);
                cuotaCollectionCuota = em.merge(cuotaCollectionCuota);
                if (oldContratoIdContratoOfCuotaCollectionCuota != null) {
                    oldContratoIdContratoOfCuotaCollectionCuota.getCuotaCollection().remove(cuotaCollectionCuota);
                    oldContratoIdContratoOfCuotaCollectionCuota = em.merge(oldContratoIdContratoOfCuotaCollectionCuota);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findContrato(contrato.getIdContrato()) != null) {
                throw new PreexistingEntityException("Contrato " + contrato + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contrato contrato) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato persistentContrato = em.find(Contrato.class, contrato.getIdContrato());
            Empresa empresaIdEmpresaOld = persistentContrato.getEmpresaIdEmpresa();
            Empresa empresaIdEmpresaNew = contrato.getEmpresaIdEmpresa();
            EstadoContrato estadoContratoIdEstConOld = persistentContrato.getEstadoContratoIdEstCon();
            EstadoContrato estadoContratoIdEstConNew = contrato.getEstadoContratoIdEstCon();
            Collection<Servicio> servicioCollectionOld = persistentContrato.getServicioCollection();
            Collection<Servicio> servicioCollectionNew = contrato.getServicioCollection();
            Collection<Cuota> cuotaCollectionOld = persistentContrato.getCuotaCollection();
            Collection<Cuota> cuotaCollectionNew = contrato.getCuotaCollection();
            List<String> illegalOrphanMessages = null;
            for (Servicio servicioCollectionOldServicio : servicioCollectionOld) {
                if (!servicioCollectionNew.contains(servicioCollectionOldServicio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Servicio " + servicioCollectionOldServicio + " since its contratoIdContrato field is not nullable.");
                }
            }
            for (Cuota cuotaCollectionOldCuota : cuotaCollectionOld) {
                if (!cuotaCollectionNew.contains(cuotaCollectionOldCuota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cuota " + cuotaCollectionOldCuota + " since its contratoIdContrato field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaIdEmpresaNew != null) {
                empresaIdEmpresaNew = em.getReference(empresaIdEmpresaNew.getClass(), empresaIdEmpresaNew.getIdEmpresa());
                contrato.setEmpresaIdEmpresa(empresaIdEmpresaNew);
            }
            if (estadoContratoIdEstConNew != null) {
                estadoContratoIdEstConNew = em.getReference(estadoContratoIdEstConNew.getClass(), estadoContratoIdEstConNew.getIdEstCon());
                contrato.setEstadoContratoIdEstCon(estadoContratoIdEstConNew);
            }
            Collection<Servicio> attachedServicioCollectionNew = new ArrayList<Servicio>();
            for (Servicio servicioCollectionNewServicioToAttach : servicioCollectionNew) {
                servicioCollectionNewServicioToAttach = em.getReference(servicioCollectionNewServicioToAttach.getClass(), servicioCollectionNewServicioToAttach.getIdServicio());
                attachedServicioCollectionNew.add(servicioCollectionNewServicioToAttach);
            }
            servicioCollectionNew = attachedServicioCollectionNew;
            contrato.setServicioCollection(servicioCollectionNew);
            Collection<Cuota> attachedCuotaCollectionNew = new ArrayList<Cuota>();
            for (Cuota cuotaCollectionNewCuotaToAttach : cuotaCollectionNew) {
                cuotaCollectionNewCuotaToAttach = em.getReference(cuotaCollectionNewCuotaToAttach.getClass(), cuotaCollectionNewCuotaToAttach.getIdCuota());
                attachedCuotaCollectionNew.add(cuotaCollectionNewCuotaToAttach);
            }
            cuotaCollectionNew = attachedCuotaCollectionNew;
            contrato.setCuotaCollection(cuotaCollectionNew);
            contrato = em.merge(contrato);
            if (empresaIdEmpresaOld != null && !empresaIdEmpresaOld.equals(empresaIdEmpresaNew)) {
                empresaIdEmpresaOld.getContratoCollection().remove(contrato);
                empresaIdEmpresaOld = em.merge(empresaIdEmpresaOld);
            }
            if (empresaIdEmpresaNew != null && !empresaIdEmpresaNew.equals(empresaIdEmpresaOld)) {
                empresaIdEmpresaNew.getContratoCollection().add(contrato);
                empresaIdEmpresaNew = em.merge(empresaIdEmpresaNew);
            }
            if (estadoContratoIdEstConOld != null && !estadoContratoIdEstConOld.equals(estadoContratoIdEstConNew)) {
                estadoContratoIdEstConOld.getContratoCollection().remove(contrato);
                estadoContratoIdEstConOld = em.merge(estadoContratoIdEstConOld);
            }
            if (estadoContratoIdEstConNew != null && !estadoContratoIdEstConNew.equals(estadoContratoIdEstConOld)) {
                estadoContratoIdEstConNew.getContratoCollection().add(contrato);
                estadoContratoIdEstConNew = em.merge(estadoContratoIdEstConNew);
            }
            for (Servicio servicioCollectionNewServicio : servicioCollectionNew) {
                if (!servicioCollectionOld.contains(servicioCollectionNewServicio)) {
                    Contrato oldContratoIdContratoOfServicioCollectionNewServicio = servicioCollectionNewServicio.getContratoIdContrato();
                    servicioCollectionNewServicio.setContratoIdContrato(contrato);
                    servicioCollectionNewServicio = em.merge(servicioCollectionNewServicio);
                    if (oldContratoIdContratoOfServicioCollectionNewServicio != null && !oldContratoIdContratoOfServicioCollectionNewServicio.equals(contrato)) {
                        oldContratoIdContratoOfServicioCollectionNewServicio.getServicioCollection().remove(servicioCollectionNewServicio);
                        oldContratoIdContratoOfServicioCollectionNewServicio = em.merge(oldContratoIdContratoOfServicioCollectionNewServicio);
                    }
                }
            }
            for (Cuota cuotaCollectionNewCuota : cuotaCollectionNew) {
                if (!cuotaCollectionOld.contains(cuotaCollectionNewCuota)) {
                    Contrato oldContratoIdContratoOfCuotaCollectionNewCuota = cuotaCollectionNewCuota.getContratoIdContrato();
                    cuotaCollectionNewCuota.setContratoIdContrato(contrato);
                    cuotaCollectionNewCuota = em.merge(cuotaCollectionNewCuota);
                    if (oldContratoIdContratoOfCuotaCollectionNewCuota != null && !oldContratoIdContratoOfCuotaCollectionNewCuota.equals(contrato)) {
                        oldContratoIdContratoOfCuotaCollectionNewCuota.getCuotaCollection().remove(cuotaCollectionNewCuota);
                        oldContratoIdContratoOfCuotaCollectionNewCuota = em.merge(oldContratoIdContratoOfCuotaCollectionNewCuota);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = contrato.getIdContrato();
                if (findContrato(id) == null) {
                    throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.");
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
            Contrato contrato;
            try {
                contrato = em.getReference(Contrato.class, id);
                contrato.getIdContrato();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contrato with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Servicio> servicioCollectionOrphanCheck = contrato.getServicioCollection();
            for (Servicio servicioCollectionOrphanCheckServicio : servicioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the Servicio " + servicioCollectionOrphanCheckServicio + " in its servicioCollection field has a non-nullable contratoIdContrato field.");
            }
            Collection<Cuota> cuotaCollectionOrphanCheck = contrato.getCuotaCollection();
            for (Cuota cuotaCollectionOrphanCheckCuota : cuotaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Contrato (" + contrato + ") cannot be destroyed since the Cuota " + cuotaCollectionOrphanCheckCuota + " in its cuotaCollection field has a non-nullable contratoIdContrato field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa empresaIdEmpresa = contrato.getEmpresaIdEmpresa();
            if (empresaIdEmpresa != null) {
                empresaIdEmpresa.getContratoCollection().remove(contrato);
                empresaIdEmpresa = em.merge(empresaIdEmpresa);
            }
            EstadoContrato estadoContratoIdEstCon = contrato.getEstadoContratoIdEstCon();
            if (estadoContratoIdEstCon != null) {
                estadoContratoIdEstCon.getContratoCollection().remove(contrato);
                estadoContratoIdEstCon = em.merge(estadoContratoIdEstCon);
            }
            em.remove(contrato);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contrato> findContratoEntities() {
        return findContratoEntities(true, -1, -1);
    }

    public List<Contrato> findContratoEntities(int maxResults, int firstResult) {
        return findContratoEntities(false, maxResults, firstResult);
    }

    private List<Contrato> findContratoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contrato.class));
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

    public Contrato findContrato(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contrato.class, id);
        } finally {
            em.close();
        }
    }

    public int getContratoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contrato> rt = cq.from(Contrato.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public boolean buscarCodigo(BigDecimal idContrato) {
        EntityManager em = getEntityManager();
        boolean valor;
        try {
            Query query = em.createNamedQuery("Contrato.findByIdContrato");
            query.setParameter("idContrato", idContrato);

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

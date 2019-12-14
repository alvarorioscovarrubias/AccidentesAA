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
import Modelo.EstadoServicio;
import Modelo.Servicio;
import Modelo.TipoServicio;
import Modelo.Usuario;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ivans
 */
public class ServicioJpaController implements Serializable {

    public ServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Servicio servicio) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        EstadoServicio estadoServicioIdEstServOrphanCheck = servicio.getEstadoServicioIdEstServ();
        if (estadoServicioIdEstServOrphanCheck != null) {
            Servicio oldServicioOfEstadoServicioIdEstServ = estadoServicioIdEstServOrphanCheck.getServicio();
            if (oldServicioOfEstadoServicioIdEstServ != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The EstadoServicio " + estadoServicioIdEstServOrphanCheck + " already has an item of type Servicio whose estadoServicioIdEstServ column cannot be null. Please make another selection for the estadoServicioIdEstServ field.");
            }
        }
        TipoServicio tipoServicioIdTipoServicioOrphanCheck = servicio.getTipoServicioIdTipoServicio();
        if (tipoServicioIdTipoServicioOrphanCheck != null) {
            Servicio oldServicioOfTipoServicioIdTipoServicio = tipoServicioIdTipoServicioOrphanCheck.getServicio();
            if (oldServicioOfTipoServicioIdTipoServicio != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The TipoServicio " + tipoServicioIdTipoServicioOrphanCheck + " already has an item of type Servicio whose tipoServicioIdTipoServicio column cannot be null. Please make another selection for the tipoServicioIdTipoServicio field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contrato contratoIdContrato = servicio.getContratoIdContrato();
            if (contratoIdContrato != null) {
                contratoIdContrato = em.getReference(contratoIdContrato.getClass(), contratoIdContrato.getIdContrato());
                servicio.setContratoIdContrato(contratoIdContrato);
            }
            EstadoServicio estadoServicioIdEstServ = servicio.getEstadoServicioIdEstServ();
            if (estadoServicioIdEstServ != null) {
                estadoServicioIdEstServ = em.getReference(estadoServicioIdEstServ.getClass(), estadoServicioIdEstServ.getId());
                servicio.setEstadoServicioIdEstServ(estadoServicioIdEstServ);
            }
            TipoServicio tipoServicioIdTipoServicio = servicio.getTipoServicioIdTipoServicio();
            if (tipoServicioIdTipoServicio != null) {
                tipoServicioIdTipoServicio = em.getReference(tipoServicioIdTipoServicio.getClass(), tipoServicioIdTipoServicio.getIdTipoServicio());
                servicio.setTipoServicioIdTipoServicio(tipoServicioIdTipoServicio);
            }
            Usuario usuarioIdUsuario = servicio.getUsuarioIdUsuario();
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario = em.getReference(usuarioIdUsuario.getClass(), usuarioIdUsuario.getIdUsuario());
                servicio.setUsuarioIdUsuario(usuarioIdUsuario);
            }
            em.persist(servicio);
            if (contratoIdContrato != null) {
                contratoIdContrato.getServicioCollection().add(servicio);
                contratoIdContrato = em.merge(contratoIdContrato);
            }
            if (estadoServicioIdEstServ != null) {
                estadoServicioIdEstServ.setServicio(servicio);
                estadoServicioIdEstServ = em.merge(estadoServicioIdEstServ);
            }
            if (tipoServicioIdTipoServicio != null) {
                tipoServicioIdTipoServicio.setServicio(servicio);
                tipoServicioIdTipoServicio = em.merge(tipoServicioIdTipoServicio);
            }
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario.getServicioCollection().add(servicio);
                usuarioIdUsuario = em.merge(usuarioIdUsuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findServicio(servicio.getIdServicio()) != null) {
                throw new PreexistingEntityException("Servicio " + servicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Servicio servicio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servicio persistentServicio = em.find(Servicio.class, servicio.getIdServicio());
            Contrato contratoIdContratoOld = persistentServicio.getContratoIdContrato();
            Contrato contratoIdContratoNew = servicio.getContratoIdContrato();
            EstadoServicio estadoServicioIdEstServOld = persistentServicio.getEstadoServicioIdEstServ();
            EstadoServicio estadoServicioIdEstServNew = servicio.getEstadoServicioIdEstServ();
            TipoServicio tipoServicioIdTipoServicioOld = persistentServicio.getTipoServicioIdTipoServicio();
            TipoServicio tipoServicioIdTipoServicioNew = servicio.getTipoServicioIdTipoServicio();
            Usuario usuarioIdUsuarioOld = persistentServicio.getUsuarioIdUsuario();
            Usuario usuarioIdUsuarioNew = servicio.getUsuarioIdUsuario();
            List<String> illegalOrphanMessages = null;
            if (estadoServicioIdEstServNew != null && !estadoServicioIdEstServNew.equals(estadoServicioIdEstServOld)) {
                Servicio oldServicioOfEstadoServicioIdEstServ = estadoServicioIdEstServNew.getServicio();
                if (oldServicioOfEstadoServicioIdEstServ != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The EstadoServicio " + estadoServicioIdEstServNew + " already has an item of type Servicio whose estadoServicioIdEstServ column cannot be null. Please make another selection for the estadoServicioIdEstServ field.");
                }
            }
            if (tipoServicioIdTipoServicioNew != null && !tipoServicioIdTipoServicioNew.equals(tipoServicioIdTipoServicioOld)) {
                Servicio oldServicioOfTipoServicioIdTipoServicio = tipoServicioIdTipoServicioNew.getServicio();
                if (oldServicioOfTipoServicioIdTipoServicio != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The TipoServicio " + tipoServicioIdTipoServicioNew + " already has an item of type Servicio whose tipoServicioIdTipoServicio column cannot be null. Please make another selection for the tipoServicioIdTipoServicio field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (contratoIdContratoNew != null) {
                contratoIdContratoNew = em.getReference(contratoIdContratoNew.getClass(), contratoIdContratoNew.getIdContrato());
                servicio.setContratoIdContrato(contratoIdContratoNew);
            }
            if (estadoServicioIdEstServNew != null) {
                estadoServicioIdEstServNew = em.getReference(estadoServicioIdEstServNew.getClass(), estadoServicioIdEstServNew.getId());
                servicio.setEstadoServicioIdEstServ(estadoServicioIdEstServNew);
            }
            if (tipoServicioIdTipoServicioNew != null) {
                tipoServicioIdTipoServicioNew = em.getReference(tipoServicioIdTipoServicioNew.getClass(), tipoServicioIdTipoServicioNew.getIdTipoServicio());
                servicio.setTipoServicioIdTipoServicio(tipoServicioIdTipoServicioNew);
            }
            if (usuarioIdUsuarioNew != null) {
                usuarioIdUsuarioNew = em.getReference(usuarioIdUsuarioNew.getClass(), usuarioIdUsuarioNew.getIdUsuario());
                servicio.setUsuarioIdUsuario(usuarioIdUsuarioNew);
            }
            servicio = em.merge(servicio);
            if (contratoIdContratoOld != null && !contratoIdContratoOld.equals(contratoIdContratoNew)) {
                contratoIdContratoOld.getServicioCollection().remove(servicio);
                contratoIdContratoOld = em.merge(contratoIdContratoOld);
            }
            if (contratoIdContratoNew != null && !contratoIdContratoNew.equals(contratoIdContratoOld)) {
                contratoIdContratoNew.getServicioCollection().add(servicio);
                contratoIdContratoNew = em.merge(contratoIdContratoNew);
            }
            if (estadoServicioIdEstServOld != null && !estadoServicioIdEstServOld.equals(estadoServicioIdEstServNew)) {
                estadoServicioIdEstServOld.setServicio(null);
                estadoServicioIdEstServOld = em.merge(estadoServicioIdEstServOld);
            }
            if (estadoServicioIdEstServNew != null && !estadoServicioIdEstServNew.equals(estadoServicioIdEstServOld)) {
                estadoServicioIdEstServNew.setServicio(servicio);
                estadoServicioIdEstServNew = em.merge(estadoServicioIdEstServNew);
            }
            if (tipoServicioIdTipoServicioOld != null && !tipoServicioIdTipoServicioOld.equals(tipoServicioIdTipoServicioNew)) {
                tipoServicioIdTipoServicioOld.setServicio(null);
                tipoServicioIdTipoServicioOld = em.merge(tipoServicioIdTipoServicioOld);
            }
            if (tipoServicioIdTipoServicioNew != null && !tipoServicioIdTipoServicioNew.equals(tipoServicioIdTipoServicioOld)) {
                tipoServicioIdTipoServicioNew.setServicio(servicio);
                tipoServicioIdTipoServicioNew = em.merge(tipoServicioIdTipoServicioNew);
            }
            if (usuarioIdUsuarioOld != null && !usuarioIdUsuarioOld.equals(usuarioIdUsuarioNew)) {
                usuarioIdUsuarioOld.getServicioCollection().remove(servicio);
                usuarioIdUsuarioOld = em.merge(usuarioIdUsuarioOld);
            }
            if (usuarioIdUsuarioNew != null && !usuarioIdUsuarioNew.equals(usuarioIdUsuarioOld)) {
                usuarioIdUsuarioNew.getServicioCollection().add(servicio);
                usuarioIdUsuarioNew = em.merge(usuarioIdUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = servicio.getIdServicio();
                if (findServicio(id) == null) {
                    throw new NonexistentEntityException("The servicio with id " + id + " no longer exists.");
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
            Servicio servicio;
            try {
                servicio = em.getReference(Servicio.class, id);
                servicio.getIdServicio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicio with id " + id + " no longer exists.", enfe);
            }
            Contrato contratoIdContrato = servicio.getContratoIdContrato();
            if (contratoIdContrato != null) {
                contratoIdContrato.getServicioCollection().remove(servicio);
                contratoIdContrato = em.merge(contratoIdContrato);
            }
            EstadoServicio estadoServicioIdEstServ = servicio.getEstadoServicioIdEstServ();
            if (estadoServicioIdEstServ != null) {
                estadoServicioIdEstServ.setServicio(null);
                estadoServicioIdEstServ = em.merge(estadoServicioIdEstServ);
            }
            TipoServicio tipoServicioIdTipoServicio = servicio.getTipoServicioIdTipoServicio();
            if (tipoServicioIdTipoServicio != null) {
                tipoServicioIdTipoServicio.setServicio(null);
                tipoServicioIdTipoServicio = em.merge(tipoServicioIdTipoServicio);
            }
            Usuario usuarioIdUsuario = servicio.getUsuarioIdUsuario();
            if (usuarioIdUsuario != null) {
                usuarioIdUsuario.getServicioCollection().remove(servicio);
                usuarioIdUsuario = em.merge(usuarioIdUsuario);
            }
            em.remove(servicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Servicio> findServicioEntities() {
        return findServicioEntities(true, -1, -1);
    }

    public List<Servicio> findServicioEntities(int maxResults, int firstResult) {
        return findServicioEntities(false, maxResults, firstResult);
    }

    private List<Servicio> findServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicio.class));
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

    public Servicio findServicio(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Servicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servicio> rt = cq.from(Servicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

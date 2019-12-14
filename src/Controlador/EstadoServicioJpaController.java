/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import Modelo.EstadoServicio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Servicio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ivans
 */
public class EstadoServicioJpaController implements Serializable {

    public EstadoServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoServicio estadoServicio) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servicio servicio = estadoServicio.getServicio();
            if (servicio != null) {
                servicio = em.getReference(servicio.getClass(), servicio.getIdServicio());
                estadoServicio.setServicio(servicio);
            }
            em.persist(estadoServicio);
            if (servicio != null) {
                EstadoServicio oldEstadoServicioIdEstServOfServicio = servicio.getEstadoServicioIdEstServ();
                if (oldEstadoServicioIdEstServOfServicio != null) {
                    oldEstadoServicioIdEstServOfServicio.setServicio(null);
                    oldEstadoServicioIdEstServOfServicio = em.merge(oldEstadoServicioIdEstServOfServicio);
                }
                servicio.setEstadoServicioIdEstServ(estadoServicio);
                servicio = em.merge(servicio);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadoServicio(estadoServicio.getId()) != null) {
                throw new PreexistingEntityException("EstadoServicio " + estadoServicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoServicio estadoServicio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoServicio persistentEstadoServicio = em.find(EstadoServicio.class, estadoServicio.getId());
            Servicio servicioOld = persistentEstadoServicio.getServicio();
            Servicio servicioNew = estadoServicio.getServicio();
            List<String> illegalOrphanMessages = null;
            if (servicioOld != null && !servicioOld.equals(servicioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Servicio " + servicioOld + " since its estadoServicioIdEstServ field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (servicioNew != null) {
                servicioNew = em.getReference(servicioNew.getClass(), servicioNew.getIdServicio());
                estadoServicio.setServicio(servicioNew);
            }
            estadoServicio = em.merge(estadoServicio);
            if (servicioNew != null && !servicioNew.equals(servicioOld)) {
                EstadoServicio oldEstadoServicioIdEstServOfServicio = servicioNew.getEstadoServicioIdEstServ();
                if (oldEstadoServicioIdEstServOfServicio != null) {
                    oldEstadoServicioIdEstServOfServicio.setServicio(null);
                    oldEstadoServicioIdEstServOfServicio = em.merge(oldEstadoServicioIdEstServOfServicio);
                }
                servicioNew.setEstadoServicioIdEstServ(estadoServicio);
                servicioNew = em.merge(servicioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = estadoServicio.getId();
                if (findEstadoServicio(id) == null) {
                    throw new NonexistentEntityException("The estadoServicio with id " + id + " no longer exists.");
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
            EstadoServicio estadoServicio;
            try {
                estadoServicio = em.getReference(EstadoServicio.class, id);
                estadoServicio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoServicio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Servicio servicioOrphanCheck = estadoServicio.getServicio();
            if (servicioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoServicio (" + estadoServicio + ") cannot be destroyed since the Servicio " + servicioOrphanCheck + " in its servicio field has a non-nullable estadoServicioIdEstServ field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoServicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoServicio> findEstadoServicioEntities() {
        return findEstadoServicioEntities(true, -1, -1);
    }

    public List<EstadoServicio> findEstadoServicioEntities(int maxResults, int firstResult) {
        return findEstadoServicioEntities(false, maxResults, firstResult);
    }

    private List<EstadoServicio> findEstadoServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoServicio.class));
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

    public EstadoServicio findEstadoServicio(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoServicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoServicio> rt = cq.from(EstadoServicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

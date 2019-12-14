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
import Modelo.Servicio;
import Modelo.TipoServicio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ivans
 */
public class TipoServicioJpaController implements Serializable {

    public TipoServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoServicio tipoServicio) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Servicio servicio = tipoServicio.getServicio();
            if (servicio != null) {
                servicio = em.getReference(servicio.getClass(), servicio.getIdServicio());
                tipoServicio.setServicio(servicio);
            }
            em.persist(tipoServicio);
            if (servicio != null) {
                TipoServicio oldTipoServicioIdTipoServicioOfServicio = servicio.getTipoServicioIdTipoServicio();
                if (oldTipoServicioIdTipoServicioOfServicio != null) {
                    oldTipoServicioIdTipoServicioOfServicio.setServicio(null);
                    oldTipoServicioIdTipoServicioOfServicio = em.merge(oldTipoServicioIdTipoServicioOfServicio);
                }
                servicio.setTipoServicioIdTipoServicio(tipoServicio);
                servicio = em.merge(servicio);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoServicio(tipoServicio.getIdTipoServicio()) != null) {
                throw new PreexistingEntityException("TipoServicio " + tipoServicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoServicio tipoServicio) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoServicio persistentTipoServicio = em.find(TipoServicio.class, tipoServicio.getIdTipoServicio());
            Servicio servicioOld = persistentTipoServicio.getServicio();
            Servicio servicioNew = tipoServicio.getServicio();
            List<String> illegalOrphanMessages = null;
            if (servicioOld != null && !servicioOld.equals(servicioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Servicio " + servicioOld + " since its tipoServicioIdTipoServicio field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (servicioNew != null) {
                servicioNew = em.getReference(servicioNew.getClass(), servicioNew.getIdServicio());
                tipoServicio.setServicio(servicioNew);
            }
            tipoServicio = em.merge(tipoServicio);
            if (servicioNew != null && !servicioNew.equals(servicioOld)) {
                TipoServicio oldTipoServicioIdTipoServicioOfServicio = servicioNew.getTipoServicioIdTipoServicio();
                if (oldTipoServicioIdTipoServicioOfServicio != null) {
                    oldTipoServicioIdTipoServicioOfServicio.setServicio(null);
                    oldTipoServicioIdTipoServicioOfServicio = em.merge(oldTipoServicioIdTipoServicioOfServicio);
                }
                servicioNew.setTipoServicioIdTipoServicio(tipoServicio);
                servicioNew = em.merge(servicioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = tipoServicio.getIdTipoServicio();
                if (findTipoServicio(id) == null) {
                    throw new NonexistentEntityException("The tipoServicio with id " + id + " no longer exists.");
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
            TipoServicio tipoServicio;
            try {
                tipoServicio = em.getReference(TipoServicio.class, id);
                tipoServicio.getIdTipoServicio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoServicio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Servicio servicioOrphanCheck = tipoServicio.getServicio();
            if (servicioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoServicio (" + tipoServicio + ") cannot be destroyed since the Servicio " + servicioOrphanCheck + " in its servicio field has a non-nullable tipoServicioIdTipoServicio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoServicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoServicio> findTipoServicioEntities() {
        return findTipoServicioEntities(true, -1, -1);
    }

    public List<TipoServicio> findTipoServicioEntities(int maxResults, int firstResult) {
        return findTipoServicioEntities(false, maxResults, firstResult);
    }

    private List<TipoServicio> findTipoServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoServicio.class));
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

    public TipoServicio findTipoServicio(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoServicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoServicio> rt = cq.from(TipoServicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

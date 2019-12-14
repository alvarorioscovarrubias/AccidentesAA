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
import Modelo.TipoUsuario;
import Modelo.Checklist;
import java.util.ArrayList;
import java.util.Collection;
import Modelo.Empresa;
import Modelo.Servicio;
import Modelo.Usuario;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ivans
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController() {
        this.emf = Persistence.createEntityManagerFactory("AccidentesAAPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getChecklistCollection() == null) {
            usuario.setChecklistCollection(new ArrayList<Checklist>());
        }
        if (usuario.getEmpresaCollection() == null) {
            usuario.setEmpresaCollection(new ArrayList<Empresa>());
        }
        if (usuario.getServicioCollection() == null) {
            usuario.setServicioCollection(new ArrayList<Servicio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoUsuario tipoUsuarioIdTipoUsuario = usuario.getTipoUsuarioIdTipoUsuario();
            if (tipoUsuarioIdTipoUsuario != null) {
                tipoUsuarioIdTipoUsuario = em.getReference(tipoUsuarioIdTipoUsuario.getClass(), tipoUsuarioIdTipoUsuario.getIdTipoUsuario());
                usuario.setTipoUsuarioIdTipoUsuario(tipoUsuarioIdTipoUsuario);
            }
            Collection<Checklist> attachedChecklistCollection = new ArrayList<Checklist>();
            for (Checklist checklistCollectionChecklistToAttach : usuario.getChecklistCollection()) {
                checklistCollectionChecklistToAttach = em.getReference(checklistCollectionChecklistToAttach.getClass(), checklistCollectionChecklistToAttach.getIdChecklist());
                attachedChecklistCollection.add(checklistCollectionChecklistToAttach);
            }
            usuario.setChecklistCollection(attachedChecklistCollection);
            Collection<Empresa> attachedEmpresaCollection = new ArrayList<Empresa>();
            for (Empresa empresaCollectionEmpresaToAttach : usuario.getEmpresaCollection()) {
                empresaCollectionEmpresaToAttach = em.getReference(empresaCollectionEmpresaToAttach.getClass(), empresaCollectionEmpresaToAttach.getIdEmpresa());
                attachedEmpresaCollection.add(empresaCollectionEmpresaToAttach);
            }
            usuario.setEmpresaCollection(attachedEmpresaCollection);
            Collection<Servicio> attachedServicioCollection = new ArrayList<Servicio>();
            for (Servicio servicioCollectionServicioToAttach : usuario.getServicioCollection()) {
                servicioCollectionServicioToAttach = em.getReference(servicioCollectionServicioToAttach.getClass(), servicioCollectionServicioToAttach.getIdServicio());
                attachedServicioCollection.add(servicioCollectionServicioToAttach);
            }
            usuario.setServicioCollection(attachedServicioCollection);
            em.persist(usuario);
            if (tipoUsuarioIdTipoUsuario != null) {
                tipoUsuarioIdTipoUsuario.getUsuarioCollection().add(usuario);
                tipoUsuarioIdTipoUsuario = em.merge(tipoUsuarioIdTipoUsuario);
            }
            for (Checklist checklistCollectionChecklist : usuario.getChecklistCollection()) {
                Usuario oldUsuarioIdUsuarioOfChecklistCollectionChecklist = checklistCollectionChecklist.getUsuarioIdUsuario();
                checklistCollectionChecklist.setUsuarioIdUsuario(usuario);
                checklistCollectionChecklist = em.merge(checklistCollectionChecklist);
                if (oldUsuarioIdUsuarioOfChecklistCollectionChecklist != null) {
                    oldUsuarioIdUsuarioOfChecklistCollectionChecklist.getChecklistCollection().remove(checklistCollectionChecklist);
                    oldUsuarioIdUsuarioOfChecklistCollectionChecklist = em.merge(oldUsuarioIdUsuarioOfChecklistCollectionChecklist);
                }
            }
            for (Empresa empresaCollectionEmpresa : usuario.getEmpresaCollection()) {
                Usuario oldUsuarioIdUsuarioOfEmpresaCollectionEmpresa = empresaCollectionEmpresa.getUsuarioIdUsuario();
                empresaCollectionEmpresa.setUsuarioIdUsuario(usuario);
                empresaCollectionEmpresa = em.merge(empresaCollectionEmpresa);
                if (oldUsuarioIdUsuarioOfEmpresaCollectionEmpresa != null) {
                    oldUsuarioIdUsuarioOfEmpresaCollectionEmpresa.getEmpresaCollection().remove(empresaCollectionEmpresa);
                    oldUsuarioIdUsuarioOfEmpresaCollectionEmpresa = em.merge(oldUsuarioIdUsuarioOfEmpresaCollectionEmpresa);
                }
            }
            for (Servicio servicioCollectionServicio : usuario.getServicioCollection()) {
                Usuario oldUsuarioIdUsuarioOfServicioCollectionServicio = servicioCollectionServicio.getUsuarioIdUsuario();
                servicioCollectionServicio.setUsuarioIdUsuario(usuario);
                servicioCollectionServicio = em.merge(servicioCollectionServicio);
                if (oldUsuarioIdUsuarioOfServicioCollectionServicio != null) {
                    oldUsuarioIdUsuarioOfServicioCollectionServicio.getServicioCollection().remove(servicioCollectionServicio);
                    oldUsuarioIdUsuarioOfServicioCollectionServicio = em.merge(oldUsuarioIdUsuarioOfServicioCollectionServicio);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            TipoUsuario tipoUsuarioIdTipoUsuarioOld = persistentUsuario.getTipoUsuarioIdTipoUsuario();
            TipoUsuario tipoUsuarioIdTipoUsuarioNew = usuario.getTipoUsuarioIdTipoUsuario();
            Collection<Checklist> checklistCollectionOld = persistentUsuario.getChecklistCollection();
            Collection<Checklist> checklistCollectionNew = usuario.getChecklistCollection();
            Collection<Empresa> empresaCollectionOld = persistentUsuario.getEmpresaCollection();
            Collection<Empresa> empresaCollectionNew = usuario.getEmpresaCollection();
            Collection<Servicio> servicioCollectionOld = persistentUsuario.getServicioCollection();
            Collection<Servicio> servicioCollectionNew = usuario.getServicioCollection();
            List<String> illegalOrphanMessages = null;
            for (Checklist checklistCollectionOldChecklist : checklistCollectionOld) {
                if (!checklistCollectionNew.contains(checklistCollectionOldChecklist)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Checklist " + checklistCollectionOldChecklist + " since its usuarioIdUsuario field is not nullable.");
                }
            }
            for (Empresa empresaCollectionOldEmpresa : empresaCollectionOld) {
                if (!empresaCollectionNew.contains(empresaCollectionOldEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empresa " + empresaCollectionOldEmpresa + " since its usuarioIdUsuario field is not nullable.");
                }
            }
            for (Servicio servicioCollectionOldServicio : servicioCollectionOld) {
                if (!servicioCollectionNew.contains(servicioCollectionOldServicio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Servicio " + servicioCollectionOldServicio + " since its usuarioIdUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoUsuarioIdTipoUsuarioNew != null) {
                tipoUsuarioIdTipoUsuarioNew = em.getReference(tipoUsuarioIdTipoUsuarioNew.getClass(), tipoUsuarioIdTipoUsuarioNew.getIdTipoUsuario());
                usuario.setTipoUsuarioIdTipoUsuario(tipoUsuarioIdTipoUsuarioNew);
            }
            Collection<Checklist> attachedChecklistCollectionNew = new ArrayList<Checklist>();
            for (Checklist checklistCollectionNewChecklistToAttach : checklistCollectionNew) {
                checklistCollectionNewChecklistToAttach = em.getReference(checklistCollectionNewChecklistToAttach.getClass(), checklistCollectionNewChecklistToAttach.getIdChecklist());
                attachedChecklistCollectionNew.add(checklistCollectionNewChecklistToAttach);
            }
            checklistCollectionNew = attachedChecklistCollectionNew;
            usuario.setChecklistCollection(checklistCollectionNew);
            Collection<Empresa> attachedEmpresaCollectionNew = new ArrayList<Empresa>();
            for (Empresa empresaCollectionNewEmpresaToAttach : empresaCollectionNew) {
                empresaCollectionNewEmpresaToAttach = em.getReference(empresaCollectionNewEmpresaToAttach.getClass(), empresaCollectionNewEmpresaToAttach.getIdEmpresa());
                attachedEmpresaCollectionNew.add(empresaCollectionNewEmpresaToAttach);
            }
            empresaCollectionNew = attachedEmpresaCollectionNew;
            usuario.setEmpresaCollection(empresaCollectionNew);
            Collection<Servicio> attachedServicioCollectionNew = new ArrayList<Servicio>();
            for (Servicio servicioCollectionNewServicioToAttach : servicioCollectionNew) {
                servicioCollectionNewServicioToAttach = em.getReference(servicioCollectionNewServicioToAttach.getClass(), servicioCollectionNewServicioToAttach.getIdServicio());
                attachedServicioCollectionNew.add(servicioCollectionNewServicioToAttach);
            }
            servicioCollectionNew = attachedServicioCollectionNew;
            usuario.setServicioCollection(servicioCollectionNew);
            usuario = em.merge(usuario);
            if (tipoUsuarioIdTipoUsuarioOld != null && !tipoUsuarioIdTipoUsuarioOld.equals(tipoUsuarioIdTipoUsuarioNew)) {
                tipoUsuarioIdTipoUsuarioOld.getUsuarioCollection().remove(usuario);
                tipoUsuarioIdTipoUsuarioOld = em.merge(tipoUsuarioIdTipoUsuarioOld);
            }
            if (tipoUsuarioIdTipoUsuarioNew != null && !tipoUsuarioIdTipoUsuarioNew.equals(tipoUsuarioIdTipoUsuarioOld)) {
                tipoUsuarioIdTipoUsuarioNew.getUsuarioCollection().add(usuario);
                tipoUsuarioIdTipoUsuarioNew = em.merge(tipoUsuarioIdTipoUsuarioNew);
            }
            for (Checklist checklistCollectionNewChecklist : checklistCollectionNew) {
                if (!checklistCollectionOld.contains(checklistCollectionNewChecklist)) {
                    Usuario oldUsuarioIdUsuarioOfChecklistCollectionNewChecklist = checklistCollectionNewChecklist.getUsuarioIdUsuario();
                    checklistCollectionNewChecklist.setUsuarioIdUsuario(usuario);
                    checklistCollectionNewChecklist = em.merge(checklistCollectionNewChecklist);
                    if (oldUsuarioIdUsuarioOfChecklistCollectionNewChecklist != null && !oldUsuarioIdUsuarioOfChecklistCollectionNewChecklist.equals(usuario)) {
                        oldUsuarioIdUsuarioOfChecklistCollectionNewChecklist.getChecklistCollection().remove(checklistCollectionNewChecklist);
                        oldUsuarioIdUsuarioOfChecklistCollectionNewChecklist = em.merge(oldUsuarioIdUsuarioOfChecklistCollectionNewChecklist);
                    }
                }
            }
            for (Empresa empresaCollectionNewEmpresa : empresaCollectionNew) {
                if (!empresaCollectionOld.contains(empresaCollectionNewEmpresa)) {
                    Usuario oldUsuarioIdUsuarioOfEmpresaCollectionNewEmpresa = empresaCollectionNewEmpresa.getUsuarioIdUsuario();
                    empresaCollectionNewEmpresa.setUsuarioIdUsuario(usuario);
                    empresaCollectionNewEmpresa = em.merge(empresaCollectionNewEmpresa);
                    if (oldUsuarioIdUsuarioOfEmpresaCollectionNewEmpresa != null && !oldUsuarioIdUsuarioOfEmpresaCollectionNewEmpresa.equals(usuario)) {
                        oldUsuarioIdUsuarioOfEmpresaCollectionNewEmpresa.getEmpresaCollection().remove(empresaCollectionNewEmpresa);
                        oldUsuarioIdUsuarioOfEmpresaCollectionNewEmpresa = em.merge(oldUsuarioIdUsuarioOfEmpresaCollectionNewEmpresa);
                    }
                }
            }
            for (Servicio servicioCollectionNewServicio : servicioCollectionNew) {
                if (!servicioCollectionOld.contains(servicioCollectionNewServicio)) {
                    Usuario oldUsuarioIdUsuarioOfServicioCollectionNewServicio = servicioCollectionNewServicio.getUsuarioIdUsuario();
                    servicioCollectionNewServicio.setUsuarioIdUsuario(usuario);
                    servicioCollectionNewServicio = em.merge(servicioCollectionNewServicio);
                    if (oldUsuarioIdUsuarioOfServicioCollectionNewServicio != null && !oldUsuarioIdUsuarioOfServicioCollectionNewServicio.equals(usuario)) {
                        oldUsuarioIdUsuarioOfServicioCollectionNewServicio.getServicioCollection().remove(servicioCollectionNewServicio);
                        oldUsuarioIdUsuarioOfServicioCollectionNewServicio = em.merge(oldUsuarioIdUsuarioOfServicioCollectionNewServicio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Checklist> checklistCollectionOrphanCheck = usuario.getChecklistCollection();
            for (Checklist checklistCollectionOrphanCheckChecklist : checklistCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Checklist " + checklistCollectionOrphanCheckChecklist + " in its checklistCollection field has a non-nullable usuarioIdUsuario field.");
            }
            Collection<Empresa> empresaCollectionOrphanCheck = usuario.getEmpresaCollection();
            for (Empresa empresaCollectionOrphanCheckEmpresa : empresaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Empresa " + empresaCollectionOrphanCheckEmpresa + " in its empresaCollection field has a non-nullable usuarioIdUsuario field.");
            }
            Collection<Servicio> servicioCollectionOrphanCheck = usuario.getServicioCollection();
            for (Servicio servicioCollectionOrphanCheckServicio : servicioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Servicio " + servicioCollectionOrphanCheckServicio + " in its servicioCollection field has a non-nullable usuarioIdUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoUsuario tipoUsuarioIdTipoUsuario = usuario.getTipoUsuarioIdTipoUsuario();
            if (tipoUsuarioIdTipoUsuario != null) {
                tipoUsuarioIdTipoUsuario.getUsuarioCollection().remove(usuario);
                tipoUsuarioIdTipoUsuario = em.merge(tipoUsuarioIdTipoUsuario);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        String query = "SELECT * FROM USUARIO WHERE TIPO_USUARIO_ID_TIPO_USUARIO = 2";
        List<Usuario> listEntrada = null;

        try {
            listEntrada = em.createNativeQuery(query, Usuario.class).getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        if (listEntrada == null) {
            listEntrada = new ArrayList<>();
        }
        return listEntrada;
    }

    public List<Usuario> findUsuariosEntitiesCliente() {
        return findUsuariosEntitiesCliente(true, -1, -1);
    }

    public List<Usuario> findUsuariosEntitiesCliente(int maxResults, int firstResult) {
        return findUsuariosEntitiesCliente(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuariosEntitiesCliente(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        String query = "SELECT * FROM USUARIO WHERE TIPO_USUARIO_ID_TIPO_USUARIO = 3";
        List<Usuario> listEntrada = null;

        try {
            listEntrada = em.createNativeQuery(query, Usuario.class).getResultList();
        } catch (Exception ex) {
            ex.getMessage();
        }
        if (listEntrada == null) {
            listEntrada = new ArrayList<>();
        }
        return listEntrada;
    }

    public Usuario findUsuario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<Usuario> login(String email, String clave) {
        EntityManager em = getEntityManager();
        //SELECT u FROM Usuarios u WHERE u.email = :email AND u.clave = :clave
        Query query = em.createNamedQuery("Usuario.findUser");
        query.setParameter("email", email);
        query.setParameter("clave", clave);
        List<Usuario> lista = query.getResultList();
        return lista;
    }

    public List<Usuario> buscarUsuario(String rut, TipoUsuario tipoUsuarioIdTipoUsuario) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Usuario.findByRutId");
        query.setParameter("rut", rut);
        query.setParameter("tipoUsuarioIdTipoUsuario", tipoUsuarioIdTipoUsuario);
        List<Usuario> lista = query.getResultList();
        return lista;
    }

    public boolean rutExistente(String rut, TipoUsuario tipoUsuarioIdTipoUsuario) {
        EntityManager em = getEntityManager();
        boolean valor;
        Query query = em.createNamedQuery("Usuario.findByRutId");
        query.setParameter("rut", rut);
        query.setParameter("tipoUsuarioIdTipoUsuario", tipoUsuarioIdTipoUsuario);
        List<Usuario> lista = query.getResultList();
        if (lista.isEmpty()) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public boolean emailExistente(String email) {
        EntityManager em = getEntityManager();
        boolean valor;
        Query query = em.createNamedQuery("Usuario.findByEmail");
        query.setParameter("email", email);
        List<Usuario> lista = query.getResultList();
        if (lista.isEmpty()) {
            valor = true;
        } else {
            valor = false;
        }
        return valor;
    }

    public boolean buscarCodigo(BigDecimal idUsuario) {
        EntityManager em = getEntityManager();
        boolean valor;
        try {
            Query query = em.createNamedQuery("Usuario.findByIdUsuario");
            query.setParameter("idUsuario", idUsuario);

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

    public String FormatearRUT(String rut) {
        int cont = 0;
        String format;
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        format = "-" + rut.substring(rut.length() - 1);
        for (int i = rut.length() - 2; i >= 0; i--) {
            format = rut.substring(i, i + 1) + format;
            cont++;
            if (cont == 3 && i != 0) {
                format = "." + format;
                cont = 0;
            }
        }
        return format;
    }
}

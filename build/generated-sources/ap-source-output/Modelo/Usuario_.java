package Modelo;

import Modelo.Checklist;
import Modelo.Empresa;
import Modelo.Servicio;
import Modelo.TipoUsuario;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-13T23:33:46")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> clave;
    public static volatile SingularAttribute<Usuario, BigInteger> estado;
    public static volatile CollectionAttribute<Usuario, Servicio> servicioCollection;
    public static volatile SingularAttribute<Usuario, String> paterno;
    public static volatile SingularAttribute<Usuario, Date> fechaModificacion;
    public static volatile SingularAttribute<Usuario, Date> fechaNacimiento;
    public static volatile SingularAttribute<Usuario, BigDecimal> idUsuario;
    public static volatile SingularAttribute<Usuario, String> direccion;
    public static volatile CollectionAttribute<Usuario, Checklist> checklistCollection;
    public static volatile SingularAttribute<Usuario, String> nombre;
    public static volatile SingularAttribute<Usuario, String> rut;
    public static volatile SingularAttribute<Usuario, String> materno;
    public static volatile CollectionAttribute<Usuario, Empresa> empresaCollection;
    public static volatile SingularAttribute<Usuario, BigInteger> celular;
    public static volatile SingularAttribute<Usuario, Date> fechaCreacion;
    public static volatile SingularAttribute<Usuario, BigInteger> telefono;
    public static volatile SingularAttribute<Usuario, String> email;
    public static volatile SingularAttribute<Usuario, TipoUsuario> tipoUsuarioIdTipoUsuario;

}
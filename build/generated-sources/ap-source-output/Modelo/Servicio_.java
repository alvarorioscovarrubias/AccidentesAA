package Modelo;

import Modelo.Contrato;
import Modelo.EstadoServicio;
import Modelo.TipoServicio;
import Modelo.Usuario;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-12T18:44:58")
@StaticMetamodel(Servicio.class)
public class Servicio_ { 

    public static volatile SingularAttribute<Servicio, Usuario> usuarioIdUsuario;
    public static volatile SingularAttribute<Servicio, TipoServicio> tipoServicioIdTipoServicio;
    public static volatile SingularAttribute<Servicio, BigDecimal> idServicio;
    public static volatile SingularAttribute<Servicio, Contrato> contratoIdContrato;
    public static volatile SingularAttribute<Servicio, Date> fechaServicio;
    public static volatile SingularAttribute<Servicio, EstadoServicio> estadoServicioIdEstServ;

}
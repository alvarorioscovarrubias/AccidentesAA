package Modelo;

import Modelo.Servicio;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-13T23:33:46")
@StaticMetamodel(EstadoServicio.class)
public class EstadoServicio_ { 

    public static volatile SingularAttribute<EstadoServicio, String> descripcion;
    public static volatile SingularAttribute<EstadoServicio, Servicio> servicio;
    public static volatile SingularAttribute<EstadoServicio, BigDecimal> id;

}
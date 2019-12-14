package Modelo;

import Modelo.Servicio;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-12T18:44:58")
@StaticMetamodel(TipoServicio.class)
public class TipoServicio_ { 

    public static volatile SingularAttribute<TipoServicio, String> descripcion;
    public static volatile SingularAttribute<TipoServicio, Servicio> servicio;
    public static volatile SingularAttribute<TipoServicio, BigDecimal> idTipoServicio;

}
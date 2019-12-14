package Modelo;

import Modelo.Pago;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-13T23:33:46")
@StaticMetamodel(EstadoPago.class)
public class EstadoPago_ { 

    public static volatile SingularAttribute<EstadoPago, String> descripcion;
    public static volatile SingularAttribute<EstadoPago, BigDecimal> idEstadoPago;
    public static volatile CollectionAttribute<EstadoPago, Pago> pagoCollection;

}
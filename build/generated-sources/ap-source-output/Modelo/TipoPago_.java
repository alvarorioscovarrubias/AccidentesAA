package Modelo;

import Modelo.Pago;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-13T23:33:46")
@StaticMetamodel(TipoPago.class)
public class TipoPago_ { 

    public static volatile SingularAttribute<TipoPago, String> descripcion;
    public static volatile SingularAttribute<TipoPago, BigDecimal> idTipoPago;
    public static volatile CollectionAttribute<TipoPago, Pago> pagoCollection;

}
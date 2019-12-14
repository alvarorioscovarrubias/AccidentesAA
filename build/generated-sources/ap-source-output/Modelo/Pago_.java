package Modelo;

import Modelo.Cuota;
import Modelo.EstadoPago;
import Modelo.TipoPago;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-12T18:44:58")
@StaticMetamodel(Pago.class)
public class Pago_ { 

    public static volatile SingularAttribute<Pago, Cuota> cuotaIdCuota;
    public static volatile SingularAttribute<Pago, TipoPago> tipoPagoIdTipoPago;
    public static volatile SingularAttribute<Pago, BigDecimal> idPago;
    public static volatile SingularAttribute<Pago, Date> fechaPago;
    public static volatile SingularAttribute<Pago, EstadoPago> estadoPagoIdEstadoPago;

}
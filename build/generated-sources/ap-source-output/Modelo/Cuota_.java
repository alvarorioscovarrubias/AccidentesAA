package Modelo;

import Modelo.CargoExtra;
import Modelo.Contrato;
import Modelo.Pago;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-14T00:10:52")
@StaticMetamodel(Cuota.class)
public class Cuota_ { 

    public static volatile SingularAttribute<Cuota, BigInteger> monto;
    public static volatile CollectionAttribute<Cuota, CargoExtra> cargoExtraCollection;
    public static volatile SingularAttribute<Cuota, BigDecimal> idCuota;
    public static volatile SingularAttribute<Cuota, Date> fechaVencimiento;
    public static volatile SingularAttribute<Cuota, Contrato> contratoIdContrato;
    public static volatile SingularAttribute<Cuota, Date> fechaPago;
    public static volatile CollectionAttribute<Cuota, Pago> pagoCollection;

}
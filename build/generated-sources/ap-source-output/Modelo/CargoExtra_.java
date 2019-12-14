package Modelo;

import Modelo.Cuota;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-12T18:44:58")
@StaticMetamodel(CargoExtra.class)
public class CargoExtra_ { 

    public static volatile SingularAttribute<CargoExtra, BigDecimal> idCargoExtra;
    public static volatile SingularAttribute<CargoExtra, String> descripcion;
    public static volatile SingularAttribute<CargoExtra, Cuota> cuotaIdCuota;
    public static volatile SingularAttribute<CargoExtra, BigInteger> monto;

}
package Modelo;

import Modelo.Contrato;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-12T18:44:58")
@StaticMetamodel(EstadoContrato.class)
public class EstadoContrato_ { 

    public static volatile SingularAttribute<EstadoContrato, String> descripcion;
    public static volatile SingularAttribute<EstadoContrato, BigDecimal> idEstCon;
    public static volatile SingularAttribute<EstadoContrato, Contrato> contrato;

}
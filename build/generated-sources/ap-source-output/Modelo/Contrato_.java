package Modelo;

import Modelo.Cuota;
import Modelo.Empresa;
import Modelo.EstadoContrato;
import Modelo.Servicio;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-12T18:44:58")
@StaticMetamodel(Contrato.class)
public class Contrato_ { 

    public static volatile SingularAttribute<Contrato, Empresa> empresaIdEmpresa;
    public static volatile CollectionAttribute<Contrato, Servicio> servicioCollection;
    public static volatile SingularAttribute<Contrato, Date> fechaInicio;
    public static volatile SingularAttribute<Contrato, Date> fechaTermino;
    public static volatile SingularAttribute<Contrato, EstadoContrato> estadoContratoIdEstCon;
    public static volatile SingularAttribute<Contrato, BigDecimal> idContrato;
    public static volatile CollectionAttribute<Contrato, Cuota> cuotaCollection;

}
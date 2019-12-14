package Modelo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-12T18:44:58")
@StaticMetamodel(Empresa.class)
public class Empresa_ { 

    public static volatile SingularAttribute<Empresa, String> razonSocial;
    public static volatile SingularAttribute<Empresa, Date> fechaCreado;
    public static volatile SingularAttribute<Empresa, Date> fechaModificado;
    public static volatile SingularAttribute<Empresa, String> direccion;
    public static volatile SingularAttribute<Empresa, BigDecimal> idEmpresa;
    public static volatile SingularAttribute<Empresa, String> rutRepresentante;
    public static volatile SingularAttribute<Empresa, BigInteger> telefono;
    public static volatile SingularAttribute<Empresa, String> nombre;

}
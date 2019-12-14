package Modelo;

import Modelo.ChecklistItem;
import Modelo.Usuario;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-14T00:10:52")
@StaticMetamodel(Checklist.class)
public class Checklist_ { 

    public static volatile SingularAttribute<Checklist, Usuario> usuarioIdUsuario;
    public static volatile SingularAttribute<Checklist, BigDecimal> idChecklist;
    public static volatile CollectionAttribute<Checklist, ChecklistItem> checklistItemCollection;
    public static volatile SingularAttribute<Checklist, String> nombre;

}
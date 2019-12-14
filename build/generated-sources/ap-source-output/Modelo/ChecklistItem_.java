package Modelo;

import Modelo.Checklist;
import Modelo.Empresa;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-12T18:44:58")
@StaticMetamodel(ChecklistItem.class)
public class ChecklistItem_ { 

    public static volatile SingularAttribute<ChecklistItem, Character> estado;
    public static volatile SingularAttribute<ChecklistItem, Empresa> empresaIdEmpresa;
    public static volatile SingularAttribute<ChecklistItem, Checklist> checklistIdChecklist;
    public static volatile SingularAttribute<ChecklistItem, BigDecimal> idChecklistItem;
    public static volatile SingularAttribute<ChecklistItem, String> pregunta;

}
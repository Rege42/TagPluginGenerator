package ru.rtec.cf2.plugin.tag;

import ru.rtec.cf2.plugin.model.objects.Parameter;
import ru.rtec.cf2.plugin.model.objects.prop.CF2StringPropertyDescriptor;
import ru.rtec.cf2.plugin.model.objects.prop.ParameterPropertyGroupDescriptor;

/**
 * Группа свойств для хранения альтернативного имени параметра.
 * В отличие от {@link ru.rtec.cf2.plugin.tag.ManualTagComment} хранит только имя, а не полный комментарий для
 * параметра.
 * 
 * @author o.lemina
 *
 */
public class AltParameterNamePropertyGroup extends ParameterPropertyGroupDescriptor {
  
  public static final int NAME_PROPERTY_ID = 1;
  
  public AltParameterNamePropertyGroup() {
    super();
    addProperty(new CF2StringPropertyDescriptor("Имя", "Имя параметра", NAME_PROPERTY_ID, ""));
  }
  
  @Override
  public boolean canBeBoundTo(Parameter par) {
    return true;
  }
  
  @Override
  public int getVersion() {
    return 1;
  }
  
  @Override
  public String getName() {
    return "Общие: Альтернативное имя параметра";
  }
  
  @Override
  public String getDescription() {
    return "Группа свойств для хранения альтернативного имени параметра";
  }
  
}

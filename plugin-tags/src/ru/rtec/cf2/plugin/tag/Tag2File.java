package ru.rtec.cf2.plugin.tag;

import ru.g4.io3.ui.resources.IResourceBundleWrapper;
import ru.rtec.cf2.ResourcesStorage;
import ru.rtec.cf2.plugin.model.objects.Parameter;
import ru.rtec.cf2.plugin.model.objects.prop.ParameterPropertyGroupDescriptor;

/**
 * Группа свойства маркирующая параметр как параметр значение которого должно
 * сохраняться в файл модулем Tag2File сервера данных
 * <p>
 * <b>Author</b> : Denis Tsyplakov <b> Date</b>: 09.06.2005 17:48:08
 * </p>
 */
public class Tag2File extends ParameterPropertyGroupDescriptor {
  private IResourceBundleWrapper resourceBundleWrapper = ResourcesStorage.getBundle(getClass());
  
  public Tag2File() {
  }
  
  public String getDescription() {
    return resourceBundleWrapper.getString("Tag2FilePG_Desc_Tag2File");
  }
  
  public String getName() {
    return resourceBundleWrapper.getString("Tag2FilePG_Name_Tag2File");
  }
  
  public int getVersion() {
    return 1;
  }
  
  public boolean canBeBoundTo(Parameter par) {
    return true;
  }
}

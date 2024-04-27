package ru.rtec.cf2.plugin.tag;

import ru.g4.io3.ui.resources.IResourceBundleWrapper;
import ru.rtec.cf2.ResourcesStorage;
import ru.rtec.cf2.plugin.model.objects.Parameter;
import ru.rtec.cf2.plugin.model.objects.prop.CF2StringPropertyDescriptor;
import ru.rtec.cf2.plugin.model.objects.prop.ParameterPropertyGroupDescriptor;

/**
 * Группа свойств параметра говрящая что для данного параметр надо брать не
 * автоматически генерируемый комментарий, а значение совйтва из данной группы.
 * <p>
 * <b>Author</b> : Denis Tsyplakov <b> Date</b>: 25.07.2005 16:19:39
 * </p>
 */
public class ManualTagComment extends ParameterPropertyGroupDescriptor {
  private IResourceBundleWrapper resourceBundleWrapper;
  
  public ManualTagComment() {
    resourceBundleWrapper = ResourcesStorage.getBundle(getClass());
    addProperty(new CF2StringPropertyDescriptor(
                                                resourceBundleWrapper
                                                    .getString("ManualTagCommentPG_PropertyName_CommentParameter"),
                                                resourceBundleWrapper
                                                    .getString("ManualTagCommentPG_PropertyDesc_CommentParameter"), 1,
                                                null));
  }
  
  public int getVersion() {
    return 1;
  }
  
  public String getName() {
    return resourceBundleWrapper.getString("ManualTagCommentPG_Name_ManualTagComment");
  }
  
  public String getDescription() {
    return resourceBundleWrapper.getString("ManualTagCommentPG_Desc_ManualTagComment");
  }
  
  /**
   * Проверка, можно ли привязывать группу свойств в параметру. Метод
   * вызывается при привязке и при смене атрибутов параметра. Проверка
   * выполняется вне конекста открытой сессии, соответственно можно
   * безбоязненно проверять только атрибуты непосредственно саомго параметра.
   *
   * @param par проверяемый параметр.
   * @return true если можно.
   */
  public boolean canBeBoundTo(Parameter par) {
    return true;
  }
  
}

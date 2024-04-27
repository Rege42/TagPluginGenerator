package ru.rtec.cf2.plugin.tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import ru.rtec.cf2.CF2Utils;
import ru.rtec.cf2.plugin.model.objects.CObject;
import ru.rtec.cf2.plugin.model.objects.ModelView;
import ru.rtec.cf2.plugin.model.objects.Parameter;
import ru.rtec.cf2.plugin.model.objects.ParameterOrigin;
import ru.rtec.cf2.plugin.model.objects.prop.predefined.TUParameter;

/**
 * Объект генерит список тегов для переданных параметров в виде: &lt;Tag
 * name>=&lt;Tag type> ('Boolean'|'Float'|'Integer'|'String') ';' &lt;delivery
 * type>('in'|'in-tu') .
 *
 * <p>
 * <b>Author</b> : Anna Nesterenko <b> Date</b>: 09.06.2005 12:26:33
 * </p>
 */
public class TagListGenerator {
  
  /**
   * Таблица описаний тегов. Ключ - имя тега, значение - набор свойств тега.
   */
  private Map<String, TagListContainer> tags = new HashMap<String, TagListGenerator.TagListContainer>();
  
  
  
  /**
   * Добавляет строку в список тегов.
   *
   * @param object объект
   * @param parameter параметр
   * @param modelView вид на модель
   * @param parameterTagName имя тега параметра
   */
  public void add(CObject object, Parameter parameter, ModelView modelView, String parameterTagName) {
    boolean isTU = isTU(object, parameter, modelView);
    
    checkDoubleAndAdd(new TagListContainer(parameterTagName, object, parameter, isTU, parameter.getOrigin()));
  }
  
  /**
   * Добавляет тег с явным указанием направления тега и флагом игнорирования ТУ.
   * 
   * @param object объект
   * @param parameter параметр
   * @param modelView
   * @param parameterTagName имя тега
   * @param origin направление тега
   * @param ignoreTU флаг игнорирования признака ТУ у тега
   */
  public void addTag(CObject object, Parameter parameter, ModelView modelView, String parameterTagName,
      ParameterOrigin origin, boolean ignoreTU) {
    boolean isTU = !ignoreTU && isTU(object, parameter, modelView);
    checkDoubleAndAdd(new TagListContainer(parameterTagName, object, parameter, isTU, origin));
  }
  
  /**
   * Добавляет строку в список тегов инверсно, т.е. для сервера данных.
   *
   * @param parameter параметр
   * @param parameterTagName имя тега параметра
   */
  public void addInverse(Parameter parameter, String parameterTagName) {
    ParameterOrigin origin;
    switch (parameter.getOrigin()) {
      case IN:
        origin = ParameterOrigin.OUT;
        break;
      case OUT:
        origin = ParameterOrigin.IN;
        break;
      case IN_OUT:
        origin = ParameterOrigin.IN_OUT;
        break;
      default:
        origin = parameter.getOrigin();
    }
    checkDoubleAndAdd(new TagListContainer(parameterTagName, null, parameter, false, origin));
  }
  
  /**
   * Проверяет наличие дубля в tag-list и добавляет тег. Если дубль есть, сливает 2 контейнера.
   * 
   * @param tagContainer описатель тега, который надо добавить
   */
  private void checkDoubleAndAdd(TagListContainer tagContainer) {
    if (tags.containsKey(tagContainer.tagName)) {
      TagListContainer existsTagContainer = tags.get(tagContainer.tagName);
      // проверяем origin
      existsTagContainer.origin = mergeOrigin(existsTagContainer.origin, tagContainer.origin);
      // проверяем TU
      existsTagContainer.isTU = mergeTUFlag(existsTagContainer.isTU, tagContainer.isTU);
    } else {
      tags.put(tagContainer.tagName, tagContainer);
    }
  }
  
  private boolean mergeTUFlag(boolean tuFlag1, boolean tuFlag2) {
    boolean mergedTU = tuFlag1;
    if (tuFlag1 != tuFlag2) {
      mergedTU = true;
    }
    return mergedTU;
  }
  
  private ParameterOrigin mergeOrigin(ParameterOrigin o1, ParameterOrigin o2) {
    ParameterOrigin mergedOrigin;
    switch (o1) {
      case IN:
        if (o2.equals(ParameterOrigin.IN)) {
          mergedOrigin = ParameterOrigin.IN;
        } else {
          mergedOrigin = ParameterOrigin.IN_OUT;
        }
        break;
      case IN_OUT:
        mergedOrigin = ParameterOrigin.IN_OUT;
        break;
      case OUT:
        if (o2.equals(ParameterOrigin.OUT)) {
          mergedOrigin = ParameterOrigin.OUT;
        } else {
          mergedOrigin = ParameterOrigin.IN_OUT;
        }
        break;
      default:
        mergedOrigin = ParameterOrigin.IN_OUT;
    }
    return mergedOrigin;
  }
  
  /**
   * Возвращает строку, содержащую накопленный список тегов.
   *
   * @return список тегов в виде строки.
   */
  public String getTagList() {
    Properties properties = new Properties() {
      
      private static final long serialVersionUID = 1L;

      /**
       * Перекрываем метод, чтобы отсортировать список ключей и получить в tag-list сортированный список тегов.
       * Именно keys() вызывается в методе {@link Properties#store} для пребора всех элементов
       */
      @Override
      public synchronized Enumeration<Object> keys() {
        Vector<Object> keySet = new Vector<Object>(super.keySet());
        Collections.sort(keySet, new Comparator<Object>() {
          @Override
          public int compare(Object o1, Object o2) {
            if (o1 instanceof String && o2 instanceof String) {
              return ((String) o1).compareTo((String) o2);
            } else {
              return 0;
            }
          }
        });
        return keySet.elements();
      }
    };
    
    
    ByteArrayOutputStream bo = new ByteArrayOutputStream();
    for (String tagName : tags.keySet()) {
      TagListContainer tagListContainer = tags.get(tagName);
      properties.put(tagName, getTagListRow(tagListContainer));
    }
    try {
      properties.store(bo, "Generated with cf2" + " user: " + System.getProperty("user.name"));
      return new String(bo.toByteArray());
    } catch (IOException e) {
      return CF2Utils.dumpThrowable(e);
    }
  }
  
  private String getTagListRow(TagListContainer tagContainer) {
    String tagRow = tagContainer.parameter.getType().getTagListTypeName();
    if (tagContainer.origin.isIn()) {
      tagRow += ";in";
    }
    if (tagContainer.isTU) {
      tagRow += "-tu";
    }
    return tagRow;
  }
  
  private boolean isTU(CObject object, Parameter parameter, ModelView modelView) {
    return (ParameterOrigin.IN.equals(parameter.getOrigin())) || (ParameterOrigin.IN_OUT.equals(parameter.getOrigin()))
        && modelView.hasPropertyGroup(object, parameter, TUParameter.class);
  }
  
  private static class TagListContainer {
    private String tagName;
    private Object object;
    private Parameter parameter;
    
    private boolean isTU;
    private ParameterOrigin origin;
    
    public TagListContainer(String tagName, Object object, Parameter parameter, boolean isTU, ParameterOrigin origin) {
      this.tagName = tagName;
      this.object = object;
      this.parameter = parameter;
      this.isTU = isTU;
      this.origin = origin;
    }
  }
}
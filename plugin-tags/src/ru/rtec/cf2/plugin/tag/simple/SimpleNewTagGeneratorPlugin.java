package ru.rtec.cf2.plugin.tag.simple;

import org.hibernate.Session;
import ru.g4.io3.ui.resources.IResourceBundleWrapper;
import ru.rtec.cf2.CF2Utils;
import ru.rtec.cf2.IApplicationContext;
import ru.rtec.cf2.ResourcesStorage;
import ru.rtec.cf2.pi.IPlugin;
import ru.rtec.cf2.pi.PluginVersion;
import ru.rtec.cf2.plugin.model.objects.COClass;
import ru.rtec.cf2.plugin.model.objects.CObject;
import ru.rtec.cf2.plugin.model.objects.Parameter;
import ru.rtec.cf2.plugin.tag.ETagNameError;
import ru.rtec.cf2.plugin.tag.ITagGenerator;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class SimpleNewTagGeneratorPlugin implements IPlugin, ITagGenerator {

  private final String tagSeparator = "??";  //сепаратор тегов

  private final String numberSeparator = "_"; //сепаратор номера тэга

  private final String format = "00000";    //формат, где количество нулей отвечает за разряды

  private Logger log = Logger.getLogger("ru.rtec.cf2.plugin.tag.simple.SimpleNewTagGeneratorPlugin");
  
  private IResourceBundleWrapper resourceBundleWrapper = ResourcesStorage.getBundle(getClass());

  /**
   * Получить описание плагина.
   *
   * @return описание плагина.
   */
  public String getDescription() {
    return resourceBundleWrapper.getString("SimpleNewTagGeneratorPlugin_Name_SimpleTagCommentGeneratorPlugin");
  }
  
  /**
   * Установить контекст приложения. Метод нужен почти всем плагинам, поэтому
   * он данном интерфейсе.
   *
   * @param context контекст приложения.
   */
  public void setApplicationContext(IApplicationContext context) {
    // ничего не делает
  }
  
  /**
   * Получить версию плагина.
   *
   * @return версия плагина.
   */
  public PluginVersion getVersion() {
    return new PluginVersion(0, 1);
  }
  
  /**
   * Получить имя тега для указанного параметра. Метод всегда вызывается в
   * контексте открытой сессии, которая к тому-же передается в качестве
   * параметра. В принципе параметр может быть для объекта скрытым, но для
   * генерации тега это неважно.
   *
   * @param session сессия.
   * @param par параметр.
   * @param object объект к которому привязан параметр.
   * @return имя тега.
   * @throws ETagNameError По той или иной причине имя
   *         тега сгенерировать нельзя. Может включать в качестве
   *         вложенной причины ошибку базы данных.
   */
  public String getTagName(Session session, CObject object, Parameter par) throws ETagNameError {
    try {
      return getGroupName(session, object) + tagSeparator + par.getTagSuffix().toUpperCase();
    } catch (Throwable ex) {
      log.info(CF2Utils.dumpThrowable(ex));
      return resourceBundleWrapper.getStringFormat("SimpleNewTagGeneratorPlugin_Error", ex.toString());
    }
  }
  
  /**
   * Получить имя группы. Имя группы это составляющая имени тега относящаяся к
   * объекту. Алгоритм генерации симметричен алгоритму генерации имени тега.
   *
   * @param session сессия
   * @param object параметр
   * @return имя группы
   * @throws ETagNameError по той или иной причине имя тега сгенерировать
   *         нельзя. Может включать в качестве вложенной причины ошибку
   *         базы данных.
   */
  public String getGroupName(Session session, CObject  object) throws ETagNameError {
    try {
      StringBuffer sb = new StringBuffer();
      while (object != null) {
        COClass cls = object.getHierarchyElement().getElementClass();
        COClass parentWithPrefix = cls.findNearestPrefixParent();
        if (parentWithPrefix != null) {
          if (!parentWithPrefix.getOnlySuffixInTagName()) {
            sb.insert(0, formatTagNumber(object.getTechNumber()));
            sb.insert(0, numberSeparator);
          }
          sb.insert(0, parentWithPrefix.getTagPrefix());
          sb.insert(0, tagSeparator);
        }
        object = object.getParent();
      }
      return sb.toString().toUpperCase();
      
    } catch (Throwable ex) {
      log.info(CF2Utils.dumpThrowable(ex));
      return resourceBundleWrapper.getStringFormat("SimpleNewTagGeneratorPlugin_Error", ex.toString());
    }
  }

  // функция форматирования номера тэга
  private String formatTagNumber(String techNumber) {

    Integer value = Integer.valueOf(techNumber);              //превращаем строку в число
    DecimalFormat decimalFormat = new DecimalFormat(format);  //задаем формат представления числа
    return decimalFormat.format(value);                       //передаем форматированное число
  }
}

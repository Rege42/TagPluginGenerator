package ru.rtec.cf2.plugin.tag.simple;

import java.util.logging.Logger;

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

/**
 * Простейший генератор имен тегов.
 * <p>
 * <b>Author</b> : Denis Tsyplakov <b> Date</b>: 13.05.2005 16:23:01
 * </p>
 */
public class SimpleWithSeparatorTagGeneratorPlugin implements IPlugin, ITagGenerator {
  private Logger log = Logger.getLogger("ru.rtec.cf2.plugin.tag.simple.SimpleTagGeneratorPlugin");
  
  private IResourceBundleWrapper resourceBundleWrapper = ResourcesStorage.getBundle(getClass());
  
  /**
   * Получить описание плагина.
   * 
   * @return описание плагина.
   */
  @Override
  public String getDescription() {
    return resourceBundleWrapper.getString("SimpleTagGeneratorPlugin_Name_SimpleTagCommentGeneratorPlugin");
  }
  
  /**
   * Установить контекст приложения. Метод нужен почти всем плагинам, поэтому
   * он данном интерфейсе.
   * 
   * @param context контекст приложения.
   */
  @Override
  public void setApplicationContext(IApplicationContext context) {
    // ничего не делает
  }
  
  /**
   * Получить версию плагина.
   * 
   * @return версия плагина.
   */
  @Override
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
   * @throws ru.rtec.cf2.plugin.tag.ETagNameError По той или иной причине имя
   *         тега сгенерировать нельзя. Может включать в качестве
   *         вложенной причины ошибку базы данных.
   */
  @Override
  public String getTagName(Session session, CObject object, Parameter par) throws ETagNameError {
    try {
      return getGroupName(session, object) + "#" + par.getTagSuffix().toUpperCase();
    } catch (Throwable ex) {
      log.info(CF2Utils.dumpThrowable(ex));
      return resourceBundleWrapper.getStringFormat("SimpleTagGeneratorPlugin_Error", ex.toString());
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
  @Override
  public String getGroupName(Session session, CObject object) throws ETagNameError {
    try {
      StringBuffer sb = new StringBuffer();
      while (object != null) {
        COClass cls = object.getHierarchyElement().getElementClass();
        COClass parentWithPrefix = cls.findNearestPrefixParent();
        if (parentWithPrefix != null) {
          if (!parentWithPrefix.getOnlySuffixInTagName()) {
            sb.insert(0, object.getTechNumber());
            sb.insert(0, "_");
          }
          sb.insert(0, parentWithPrefix.getTagPrefix());
          sb.insert(0, "#");
        }
        object = object.getParent();
      }
      return sb.toString().toUpperCase();
      
    } catch (Throwable ex) {
      log.info(CF2Utils.dumpThrowable(ex));
      return resourceBundleWrapper.getStringFormat("SimpleTagGeneratorPlugin_Error", ex.toString());
    }
  }
}

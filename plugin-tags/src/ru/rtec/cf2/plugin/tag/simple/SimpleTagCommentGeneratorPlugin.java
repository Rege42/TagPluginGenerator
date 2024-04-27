package ru.rtec.cf2.plugin.tag.simple;

import org.hibernate.Session;

import ru.g4.io3.ui.resources.IResourceBundleWrapper;
import ru.rtec.cf2.CF2Utils;
import ru.rtec.cf2.IApplicationContext;
import ru.rtec.cf2.ResourcesStorage;
import ru.rtec.cf2.pi.IPlugin;
import ru.rtec.cf2.pi.PluginVersion;
import ru.rtec.cf2.plugin.model.objects.*;
import ru.rtec.cf2.plugin.tag.ETagNameError;
import ru.rtec.cf2.plugin.tag.ITagCommentGenerator;

import java.util.logging.Logger;

/**
 * Плагин, который генерит комментарии к тегам.
 * <p>
 * <b>Author</b> : Anna Nesterenko <b> Date</b>: 17.05.2005 14:51:04
 * </p>
 */
public class SimpleTagCommentGeneratorPlugin implements IPlugin, ITagCommentGenerator {
  private Logger log = Logger.getLogger("ru.rtec.cf2.plugin.tag.simple.SimpleTagCommentGeneratorPlugin");
  
  private IResourceBundleWrapper resourceBundleWrapper = ResourcesStorage.getBundle(getClass());
  
  /**
   * Получить описание плагина.
   *
   * @return описание плагина.
   */
  public String getDescription() {
    return resourceBundleWrapper.getString("SimpleTagCommentGeneratorPlugin_Name_SimpleTagCommentGeneratorPlugin");
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
    return new PluginVersion(1, 0);
  }
  
  /**
   * Получить комментарий тега для указанного параметра. Метод всегда
   * вызывается в контексте открытой сессии, которая к тому-же передается в
   * качестве параметра. В принципе параметр может быть для объекта скрытым,
   * но для генерации тега это неважно.
   *
   * @param session сессия.
   * @param par параметр.
   * @param object объект к которому привязан параметр.
   * @return комментарий тега.
   * @throws ru.rtec.cf2.plugin.tag.ETagNameError По той или иной причине
   *         комментарий тега сгенерировать нельзя. Может включать в
   *         качестве вложенной причины ошибку базы данных.
   */
  public String getTagComment(Session session, CObject object, Parameter par) throws ETagNameError {
    try {
      return (getGroupComment(session, object) + " " + par.getName()).trim();
    } catch (Throwable ex) {
      log.info(CF2Utils.dumpThrowable(ex));
      return resourceBundleWrapper.getStringFormat("SimpleTagCommentGeneratorPlugin_Error", ex.toString());
    }
  }
  
  /**
   * Получить комментарий группы.
   *
   * @param session сессия.
   * @param object объект представляющий группу.
   * @return комментарий для группы.
   * @throws ru.rtec.cf2.plugin.tag.ETagNameError По той или иной причине
   *         комментарий для группы сгенерировать нельзя. Может включать в
   *         качестве вложенной причины ошибку базы данных.
   */
  public String getGroupComment(Session session, CObject object) throws ETagNameError {
    try {
      StringBuffer sb = new StringBuffer();
      while (object != null) {
        COClass cls = object.getHierarchyElement().getElementClass();
        COClass parentWithPrefix = cls.findNearestPrefixParent();
        if (parentWithPrefix != null) {
          String name = object.getName();
          if (name != null && !parentWithPrefix.getOnlySuffixInTagName()) {
            sb.insert(0, " " + name);
            
          }
          // sb.insert(0, parentWithPrefix.getTagPrefix());
        }
        object = object.getParent();
      }
      return sb.toString().trim();
    } catch (Throwable ex) {
      log.info(CF2Utils.dumpThrowable(ex));
      return resourceBundleWrapper.getStringFormat("SimpleTagCommentGeneratorPlugin_Error", ex.toString());
    }
  }
}
package ru.rtec.cf2.plugin.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;

import ru.g4.io3.ui.resources.IResourceBundleWrapper;
import ru.rtec.cf2.IApplicationContext;
import ru.rtec.cf2.ResourcesStorage;
import ru.rtec.cf2.pi.ICompose;
import ru.rtec.cf2.pi.IPlugin;
import ru.rtec.cf2.pi.PluginVersion;
import ru.rtec.cf2.plugin.model.objects.CObject;
import ru.rtec.cf2.plugin.model.objects.Parameter;
import ru.rtec.cf2.plugin.model.objects.prop.IPropertyGroupPlugin;
import ru.rtec.cf2.plugin.model.objects.prop.PropertyGroupDescriptor;

/**
 * Класс плагина являющегося точкой входа для генераторов тегов.
 * <p>
 * <b>Author</b> : Denis Tsyplakov <b> Date</b>: 10.05.2005 17:13:07
 * </p>
 */
public class TagPlugin implements IPlugin, ICompose, ITags, IPropertyGroupPlugin {
  IApplicationContext context;
  
  Logger log = Logger.getLogger(TagPlugin.class.getName());
  
  private IResourceBundleWrapper resourceBundleWrapper = ResourcesStorage.getBundle(getClass());
  
  /**
   * Текущий используемый генератор тегов.
   */
  ITagGenerator tagGenerator;
  
  /**
   * Текущий используемый генератор комментариев к тегам.
   */
  ITagCommentGenerator tagCommentGenerator;
  
  /**
   * Получить описание плагина.
   *
   * @return описание плагина.
   */
  public String getDescription() {
    return resourceBundleWrapper.getString("TagPlugin_Desc_TagPlugin");
  }
  
  /**
   * Установить контекст приложения. Метод нужен почти всем плагинам, поэтому
   * он данном интерфейсе.
   *
   * @param context контекст приложения.
   */
  public void setApplicationContext(IApplicationContext context) {
    this.context = context;
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
   * Выполнить операцию связывания.
   */
  public void doCompose() {
    tagGenerator = (ITagGenerator) context.findPlugin(ITagGenerator.class);
    if (tagGenerator == null) {
      log.info(resourceBundleWrapper.getString("TagPlugin_NotFoundTagGenerator"));
    } else {
      log.info(resourceBundleWrapper.getStringFormat("TagPlugin_TagGenerator", tagGenerator.getClass().getName()));
    }
    tagCommentGenerator = (ITagCommentGenerator) context.findPlugin(ITagCommentGenerator.class);
    if (tagCommentGenerator == null) {
      log.info(resourceBundleWrapper.getString("TagPlugin_NotFoundCommentGenerator"));
    } else {
      log.info(resourceBundleWrapper.getStringFormat("TagPlugin_CommentGenerator", tagCommentGenerator
          .getClass()
          .getName()));
    }
  }
  
  /**
   * Получить имя тега для указанного параметра. Метод всегда вызывается в
   * контексте открытой сессии, которая к тому-же передается в качестве
   * параметра. В принципе параметр может быть для объекта скрытым, но для
   * генерации тега это неважно. Вопрос на самом деле переадресуется текущему
   * генератору тегов.
   *
   * @param session сессия.
   * @param par параметр.
   * @param object объект к которому привязан параметр.
   * @return имя тега.
   * @throws ETagNameError По той или иной причине имя тега сгенерировать
   *         нельзя. Может включать в качестве вложенной причины ошибку
   *         базы данных.
   */
  public String getTagName(Session session, CObject object, Parameter par) throws ETagNameError {
    if (tagGenerator == null) {
      throw new ETagNameError(resourceBundleWrapper.getString("TagPlugin_NotFoundTagGenerator"));
    }
    object = session.contains(object) ? object : (CObject) session.get(CObject.class, object.getId());
    par = session.contains(par) ? par : (Parameter) session.get(Parameter.class, par.getId());
    return tagGenerator.getTagName(session, object, par);
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
  public String getGroupName(Session session, CObject object) throws ETagNameError {
    if (tagGenerator == null) {
      throw new ETagNameError(resourceBundleWrapper.getString("TagPlugin_NotFoundTagGenerator"));
    }
    object = session.contains(object) ? object : (CObject) session.get(CObject.class, object.getId());
    return tagGenerator.getGroupName(session, object);
  }
  
  /**
   * Получить комментарий тега для указанного параметра. Метод всегда
   * вызывается в контексте открытой сессии, которая к тому-же передается в
   * качестве параметра. В принципе параметр может быть для объекта скрытым,
   * но для генерации тега это неважно. Вопрос на самом деле переадресуется
   * текущему генератору тегов.
   *
   * @param session сессия.
   * @param par параметр.
   * @param object объект к которому привязан параметр.
   * @return комментарий для тега.
   * @throws ETagNameError По той или иной причине комментарий для тега
   *         сгенерировать нельзя. Может включать в качестве вложенной
   *         причины ошибку базы данных.
   */
  public String getTagComment(Session session, CObject object, Parameter par) throws ETagNameError {
    if (tagCommentGenerator == null) {
      throw new ETagNameError(resourceBundleWrapper.getString("TagPlugin_NotFoundCommentGenerator"));
    }
    object = session.contains(object) ? object : (CObject) session.get(CObject.class, object.getId());
    par = session.contains(par) ? par : (Parameter) session.get(Parameter.class, par.getId());
    return tagCommentGenerator.getTagComment(session, object, par);
  }
  
  /**
   * Получить комментарий группы.
   *
   * @param session сессия.
   * @param object объект представляющий группу.
   * @return комментарий для группы.
   * @throws ETagNameError По той или иной причине комментарий для группы
   *         сгенерировать нельзя. Может включать в качестве вложенной
   *         причины ошибку базы данных.
   */
  public String getGroupComment(Session session, CObject object) throws ETagNameError {
    if (tagCommentGenerator == null) {
      throw new ETagNameError(resourceBundleWrapper.getString("TagPlugin_NotFoundCommentGenerator"));
    }
    object = session.contains(object) ? object : (CObject) session.get(CObject.class, object.getId());
    return tagCommentGenerator.getGroupComment(session, object);
  }
  
  public List<Class<? extends PropertyGroupDescriptor>> getPropertyGroups() {
    List<Class<? extends PropertyGroupDescriptor>> result = new ArrayList<Class<? extends PropertyGroupDescriptor>>();
    result.add(Tag2File.class);
    result.add(ManualTagComment.class);
    result.add(AltParameterNamePropertyGroup.class);
    return result;
  }
  
}

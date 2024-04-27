package ru.rtec.cf2.plugin.tag;

import org.hibernate.Session;
import ru.rtec.cf2.plugin.model.objects.CObject;
import ru.rtec.cf2.plugin.model.objects.Parameter;

/**
 * Интерфейс плагина способного генерировать комментариии к тегам.
 *
 * <p>
 * <b>Author</b> : Anna Nesterenko <b> Date</b>: 17.05.2005 14:46:12
 * </p>
 */
public interface ITagCommentGenerator {
  /**
   * Получить комментарий тега для указанного параметра. Метод всегда
   * вызывается в контексте открытой сессии, которая к тому-же передается в
   * качестве параметра. В принципе параметр может быть для объекта скрытым,
   * но для генерации тега это неважно.
   *
   * @param session сессия.
   * @param par параметр.
   * @param object объект к которому привязан параметр.
   *
   * @return комментарий тега.
   *
   * @throws ETagNameError По той или иной причине комментарий тега
   *         сгенерировать нельзя. Может включать в качестве
   *         вложенной причины ошибку базы данных.
   */
  public String getTagComment(Session session, CObject object, Parameter par) throws ETagNameError;
  
  /**
   * Получить комментарий группы.
   *
   * @param session сессия.
   * @param object объект представляющий группу.
   *
   * @return комментарий для группы.
   *
   * @throws ETagNameError По той или иной причине комментарий для группы
   *         сгенерировать нельзя. Может включать в качестве
   *         вложенной причины ошибку базы данных.
   */
  public String getGroupComment(Session session, CObject object) throws ETagNameError;
  
}
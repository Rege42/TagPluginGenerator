package ru.rtec.cf2.plugin.tag;

import org.hibernate.Session;
import ru.rtec.cf2.plugin.model.objects.CObject;
import ru.rtec.cf2.plugin.model.objects.Parameter;

/**
 * Внешний интерфейс плагина тегов.
 *
 * <p>
 * <b>Author</b> : Denis Tsyplakov <b> Date</b>: 10.05.2005 17:50:47
 * </p>
 */
public interface ITags {
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
   *
   * @return имя тега.
   *
   * @throws ETagNameError По той или иной причине имя тега сгенерировать
   *         нельзя. Может включать в качестве вложенной причины
   *         ошибку базы данных.
   */
  public String getTagName(Session session, CObject object, Parameter par) throws ETagNameError;
  
  /**
   * Получить имя группы. Имя группы это составляющая имени тега относящаяся к
   * объекту. Алгоритм генерации симметричен алгоритму генерации имени тега.
   *
   * @param session сессия
   * @param object параметр
   *
   * @return имя группы
   * @throws ETagNameError по той или иной причине имя тега сгенерировать
   *         нельзя. Может включать в качестве вложенной причины
   *         ошибку базы данных.
   */
  public String getGroupName(Session session, CObject object) throws ETagNameError;
  
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
   *
   * @return комментарий для тега.
   *
   * @throws ETagNameError По той или иной причине комментарий для тега
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
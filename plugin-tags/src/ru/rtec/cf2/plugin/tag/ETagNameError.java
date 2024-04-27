package ru.rtec.cf2.plugin.tag;

/**
 * Ошибка генерации имени тега.
 *
 * <p>
 * <b>Author</b> : Denis Tsyplakov <b> Date</b>: 10.05.2005 17:23:51
 * </p>
 */
public class ETagNameError extends Error {
  /**
   * serialVersionUID.
   */
  private static final long serialVersionUID = -6297103429978211043L;
  
  public ETagNameError() {
    super();
  }
  
  public ETagNameError(String message) {
    super(message);
  }
  
  public ETagNameError(String message, Throwable cause) {
    super(message, cause);
  }
  
  public ETagNameError(Throwable cause) {
    super(cause);
  }
}

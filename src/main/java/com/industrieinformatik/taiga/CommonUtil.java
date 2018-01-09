package com.industrieinformatik.taiga;

import java.io.Closeable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * The <code>CommonUtil</code> contains utility methods for handling
 * type independent operations.
 * 
 * @author René Jahn
 */
public final class CommonUtil {
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Initialization
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Invisible constructor because <code>CommonUtil</code> is a utility
   * class.
   */
  private CommonUtil() {
  }

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // User-defined methods
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  /**
   * Writes the stack trace of an exception/throwable object into a string.
   * 
   * @param pCause Exception/Throwable (StackTrace)
   * @param pDeep <code>true</code> to dump the trace with all causes; <code>false</code> to dump
   *          only the exception
   * @return stack trace
   */
  public static String dump(Throwable pCause, boolean pDeep) {
    if (pCause == null) {
      return "";
    } else {
      StringWriter sw = new StringWriter(512);
      PrintWriter pw = new PrintWriter(sw);

      if (pDeep) {
        pCause.printStackTrace(pw);
      } else {
        pw.println(pCause);

        StackTraceElement[] trace = pCause.getStackTrace();
        for (int i = 0, anz = trace.length; i < anz; i++) {
          pw.println("\tat " + trace[i]);
        }
      }

      pw.close();

      return sw.toString();
    }
  }

  /**
   * Gets the first cause.
   * 
   * @param pCause the origin cause
   * @return the first cause
   */
  public static Throwable getFirstCause(Throwable pCause) {
    if (pCause == null) {
      return null;
    }

    Throwable th = pCause;

    while (th.getCause() != null) {
      th = th.getCause();
    }

    return th;
  }

  /**
   * Gets whether an exception chain is or contains a specific error class.
   * This methods checks whether the given class is assignable from the found
   * exception cause.
   * 
   * @param pCause the exception (chain)
   * @param pClass the expected class
   * @return <code>true</code> if the given class or a sub class was found in the exception
   *         chain, <code>false</code> otherwise
   * @see Class#isAssignableFrom(Class)
   */
  public static boolean containsException(Throwable pCause, Class<?> pClass) {
    return getException(pCause, pClass) != null;
  }

  /**
   * Gets an exception of the given type from an exception chain.
   * This methods checks whether the given class is assignable from the found
   * exception cause.
   * 
   * @param pCause the exception (chain)
   * @param pClass the expected class
   * @return the found exception of <code>null</code> if no exception with the given type was found
   * @see Class#isAssignableFrom(Class)
   */
  public static Throwable getException(Throwable pCause, Class<?> pClass) {
    if (pCause == null) {
      return null;
    }

    Throwable th = pCause;

    if (pClass.isAssignableFrom(th.getClass())) {
      return th;
    }

    while (th.getCause() != null) {
      th = th.getCause();

      if (pClass.isAssignableFrom(th.getClass())) {
        return th;
      }
    }

    return null;
  }

  /**
   * Gets an alternative value for a <code>null</code> object.
   * 
   * @param <T> parameter type
   * @param pValue desired value
   * @param pNvlValue alternative value
   * @return <code>pValue</code> or <code>pNvlValue</code> if <code>pValue == null</code>
   */
  public static <T> T nvl(T pValue, T pNvlValue) {
    if (pValue == null) {
      return pNvlValue;
    }

    return pValue;
  }

  /**
   * Close the given object(s) if closable. An object is closable if it contains a
   * close() method.
   * 
   * @param <T> the closable type
   * @param pClosable the object(s) to close
   * @return <code>null</code>
   */
  public static <T> T close(Object... pClosable) {
    if (pClosable != null) {
      Object obj;

      for (int i = 0; i < pClosable.length; i++) {
        obj = pClosable[i];

        if (obj != null) {
          try {
            if (obj instanceof Connection) {
              ((Connection) obj).close();
            } else if (obj instanceof Statement) {
              ((Statement) obj).close();
            } else if (obj instanceof ResultSet) {
              ((ResultSet) obj).close();
            } else if (obj instanceof Closeable) {
              ((Closeable) obj).close();
            } else if (obj instanceof Clob) {
              ((Clob) obj).free();
            } else if (obj instanceof Blob) {
              ((Blob) obj).free();
            } else if (obj instanceof DatagramSocket) {
              ((DatagramSocket) obj).close();
            } else {
              //Not in any case, because we want to check above objects for documentation
              Method met = obj.getClass().getMethod("close");

              met.invoke(obj);
            }
          } catch (Throwable th) {
            // We are not interested in Logspam from not being able to close
            // This is beeing called for example for > 10000 Blobs, if the connection is closed...
            // LoggerFactory.getInstance(CommonUtil.class).debug(th);
          }
        }
      }
    }

    return null;
  }
  
  /**
   * Indicates whether two object are "equal". Two objects are equals if both are <code>null</code> or
   * the {@link Object#equals(Object)} returns <code>true</code>.
   * 
   * @param pFirst an object
   * @param pSecond another object
   * @return <code>true</code> if both objects are equal
   */
  public static boolean equals(Object pFirst, Object pSecond) {
    return pFirst == pSecond || (pFirst != null && pFirst.equals(pSecond));
  }

} // CommonUtil

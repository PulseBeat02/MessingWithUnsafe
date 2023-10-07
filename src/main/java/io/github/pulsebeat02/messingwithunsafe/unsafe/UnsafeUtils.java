package io.github.pulsebeat02.messingwithunsafe.unsafe;

import sun.misc.Unsafe;

public final class UnsafeUtils {

  private static final Unsafe UNSAFE;

  static {
    UNSAFE = UnsafeProvider.getUnsafe();
  }

  public static Object getField(final Object object, final String name)
      throws NoSuchFieldException {
    return getField(object.getClass(), object, name);
  }

  public static Object getField(final Class<?> clazz, final Object object, final String name)
      throws NoSuchFieldException {
    return UNSAFE.getObject(object, UNSAFE.objectFieldOffset(clazz.getDeclaredField(name)));
  }
}

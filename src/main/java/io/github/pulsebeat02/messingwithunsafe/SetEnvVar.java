package io.github.pulsebeat02.messingwithunsafe;

import io.github.pulsebeat02.messingwithunsafe.unsafe.UnsafeUtils;

import java.util.Map;

public final class SetEnvVar {

  public static void main(final String[] args) {
    final String key = "foo";
    final String value = "bar";
    try {
      final Map<String, String> unwritable = System.getenv();
      final Map<String, String> writable =
          (Map<String, String>) UnsafeUtils.getField(unwritable, "m");
      writable.put(key, value);
    } catch (final NoSuchFieldException e) {
      throw new AssertionError(e);
    }
    System.out.println(System.getenv());
  }
}

package io.github.pulsebeat02.messingwithunsafe;

import io.github.pulsebeat02.messingwithunsafe.unsafe.UnsafeProvider;
import sun.misc.Unsafe;

public final class CrashJVM {

  public static void main(final String[] args) {
    final Unsafe unsafe = UnsafeProvider.getUnsafe();
    unsafe.putAddress(0L, 0L);
  }
}

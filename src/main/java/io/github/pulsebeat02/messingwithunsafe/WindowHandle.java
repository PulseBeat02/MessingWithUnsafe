package io.github.pulsebeat02.messingwithunsafe;

import sun.misc.Unsafe;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WindowHandle {

  private static final Unsafe UNSAFE;

  static {
    try {
      final Field field = Unsafe.class.getDeclaredField("theUnsafe");
      field.setAccessible(true);
      UNSAFE = (Unsafe) field.get(null);
    } catch (final IllegalAccessException | NoSuchFieldException e) {
      throw new AssertionError(e);
    }
  }

  public static void main(final String[] args) throws NoSuchFieldException {

    final JFrame frame = new JFrame();
    frame.setVisible(true);
    frame.setSize(400, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final Canvas canvas = new Canvas();
    frame.add(canvas);
    frame.setVisible(true);

    System.out.println(getWindowHandle(frame));
    System.out.println(getWindowHandle(canvas));
  }

  public static List<Field> getAllFields(final Class<?> type) {
    final List<Field> fields = new ArrayList<>();
    for (Class<?> c = type; c != null; c = c.getSuperclass()) {
      final Field[] declared = c.getDeclaredFields();
      final List<Field> list = Arrays.asList(declared);
      fields.addAll(list);
    }
    return fields;
  }

  public static long getWindowHandle0(final Object component) {
    final String search = isWindows() ? "hwnd" : "window";
    final Class<?> clazz = component.getClass();
    final List<Field> fields = getAllFields(clazz);
    for (final Field field : fields) {
      final String name = field.getName();
      if (name.equals(search)) {
        final long offset = UNSAFE.objectFieldOffset(field);
        return UNSAFE.getLong(component, offset);
      }
    }
    return 0;
  }

  public static boolean isWindows() {
    return System.getProperty("os.name").toLowerCase().contains("win");
  }

  public static long getWindowHandle(final Component component) throws NoSuchFieldException {
    final Class<Component> clazz = Component.class;
    final Field field = clazz.getDeclaredField("peer");
    final long offset = UNSAFE.objectFieldOffset(field);
    final Object peer = UNSAFE.getObject(component, offset);
    return getWindowHandle0(peer);
  }
}

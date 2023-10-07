package io.github.pulsebeat02.messingwithunsafe;

import io.github.pulsebeat02.messingwithunsafe.unsafe.UnsafeUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;

public final class LoadJarRuntime {

  public static void main(final String[] args)
      throws NoSuchFieldException, ClassNotFoundException, MalformedURLException {
    final File file = new File("json_simple.jar");
    final URL url = file.toURI().toURL();
    final ClassLoader unwritable = ClassLoader.getSystemClassLoader();
    final Class<?> clazz = Class.forName("jdk.internal.loader.BuiltinClassLoader");
    final Object writable = UnsafeUtils.getField(clazz, unwritable, "ucp");
    final ArrayList<URL> urls = (ArrayList<URL>) UnsafeUtils.getField(writable, "path");
    final ArrayDeque<URL> unopenedUrls =
        (ArrayDeque<URL>) UnsafeUtils.getField(writable, "unopenedUrls");
    urls.add(url);
    unopenedUrls.addLast(url);
    final Class<?> testClazz = Class.forName("org.json.simple.JSONValue");
    System.out.println(testClazz);
  }
}

package com.github.zacscoding.tracej.agent.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 *
 */
public class DumpUtil {
    private static boolean hasError = false;

    public static void writeByteCode(String rootDir, byte[] bytes, final String className) {
        if (bytes == null || bytes.length == 0) {
            return;
        }

        final byte[] copy = new byte[bytes.length];
        System.arraycopy(bytes, 0, copy, 0, bytes.length);
        writeByteCodeInternal(rootDir, copy, className);

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                writeByteCodeInternal(copy, className);
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();
    }

    private static void writeByteCodeInternal(String rootDir, byte[] bytes, String className) {
        if (hasError) {
            System.out.println("Cant write class file");
            return;
        }

        if (bytes == null || className == null) {
            return;
        }

        className = parseClassNameToPath(className);
        File dir = null;

        String dirPath = rootDir;
        int classDot = className.lastIndexOf(File.separatorChar);
        if (classDot > -1) {
            dirPath += (File.separator + className.substring(0, classDot));
        }

        dir = new File(dirPath);
        if (!dir.canWrite()) {
            dir.mkdirs();
        }

        if (!dir.canWrite()) {
            return;
        }

        String clazz = className.substring(classDot + 1) + ".class";
        BufferedOutputStream buffOut = null;
        try {
            buffOut = new BufferedOutputStream(new FileOutputStream(new File(dir, clazz)));
            buffOut.write(bytes);
            buffOut.flush();
            buffOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String parseClassNameToPath(String className) {
        if (className == null) {
            return null;
        }
        if (className.length() == 0) {
            return "";
        }

        char[] copy = new char[className.length()];

        for (int i = 0; i < copy.length; i++) {
            char cur = className.charAt(i);
            if (cur == '.' || cur == '/') {
                cur = File.separatorChar;
            }
            copy[i] = cur;
        }

        return new String(copy);
    }
}

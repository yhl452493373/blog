package com.yang.blog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Cleaner;

import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     */
    public static void freeMappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }
            mappedByteBuffer.force();
            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                try {
                    Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner");
                    //可以访问private的权限
                    getCleanerMethod.setAccessible(true);
                    //在具有指定参数的 方法对象上调用此 方法对象表示的底层方法
                    Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(mappedByteBuffer, new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    logger.error("clean MappedByteBuffer error!!!", e);
                }
                logger.info("clean MappedByteBuffer completed!!!");
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

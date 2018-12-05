package com.yang.blog.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.bean.MultipartFileParam;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.config.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import sun.misc.Cleaner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class FileUtils {
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    //文件保存位置
    private static String basePath = SystemProperties.getFileUpload().getPath();
    //是否绝对路径
    private static Boolean absolute = SystemProperties.getFileUpload().getAbsolute();

    static {
        //basePath设置为"/"结尾
        if (!basePath.endsWith("/")) {
            basePath += "/";
        }
        //如果不是绝对路径,则补全其路径
        if (!absolute) {
            //basePath设置为"/"开头
            if (!basePath.startsWith("/")) {
                basePath = "/" + basePath;
            }
            basePath = new ApplicationHome().getDir() + basePath;
        }
    }

    /**
     * 获取系统上传路径
     *
     * @return 系统设置的上传路径
     */
    public static String uploadPath() {
        return basePath;
    }

    /**
     * 获取保存到数据库的文件记录对应的文件路径
     *
     * @param file 文件记录
     * @return 文件记录对应的文件在硬盘上的完整路径(包括文件本身名字和后缀名)
     */
    public static String uploadPath(com.yang.blog.entity.File file) {
        return basePath + file.getSaveName() + file.getExtensionName();
    }

    /**
     * 分块上传
     * 第一步：获取RandomAccessFile,随机访问文件类的对象
     * 第二步：调用RandomAccessFile的getChannel()方法，打开文件通道 FileChannel
     * 第三步：获取当前是第几个分块，计算文件的最后偏移量
     * 第四步：获取当前文件分块的字节数组，用于获取文件字节长度
     * 第五步：使用文件通道FileChannel类的 map（）方法创建直接字节缓冲器  MappedByteBuffer
     * 第六步：将分块的字节数组放入到当前位置的缓冲区内  mappedByteBuffer.put(byte[] b);
     * 第七步：释放缓冲区
     * 第八步：检查文件是否全部完成上传
     * 第九步：返回信息。如果全部完成返回成功或文件名，或其他需要信息
     *
     * @param param 上传的文件信息
     * @return 包含两个信息:taskId和file,如果上传完成,file则不为null,否则为null;taskId可以当做临时文件名,从第二次发起分段上传请求时必须要带上这个属性.否则不知道哪个文件是哪个的分片
     */
    public static Map<String, Object> upload(MultipartFileParam param) throws IOException {
        if (StringUtils.isEmpty(param.getTaskId())) {
            param.setTaskId(CommonUtils.uuid());
        }
        if (StringUtils.isEmpty(param.getFileName())) {
            param.setFileName(param.getFile().getOriginalFilename());
            param.setFileSize(param.getFile().getSize());
            param.setFileType(param.getFile().getContentType());
        }
        /*
         * basePath是我的路径，可以替换为你的
         * 1：原文件名改为UUID
         * 2：创建临时文件，和源文件一个路径
         * 3：如果文件路径不存在重新创建
         */
        //原名
        String fileName = param.getFileName();
        //文件后缀名
        String fileExtName = fileName.substring(fileName.lastIndexOf("."));
        String fileSaveName = param.getTaskId() + fileExtName;
        //临时文件名
        String tempFileName = fileSaveName + "_tmp";
        //文件保存位置:uploadPath
        String filePath = basePath;
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File tempFile = new File(filePath, tempFileName);
        //第一步
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        //第二步
        FileChannel fileChannel = raf.getChannel();
        //第三步
        long offset = param.getIsChunk() ? param.getChunk() * param.getChunkSize() : 0;
        //第四步
        byte[] fileData = param.getFile().getBytes();
        //第五步
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        //第六步
        mappedByteBuffer.put(fileData);
        //第七步
        freeMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        raf.close();
        //第八步
        boolean isComplete = checkUploadStatus(param, fileSaveName, filePath);
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", param.getTaskId());
        if (isComplete) {
            map.put("file", renameFile(tempFile, fileSaveName));
        } else {
            map.put("file", null);
        }
        return map;
    }

    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     */
    private static void freeMappedByteBuffer(final MappedByteBuffer mappedByteBuffer) {
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

    /**
     * 文件重命名
     *
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     */
    private static File renameFile(File toBeRenamed, String toFileNewName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            return null;
        }
        String p = toBeRenamed.getParent();
        File newFile = new File(p + File.separatorChar + toFileNewName);
        //修改文件名
        if (toBeRenamed.renameTo(newFile))
            return newFile;
        return null;
    }

    /**
     * 检查文件上传进度
     */
    private static boolean checkUploadStatus(MultipartFileParam param, String fileName, String filePath) throws IOException {
        if (!param.getIsChunk())
            return true;
        File confFile = new File(filePath, fileName + ".conf");
        RandomAccessFile confAccessFile = new RandomAccessFile(confFile, "rw");
        //设置文件长度
        confAccessFile.setLength(param.getChunkTotal());
        //将指定的一个字节写入文件中 127
        confAccessFile.getChannel().position(param.getChunk()).write(ByteBuffer.wrap(new byte[]{Byte.MAX_VALUE}));
        byte[] completeStatusList = org.apache.commons.io.FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        //这一段逻辑有点复杂，看的时候思考了好久，创建conf文件文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认的0,已上传的就是Byte.MAX_VALUE 127
        for (int i = 0; i < completeStatusList.length && isComplete == Byte.MAX_VALUE; i++) {
            isComplete = (byte) (isComplete & completeStatusList[i]);
        }
        //关闭
        confAccessFile.getChannel().close();
        if (isComplete == Byte.MAX_VALUE) {
            //上传完成则删除临时文件
            confFile.delete();
            return true;
        }
        return false;
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, java.io.File file, String downloadFilename) {
        BufferedInputStream bufferedInputStream = null;
        try {
            if (file.exists()) {
                long p = 0L;
                long toLength = 0L;
                long contentLength = 0L;
                int rangeSwitch = 0; // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
                long fileLength;
                String rangBytes = "";
                fileLength = file.length();

                // get file content
                InputStream fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);

                // tell the client to allow accept-ranges
                response.reset();
                response.setHeader("Accept-Ranges", "bytes");

                // client requests a file block download start byte
                String range = request.getHeader("Range");
                if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                    response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                    rangBytes = range.replaceAll("bytes=", "");
                    if (rangBytes.endsWith("-")) {  // bytes=270000-
                        rangeSwitch = 1;
                        p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                        contentLength = fileLength - p;  // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                    } else { // bytes=270000-320000
                        rangeSwitch = 2;
                        String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                        String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                        p = Long.parseLong(temp1);
                        toLength = Long.parseLong(temp2);
                        contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
                    }
                } else {
                    contentLength = fileLength;
                }

                // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
                // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
                response.setHeader("Content-Length", Long.toString(contentLength));
                response.setHeader("Yc-Length", Long.toString(contentLength));
                // 断点开始
                // 响应的格式是:
                // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                if (rangeSwitch == 1) {
                    String contentRange = "bytes " + Long.toString(p) + "-" +
                            Long.toString(fileLength - 1) + "/" +
                            Long.toString(fileLength);
                    response.setHeader("Content-Range", contentRange);
                    bufferedInputStream.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + Long.toString(fileLength);
                    response.setHeader("Content-Range", contentRange);
                    bufferedInputStream.skip(p);
                } else {
                    String contentRange = "bytes " + "0-" +
                            (fileLength - 1) + "/" +
                            fileLength;
                    response.setHeader("Content-Range", contentRange);
                }

                //String fileName = file.getName();
                response.setContentType("application/octet-stream");
                if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                    downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");
                } else {
                    downloadFilename = new String(downloadFilename.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
                }
                response.addHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
                response.setHeader("Yc-filename", downloadFilename);

                OutputStream out = response.getOutputStream();
                int n = 0;
                long readLength = 0;
                int bufferSize = 1024;
                byte[] bytes = new byte[bufferSize];
                if (rangeSwitch == 2) {
                    // 针对 bytes=27000-39000 的请求，从27000开始写数据
                    while (readLength <= contentLength - bufferSize) {
                        n = bufferedInputStream.read(bytes);
                        readLength += n;
                        out.write(bytes, 0, n);
                    }
                    if (readLength <= contentLength) {
                        n = bufferedInputStream.read(bytes, 0, (int) (contentLength - readLength));
                        out.write(bytes, 0, n);
                    }
                } else {
                    while ((n = bufferedInputStream.read(bytes)) != -1) {
                        out.write(bytes, 0, n);
                    }
                }
                out.flush();
                out.close();
                bufferedInputStream.close();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Error: file " + file.getPath() + " not found.");
                }
            }
        } catch (Exception ignored) {
            //忽略异常
        } finally {
            try {
                assert bufferedInputStream != null;
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean delete(String fileId) {
        boolean result = true;
        com.yang.blog.entity.File file = ServiceConfig.serviceConfig.fileService.getById(fileId);
        if (file != null) {
            java.io.File uploadFile = new java.io.File(FileUtils.uploadPath(file));
            uploadFile.delete();
            result = ServiceConfig.serviceConfig.fileService.removeById(file.getId());
        }
        return result;
    }

    public static boolean delete(List<String> fileIdList) {
        if (fileIdList.isEmpty()) {
            return true;
        }
        Collection<com.yang.blog.entity.File> fileList = ServiceConfig.serviceConfig.fileService.listByIds(fileIdList);
        fileList.forEach(file -> {
            java.io.File uploadFile = new java.io.File(FileUtils.uploadPath(file));
            uploadFile.delete();
        });
        return ServiceConfig.serviceConfig.fileService.removeByIds(fileIdList);
    }
}

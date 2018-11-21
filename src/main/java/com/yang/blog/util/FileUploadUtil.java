package com.yang.blog.util;

import com.github.yhl452493373.bean.JSONResult;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.bean.MultipartFileParam;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileUploadUtil {
    private static String basePath = "E:/Temp";

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
     */
    public static File chunkUploadByMappedByteBuffer(MultipartFileParam param) throws IOException {
        if (param.getTaskId() == null || "".equals(param.getTaskId())) {
            param.setTaskId(CommonUtils.uuid());
        }
        /*
         * basePath是我的路径，可以替换为你的
         * 1：原文件名改为UUID
         * 2：创建临时文件，和源文件一个路径
         * 3：如果文件路径不存在重新创建
         */
        //原名
        String fileName = param.getFile().getOriginalFilename();
        assert fileName != null;
        //临时文件名
        String tempFileName = param.getTaskId() + fileName.substring(fileName.lastIndexOf(".")) + "_tmp";
        //文件保存位置
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
        long offset = param.getChunk() * param.getSize();
        //第四步
        byte[] fileData = param.getFile().getBytes();
        //第五步
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        //第六步
        mappedByteBuffer.put(fileData);
        //第七步
        FileUtil.freeMappedByteBuffer(mappedByteBuffer);
        fileChannel.close();
        raf.close();
        //第八步
        boolean isComplete = checkUploadStatus(param, fileName, filePath);
        if (isComplete) {
            return renameFile(tempFile, fileName);
        }
        return null;
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
        File confFile = new File(filePath, fileName + ".conf");
        RandomAccessFile confAccessFile = new RandomAccessFile(confFile, "rw");
        //设置文件长度
        confAccessFile.setLength(param.getChunkTotal());
        //设置起始偏移量
        confAccessFile.setLength(param.getChunk());
        //将指定的一个字节写入文件中 127，
        confAccessFile.write(Byte.MAX_VALUE);
        byte[] completeStatusList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        //这一段逻辑有点复杂，看的时候思考了好久，创建conf文件.文件长度为总分片数，每上传一个分块即向conf文件中写入一个127，那么没上传的位置就是默认的0,已上传的就是Byte.MAX_VALUE 127
        for (int i = 0; i < completeStatusList.length && isComplete == Byte.MAX_VALUE; i++) {
            isComplete = (byte) (isComplete & completeStatusList[i]);
            System.out.println("check part " + i + " is complete:" + (completeStatusList[i] == Byte.MAX_VALUE));
        }
        if (isComplete == Byte.MAX_VALUE) {
            confAccessFile.close();
            //noinspection ResultOfMethodCallIgnored
            confFile.delete();
            return true;
        }
        return false;
    }
}

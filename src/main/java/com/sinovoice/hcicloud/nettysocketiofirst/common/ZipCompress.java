package com.sinovoice.hcicloud.nettysocketiofirst.common;

import java.io.IOException;

/**
 *
 * 提供对单个文件与目录的压缩，并支持是否需要创建压缩源目录、中文路径
 *
 * @author jzj
 */
public class ZipCompress {

    private static boolean isCreateSrcDir = true;//是否创建源目录

    /**
     * @param args
     * @throws IOException
     */
//    public static void main(String[] args) throws IOException {
////        String src = "m:/新建文本文档.txt";//指定压缩源，可以是目录或文件
////        String decompressDir = "e:/tmp/decompress";//解压路径
////        String archive = "e:/tmp/test.zip";//压缩包路径
////        String comment = "Java Zip 测试.";//压缩包注释
//
//
////        String src = "D:\\pinganftp";//指定压缩源，可以是目录或文件
////        String decompressDir = "D:\\home\\apachezip";//解压路径
////        String archive = "D:\\home\\apachezip\\success.zip";//压缩包路径
////        String comment = "Java Zip 测试.";//压缩包注释
//
//        //168测试
//        String src = "/home/mhn/contentTxt";//指定压缩源，可以是目录或文件
//        String decompressDir = "/home/mhn/unzip";//解压路径
//        String archive = "/home/mhn/zip/contentTxt.zip";//压缩包路径，生成的压缩文件名字
//        String comment = "Java Zip 测试.";//压缩包注释
//
//
//
//        //----压缩文件或目录
//        long time1 = System.currentTimeMillis();
//        ZipUtils.writeByApacheZipOutputStream(src, archive, comment);
//        long time2 = System.currentTimeMillis();
//        System.out.println("压缩文件耗时为：" + DateConversion.formatTime(time2 - time1));
//
//        /*
//         * 读压缩文件，注释掉，因为使用的是apache的压缩类，所以使用java类库中
//         * 解压类时出错，这里不能运行
//         */
//        //readByZipInputStream();
//        //----使用apace ZipFile读取压缩文件
//        long time3 = System.currentTimeMillis();
//        ZipUtils.readByApacheZipFile(archive, decompressDir);
//        long time4 = System.currentTimeMillis();
//        System.out.println("压缩文件耗时为：" + DateConversion.formatTime(time2 - time1));
//        System.out.println("解压文件耗时为：" + DateConversion.formatTime(time4 - time3));
//    }
}
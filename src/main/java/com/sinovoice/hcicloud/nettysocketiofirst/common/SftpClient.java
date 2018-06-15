package com.sinovoice.hcicloud.nettysocketiofirst.common;

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

/**
 * name：SFTPClient
 * <p>
 * </p>
 *
 * @author：lipeng
 * @data：2014-9-22 下午04:29:45
 * @version 1.0
 */
public class SftpClient {

    private static final Logger logger = Logger.getLogger(SftpClient.class);

    private ChannelSftp sftp;

    private boolean isReady = false;

    private Session sshSession;

    /** 这个是该账户的根目录，当前工作目录，每次关闭连接要回复到null，因为当前类是单例类 */

    //本地测试的地址
//    private String directory = "/home/ftpuser";
//    private static String suid = "root";
//    private static String shost = "10.10.10.102";
//    private static int sport = 22;
//    private static String setPassword = "jths201";


    //正式的地址
    private String directory = "/fsr";
    private static String suid = "pinganftp";
    private static String shost = "172.16.44.40";
    private static int sport = 22;
    private static String setPassword = "pinganftp";

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * 连接sftp服务器
     * 具体参数写在构造器里面
     */
    private void setReady() throws Exception {
        try {
            if (!isReady) {
                JSch jsch = new JSch();
                sshSession =
                        jsch.getSession(suid, shost, sport);
                System.out.println("Session created.");
                sshSession.setPassword(setPassword);
                Properties sshConfig = new Properties();
                sshConfig.put("StrictHostKeyChecking", "no");
                sshSession.setConfig(sshConfig);
                isReady = true;
            }
            if (sshSession != null && !sshSession.isConnected()) {
                sshSession.connect();
                Channel channel = sshSession.openChannel("sftp");
                channel.connect();
                sftp = (ChannelSftp) channel;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            this.close();
            logger.error("sftp连接服务器出错,host:" + shost, e);
            throw e;
        }
    }

    /**
     * 上传文件
     *
     * @param remoteName 远程文件的完整路径地址
     * @param uploadFile 要上传的文件  指本地的文件
     * @throws Exception
     */
    public boolean uploadFile(String uploadFile, String remoteName) throws Exception {
        try {
            setReady();

            if (remoteName.contains("/")) {
                String remotePath = remoteName.substring(0,remoteName.lastIndexOf("/"));
//                try {
                    sftp.cd(directory);
                    //TODO 这里很重要，创建目录的时候，必须指定该用户登录后，显示的操作目录 比如 success.fsr.current
                    createDir(directory + "/" + remotePath);
                    //sftp的mkdir不支持创建多个目录
//                    sftp.mkdir(remotePath);
                    System.out.println("远程文件夹建立成功" + directory + "/" + remotePath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                sftp.cd(directory+"/"+remotePath);
                remoteName=remoteName.substring(remoteName.lastIndexOf("/") + 1,remoteName.length());
            }else{
                sftp.cd(directory);
            }
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), remoteName);
            System.out.println("文件传送成功" + remoteName);
            return true;
        } catch (Exception e) {
            logger.error("sftp上传文件出错,directory:" + directory, e);

            System.out.println("sftp上传文件出错,directory:" + directory);
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * 下载文件
     *
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @throws Exception
     */
    public boolean downloadFile(String downloadFile, String saveFile) throws Exception {
        try {
            setReady();
            sftp.cd(directory);
            File localFile=new File(saveFile);
            if(localFile!=null&&!localFile.exists()){
                if(localFile.getParentFile()!=null&&!localFile.getParentFile().exists()){
                    localFile.getParentFile().mkdirs();
                }
                localFile.createNewFile();
            }
            sftp.get(downloadFile, new FileOutputStream(localFile));
            return true;
        } catch (Exception e) {
            logger.error("sftp下载文件出错,directory:" + directory, e);
            throw e;
        }
    }

    /**
     * 删除文件
     *
     * @param deleteFile 要删除的文件
     * @throws Exception
     */
    public boolean removeFile(String deleteFile) throws Exception {
        try {
            setReady();
            sftp.cd(directory);
            sftp.rm(deleteFile);
            return true;
        } catch (Exception e) {
            logger.error("sftp删除文件出错,directory:" + directory, e);
            throw e;
        }
    }

    /**
     *
     * 复制文件
     * @param @param src
     * @param @param dst
     * @param @return
     * @param @throws Exception
     * @return boolean
     */
    public boolean copyFile(String src, String dst) throws Exception {
        ByteArrayInputStream bStreams =null;
        try {
            setReady();
            if (!isFileExist(src)) {
                //文件不存在直接反回.
                return false;
            }
            String parentPath=dst.substring(0,dst.lastIndexOf("/"));
            if (!this.isDirExist(parentPath)) {
                createDir(parentPath);
            }
            byte[] srcFtpFileByte = inputStreamToByte(sftp.get(src));
            bStreams = new ByteArrayInputStream(srcFtpFileByte);
            //二进制流写文件
            sftp.put(bStreams, dst);

            return true;
        } catch (Exception e) {
            logger.error("sftp移动文件出错,[src:" + src+",dst:"+dst+"]", e);
            throw e;
        }finally{
            if(bStreams!=null){
                bStreams.close();
            }
        }
    }

    /**
     * 判断远程文件是否存在
     * @param srcSftpFilePath
     * @return
     * @throws SftpException
     */
    public boolean isFileExist (String srcSftpFilePath) throws SftpException {
        boolean isExitFlag = false;
        // 文件大于等于0则存在文件
        if (getFileSize(srcSftpFilePath) >= 0) {
            isExitFlag = true;
        }
        return isExitFlag;
    }

    /**
     * 得到远程文件大小
     * @param srcSftpFilePath
     * @return 返回文件大小，如返回-2 文件不存在，-1文件读取异常
     * @throws SftpException
     */
    public long getFileSize (String srcSftpFilePath) throws SftpException {
        long filesize = 0;//文件大于等于0则存在
        try {
            SftpATTRS sftpATTRS = sftp.lstat(srcSftpFilePath);
            filesize = sftpATTRS.getSize();
        } catch (Exception e) {
            filesize = -1;//获取文件大小异常
            if (e.getMessage().toLowerCase().equals("no such file")) {
                filesize = -2;//文件不存在
            }
        }
        return filesize;
    }
    /**
     * inputStream类型转换为byte类型
     * @param iStrm
     * @return
     * @throws IOException
     */
    public byte[] inputStreamToByte (InputStream iStrm) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = iStrm.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    /**
     * 创建远程目录
     * @param sftpDirPath
     * @throws SftpException
     */
    public void createDir (String sftpDirPath) throws SftpException {
        sftp.cd("/");
        String pathArry[] = sftpDirPath.split("/");
        for (String path : pathArry) {
            if (path.equals("")) {
                continue;
            }
            if (isDirExist(path)) {
                sftp.cd(path);
            }
            else {
                //建立目录
                sftp.mkdir(path);
                //进入并设置为当前目录
                sftp.cd(path);
            }
        }
        sftp.cd(directory);
    }

//    /**
//     * 创建一个文件目录
//     */
//    public void createDir(String createpath, ChannelSftp sftp) throws Exception {
//        try {
//            if (isDirExist(createpath)) {
//                this.sftp.cd(createpath);
//            }
//            String pathArry[] = createpath.split("/");
//            StringBuffer filePath = new StringBuffer("/");
//            for (String path : pathArry) {
//                if (path.equals("")) {
//                    continue;
//                }
//                filePath.append(path + "/");
//                if (isDirExist(filePath.toString())) {
//                    sftp.cd(filePath.toString());
//                } else {
//                    // 建立目录
//                    sftp.mkdir(filePath.toString());
//                    // 进入并设置为当前目录
//                    sftp.cd(filePath.toString());
//                }
//            }
//            this.sftp.cd(createpath);
//        } catch (SftpException e) {
//            throw new Exception("创建路径错误：" + createpath);
//        }
//    }

    /**
     * 判断目录是否存在
     * @param directory
     * @return
     * @throws SftpException
     */
    public boolean isDirExist (String directory) throws SftpException {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }
    /**
     * 列出目录下的文件
     *
     * @param
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles() throws Exception {
        setReady();
        return sftp.ls(directory);
    }

    public ChannelSftp getSftp() {
        return sftp;
    }

    public void setSftp(ChannelSftp sftp) {
        this.sftp = sftp;
    }

    public void close(){
        try {
            if (sftp != null && sftp.isConnected()) {
                sftp.disconnect();
            }
            if (sshSession != null && sshSession.isConnected()) {
                sshSession.disconnect();
            }
            isReady = false;
            System.out.println("JSCH session close");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
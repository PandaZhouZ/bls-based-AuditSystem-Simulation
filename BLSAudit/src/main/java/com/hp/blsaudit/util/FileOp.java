package com.hp.blsaudit.util;

import com.hp.blsaudit.configure.Configure;
import com.hp.blsaudit.entity.PKG;
import edu.princeton.cs.algs4.StdOut;
import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;

public class FileOp {
    public static String getPrefix(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    // 文件切割，blkSize(KB)
    public static int splitFile(String fileFrom, String doID,int blkSize) throws IOException {
        FileInputStream fis = new FileInputStream(Configure.FILEROOT + fileFrom);
        FileChannel inChannel = fis.getChannel();
        FileOutputStream fos = null;
        FileChannel outChannel = null;

        // KB -> B
        long byteUnit = blkSize * 1024;
        int subFileNum = (int)(inChannel.size() / byteUnit);
//        StdOut.println("文件大小: " + inChannel.size() + ", 子文件个数: " + subFileNum);

        String toDir = Configure.FILEROOT + getPrefix(fileFrom) +doID.hashCode()+ "Blks";
        File dir = new File(toDir);
        File blkFile;
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (int i = 0; i <= subFileNum; i++) {
            // 子文件路径
            String subPath = toDir+"/"+ i + ".blk";

            try {
                blkFile = new File(subPath);
                if (!blkFile.exists())
                    blkFile.createNewFile();
                fos = new FileOutputStream(subPath);
                outChannel = fos.getChannel();

                // 从 inChannel 的 byteUnit * i 处，读取固定长度的数据并写入 outChannel
                if (i != subFileNum) {
                    inChannel.transferTo(byteUnit * i, byteUnit, outChannel);
                } else {
                    inChannel.transferTo(byteUnit * i, inChannel.size() - byteUnit * subFileNum, outChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fos.close();
                outChannel.close();
            }
        }

        fis.close();
        inChannel.close();

//        StdOut.println("文件" + fileFrom + "分块完成！");

        return subFileNum;
    }

    //主方案的文件分割，盲化
    public static int blindSplitFile(String fileFrom, String doID, int blkSize, PKG pkg) throws IOException {
        FileInputStream fis = new FileInputStream(Configure.FILEROOT + fileFrom);
        FileOutputStream fos = null;


        // KB -> B
        int byteUnit = blkSize * 1024;
        int subFileNum = 0;
        int readFlag=0;


        String toDir = Configure.FILEROOT + getPrefix(fileFrom) +doID.hashCode()+ "Blks";
        File dir = new File(toDir);
        File blkFile;
        if (!dir.exists()) {
            dir.mkdirs();
        }


        try {
            Element mi,temp;
            Element alpha=pkg.getZp().newRandomElement().getImmutable();
            Element randL=Transform.bytesToZp(pkg,pkg.getg1().powZn(alpha).getImmutable().toBytes());
            byte[] tempBytes=new byte[byteUnit];
            readFlag=fis.read(tempBytes);
            while (readFlag>0){
                // 子文件路径
                String subPath = toDir+"/"+subFileNum+ ".blk";
                blkFile = new File(subPath);
                if (!blkFile.exists())
                    blkFile.createNewFile();
                fos = new FileOutputStream(subPath);
                if (readFlag==byteUnit){
                    temp=alpha.mul(Transform.bytesToZp(pkg,tempBytes)).getImmutable();
                    mi=temp.add(randL).getImmutable();
                }else {
                    byte[] tempBytes2=new byte[readFlag];
                    System.arraycopy(tempBytes,0,tempBytes2,0,readFlag);
                    mi=alpha.mul(Transform.bytesToZp(pkg,tempBytes2)).add(randL).getImmutable();
                }
                fos.write(mi.toBytes());
                ++subFileNum;
                readFlag=fis.read(tempBytes);
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        fis.close();
//        StdOut.println("文件" + fileFrom + "分块完成！");
//        StdOut.println("文件大小: " +(byteUnit*subFileNum+readFlag)+ ", 子文件个数: " + subFileNum);

        return subFileNum;
    }



    // 子文件合并
    public static void mergeFile(String blkFrom) throws IOException {
        String fileTo = blkFrom.substring(0, blkFrom.indexOf("Blks"));
        FileOutputStream fos = new FileOutputStream(fileTo, true);
        FileChannel outChannel = fos.getChannel();

        FileInputStream fis = null;
        FileChannel inChannel = null;

        File in = new File(Configure.FILEROOT + blkFrom);
        if (in.isDirectory()) {
            File[] ins = in.listFiles();
            Arrays.sort(ins, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()) {
                        return -1;
                    }
                    if (o1.isFile() && o2.isDirectory()) {
                        return 1;
                    }
                    return Integer.valueOf(getPrefix(o1.getName())).compareTo(Integer.valueOf(getPrefix(o2.getName())));
                }
            });
            // 记录新文件的最后一个数据的位置
            long startPos = 0;
            for (File tmp :  ins) {
                fis = new FileInputStream(tmp);
                inChannel = fis.getChannel();

                outChannel.transferFrom(inChannel, startPos, tmp.length());
                startPos += tmp.length();

                fis.close();
                inChannel.close();
            }
        }

        fos.close();
        outChannel.close();
    }

    // 读取文件到 byte[]
    public static byte[] toByteArray(String fileName, String doID,int index) {
        String blkPath = getPrefix(fileName) + doID.hashCode() +"Blks/";
        byte[] result = null;
        try {
            FileChannel inChannel = new RandomAccessFile(Configure.FILEROOT + blkPath + index + ".blk", "r").getChannel();
            MappedByteBuffer byteBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size()).load();
            result = new byte[(int)inChannel.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            inChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

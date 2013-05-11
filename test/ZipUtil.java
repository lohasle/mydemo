import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tools.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: fule
 * Date: 13-7-14
 * Time: 下午10:13
 * To change this template use File | Settings | File Templates.
 */

/**
 * <dependency>
 <groupId>org.apache.ant</groupId>
 <artifactId>ant</artifactId>
 <version>1.8.2</version>
 </dependency>
 */

public class ZipUtil {

    private static int BUFFER = 2048;
    private static String ENCODING = "GBK";

    /**
     * 压缩文件
     *
     * @param src  源文件/目录
     * @param dest 压缩后的文件/目录
     */
    public static void zip(String src, String dest) {
        System.out.println("生成压缩文件……");
        File srcFile = new File(src);
        File destFile = new File(dest);
        if (destFile.isDirectory()) {
            //构造压缩文件名。取当前文件/目录名称为压缩文件名。
            String name = srcFile.getName();
            System.out.println(name);
            name = name.indexOf(".") > 0 ? name.substring(0, name.indexOf(".")) : name;
            name = name + ".zip";
            destFile = new File(destFile + "/" + name);
        }
        System.out.println(destFile.getAbsolutePath());
        zip(srcFile, destFile);
    }

    /**
     * 压缩文件
     *
     * @param src
     * @param dest
     */
    public static void zip(File src, File dest) {
        try {
            FileOutputStream fout = new FileOutputStream(dest);
            CheckedOutputStream chc = new CheckedOutputStream(fout, new CRC32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(chc));
            System.out.println("开始递归压缩……");
            zip(out, src, src.getName());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 递归压缩文件夹
     *
     * @param out     输出流
     * @param srcFile 压缩文件名
     * @param path    压缩文件路径
     */
    public static void zip(ZipOutputStream out, File srcFile, String path) {

        try {
            if (srcFile.isDirectory()) {
                System.out.println("压缩文件夹" + srcFile.getName());
                File[] f = srcFile.listFiles();
                out.putNextEntry(new org.apache.tools.zip.ZipEntry(path + "/"));
                path = path.equals("") ? "" : path + "/";

                for (int i = 0; i < f.length; i++) {
                    zip(out, f[i], path + f[i].getName());
                }
            } else {
                System.out.println("压缩文件" + path);
                out.putNextEntry(new org.apache.tools.zip.ZipEntry(path));
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        new FileInputStream(srcFile), "iso8859-1"));

                int c;
                while (-1 != (c = in.read())) {
                    out.write(c);
                }
                in.close();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * 定义解压缩zip文件的方法。
     * 未解决中文文件名问题
     *
     * @param zipFileName
     * @param outputDirectory
     */
    public static void unZip(String zipFileName, String outputDirectory) {
        try {
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(
                    new FileInputStream(zipFileName)));
            BufferedOutputStream bos = null;
            //获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
            //当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
            //输入流读取完成；
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                System.out.println("unziping " + entry.getName());

                //创建以zip包文件名为目录名的根目录
                File f = new File(outputDirectory);
                f.mkdir();
                if (entry.isDirectory()) {
                    String name = entry.getName();
                    name = name.substring(0, name.length() - 1);
                    System.out.println("name " + name);
                    f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                    System.out.println("mkdir " + outputDirectory
                            + File.separator + name);
                } else {
                    f = new File(outputDirectory + File.separator
                            + entry.getName());
                    f.createNewFile();
                    FileOutputStream out = new FileOutputStream(f);
                    bos = new BufferedOutputStream(out, BUFFER);
                    int b;
                    byte data[] = new byte[BUFFER];
                    while ((b = in.read(data, 0, BUFFER)) != -1) {
                        bos.write(data, 0, b);
                    }
                    bos.close();
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置缓冲区大小
     *
     * @param size
     */
    public static void setBuffer(int size) {
        BUFFER = size;
    }

    /**
     * 设置字符编码
     *
     * @param size
     */
    public static void setEncoding(String encoding) {
        ENCODING = encoding;
    }

    public static void main(String[] args) throws IOException {

        //String path = "F:\\收藏\\图片\\壁纸";
        String path2 = "F:\\收藏\\图片\\壁纸.zip";
        String path3 = "E:";

        //ZipUtil.zip(path, path2);
        //System.out.println("压缩成功！");


       ZipUtil.unZip(path2, path3);
        //System.out.println("解压成功！");
    }

}
package com.zuji.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

/**
 * 生成二维码工具类
 *
 * @author Ink-足迹
 * @create 2018-05-16 18:36
 **/
public class QRCodeUtil {
    private static final String CHARSET = "UTF-8";
    private static final String FORMAT_NAME = "PNG";

    /**
     * 二维码尺寸
     */
    private static final int QRCODE_SIZE = 300;

    /**
     * LOGO宽度
     */
    private static final int WIDTH = 60;

    /**
     * LOGO高度
     */
    private static final int HEIGHT = 60;

    /**
     * 创建二维码图片
     *
     * @param content      二维码内容
     * @param logoImgPath  Logo
     * @param needCompress 是否压缩Logo
     * @param isClarity    是否透明底
     * @return 返回二维码图片
     * @throws IOException BufferedImage
     */
    private static BufferedImage createImage(String content, String logoImgPath, boolean needCompress, boolean isClarity) throws WriterException, IOException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>(4);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);

        // 纠错等级，纠错等级越高存储信息越少
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        // 边距
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        // 选择支持透明的类型
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitMatrix.get(x, y)) {
                    image.setRGB(x, y, Color.black.getRGB());
                } else {
                    image.setRGB(x, y, isClarity ? 0x00FFFFFF : Color.white.getRGB());
                }
            }
        }
        if (logoImgPath == null || "".equals(logoImgPath)) {
            return image;
        }

        // 插入图片
        QRCodeUtil.insertImage(image, logoImgPath, needCompress);
        return image;
    }

    /**
     * 添加Logo
     *
     * @param source       二维码图片
     * @param logoImgPath  Logo
     * @param needCompress 是否压缩Logo
     * @throws IOException void
     */
    private static void insertImage(BufferedImage source, String logoImgPath, boolean needCompress) throws IOException {
        File file = new File(logoImgPath);
        if (!file.exists()) {
            return;
        }

        Image src = ImageIO.read(new File(logoImgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        // 压缩LOGO
        if (needCompress) {
            if (width > WIDTH) {
                width = WIDTH;
            }

            if (height > HEIGHT) {
                height = HEIGHT;
            }

            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            // 绘制缩小后的图
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }

        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成带Logo的二维码
     *
     * @param content      二维码内容
     * @param logoImgPath  Logo
     * @param destPath     二维码输出路径
     * @param needCompress 是否压缩Logo
     * @throws Exception void
     */
    public static void encode(String content, String logoImgPath, String destPath, boolean needCompress, boolean isClarity) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoImgPath, needCompress, isClarity);
        makedirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }

    /**
     * 生成不带Logo的二维码
     *
     * @param content  二维码内容
     * @param destPath 二维码输出路径
     * @throws Exception void
     */
    public static void encode(String content, String destPath, boolean isClarity) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false, isClarity);
    }

    /**
     * 生成不带Logo的二维码
     *
     * @param content 二维码内容
     * @param file    二维码输出文件
     * @throws Exception void
     */
    public static void encode(String content, File file, boolean isClarity) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, null, false, isClarity);
        ImageIO.write(image, FORMAT_NAME, file);
    }

    /**
     * 生成不带Logo的二维码，并输出到输入流
     *
     * @param content 二维码内容
     * @return 返回 InputStream
     * @throws Exception
     */
    public static InputStream encode(String content, boolean isClarity) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, null, false, isClarity);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(image, FORMAT_NAME, imOut);
        InputStream is = new ByteArrayInputStream(bs.toByteArray());
        return is;
    }

    /**
     * 生成带Logo的二维码，并输出到指定的输出流
     *
     * @param content      二维码内容
     * @param logoImgPath  Logo
     * @param output       输出流
     * @param needCompress 是否压缩Logo
     * @throws Exception void
     */
    public static void encode(String content, String logoImgPath, OutputStream output, boolean needCompress, boolean isClarity) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoImgPath, needCompress, isClarity);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * 生成不带Logo的二维码，并输出到指定的输出流
     *
     * @param content 二维码内容
     * @param output  输出流
     * @throws Exception void
     */
    public static void encode(String content, OutputStream output, boolean isClarity) throws Exception {
        QRCodeUtil.encode(content, null, output, false, isClarity);
    }

    /**
     * 二维码解析
     *
     * @param file 二维码
     * @return 返回解析得到的二维码内容
     * @throws Exception String
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 二维码解析
     *
     * @param path 二维码存储位置
     * @return 返回解析得到的二维码内容
     * @throws Exception String
     */

    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }

    /**
     * 新建文件夹
     *
     * @param dir
     */
    public static void makedirs(String dir) {
        if (StringUtils.isEmpty(dir)) return;

        File file = new File(dir);
        if (file.isDirectory()) return;
        else file.mkdirs();

    }

    /*public static void main(String[] args) {
        try {
            String dir = "D://1.jpg";
            QRCodeUtil.encode("http://www.baidu.com/", dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}

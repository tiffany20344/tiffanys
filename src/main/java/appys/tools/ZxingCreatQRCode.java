package appys.tools;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public  class ZxingCreatQRCode {
    public static void createQRcode(int name,String url){
        int width = 300;
        int height = 300;
        String format = "png";
        String content = "你咋不上天呢?";
        String urlcontent =url+name ;//链接的写法
        //定义二维码参数
        Map hints = new HashMap();
//      hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//文字编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//容错级别
        hints.put(EncodeHintType.MARGIN, 2);//图片边距

        //生成二维码
        try {
            urlcontent = new String(urlcontent.getBytes("UTF-8"), "ISO-8859-1");//微信,UC可识别的编码格式
            BitMatrix bitMatrix = new MultiFormatWriter().encode(urlcontent, BarcodeFormat.QR_CODE, width, height);
            Path file = new File("D:/Study/AppInfos/src/main/webapp/statics/images/"+name+".png").toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file);
           Path file1 = new File("D:/Study/AppInfos/out/artifacts/AppInfos/statics/images/"+name+".png").toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file1);
            Path file2 = new File("D:/Study/AppInfos/target/AppInfos/statics/images/"+name+".png").toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int width = 500;
        int height = 500;
        String format = "png";
        String content = "你咋不上天呢?";
        String urlcontent = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx41545d22414fc3ab&redirect_uri=http://32d2fde0.ngrok.io/api/v1/wechat/token&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirectQ";//链接的写法
        //定义二维码参数
        Map hints = new HashMap();
//      hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//文字编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);//容错级别
        hints.put(EncodeHintType.MARGIN,5);//图片边距

        //生成二维码
        try {
            urlcontent = new String(urlcontent.getBytes("UTF-8"), "ISO-8859-1");//微信,UC可识别的编码格式
            BitMatrix bitMatrix = new MultiFormatWriter().encode(urlcontent, BarcodeFormat.QR_CODE, width, height);
            Path file = new File("D:/code/img.png").toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

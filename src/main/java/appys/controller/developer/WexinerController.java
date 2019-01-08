package appys.controller.developer;

import appys.pojo.MyX509TrustManager;
import appys.tools.ZxingCreatQRCode;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/wechat")
public class WexinerController {
    /**
     * 微信开放平台二维码连接
     */
    private final static String OPEN_QRCODE_URL= "https://open.weixin.qq.com/connect/oauth2/authorize?appid={APPID}&redirect_uri={REUTL}/api/v1/wechat/token&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
    //https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=#wechat_redirect

    /**
     * 开放平台回调url
     * 注意：test16web.tunnel.qydev.com 域名地址要和在微信端 回调域名配置 地址一直，否则会报回调地址参数错误
     */
        private final static String OPEN_REDIRECT_URL= "http://32d2fde0.ngrok.io";

    /**
     * 微信审核通过后的appid
     */
    private final static String OPEN_APPID= "wx7f836278fc2c2e87";
    /**
     * 公众平台密码嘛
     */
    private final  static  String OPEN_SECRET="70feff29dc5b4accdb95e0b7cb185441";
    /**
     * 拼装微信扫一扫登录url
     */
    /*@RequestMapping(value = "login_url",method = RequestMethod.GET)
    public void  loginUrl(HttpServletResponse response) throws Exception {

        //官方文档说明需要进行编码
        String callbackUrl = URLEncoder.encode(OPEN_REDIRECT_URL,"UTF-8"); //进行编码

        //格式化，返回拼接后的url，去调微信的二维码
        String qrcodeUrl = String.format(OPEN_QRCODE_URL,OPEN_APPID);
        response.sendRedirect(qrcodeUrl);
    }*/

    @RequestMapping("/token")
    public void gettoken(String code, HttpServletRequest request) throws NoSuchProviderException, NoSuchAlgorithmException, IOException, KeyManagementException {
        //修改appID，secret
        String a = request.getParameter("code");
        String tokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx7f836278fc2c2e87&secret=70feff29dc5b4accdb95e0b7cb185441&code="+a+"&grant_type=authorization_code";
        //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx7f836278fc2c2e87&secret=70feff29dc5b4accdb95e0b7cb185441
        //建立连接
            URL url = new URL(tokenUrl);
        HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
        TrustManager[] tm = { new MyX509TrustManager() };
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        httpUrlConn.setSSLSocketFactory(ssf);
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);

        // 设置请求方式（GET/POST）
        httpUrlConn.setRequestMethod("GET");

        // 取得输入流
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        //读取响应内容
        StringBuffer buffer = new StringBuffer();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        // 释放资源
        inputStream.close();
        httpUrlConn.disconnect();
        //输出返回结果
        Map json = (Map) JSONObject.parse(String.valueOf(buffer));
        System.out.println("这个是用JSONObject类的parse方法来解析JSON字符串!!!");
        for (Object map : json.entrySet()){
            System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());
        }
        request.getSession().getServletContext().setAttribute("1",json);
    }
    @RequestMapping("/map")
    @ResponseBody
    public Object getmap(HttpServletRequest request,HttpSession session){
        int b= (int)session.getAttribute("ab");
        File file = new File("D:/Study/AppInfos/src/main/webapp/statics/images/"+b+".png");
        File file1 = new File("D:/Study/AppInfos/out/artifacts/AppInfos/statics/images/"+b+".png");
        File file2 = new File("D:/Study/AppInfos/target/AppInfos/statics/images/"+b+".png");
        file.delete();
        file1.delete();
        file2.delete();
        Map json = (Map) request.getSession().getServletContext().getAttribute("1");
        return JSONArray.toJSONString(json);
    }

    @RequestMapping("/userinfo")
    public String userinfo(HttpSession session,HttpServletRequest request,String token,String opid)  throws NoSuchProviderException, NoSuchAlgorithmException, IOException, KeyManagementException{
        //修改appID，secret
        Map json = (Map) request.getSession().getServletContext().getAttribute("1");
        String tokenUrl="https://api.weixin.qq.com/sns/userinfo?access_token="+json.get("access_token")+"&openid="+json.get("openid")+"&lang=zh_C";
        //建立连接
        URL url = new URL(tokenUrl);
        HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
        TrustManager[] tm = { new MyX509TrustManager() };
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        httpUrlConn.setSSLSocketFactory(ssf);
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);

        // 设置请求方式（GET/POST）
        httpUrlConn.setRequestMethod("GET");

        // 取得输入流
        InputStream inputStream = httpUrlConn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        //读取响应内容
        StringBuffer buffer = new StringBuffer();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        // 释放资源
        inputStream.close();
        httpUrlConn.disconnect();
        //输出返回结果
        Map jsons = (Map) JSONObject.parse(String.valueOf(buffer));
        System.out.println("这个是用JSONObject类的parse方法来解析JSON字符串!!!");
        for (Object map : json.entrySet()){
            System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());
        }

        request.getSession().getServletContext().removeAttribute("1");
        request.getSession().getServletContext().setAttribute("user",jsons);
        return "backendlogin";
    }
    @RequestMapping("/createQ")
    @ResponseBody
    public Object jiexi(HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {
        String wxLoginurl = OPEN_QRCODE_URL;
        wxLoginurl = wxLoginurl.replace("{APPID}", OPEN_APPID).replace("{REUTL}", OPEN_REDIRECT_URL);
        wxLoginurl = response.encodeURL(wxLoginurl);
        int a = (int)(30+Math.random()*(1000-30+1));
        ZxingCreatQRCode.createQRcode(a, wxLoginurl);
        session.setAttribute("ab",a);
        String uri ="<img src='statics/images/"+a+".png'>";
        return uri;
    }

}
package appys.controller.developer;

import appys.tools.CheckoutUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/weixin")
public class WxLoginController {

    String WX_SCAN_CODE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid={APPID}&redirect_uri={REUTL}&response_type=code&scope=snsapi_login&state={STATE}#wechat_redirect";
    // 千万要记住，这个是微信开放平台的APPID
    String WX_PLATFROM_APPID = "wx41545d22414fc3ab";
    // 你的回调地址
    String scanReUrl = "http://15d87161.ngrok.io/weixin/wxLoginCallback";

    /**
     * 微信扫码登陆
     *
     * @param request
     * @param response
     */
   /* @RequestMapping(value = "weixinScanLogin", method = RequestMethod.GET)
    public void weixinRetrun(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取回调url(非必填，只是附带上你扫码之前要进入的网址，具体看业务是否需要)
        String url = request.getParameter("reurl");
        // 拼接扫码登录url
        String wxLoginurl = WX_SCAN_CODE_URL;
        wxLoginurl = wxLoginurl.replace("{APPID}", WX_PLATFROM_APPID).replace("{REUTL}", scanReUrl).replace("{STATE}",
                url);
        wxLoginurl = response.encodeURL(wxLoginurl);
        response.sendRedirect(wxLoginurl);
    }*/


    /**
     * 微信消息接收和token验证
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/ownerCheck")
    public void ownerCheck( HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(111);
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter print;
        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
                try {
                    print = response.getWriter();
                    print.write(echostr);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

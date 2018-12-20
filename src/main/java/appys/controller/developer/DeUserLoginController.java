package appys.controller.developer;

import appys.pojo.DevUser;
import appys.service.developer.DevUserService;
import appys.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dev")
public class DeUserLoginController {
    @Autowired
    private DevUserService devUserService;


    @RequestMapping("/dologin")
    public  String dologin(String devCode, String devPassword , HttpSession session) throws Exception {
        try {
            DevUser devUser = devUserService.login(devCode, devPassword);
            if(devUser!=null){
                session.setAttribute(Constants.DEV_USER_SESSION,devUser);
                return "redirect:/dev/main";
            }
            return "devlogin";
        } catch (Exception e) {
            e.printStackTrace();
            return "devlogin";
        }
    }

    @RequestMapping("/login")
    public String login(){
        return "devlogin";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        if (session.getAttribute(Constants.DEV_USER_SESSION)!=null){
            session.invalidate();
        }
        return  "main";
    }

    @RequestMapping("/main")
    public String main (){
        return  "developer/main";
    }
}

package appys.controller.backend;

import appys.pojo.BackendUser;
import appys.service.backend.BackendUserService;
import appys.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/mag")
public class UserLoginController {

    @Autowired
    private BackendUserService backendUserService;

    @RequestMapping("/login")
    public String login(){
        return "backendlogin";
    }


    @RequestMapping("/dologin")
    public String BackendUserlLogin(HttpSession session, String userCode,String userPassword, HttpServletRequest request){
        if(userCode!=null){
            try {
               BackendUser backendUser= backendUserService.login(userCode,userPassword);
               if (backendUser!=null){
                   session.setAttribute(Constants.USER_SESSION,backendUser);
                   return "redirect:/mag/main";
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "backendlogin";
    }
    @RequestMapping("/logout")
    public String BackendUserLogout(HttpSession session){
        session.removeAttribute(Constants.USER_SESSION);
        return "backendlogin";
    }

    @RequestMapping("/main")
    public String main (){
        return  "backend/main";
    }

}

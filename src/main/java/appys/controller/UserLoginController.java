package appys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class UserLoginController {

    @RequestMapping("/login")
    public String login(){
        return "backendlogin";
    }

    public String BackendUserlLogin(HttpSession session, String userCode, HttpServletRequest request){
        if(userCode!=null){

        }
        return null;
    }

}

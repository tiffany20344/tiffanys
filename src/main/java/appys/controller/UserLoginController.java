package appys.controller;

import appys.service.BackendUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manager")
public class UserLoginController {

    @Autowired
    private BackendUserService backendUserService;

    @RequestMapping("/login")
    public String login(){
        return "backendlogin";
    }

    public String BackendUserlLogin(HttpSession session, String userCode,String password, HttpServletRequest request){
        if(userCode!=null){

        }
        return null;
    }

}

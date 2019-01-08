package appys.controller.developer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import appys.pojo.DevUser;
import appys.service.developer.DevUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/dev")
@Api(tags = "开发者controller",description = "开发者登陆接口")
public class DveUserLoginController {
    @Resource
    private DevUserService devUserService;


    @RequestMapping(value = "/tologin",method = RequestMethod.GET)
    @ApiOperation(value="去到开发者平台登陆",httpMethod = "get")
    public String toLogin(){
        return "dveLogin";
    }


    @RequestMapping(value="/login",method=RequestMethod.POST)
    @ApiOperation(value="开发者登陆", httpMethod = "post")
    public String login(@ApiParam(name = "name",value = "用户名",required = true)String name, @ApiParam(name = "pwd",value = "密码",required = true)String pwd, HttpServletRequest request) throws Exception {
        DevUser devUser = devUserService.login(name,pwd);
        if(devUser!=null){
            if(devUser.getDevPassword().equals(pwd)){
                request.getSession().setAttribute("devUserSession", devUser);
                return "dev/main";
            }
        }
        request.setAttribute("error", "用户名或密码不正确");
        return "dveLogin";
    }


    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ApiOperation(value="开发者注销",httpMethod = "get")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/index.jsp";
    }

}

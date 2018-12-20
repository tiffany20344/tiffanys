package appys.service.developer;

import appys.dao.devuser.DevUserMapper;
import appys.pojo.DevUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class DevUserServiceImpl implements DevUserService {

    @Resource
    private  DevUserMapper devUserMapper;

    @Override
    public DevUser login(String devCode, String devPassword) throws Exception {
            DevUser devUser = devUserMapper.getLoginUser(devCode);
            if(devUser!=null){
                //验证密码
                if (!devUser.getDevPassword().equals(devPassword)){
                    devUser = null;
                }
            }
            return  devUser;
    }
}

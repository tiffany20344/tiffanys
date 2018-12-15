package appys.service.impl;

import appys.dao.backenduser.BackendUserMapper;
import appys.pojo.BackendUser;
import appys.service.BackendUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BackendUserServiceImpl implements BackendUserService{

    @Resource
    private BackendUserMapper backendUserMapper;

    public BackendUser login(String userCode, String userPassword) throws Exception {
        BackendUser backendUser = backendUserMapper.getLoginCode(userCode);
        if (backendUser != null) {
            if (!backendUser.getUserPassword().equals(userPassword)) {
                backendUser = null;
            }
        }
            return  backendUser;
    }

};

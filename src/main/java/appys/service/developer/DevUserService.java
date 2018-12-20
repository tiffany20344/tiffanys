package appys.service.developer;

import appys.pojo.DevUser;

public interface DevUserService {
    /**
     * 开发者用户登录
     * @param devCode
     * @param devPassword
     * @return
     */
    public DevUser login(String devCode, String devPassword) throws Exception;


}

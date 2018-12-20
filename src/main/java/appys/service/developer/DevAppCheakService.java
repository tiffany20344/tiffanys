package appys.service.developer;

import appys.pojo.AppInfo;

public interface DevAppCheakService {

    /**
     * 开发者添加app
     * @param appInfo
     * @return
     */
    public Boolean addAppInfo(AppInfo appInfo);

    /**
     * 验证apk名称
     * @param apkName
     * @return
     */
    public AppInfo verifyApkName(String apkName);
    /**
     * 根据id获取需要修改的app信息
     * @param id
     * @return
     */
    public AppInfo getUpdInfoById(Integer id);
}


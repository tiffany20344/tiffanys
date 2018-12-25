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

    /**
     * 修改信息appinfo
     * @param
     * @return
     */
    public boolean updateInfo(AppInfo appInfo);

    /**
     * 删除logo
     * @param id
     * @return
     */
    public boolean deleApplogo(Integer id);

    /**
     * 增加app版本
     * @param vid
     * @param aid
     * @return
     */
    public boolean updAppVersion(Integer vid,Integer aid);

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean appsysdeleteAppById(Integer id) throws Exception ;

    /**
     * 上架下架
     * @param appInfo
     * @return
     */
    public boolean updAppSateStatus(AppInfo appInfo);
}


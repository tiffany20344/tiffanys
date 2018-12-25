package appys.service.developer;

import appys.pojo.AppVersion;

import java.util.List;

public interface DevAppVersionService {
    /**
     * 查看app版本信息
     * @param id
     * @return
     */
    public List<AppVersion> getAppVersionList(Integer id);

    /**
     * 增加app版本信息
     * @param appVersion
     * @return
     */
    public Boolean addAppVersion(AppVersion appVersion);
    /**
     * 修改app版本信息
     * @param appVersion
     * @return
     */
    public boolean modify(AppVersion appVersion);
    /**
     * 根据id获取app版本信息
     * @param id
     * @return
     */
    public AppVersion getAppVersion( Integer id);
    /**
     * 删除apk文件
     * @param id
     * @return
     */
    public boolean deleteApkFile( Integer id);
}

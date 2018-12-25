package appys.dao.appversion;

import appys.pojo.AppVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppVersionMapper {
    /**
     * 查看app版本信息
     * @param id
     * @return
     */
    public List<AppVersion>  getAppVersionList(@Param("id") Integer id);

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
    public AppVersion getAppVersion(@Param("id") Integer id);

    /**
     * 判断是否拥有版本
     * @param id
     * @return
     */
    public Integer getAppVersionIdById(@Param("id") Integer id);

    /**
     * 删除appbanb
     * @param id
     * @return
     */
    public boolean deleteVersionByAppId(@Param("id") Integer id);

    /**
     * 删除apk文件
     * @param id
     * @return
     */
    public boolean deleteApkFile(@Param("id") Integer id);
}

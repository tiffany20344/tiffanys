package appys.dao.appinfo;

import appys.pojo.AppInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppInfoMapper {
    /**
     * 查询app信息
     * @param currentPageNo
     * @param pageSize
     * @param querySoftwareName
     * @param queryCategoryLevel1
     * @param queryCategoryLevel2
     * @param queryCategoryLevel3
     * @param queryFlatformId
     * @param querystatus
     * @param devId
     * @return
     */
    public List<AppInfo> getList(@Param(value = "from")Integer currentPageNo,
                                 @Param(value = "pageSize")Integer pageSize,
                                 @Param(value = "SoftwareName")String querySoftwareName,
                                 @Param(value = "CategoryLevel1")Integer queryCategoryLevel1,
                                 @Param(value = "CategoryLevel2")Integer queryCategoryLevel2,
                                 @Param(value = "CategoryLevel3")Integer queryCategoryLevel3,
                                 @Param(value = "FlatformId")Integer queryFlatformId,
                                 @Param(value = "Status")Integer querystatus,
                                 @Param(value = "dId") Integer devId);

    /**
     * 分页所需记录数
     * @param querySoftwareName
     * @param queryCategoryLevel1
     * @param queryCategoryLevel2
     * @param queryCategoryLevel3
     * @param queryFlatformId
     * @param querystatus
     * @param devId
     * @return
     */
    public Integer getCount(@Param(value = "SoftwareName")String querySoftwareName,
                            @Param(value = "CategoryLevel1")Integer queryCategoryLevel1,
                            @Param(value = "CategoryLevel2")Integer queryCategoryLevel2,
                            @Param(value = "CategoryLevel3")Integer queryCategoryLevel3,
                            @Param(value = "FlatformId")Integer queryFlatformId,
                            @Param(value = "status")Integer querystatus,
                            @Param(value = "dId") Integer devId);

    /**
     * 新增app
     * @param appInfo
     * @return
     */
    public Boolean addAppInfo(AppInfo appInfo);

    /**
     * 验证apk名称
     * @param APKName
     * @return
     */
    public AppInfo verifyApkName(@Param("APKName") String APKName);

    /**
     * 根据id获取需要修改的app信息
     * @param id
     * @return
     */
    public AppInfo getUpdInfoById(@Param("id")Integer id);

    /**
     * 修改信息
     * @param appInfo
     * @return
     */
    public boolean updateInfo(AppInfo appInfo);

    /**
     * 删除logo
     * @param id
     * @return
     */
    public boolean deleApplogo(@Param("id") Integer id);

    /**
     * 增加app版本
     * @param vid
     * @param aid
     * @return
     */
    public boolean updAppVersion(@Param("vid") Integer vid,@Param("aid")Integer aid);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    public boolean deleteAppInfoById(@Param("id") Integer id);

    /**
     * 审核
     * @param id
     * @param status
     * @return
     */
    public boolean updateSatus(@Param("id") Integer id,@Param("status") Integer status);
}

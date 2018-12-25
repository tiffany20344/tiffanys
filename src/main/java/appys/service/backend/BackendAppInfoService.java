package appys.service.backend;

import appys.pojo.AppInfo;

import java.util.List;

public interface BackendAppInfoService {
    public List<AppInfo> getList(Integer currentPageNo,
                                 Integer pageSize,
                                 String querySoftwareName,
                                 Integer queryCategoryLevel1,
                                 Integer queryCategoryLevel2,
                                 Integer queryCategoryLevel3,
                                 Integer queryFlatformId,
                                 Integer querystatus,
                                 Integer devId
    );

    public Integer getCount(String querySoftwareName,
                            Integer queryCategoryLevel1,
                            Integer queryCategoryLevel2,
                            Integer queryCategoryLevel3,
                            Integer queryFlatformId,
                            Integer querystatus,
                            Integer devId);
    /**
     * 审核
     * @param id
     * @param status
     * @return
     */
    public boolean updateSatus(Integer id, Integer status);
}

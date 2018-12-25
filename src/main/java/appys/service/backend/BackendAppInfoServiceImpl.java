package appys.service.backend;

        import appys.dao.appinfo.AppInfoMapper;
        import appys.pojo.AppInfo;
        import org.springframework.stereotype.Service;

        import javax.annotation.Resource;
        import java.util.List;

@Service
public class BackendAppInfoServiceImpl implements BackendAppInfoService {

    @Resource
    private AppInfoMapper appInfoMapper;

    @Override
    public List<AppInfo> getList(Integer currentPageNo, Integer pageSize, String querySoftwareName, Integer queryCategoryLevel1, Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId, Integer querystatus, Integer devId) {
        Integer pageNo =0;
        if (currentPageNo>0){
             pageNo  =  (currentPageNo-1)*pageSize;
        }
        List<AppInfo> list = appInfoMapper.getList(pageNo,pageSize,querySoftwareName,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,querystatus,devId);
        if (list ==null){
            list = null;
        }
        return list;
    }

    @Override
    public Integer getCount(String querySoftwareName, Integer queryCategoryLevel1, Integer queryCategoryLevel2, Integer queryCategoryLevel3, Integer queryFlatformId, Integer querystatus, Integer devId) {
        Integer  count = appInfoMapper.getCount(querySoftwareName,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,querystatus,devId);
        if (count==null){
            count = 0;
        }
        return count;
    }

    @Override
    public boolean updateSatus(Integer id, Integer status) {
        boolean flag = appInfoMapper.updateSatus(id,status);
        if (flag==false){
            flag=false;
        }
        return flag;
    }

}

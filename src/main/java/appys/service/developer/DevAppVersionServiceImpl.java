package appys.service.developer;

import appys.dao.appversion.AppVersionMapper;
import appys.pojo.AppVersion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class DevAppVersionServiceImpl implements  DevAppVersionService {

    @Resource
    private AppVersionMapper appVersionMapper;

    @Override
    public List<AppVersion> getAppVersionList(Integer id) {
        List<AppVersion> list= appVersionMapper.getAppVersionList(id);
        if (list ==null){
            list= null;
        }
        return list;
    }

    @Override
    public Boolean addAppVersion(AppVersion appVersion) {
        boolean flag = appVersionMapper.addAppVersion(appVersion);
        if (flag==false){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean modify(AppVersion appVersion) {
        boolean flag = appVersionMapper.modify(appVersion);
        if (flag==flag){
            flag=false;
        }
        return  flag;
    }

    @Override
    public AppVersion getAppVersion(Integer id) {
        AppVersion appVersion = appVersionMapper.getAppVersion(id);
        if (appVersion==null){
            appVersion=null;
        }
        return  appVersion;
    }

    @Override
    public boolean deleteApkFile(Integer id) {
        boolean flag = appVersionMapper.deleteApkFile(id);
        if (flag==flag){
            flag=false;
        }
        return  flag;
    }
}

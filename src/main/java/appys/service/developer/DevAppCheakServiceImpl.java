package appys.service.developer;

import appys.dao.appinfo.AppInfoMapper;
import appys.pojo.AppInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class DevAppCheakServiceImpl implements DevAppCheakService {

    @Resource
    private AppInfoMapper appInfoMapper;


    public Boolean addAppInfo(AppInfo appInfo) {
        boolean flag = false;
        try {
            flag = appInfoMapper.addAppInfo(appInfo);
        }catch (Exception e){
            e.printStackTrace();;
        }
        return  flag;
    }

    @Override
    public AppInfo verifyApkName(String apkName) {
        AppInfo apkNames = appInfoMapper.verifyApkName(apkName);
        if (apkNames==null){
            apkNames = null;
        }
        return apkNames;
    }

    @Override
    public AppInfo getUpdInfoById(Integer id) {
        AppInfo appInfo= appInfoMapper.getUpdInfoById(id);
        if (appInfo==null){
            appInfo =null;
        }
        return appInfo;
    }
}

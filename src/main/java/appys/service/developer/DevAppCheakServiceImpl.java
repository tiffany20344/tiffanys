package appys.service.developer;

import appys.dao.appinfo.AppInfoMapper;
import appys.dao.appversion.AppVersionMapper;
import appys.pojo.AppInfo;
import appys.pojo.AppVersion;
import appys.pojo.DevUser;
import appys.tools.Constants;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DevAppCheakServiceImpl implements DevAppCheakService {

    @Resource
    private AppInfoMapper appInfoMapper;

    @Resource
    private AppVersionMapper appVersionMapper;

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

    @Override
    public boolean updateInfo(AppInfo appinfo) {
        boolean flag = false;
        try {
            flag = appInfoMapper.updateInfo(appinfo);
        }catch (Exception e){
            e.printStackTrace();;
        }
        return  flag;
    }

    @Override
    public boolean deleApplogo(Integer id) {
        boolean flag = false;
        try {
            flag = appInfoMapper.deleApplogo(id);
        }catch (Exception e){
            e.printStackTrace();;
        }
        return  flag;
    }

    @Override
    public boolean updAppVersion(Integer vid, Integer aid) {
        boolean flag = false;
        try {
            flag = appInfoMapper.updAppVersion(vid,aid);
        }catch (Exception e){
            e.printStackTrace();;
        }
        return  flag;
    }


    public boolean updAppSateStatus(AppInfo appInfo){
        /*
		 * 上架：
			1 更改status由【2 or 5】 to 4 ， 上架时间
			2 根据versionid 更新 publishStauts 为 2

			下架：
			更改status 由4给为5
		 */
        boolean flag= true;
        Integer operator = appInfo.getModifyBy();
        if(appInfo.getId()==0||operator<0){
            flag = false;
        }
        AppInfo appInfos = appInfoMapper.getUpdInfoById(appInfo.getId());
        if (appInfo==null){
            flag=false;
        }else {
            switch (appInfos.getStatus()){
                case 2: //当状态为审核通过时，可以进行上架操作
                    sangjia(appInfos,operator,4,2);
                    break;
                case 5://当状态为下架时，可以进行上架操作
                    sangjia(appInfos,operator,4,2);
                    break;
                case 4://当状态为上架时，可以进行下架操作
                    xiajia(appInfos,operator,5);
                    break;

                default:
                    return false;
            }
        }
        return  flag;
    }

    public boolean sangjia(AppInfo appInfo,Integer operator,Integer appInfStatus,Integer versionStatus){
        AppInfo _appInfo = new AppInfo();
        _appInfo.setId(appInfo.getId());
        _appInfo.setStatus(appInfStatus);
        _appInfo.setModifyBy(operator);
        _appInfo.setOffSaleDate(new Date(System.currentTimeMillis()));
        appInfoMapper.updateInfo(_appInfo);
        AppVersion appVersion = new AppVersion();
        appVersion.setId(appInfo.getVersionId());
        appVersion.setPublishStatus(versionStatus);
        appVersion.setModifyBy(operator);
        appVersion.setModifyDate(new Date(System.currentTimeMillis()));
        appVersionMapper.modify(appVersion);
        return true;
    }
    public boolean xiajia(AppInfo appInfo,Integer operator,Integer appInfStatus){
        AppInfo _appInfo = new AppInfo();
        _appInfo.setId(appInfo.getId());
        _appInfo.setStatus(appInfStatus);
        _appInfo.setModifyBy(operator);
        _appInfo.setOffSaleDate(new Date(System.currentTimeMillis()));
        appInfoMapper.updateInfo(_appInfo);
        return true;
    }


    public boolean appsysdeleteAppById(Integer id) throws Exception {
        boolean flag = false;
        int versionCount = appVersionMapper.getAppVersionIdById(id);
        List<AppVersion> appVersionList = null;
        if(versionCount>0){//1 先删版本信息
            //<1> 删除上传的apk文件
            appVersionList = appVersionMapper.getAppVersionList(id);
            for(AppVersion appVersion:appVersionList){
                if(appVersion.getApkLocPath() != null && !appVersion.getApkLocPath().equals("")){
                    File file = new File(appVersion.getApkLocPath());
                    if(file.exists()){
                        if(!file.delete())
                            throw new Exception();
                    }
                }
            }
            //<2> 删除app_version表数据
            appVersionMapper.deleteVersionByAppId(id);
        }
        //2 再删app基础信息
        //<1> 删除上传的logo图片
        AppInfo appInfo = appInfoMapper.getUpdInfoById(id);
        if(appInfo.getLogoLocPath() != null && !appInfo.getLogoLocPath().equals("")){
            File file = new File(appInfo.getLogoLocPath());
            if(file.exists()){
                if(!file.delete())
                    throw new Exception();
            }
        }
        //<2> 删除app_info表数据
        if(appInfoMapper.deleteAppInfoById(id)){
            flag = true;
        }
        return flag;
    }




}

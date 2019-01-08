package appys.controller.developer;

import appys.pojo.*;
import appys.service.backend.BackendAppCateService;
import appys.service.backend.BackendAppInfoService;
import appys.service.backend.BackendDataService;
import appys.service.developer.DevAppCheakService;
import appys.service.developer.DevAppVersionService;
import appys.tools.Constants;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/de/info")
public class DeAppCheakController {
    @Autowired
    private BackendAppInfoService backendAppInfoService;
    @Autowired
    private BackendDataService backendDataService;
    @Autowired
    private BackendAppCateService backendAppCateService;
    @Autowired
    private DevAppCheakService devAppCheakService;
    @Autowired
    private DevAppVersionService devAppVersionService;

    @RequestMapping("/app")
    public String getAppInfo(Integer currentPageNo,
                             String querySoftwareName,
                             Integer queryCategoryLevel1,
                             Integer queryCategoryLevel2,
                             Integer queryCategoryLevel3,
                             Integer queryFlatformId,
                             Integer queryStatus,
                             HttpSession session,
                             HttpServletRequest request) {
        List<AppCategory> appCategoryList3 = null;
        List<AppCategory> appCategoryList2 = null;
        DevUser devUser = (DevUser) session.getAttribute(Constants.DEV_USER_SESSION);
        int devId = devUser.getId();
        if (currentPageNo == null) {
            currentPageNo = 1;
        }
        if (currentPageNo <= 0) {
            currentPageNo = 1;
        }
        Integer pageSize = 5;
        int pageCount = backendAppInfoService.getCount(querySoftwareName, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, queryStatus, devId);
        int page = pageCount % pageSize == 0 ? pageCount / pageSize : pageCount / pageSize + 1;
        if (currentPageNo > page) {
            currentPageNo = page;
        }
        List<DataDictionary> datalist = backendDataService.getList("APP_FLATFORM");
        List<DataDictionary> statusList = backendDataService.getList("APP_STATUS");

        List<AppCategory> appCategoryList = backendAppCateService.getAppCategoryList(null);
        if (queryCategoryLevel1 != null) {
            appCategoryList2 = backendAppCateService.getAppCategoryList(queryCategoryLevel1);
        }
        if (queryCategoryLevel2 != null) {
            appCategoryList3 = backendAppCateService.getAppCategoryList(queryCategoryLevel2);
        }
        List<AppInfo> appInfoList = backendAppInfoService.getList(currentPageNo, pageSize, querySoftwareName, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId, queryStatus, devId);
        request.setAttribute("totalPageCount", page);
        request.setAttribute("totalCount", pageCount);
        request.setAttribute("pageNo", currentPageNo);
        request.setAttribute("querySoftwareName", querySoftwareName);
        request.setAttribute("queryCategoryLevel1", queryCategoryLevel1);
        request.setAttribute("queryCategoryLevel2", queryCategoryLevel2);
        request.setAttribute("queryCategoryLevel3", queryCategoryLevel3);
        request.setAttribute("categoryLevel2List", appCategoryList2);
        request.setAttribute("categoryLevel3List", appCategoryList3);
        request.setAttribute("queryFlatformId", queryFlatformId);
        request.setAttribute("queryStatus", queryStatus);
        request.setAttribute("datalist", datalist);
        request.setAttribute("statusList", statusList);
        request.setAttribute("appCategoryList1", appCategoryList);
        session.setAttribute("applist", appInfoList);
        return "developer/appinfolist";
    }

    @RequestMapping("/category")
    @ResponseBody
    public Object category(Integer parentId) {
        List<AppCategory> appCategoryList = backendAppCateService.getAppCategoryList(parentId);
        return JSONArray.toJSONString(appCategoryList);
    }

    @RequestMapping("/addInfo")
    public String addAppinfo(AppInfo appInfo, HttpSession session, HttpServletRequest request,
                             @RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach) {
        String logoPicPath = null;//LOGO图片url路径
        String logoLocPath = null;//LOGO图片服务器存储路径
        if (!attach.isEmpty()) {
            String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
            String oldName = attach.getOriginalFilename();//获取原文件名
            String prefix = FilenameUtils.getExtension(oldName);//原文件后缀
            if (attach.getSize() > 500000) {//文件大小
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
                return "developer/appinfoadd";
            } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")) {//文件格式
                String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.apk
                File targetFile = new File(path, fileName);//创建一个文件流
                if (!targetFile.exists()) {//如果不存在新建多个文件夹
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);//转存文件
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
                }
                logoPicPath = "/statics/uploadfiles/" + fileName;
                logoLocPath = path + File.separator + fileName;
            } else {
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
                return "developer/appinfoadd";
            }
        }
        appInfo.setDevId(((DevUser) request.getSession().getAttribute(Constants.DEV_USER_SESSION)).getId());
        appInfo.setCreationDate(new Date());
        appInfo.setLogoPicPath(logoPicPath);
        appInfo.setLogoLocPath(logoLocPath);
        appInfo.setDevId(((DevUser) session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appInfo.setStatus(1);
        try {
            if (devAppCheakService.addAppInfo(appInfo)) {
                return "redirect:/de/info/app";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "developer/appinfoadd";
    }

    @RequestMapping("/flatform")
    @ResponseBody
    public Object flatformList(String typeCode) {
        List<DataDictionary> datalist = backendDataService.getList(typeCode);
        return JSONArray.toJSONString(datalist);
    }

    @RequestMapping("/addAppInfo")
    public String addRedirect() {
        return "developer/appinfoadd";
    }

    @RequestMapping("/verifyApkName")
    @ResponseBody
    public Object verifyApkName(String APKName) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        AppInfo name = devAppCheakService.verifyApkName(APKName);
        if (APKName.equals(null)||APKName.equals("")) {
            resultMap.put("APKName", "empty");
        }else{
            if (name == null) {
                resultMap.put("APKName", "noexist");
            } else {
                if (name.getAPKName().equals(APKName)) {
                    resultMap.put("APKName", "exist");
                }

            }
        }
        return JSONArray.toJSONString(resultMap);
    }
    @RequestMapping("/upview/{id}")
    public String updateView(HttpServletRequest request,@PathVariable Integer id){
        AppInfo appInfo = devAppCheakService.getUpdInfoById(id);
        if (appInfo!=null){
            request.setAttribute("appInfo",appInfo);
        }
        return "developer/appinfomodify";
    }

    @RequestMapping("/update")
    public String update(AppInfo appInfo,HttpSession session, HttpServletRequest request,
                         @RequestParam(value = "attach", required = false) MultipartFile attach){
        String logoPicPath = null;//LOGO图片url路径
        String logoLocPath = null;//LOGO图片服务器存储路径
        if (!attach.isEmpty()) {
            String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
            String oldName = attach.getOriginalFilename();//获取原文件名
            String prefix = FilenameUtils.getExtension(oldName);//原文件后缀
            if (attach.getSize() > 500000) {//文件大小
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
                return "developer/appinfoadd";
            } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")) {//文件格式
                String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.apk
                File targetFile = new File(path, fileName);//创建一个文件流
                if (!targetFile.exists()) {//如果不存在新建多个文件夹
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);//转存文件
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
                }
                logoPicPath = "/statics/uploadfiles/" + fileName;
                logoLocPath = path + File.separator + fileName;
            } else {
                request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
                return "developer/appinfoadd";
            }
        }
        DevUser u =(DevUser) request.getSession().getAttribute(Constants.DEV_USER_SESSION);
        int id =u.getId();
        appInfo.setModifyBy(id);
        appInfo.setModifyDate(new Date());
        appInfo.setLogoPicPath(logoPicPath);
        appInfo.setLogoLocPath(logoLocPath);
        if (appInfo.getStatus()==3){
            appInfo.setStatus(1);
        }
        try {
            boolean flage = devAppCheakService.updateInfo(appInfo);
            if (flage) {
                return "redirect:/de/info/app";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "developer/appinfomodify";
    }


    @RequestMapping("/dellogoOrapk")
    @ResponseBody
    public Object delLogoorapk(String pic,String id){
        String fileLocPath = null;
        HashMap<String,String>  resultMap = new HashMap<String,String>();
        if(pic == null || pic.equals("") ||
                id == null || id.equals("")){
            resultMap.put("result", "failed");
        }else if(pic.equals("logo")){
            try {
                fileLocPath = (devAppCheakService.getUpdInfoById(Integer.parseInt(id))).getLogoLocPath();
                File file = new File(fileLocPath);
                if(file.exists())
                    if(file.delete()){//删除服务器存储的物理文件
                        if(devAppCheakService.deleApplogo(Integer.parseInt(id))){//更新表
                            resultMap.put("result", "success");
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(pic.equals("apk")){
            fileLocPath = (devAppCheakService.getUpdInfoById(Integer.parseInt(id))).getLogoLocPath();
            File file = new File(fileLocPath);
            if(file.exists())
                if(file.delete()){//删除服务器存储的物理文件
                    if(devAppVersionService.deleteApkFile(Integer.parseInt(id))){//更新表
                        resultMap.put("result", "success");
                    }
                }
        }
            return JSONArray.toJSONString(resultMap);
    }


    @RequestMapping("/appview/{id}")
    public String loookInfo(HttpServletRequest request,@PathVariable Integer id){
        AppInfo  appInfo = devAppCheakService.getUpdInfoById(id);
        List<AppVersion> list = devAppVersionService.getAppVersionList(id);
        if (appInfo!=null && list!=null){
            request.setAttribute("appInfo",appInfo);
            request.setAttribute("appverlist",list);
        }
        return "developer/appinfoview";

    }


    @RequestMapping("/addversion")
    public String addVersiontiao(HttpServletRequest request ,Integer id,
                                 @RequestParam(value="error",required= false)String fileUploadError,AppVersion appVersion){
        if(null != fileUploadError && fileUploadError.equals("error1")){
            fileUploadError = Constants.FILEUPLOAD_ERROR_1;
        }else if(null != fileUploadError && fileUploadError.equals("error2")){
            fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
        }else if(null != fileUploadError && fileUploadError.equals("error3")){
            fileUploadError = Constants.FILEUPLOAD_ERROR_3;
        }
        appVersion.setAppId(id);
        List<AppVersion> appVersionList = null;
        try {
            appVersionList = devAppVersionService.getAppVersionList(id);
            appVersion.setAppName(devAppCheakService.getUpdInfoById(id).getSoftwareName());
        }catch (Exception e){
            e.printStackTrace();
        }
        request.setAttribute("appVersionList",appVersionList);
        request.setAttribute("appVersion",appVersion);
        request.setAttribute("fileUploadError",fileUploadError);
        return  "developer/appversionadd";

    }

    @RequestMapping("/appversionmodify")
    public String modifyVersiontiao(HttpServletRequest request ,Integer vid,
                                    Integer aid,
                                 @RequestParam(value="error",required= false)String fileUploadError,AppVersion appVersion){
        if(null != fileUploadError && fileUploadError.equals("error1")){
            fileUploadError = Constants.FILEUPLOAD_ERROR_1;
        }else if(null != fileUploadError && fileUploadError.equals("error2")){
            fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
          fileUploadError = Constants.FILEUPLOAD_ERROR_3;
        }
        List<AppVersion> appVersionList = null;
        try {
            appVersion = devAppVersionService.getAppVersion(vid);
            appVersionList = devAppVersionService.getAppVersionList(aid);
        }catch (Exception e){
            e.printStackTrace();
        }
        request.setAttribute("appVersionList",appVersionList);
        request.setAttribute("appVersions",appVersion);
        request.setAttribute("fileUploadError",fileUploadError);
        return  "redirect:/de/info/app";

    }


    @RequestMapping("addversions")
    public String addAppVersion(HttpSession session,AppVersion appVersion,HttpServletRequest request,@RequestParam(value="a_downloadLink",required= false)MultipartFile attach){
        String downloadLink =  null;
        String apkLocPath = null;
        String apkFileName = null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
                String apkName = null;
                try {
                    apkName = devAppCheakService.getUpdInfoById(appVersion.getAppId()).getAPKName();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if(apkName == null || "".equals(apkName)){
                    return "redirect:/de/flatform/app/appversionadd?id="+appVersion.getAppId()
                            +"&error=error1";
                }
                apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
                File targetFile = new File(path,apkFileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "redirect:/de/info/addversion?id="+appVersion.getAppId()
                            +"&error=error2";
                }
                downloadLink = "/statics/uploadfiles/"+apkFileName;//下载路径
                apkLocPath = path+File.separator+apkFileName;
            }else{
                return "redirect:/de/info/addversion?id="+appVersion.getAppId()
                        +"&error=error3";
            }
        }
        appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appVersion.setCreationDate(new Date());
        appVersion.setDownloadLink(downloadLink);
        appVersion.setApkLocPath(apkLocPath);
        appVersion.setApkFileName(apkFileName);
        try {
            if(devAppVersionService.addAppVersion(appVersion)&&devAppCheakService.updAppVersion(appVersion.getId(),appVersion.getAppId())){
                return "redirect:/dev/info/app";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/de/info/addversion?id="+appVersion.getAppId();
    }


    @RequestMapping(value="/appversionmodifysave",method= RequestMethod.POST)
    public String modifyAppVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
                                       @RequestParam(value="attach",required= false) MultipartFile attach){
        String downloadLink =  null;
        String apkLocPath = null;
        String apkFileName = null;
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
                String apkName = null;
                try {
                    apkName = devAppCheakService.getUpdInfoById(appVersion.getAppId()).getAPKName();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                if(apkName == null || "".equals(apkName)){
                    return "redirect:/de/info/appversionmodify?vid="+appVersion.getId()
                            +"&aid="+appVersion.getAppId()
                            +"&error=error1";
                }
                apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
                File targetFile = new File(path,apkFileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "redirect:/de/info/appversionmodify?vid="+appVersion.getId()
                            +"&aid="+appVersion.getAppId()
                            +"&error=error2";
                }
                downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
                apkLocPath = path+File.separator+apkFileName;
            }else{
                return "redirect:/de/info/appversionmodify?vid="+appVersion.getId()
                        +"&aid="+appVersion.getAppId()
                        +"&error=error3";
            }
        }
        appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        appVersion.setModifyDate(new Date());
        appVersion.setDownloadLink(downloadLink);
        appVersion.setApkLocPath(apkLocPath);
        appVersion.setApkFileName(apkFileName);
        try {
            if(devAppVersionService.modify(appVersion)){
                return "redirect:/dev/flatform/app/list";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "developer/appversionmodify";
    }


    @RequestMapping(value="/delapp")
    @ResponseBody
    public Object delApp(@RequestParam String id){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(id)){
            resultMap.put("delResult", "notexist");
        }else{
            try {
                if(devAppCheakService.appsysdeleteAppById(Integer.parseInt(id)))
                    resultMap.put("delResult", "true");
                else
                    resultMap.put("delResult", "false");
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }


    @RequestMapping(value="/{appid}/sale",method= RequestMethod.PUT)
    @ResponseBody
    public Object sale(@PathVariable String appid, HttpSession session){
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        Integer appIdInteger = 0;
        try{
            appIdInteger = Integer.parseInt(appid);
        }catch(Exception e){
            appIdInteger = 0;
        }
        resultMap.put("errorCode", "0");
        resultMap.put("appId", appid);
        if(appIdInteger>0){
            try {
                DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
                AppInfo appInfo = new AppInfo();
                appInfo.setId(appIdInteger);
                appInfo.setModifyBy(devUser.getId());
                if(devAppCheakService.updAppSateStatus(appInfo)){
                    resultMap.put("resultMsg", "success");
                }else{
                    resultMap.put("resultMsg", "success");
                }
            } catch (Exception e) {
                resultMap.put("errorCode", "exception000001");
            }
        }else{
            //errorCode:0为正常
            resultMap.put("errorCode", "param000001");
        }

		/*
		 * resultMsg:success/failed
		 * errorCode:exception000001
		 * appId:appId
		 * errorCode:param000001
		 */
        return resultMap;
    }


}
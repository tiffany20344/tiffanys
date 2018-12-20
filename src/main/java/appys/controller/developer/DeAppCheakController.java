package appys.controller.developer;

import appys.pojo.AppCategory;
import appys.pojo.AppInfo;
import appys.pojo.DataDictionary;
import appys.pojo.DevUser;
import appys.service.backend.BackendAppCateService;
import appys.service.backend.BackendAppInfoService;
import appys.service.backend.BackendDataService;
import appys.service.developer.DevAppCheakService;
import appys.tools.Constants;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
            String path = request.getSession().getServletContext().getRealPath("statis" + File.separator + "uploadfiles");
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
                logoPicPath = request.getContextPath() + "/statics/uploadfiles/" + fileName;
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
}
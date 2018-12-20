package appys.controller.backend;

import appys.pojo.AppCategory;
import appys.pojo.AppInfo;
import appys.pojo.DataDictionary;
import appys.service.backend.BackendAppCateService;
import appys.service.backend.BackendAppInfoService;
import appys.service.backend.BackendDataService;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/mag/info")
public class BackendUserController {

    @Autowired
    private BackendAppInfoService backendAppInfoService;
    @Autowired
    private BackendDataService backendDataService;
    @Autowired
    private BackendAppCateService backendAppCateService;

    @RequestMapping("/app")
    public  String getAppInfo(Integer currentPageNo,
                              String querySoftwareName,
                              Integer queryCategoryLevel1,
                              Integer queryCategoryLevel2,
                              Integer queryCategoryLevel3,
                              Integer queryFlatformId,
                              HttpSession session,
                              HttpServletRequest request){
        List<AppCategory> appCategoryList3=null;
        List<AppCategory> appCategoryList2=null;
        Integer devId = null;
        Integer querystatus =1;
        if(currentPageNo==null){
            currentPageNo=1;
        }
        if(currentPageNo<=0){
            currentPageNo=1;
        }
        Integer pageSize=5;
        int pageCount = backendAppInfoService.getCount(querySoftwareName,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,querystatus,devId);
        int page = pageCount%pageSize ==0?pageCount/pageSize:pageCount/pageSize+1;
        if (currentPageNo>page){
            currentPageNo= page;
        }
        List<DataDictionary> datalist = backendDataService.getList("APP_FLATFORM");
        List<AppCategory> appCategoryList =backendAppCateService.getAppCategoryList(null);
        if(queryCategoryLevel1!=null){
            appCategoryList2 =backendAppCateService.getAppCategoryList(queryCategoryLevel1);
        }
        if(queryCategoryLevel2!=null){
            appCategoryList3 =backendAppCateService.getAppCategoryList(queryCategoryLevel2);
        }
        List<AppInfo> appInfoList = backendAppInfoService.getList(currentPageNo,pageSize,querySoftwareName,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,queryFlatformId,querystatus,devId);
        request.setAttribute("totalPageCount",page);
        request.setAttribute("totalCount",pageCount);
        request.setAttribute("pageNo",currentPageNo);
        request.setAttribute("querySoftwareName",querySoftwareName);
        request.setAttribute("queryCategoryLevel1",queryCategoryLevel1);
        request.setAttribute("queryCategoryLevel2",queryCategoryLevel2);
        request.setAttribute("queryCategoryLevel3",queryCategoryLevel3);
        request.setAttribute("categoryLevel2List",appCategoryList2);
        request.setAttribute("categoryLevel3List",appCategoryList3);
        request.setAttribute("queryFlatformId",queryFlatformId);
        request.setAttribute("datalist",datalist);
        request.setAttribute("appCategoryList1",appCategoryList);
        session.setAttribute("applist",appInfoList);
        return  "backend/applist";
    }

    @RequestMapping("/category")
    @ResponseBody
    public  Object category(Integer parentId){
        List<AppCategory> appCategoryList =backendAppCateService.getAppCategoryList(parentId);
        return JSONArray.toJSONString(appCategoryList);
    }
}

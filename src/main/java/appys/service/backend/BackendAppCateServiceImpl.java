package appys.service.backend;

import appys.dao.appcategory.AppCategoryMapper;
import appys.pojo.AppCategory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class BackendAppCateServiceImpl implements BackendAppCateService {

    @Resource
    private AppCategoryMapper appCategoryMapper;


    public List<AppCategory> getAppCategoryList(Integer parentId) {
        List<AppCategory> list =   appCategoryMapper.getAppCategoryList(parentId);
        if (list ==null){
            list = null;
        }
        return list;
    }
}

package appys.service.backend;

import appys.pojo.AppCategory;

import java.util.List;

public interface BackendAppCateService {
    public List<AppCategory> getAppCategoryList(Integer parentId);
}

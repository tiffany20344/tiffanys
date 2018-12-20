package appys.dao.appcategory;

import appys.pojo.AppCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppCategoryMapper {
    public List<AppCategory> getAppCategoryList(@Param("parentId") Integer parentId);
}

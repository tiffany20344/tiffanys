package appys.dao.backenduser;

import appys.pojo.BackendUser;
import org.apache.ibatis.annotations.Param;

public interface BackendUserMapper {
    public BackendUser getLoginCode(@Param("userCode") String userCode);
}

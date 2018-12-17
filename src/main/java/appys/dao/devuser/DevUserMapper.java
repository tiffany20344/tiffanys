package appys.dao.devuser;

import appys.pojo.DevUser;
import org.apache.ibatis.annotations.Param;

public interface DevUserMapper {
    public DevUser getLoginUser(@Param("devCode") String devCode);
}

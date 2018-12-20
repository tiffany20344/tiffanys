package appys.dao.datadictionary;

import appys.pojo.DataDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DatadictionaryMapper {
    public List<DataDictionary> getList(@Param("typeCode") String typeCode);
}

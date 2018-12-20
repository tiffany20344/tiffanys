package appys.service.backend;

import appys.dao.datadictionary.DatadictionaryMapper;
import appys.pojo.DataDictionary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class BackendDataServiceImpl implements BackendDataService {

    @Resource
    private DatadictionaryMapper datadictionaryMapper;

    @Override
    public List<DataDictionary> getList(String typeCode) {
        List<DataDictionary> list = datadictionaryMapper.getList(typeCode);
        if (list==null){
            list=null;
        }
        return list;
    }
}

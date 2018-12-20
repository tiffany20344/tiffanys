package appys.service.backend;

import appys.pojo.DataDictionary;

import java.util.List;

public interface BackendDataService {
    public List<DataDictionary> getList(String typeCode);
}

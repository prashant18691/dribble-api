package com.prs.dribbleapi.dao;

import java.util.List;
import com.prs.dribbleapi.dto.Company;
import com.prs.dribbleapi.request.SearchRequest;


public interface DribbleDao {
    public List<Object[]> search(SearchRequest searchRequest);

    public void save(Company request);
}

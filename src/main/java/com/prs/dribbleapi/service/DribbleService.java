package com.prs.dribbleapi.service;

import java.util.List;
import com.prs.dribbleapi.dto.Company;
import com.prs.dribbleapi.request.SearchRequest;
import com.prs.dribbleapi.vo.DribbleVO;


public interface DribbleService {
    public List<DribbleVO> search(SearchRequest searchRequest);

    public void save(Company company);
}

package com.prs.dribbleapi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.prs.dribbleapi.dao.DribbleDao;
import com.prs.dribbleapi.dto.Company;
import com.prs.dribbleapi.helper.DribbleHelper;
import com.prs.dribbleapi.request.SearchRequest;
import com.prs.dribbleapi.service.DribbleService;
import com.prs.dribbleapi.vo.CompanyVO;
import com.prs.dribbleapi.vo.DribbleVO;


@Service
public class DribbleServiceImpl implements DribbleService{

    @Autowired
    private DribbleDao dribbleDao;

    @Override public List<DribbleVO> search(final SearchRequest searchRequest) {

        return DribbleHelper.transformEntityToVO(dribbleDao.search(searchRequest));
    }

    @Override public void save(final CompanyVO company) {
         dribbleDao.save(DribbleHelper.transformVOToEntity(company));
    }
}

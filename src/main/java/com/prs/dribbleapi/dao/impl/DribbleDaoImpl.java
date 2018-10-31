package com.prs.dribbleapi.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.prs.dribbleapi.dao.DribbleDao;
import com.prs.dribbleapi.dto.Company;
import com.prs.dribbleapi.request.SearchRequest;

@Repository
@Transactional
public class DribbleDaoImpl implements DribbleDao{

    @PersistenceContext
    private EntityManager em;

    @Override public List<Object[]> search(final SearchRequest searchRequest) {
        String sql = "select c.companyname,c.description as company_desc, c.mainphonenumber,l.state,l.phonenumber,"
                + "l.description as loc_desc,l.province,l.country,\n"
                + "j.jobtitle,j.jobtype,j.availability_type,j.charge,j.description as job_desc,j.explevel,j.skills,j"
                + ".postedon,j.currency\n"
                + "from company c  LEFT OUTER JOIN location_details l on c.companyid = l.companyid LEFT OUTER JOIN "
                + "job_details j on l.locationid=j.locationid";

        StringBuilder filters = addFilters(searchRequest);

        if (filters!=null && filters.length()>0){
            sql+=" where "+filters.toString();
        }

        Query query = em.createNativeQuery(sql);
        List<Object[]> companyList = query.getResultList();
        return companyList;
    }

    private StringBuilder addFilters(final SearchRequest searchRequest) {
        StringBuilder filters = null;
        if (searchRequest!=null){
            filters = new StringBuilder("");
            if (searchRequest.getAvailabilityCriteria()!=null){
                filters.append("j.availability_type in "+searchRequest.getAvailabilityCriteria());
            }
            if (searchRequest.getExperienceCriteria()!=null){
                if (filters.length()>0){
                    filters.append(" and j.explevel = "+searchRequest.getExperienceCriteria());
                }
                else
                    filters.append(" j.explevel = "+searchRequest.getExperienceCriteria());
            }
            if (searchRequest.getJobType()!=null){
                if (filters.length()>0){
                    filters.append(" and j.jobtype like %"+searchRequest.getJobType()+"%");
                }
                else
                    filters.append(" j.jobtype like %"+searchRequest.getJobType()+"%");
            }
            if (searchRequest.getSkillsCriteria()!=null){
                if (filters.length()>0){
                    filters.append(" and j.skills in "+searchRequest.getSkillsCriteria());
                }
                else
                    filters.append(" j.skills in "+searchRequest.getSkillsCriteria());

            }
            if (searchRequest.getJobTitle()!=null){
                if (filters.length()>0){
                    filters.append(" and j.jobtitle like %"+searchRequest.getJobTitle()+"%");
                }
                else
                    filters.append(" j.jobtitle like %"+searchRequest.getJobTitle()+"%");
            }
            if (searchRequest.getLocation()!=null){
                String filterValue = "%"+searchRequest.getLocation()+"%";
                if (filters.length()>0){
                    filters.append(" and (l.country like "+filterValue+" or l.state like "+filterValue+" or l"
                            + ".province "
                            + "like "+filterValue+ ")");
                }
                else
                    filters.append(" (l.country like "+filterValue+" or l.state like "+filterValue+" or l.province "
                            + "like "+filterValue+ ")");
            }
            if (searchRequest.getPayRateto()!=0){
                if (filters.length()>0){
                    filters.append(" and j.charge between "+searchRequest.getPayRatefrom()+" and "+searchRequest.getPayRateto());
                }
                else
                    filters.append(" j.charge between "+searchRequest.getPayRatefrom()+" and "+searchRequest.getPayRateto());
            }
            if (searchRequest.getShowJobsWithoutPayRate().equalsIgnoreCase("NO")){
                if (filters.length()>0){
                    filters.append(" and j.charge is not null");
                }
                else
                    filters.append(" j.charge is not null");
            }
        }
        return filters;
    }

    @Override public void save(final Company company) {
        em.merge(company);
    }
}

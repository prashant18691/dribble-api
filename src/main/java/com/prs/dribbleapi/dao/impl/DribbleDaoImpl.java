package com.prs.dribbleapi.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.prs.dribbleapi.dao.DribbleDao;
import com.prs.dribbleapi.dto.Company;
import com.prs.dribbleapi.dto.Job;
import com.prs.dribbleapi.dto.Location;
import com.prs.dribbleapi.request.SearchRequest;

@Repository
@Transactional
public class DribbleDaoImpl implements DribbleDao{

    @PersistenceContext
    private EntityManager em;

    @Override public List<Company> search(final SearchRequest searchRequest) {
        String sql = "select distinct c from Company c join fetch c.locations l join "
                    + "fetch l.jobs j";

        StringBuilder filters = addFilters(searchRequest);

        if (filters!=null && filters.length()>0){
            sql+=" where "+filters.toString();
        }

        Query query = em.createQuery(sql, Company.class);
        List<Company> companyList = query.getResultList();
        em.close();
        return companyList;
    }

    private StringBuilder addFilters(final SearchRequest searchRequest) {
        StringBuilder filters = null;
        if (searchRequest!=null){
            filters = new StringBuilder("");
            if (searchRequest.getAvailabilityCriteria()!=null){
                filters.append("j.availability in "+searchRequest.getAvailabilityCriteria());
            }
            if (searchRequest.getExperienceCriteria()!=null){
                if (filters.length()>0){
                    filters.append(" and j.expLevel = "+searchRequest.getExperienceCriteria());
                }
                else
                    filters.append(" j.expLevel = "+searchRequest.getExperienceCriteria());
            }
            if (searchRequest.getJobType()!=null){
                if (filters.length()>0){
                    filters.append(" and j.jobType like %"+searchRequest.getJobType()+"%");
                }
                else
                    filters.append(" j.jobType like %"+searchRequest.getJobType()+"%");
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
                    filters.append(" and j.jobTitle like %"+searchRequest.getJobTitle()+"%");
                }
                else
                    filters.append(" j.jobTitle like %"+searchRequest.getJobTitle()+"%");
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
        String searchCompany = "select count(c) from Company c where c.companyId = :cCompanyId";
        Long companyCount = em.createQuery(searchCompany, Long.class).
                setParameter("cCompanyId",company.getCompanyId()).getSingleResult();
        if (companyCount==0) {
            em.persist(company);
        }
        else {
            for (Location eachLocation : company.getLocations()){
                String searchLocation = "select count(l) from Location l where l.locationId = :cLocationId";
                Long locationCount = em.createQuery(searchLocation, Long.class).setParameter("cLocationId",
                        eachLocation.getLocationId()).getSingleResult();
                if (locationCount==0){
                    em.persist(eachLocation);
                }
                else {
                    for (Job eachJob : eachLocation.getJobs()){
                        String searchJob = "select count(j) from Job j where j.jobId = :cJobId";
                        Long jobCount = em.createQuery(searchJob, Long.class).setParameter("cJobId",eachJob.getJobId()).getSingleResult();
                        if (jobCount==0){
                            em.persist(eachJob);
                        }
                    }
                }
            }
        }
        em.close();
    }
}

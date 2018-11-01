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
        String searchCompany = "select c from Company c where c.companyName = :cName";
        Company companyDb = em.createQuery(searchCompany, Company.class).
                setParameter("cName",company.getCompanyName()).getResultList().stream().findFirst().orElse(null);
        if (companyDb==null) {
            em.persist(company);
        }
        else {
            for (Location eachLocation : company.getLocations()){
                String searchLocation = "select l from Location l where l.state = :cState and l.province = :cProvince "
                        + "and l.company.companyId = :cCompanyId";
                Location locationDb = em.createQuery(searchLocation, Location.class).setParameter("cState",eachLocation
                        .getState()).setParameter("cProvince",eachLocation.getProvince()).
                        setParameter("cCompanyId",companyDb.getCompanyId()).getResultList().stream().findFirst()
                        .orElse(null);
                if (locationDb==null){
                    eachLocation.setCompany(companyDb);
                    em.persist(eachLocation);
                }
                else {
                    for (Job eachJob : eachLocation.getJobs()){
                        String searchJob = "select j from Job j where j.jobTitle = :cJobTitle and j.jobType = :cJobType"
                                + " and j.availability = :cAvailability and j.expLevel = :cExpLevel and j.location"
                                + ".locationId = :cLocationId";
                        Job jobDb = em.createQuery(searchJob, Job.class).setParameter("cJobTitle",eachJob.getJobTitle())
                                .setParameter("cJobType",eachJob.getJobType())
                                .setParameter("cAvailability",eachJob.getAvailability())
                                .setParameter("cExpLevel",eachJob.getExpLevel()).setParameter
                                        ("cLocationId",locationDb.getLocationId()).getResultList().stream()
                                .findFirst().orElse(null);
                        if (jobDb==null){
                            eachJob.setLocation(locationDb);
                            em.persist(eachJob);
                        }
                    }
                }
            }
        }
        em.close();
    }
}

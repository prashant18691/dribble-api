package com.prs.dribbleapi.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.bind.ValidationException;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.prs.dribbleapi.dto.Availability;
import com.prs.dribbleapi.dto.Company;
import com.prs.dribbleapi.dto.ExperienceLevel;
import com.prs.dribbleapi.dto.Job;
import com.prs.dribbleapi.dto.Location;
import com.prs.dribbleapi.request.SearchRequest;
import com.prs.dribbleapi.vo.CompanyVO;
import com.prs.dribbleapi.vo.DribbleVO;
import com.prs.dribbleapi.vo.JobVO;
import com.prs.dribbleapi.vo.LocationVO;


public class DribbleHelper {

    public static void validateSearchRequest(SearchRequest request) throws ValidationException {

        if (!CollectionUtils.isEmpty(request.getAvailabilities())){
            StringBuilder availabilityCriteria = new StringBuilder("(");
            for(String s : request.getAvailabilities()){
                if (Availability.fromValue(s.toUpperCase())==null)
                    throw new ValidationException("Invalid availability");
                availabilityCriteria.append(validateAvailabilityType(s)+",");
            }
            availabilityCriteria.deleteCharAt(availabilityCriteria.length()-1).append(")");
            request.setAvailabilityCriteria(availabilityCriteria.toString());
        }
        if (!StringUtils.isEmpty(request.getExperienceLevel())){
            if(ExperienceLevel.fromValue(request.getExperienceLevel())==null)
                throw new ValidationException("Invalid experience level");
            request.setExperienceCriteria(String.valueOf(validateExpLevel(request.getExperienceLevel())));
        }

        if (!CollectionUtils.isEmpty(request.getSkills())){
            StringBuilder skillsCriteria = new StringBuilder("(");
            for (String s : request.getSkills()){
                skillsCriteria.append(s+",");
            }
            skillsCriteria.deleteCharAt(skillsCriteria.length()-1).append(")");
            request.setSkillsCriteria(skillsCriteria.toString());
        }
        if (request.getPayRateto() < request.getPayRatefrom()){
            throw new ValidationException("payRateto must be greater than payRateFrom");
        }

        if (!request.getShowJobsWithoutPayRate().equalsIgnoreCase("YES") &&
                !request.getShowJobsWithoutPayRate().equalsIgnoreCase("NO")){
            throw new ValidationException("Enter either YES or NO for showJobsWithoutPayRate");
        }
    }

    public static void validateAndEnrichPostRequest(CompanyVO request) throws ValidationException {
        if (request==null)
            throw new ValidationException("Request body is empty");
        if (StringUtils.isEmpty(request.getCompanyName()))
            throw new ValidationException("Company Name cannot be empty");
        List<LocationVO> locations = request.getLocations();
        if (CollectionUtils.isEmpty(locations)){
            throw new ValidationException("locations cannot be empty");
        }
            for (LocationVO eachLocation:locations){
                if (StringUtils.isEmpty(eachLocation.getState()))
                    throw new ValidationException("State cannot be empty");
                if (StringUtils.isEmpty(eachLocation.getProvince()))
                    throw new ValidationException("Province cannot be empty");
                List<JobVO> jobs = eachLocation.getJobs();
                for (JobVO eachJob : jobs){
                    if (StringUtils.isEmpty(eachJob.getJobTitle()))
                        throw new ValidationException("Job Title cannot be empty");
                    if (StringUtils.isEmpty(eachJob.getJobType()))
                        throw new ValidationException("Job Type cannot be empty");
                    if (StringUtils.isEmpty(eachJob.getAvailabilityType()))
                        throw new ValidationException("Availability Type cannot be empty");
                    if (StringUtils.isEmpty(eachJob.getExperience()))
                        throw new ValidationException("Experience cannot be empty");
                    eachJob.setAvailability(validateAvailabilityType(eachJob.getAvailabilityType()));

                    if (eachJob.getAvailabilityType().equalsIgnoreCase("HOURLY")) {
                        String charge = eachJob.getCharge();
                        if(charge==null)
                            throw new ValidationException("charge cannot be null for availabilityType : HOURLY");
                        Integer simplifyAmount = isValidAmount(eachJob.getCharge());
                        eachJob.setCharge(String.valueOf(simplifyAmount));
                    }

                    if (eachJob.getAvailabilityType().equalsIgnoreCase("PARTTIME"))
                        eachJob.setCharge(String.valueOf(20));
                    else
                        eachJob.setCharge(String.valueOf(40));
                    eachJob.setExpLevel(validateExpLevel(eachJob.getExperience()));
                }
            }
    }

    private static Integer isValidAmount(final String charge) throws ValidationException {
        try{
            return Integer.parseInt(charge);
        }
        catch (Exception ex) {
            throw new ValidationException("charge must be a whole number");
        }
    }

    private static int validateExpLevel(String expLevel) throws ValidationException {
        if (StringUtils.isEmpty(expLevel)){
            throw new ValidationException("Please select FRESHER, INTERMEDIATE OR EXPERT for"
                    + "for experience");
        }
        if (ExperienceLevel.valueOf(expLevel.toUpperCase())==null)
            throw new ValidationException("Invalid experience. ");
        return ExperienceLevel.valueOf(expLevel.toUpperCase()).getValue();
    }

    private static int validateAvailabilityType(String availabilityType) throws ValidationException {
        if (StringUtils.isEmpty(availabilityType)){
            throw new ValidationException("Please select HOURLY, PARTTIME OR FULLTIME for "
                    + "availabilityType");
        }
        if (Availability.valueOf(availabilityType.toUpperCase())==null)
            throw new ValidationException("Invalid availabilityType. ");
        return Availability.valueOf(availabilityType.toUpperCase()).getValue();
    }

    public static List<DribbleVO> transformEntityToVO(List<Company> list) {
        List<DribbleVO> dribbleVOS = null;
        if (!CollectionUtils.isEmpty(list)) {
            dribbleVOS = new ArrayList<>();
            for (Company company : list) {
                DribbleVO dribbleVO = new DribbleVO();
                CompanyVO companyVO = null;
                companyVO = new CompanyVO(company.getCompanyName(), company.getDescription(),
                        company.getMainPhoneNumber());
                if (!CollectionUtils.isEmpty(company.getLocations())) {
                    List<LocationVO> locationVOS = new ArrayList<>();
                    for (Location location : company.getLocations()) {
                        LocationVO locationVO = new LocationVO(location.getState(), location.getProvince(),
                                location.getCountry(), location.getDescription(), location.getPhoneNumber());
                        if (!CollectionUtils.isEmpty(location.getJobs())) {
                            List<JobVO> jobVOS = new ArrayList<>();
                            for (Job job : location.getJobs()) {
                                JobVO jobVO = new JobVO(job.getJobTitle(), job.getJobType(),
                                        Availability.availabilityMap
                                                .get(Integer.parseInt(job.getAvailability
                                                        ())).name()
                                        , job.getCharge(), job.getDescription(),
                                        ExperienceLevel.experienceLevelMap.get(Integer.parseInt(job
                                                .getExpLevel())).name(),
                                        job.getSkills(), job.getCurrency(), String.valueOf(job.getPostedOn()));
                                jobVOS.add(jobVO);
                            }
                            locationVO.setJobs(jobVOS);
                        }
                        locationVOS.add(locationVO);
                    }
                    companyVO.setLocations(locationVOS);
                }
                dribbleVO.setCompany(companyVO);
                dribbleVOS.add(dribbleVO);
            }
        }
        return dribbleVOS;
    }

    public static CompanyVO createCompanyObject(final Iterator<Cell> cellIterator) throws ValidationException {
        CompanyVO company = new CompanyVO();
        if (cellIterator.hasNext())
            company.setCompanyName(String.valueOf(cellIterator.next()));
        if (cellIterator.hasNext())
            company.setDescription(String.valueOf(cellIterator.next()));
        try {
            if (cellIterator.hasNext())
                company.setMainPhoneNumber(String.valueOf(new BigDecimal(String.valueOf(cellIterator.next()))
                        .toBigInteger()));
        }
        catch (NumberFormatException ex){
            throw new ValidationException("Enter a valid phone number");
        }
        return company;
    }

    public static LocationVO createLocationObject(final Iterator<Cell> cellIterator) throws ValidationException {
        LocationVO location = new LocationVO();
        if (cellIterator.hasNext()){
            location.setState(String.valueOf(cellIterator.next()));
        }
        try {
            if (cellIterator.hasNext()) {
                location.setPhoneNumber(String.valueOf(new BigDecimal(String.valueOf(cellIterator.next()))
                        .toBigInteger()));
            }
        }
        catch (NumberFormatException ex){
            throw new ValidationException("Enter a valid phone number");
        }
        if (cellIterator.hasNext()){
            location.setDescription(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            location.setProvince(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            location.setCountry(String.valueOf(cellIterator.next()));
        }
        return location;
    }

    public static JobVO createJobObject(final Iterator<Cell> cellIterator) {
        JobVO job = new JobVO();
        if (cellIterator.hasNext()){
            job.setJobTitle(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            job.setJobType(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            job.setAvailabilityType(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            String value = String.valueOf(cellIterator.next());
            job.setCharge(value.substring(0,value.length()-2));
        }
        if (cellIterator.hasNext()){
            job.setDescription(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            job.setExperience(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            job.setSkills(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            job.setCurrency(String.valueOf(cellIterator.next()));
        }
        return job;
    }

    public static Company transformVOToEntity(final CompanyVO companyVO) {
                Company company = new Company(companyVO.getCompanyName(), companyVO.getDescription(),
                        companyVO.getMainPhoneNumber());
                if (!CollectionUtils.isEmpty(companyVO.getLocations())) {
                    Set<Location> locationList = new HashSet<>();
                    for (LocationVO locationVO : companyVO.getLocations()) {
                        Location location = new Location(locationVO.getState(), locationVO.getProvince(),
                                locationVO.getCountry(), locationVO.getDescription(), locationVO.getPhoneNumber(),
                                company);
                        if (!CollectionUtils.isEmpty(locationVO.getJobs())) {
                            Set<Job> jobList = new HashSet<>();
                            for (JobVO jobVO : locationVO.getJobs()) {
                                Job job = new Job(jobVO.getJobTitle(), jobVO.getJobType(),
                                        String.valueOf(jobVO.getAvailability())
                                        , jobVO.getCharge(), jobVO.getCurrency(),jobVO.getDescription(),
                                        String.valueOf(jobVO.getExpLevel()),new Date(),
                                        jobVO.getSkills(),location);
                                jobList.add(job);
                            }
                            location.setJobs(jobList);
                        }
                        locationList.add(location);
                    }
                    company.setLocations(locationList);
                }
        return company;
    }
}

package com.prs.dribbleapi.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
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
        if (request.getPayRateto()!=0){
            request.setPayRateto(request.getPayRateto()*100);
        }
        if (request.getPayRatefrom()!=0){
            request.setPayRatefrom(request.getPayRatefrom()*100);
        }
        if (request.getPayRateto() < request.getPayRatefrom()){
            throw new ValidationException("payRateto must be greater than payRateFrom");
        }

        if (!request.getShowJobsWithoutPayRate().equalsIgnoreCase("YES") &&
                !request.getShowJobsWithoutPayRate().equalsIgnoreCase("NO")){
            throw new ValidationException("Enter either YES or NO for showJobsWithoutPayRate");
        }
    }

    public static void validateAndEnrichPostRequest(Company request) throws ValidationException {
        if (request==null)
            throw new ValidationException("Request body is empty");
        List<Location> locations = request.getLocations();
        if (CollectionUtils.isEmpty(locations)){
            throw new ValidationException("locations cannot be empty");
        }
            for (Location eachLocation:locations){
                List<Job> jobs = eachLocation.getJobs();
                for (Job eachJob : jobs){
                    eachJob.setAvailability(String.valueOf(validateAvailabilityType(eachJob.getAvailabilityType())));

                    if (eachJob.getAvailability().equals("1")) {
                        String charge = eachJob.getCharge();
                        if(charge==null)
                            throw new ValidationException("charge cannot be null for availabilityType : HOURLY");
                        Integer simplifyAmount = isValidAmount(eachJob.getCharge())*100;
                        eachJob.setCharge(String.valueOf(simplifyAmount));
                    }

                    if (eachJob.getAvailability().equals("2"))
                        eachJob.setCharge(String.valueOf(2000));
                    else
                        eachJob.setCharge(String.valueOf(4000));
                    eachJob.setExpLevel(String.valueOf(validateExpLevel(eachJob.getExperience())));
                    eachJob.setLocation(eachLocation);
                    eachJob.setPostedOn(new Date());
                }
                eachLocation.setCompany(request);
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

    public static List<DribbleVO> transform(final List<Object[]> list) {
        List<DribbleVO> dribbleVOS = null;
        if (!CollectionUtils.isEmpty(list)){
            dribbleVOS = new ArrayList<>();
            for (Object[] objectArray : list){
                DribbleVO dribbleVO = new DribbleVO();
                CompanyVO companyVO = null;
                JobVO jobVO = null;
                LocationVO locationVO = null;
                if (objectArray.length>=2)
                 companyVO = new CompanyVO(String.valueOf(objectArray[0]),String.valueOf(objectArray[1]),
                        String.valueOf(objectArray[2]));
                if (objectArray.length>=7)
                 locationVO = new LocationVO(String.valueOf(objectArray[3]),String.valueOf(objectArray[4]),
                        String.valueOf(objectArray[5]),String.valueOf(objectArray[6]),String.valueOf(objectArray[7]));
                if (objectArray.length>=16)
                 jobVO = new JobVO( String.valueOf(objectArray[8]),String.valueOf(objectArray[9]),Availability
                         .availabilityMap
                        .get(((BigDecimal)objectArray[10]).intValue()),String.valueOf(Integer.parseInt(String.valueOf
                         (objectArray[11]))
                         /100),
                         String
                         .valueOf
                         (objectArray[12]),
                        ExperienceLevel.experienceLevelMap.get(((BigDecimal)(objectArray[13])).intValue()),String
                         .valueOf
                                        (objectArray[14]),
                        String.valueOf(objectArray[15]), String.valueOf(objectArray[16]));
                dribbleVO.setCompany(companyVO);
                dribbleVO.setLocation(locationVO);
                dribbleVO.setJob(jobVO);
                dribbleVOS.add(dribbleVO);
            }
        }
        return dribbleVOS;
    }

    public static Company createCompanyObject(final Iterator<Cell> cellIterator) {
        Company company = new Company();
        if (cellIterator.hasNext())
            company.setCompanyName(String.valueOf(cellIterator.next()));
        if (cellIterator.hasNext())
            company.setDescription(String.valueOf(cellIterator.next()));
        if (cellIterator.hasNext())
            company.setMainPhoneNumber(String.valueOf(cellIterator.next()));

        return company;
    }

    public static Location createLocationObject(final Iterator<Cell> cellIterator) {
        Location location = new Location();
        if (cellIterator.hasNext()){
            location.setState(String.valueOf(cellIterator.next()));
        }
        if (cellIterator.hasNext()){
            location.setPhoneNumber(String.valueOf(cellIterator.next()));
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

    public static Job createJobObject(final Iterator<Cell> cellIterator) {
        Job job = new Job();
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
        job.setPostedOn(new Date());
        return job;
    }
}

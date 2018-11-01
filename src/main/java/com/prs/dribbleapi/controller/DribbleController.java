package com.prs.dribbleapi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.xml.bind.ValidationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.prs.dribbleapi.helper.DribbleHelper;
import com.prs.dribbleapi.request.SearchRequest;
import com.prs.dribbleapi.service.DribbleService;
import com.prs.dribbleapi.vo.CompanyVO;
import com.prs.dribbleapi.vo.DribbleVO;
import com.prs.dribbleapi.vo.JobVO;
import com.prs.dribbleapi.vo.LocationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(tags = "Dribble API Rest Controller")
@RestController
@RequestMapping("dribble")
public class DribbleController {

    @Autowired
    private DribbleService dribbleService;

    @Autowired
    private KafkaTemplate<String, CompanyVO> kafkaTemplate;
    private static final String TOPIC = "kafka_dribble_topic";
    @ApiOperation(value = "Save Json Jobs", notes = "Saves Json job details directly to database")
    @PostMapping("/save")
    public ResponseEntity save(@RequestBody CompanyVO company){
        try {
            DribbleHelper.validateAndEnrichPostRequest(company);
            dribbleService.save(company);
            return ResponseEntity.ok("Job successfully added");
        }
        catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @ApiOperation(value = "Search Jobs Json Jobs", notes = "An Api to search jobs")
    @PostMapping("/search")
    public ResponseEntity search(@RequestBody SearchRequest request){
        List<DribbleVO> companyList = null;
        try {
            DribbleHelper.validateSearchRequest(request);
            companyList = dribbleService.search(request);
            if (CollectionUtils.isEmpty(companyList)){
                return ResponseEntity.ok("No Results found");
            }
            return ResponseEntity.ok(companyList);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @ApiOperation(value = "Upload Job details", notes = "Upload Job details using excel sheet"
            + "directly to database")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity upload(@RequestPart("file")MultipartFile file){
        String message = null;
        try {
            if(file.getContentType().equalsIgnoreCase
                    ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    CompanyVO company = DribbleHelper.createCompanyObject(cellIterator);
                    LocationVO location = DribbleHelper.createLocationObject(cellIterator);
                    JobVO job = DribbleHelper.createJobObject(cellIterator);
                    message = buildVoAndPublish(company, location, job);
                }

                return ResponseEntity.ok(message);
            }
            return ResponseEntity.badRequest().body("Only .xlsx file type are supported");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (NoSuchElementException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        catch (ValidationException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    public String buildVoAndPublish(final CompanyVO company, final LocationVO location, final JobVO job)
            throws ValidationException {
        final String message;List<LocationVO> locationList = new ArrayList<>();
        List<JobVO> jobList = new ArrayList<>();
        jobList.add(job);
        location.setJobs(jobList);
        locationList.add(location);
        company.setLocations(locationList);
        message = publish(company);
        return message;
    }

    private String publish(CompanyVO company) throws ValidationException {
        DribbleHelper.validateAndEnrichPostRequest(company);
        kafkaTemplate.send(TOPIC, company);
        return "Published successfully";
    }
}

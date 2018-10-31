package com.prs.dribbleapi.controller;

import static com.prs.dribbleapi.helper.DribbleHelper.createCompanyObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
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
import com.prs.dribbleapi.dto.Company;
import com.prs.dribbleapi.dto.Job;
import com.prs.dribbleapi.dto.Location;
import com.prs.dribbleapi.helper.DribbleHelper;
import com.prs.dribbleapi.request.SearchRequest;
import com.prs.dribbleapi.service.DribbleService;
import com.prs.dribbleapi.vo.CompanyVO;
import com.prs.dribbleapi.vo.DribbleVO;
import com.prs.dribbleapi.vo.JobVO;
import com.prs.dribbleapi.vo.LocationVO;


@RestController
@RequestMapping("dribble")
public class DribbleController {

    @Autowired
    private DribbleService dribbleService;

    @Autowired
    private KafkaTemplate<String, CompanyVO> kafkaTemplate;
    private static final String TOPIC = "kafka_dribble_topic";

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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity upload(@RequestPart("file")MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            String message = null;
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                CompanyVO company = DribbleHelper.createCompanyObject(cellIterator);
                LocationVO location = DribbleHelper.createLocationObject(cellIterator);
                JobVO job = DribbleHelper.createJobObject(cellIterator);
                List<LocationVO> locationList = new ArrayList<>();
                List<JobVO> jobList = new ArrayList<>();
                jobList.add(job);
                location.setJobs(jobList);
                locationList.add(location);
                company.setLocations(locationList);
                message = publish(company);
            }
            return ResponseEntity.ok(message);
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



    private String publish(CompanyVO company) throws ValidationException {
        DribbleHelper.validateAndEnrichPostRequest(company);
        kafkaTemplate.send(TOPIC, company);
        return "Published successfully";
    }
}

package edu.northeastern.taapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import edu.northeastern.taapp.dao.ApplicationDAO;
import edu.northeastern.taapp.dao.StudentDAO;
import edu.northeastern.taapp.model.Application;
import edu.northeastern.taapp.model.Student;



@Controller
public class FileController {

    @Autowired
    private StudentDAO studentDAO;
    
    @Autowired
    private ApplicationDAO applicationDAO;

    @GetMapping("/photo/{nuid}")
    public ResponseEntity<Resource> servePhoto(@PathVariable String nuid) {
        Student student = studentDAO.getStudentById(nuid);
        Resource resource = new FileSystemResource(student.getPhotoPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(resource);
    }
    
    @GetMapping("/downloadTranscript/{nuid}")
    public ResponseEntity<Resource> downloadTranscript(@PathVariable String nuid) throws IOException {

        Student student = studentDAO.getStudentById(nuid);
        String filePath = student.getTranscriptPath();

        Resource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(resource);
    }
    
    @GetMapping("/downloadResume/{applicationId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long applicationId) throws IOException {

        Application application = applicationDAO.getApplicationById(applicationId);
        String resumePath = application.getResumePath();

        if (resumePath == null || resumePath.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(resumePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(resource);
    }
    
    
    @GetMapping("/viewPDF/{fileType}/{applicationId}")
    public ResponseEntity<InputStreamResource> viewPdf(@PathVariable String fileType, @PathVariable Long applicationId) throws IOException {
    	Application application = applicationDAO.getApplicationById(applicationId);
    	
        String pdfFilePath = null;
        
        if(fileType.equalsIgnoreCase("Transcript")) {
        	pdfFilePath = application.getStudent().getTranscriptPath();
        }
        
        if(fileType.equalsIgnoreCase("Resume")) {
        	pdfFilePath = application.getResumePath();
        }
        
        File file = new File(pdfFilePath);
        FileInputStream inputStream = new FileInputStream(file);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + "");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }
}
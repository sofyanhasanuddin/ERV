package com.sofyan.erv.ctrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sofyan.erv.helper.RelationHelper;
import com.sofyan.erv.helper.ResponseHelper;

@RestController
public class EntityCtrl {

    @GetMapping(path = "/test")
    public Map<String,Object> test() {

        return new HashMap<>();
    }

    @GetMapping(path = "/list-entities")
    public Map<String,Object> getEntities() {

        return ResponseHelper.buildResponse( RelationHelper
        		.findAllClass("/Users/sofyanhasanuddin/DevTool/mavenrepo/org/sofyan/latihan/petclinicmvc/0.0.1-SNAPSHOT/petclinicmvc-0.0.1-SNAPSHOT.war",
        				"org.sofyan.latihan.app"));

    }

    @PostMapping("/doUpload")
    public Map<String,Object> handleFileUpload(@RequestParam("file") MultipartFile file,
    		@RequestParam("pkg") String pkg) {
    	
    	String tempDir = System.getProperty("java.io.tmpdir");
    	String fileToUpload = tempDir + File.separator + file.getOriginalFilename();

    	try {
            Files.copy(file.getInputStream(), Paths.get( fileToUpload ), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
        	ex.printStackTrace();
            throw new RuntimeException("Could not store file " + fileToUpload + ". Please try again!");
        }

    	return ResponseHelper.buildResponse(RelationHelper.findAllClass(fileToUpload,pkg));
    	
    }

    @GetMapping("generateCRFS")
    public Map<String,String> generateCRFS(HttpServletRequest request, HttpServletResponse response) {

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        // Spring Security will allow the Token to be included in this header name

        Map<String,String> tokenResponse = new HashMap<>();
        tokenResponse.put("X-CSRF-HEADER", token.getHeaderName());
        tokenResponse.put("X-CSRF-PARAM", token.getParameterName());
        tokenResponse.put("X-CSRF-TOKEN", token.getToken());

        return tokenResponse;
    }

}

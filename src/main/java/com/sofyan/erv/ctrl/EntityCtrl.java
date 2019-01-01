package com.sofyan.erv.ctrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

import com.sofyan.erv.util.ResponseUtil;

@RestController
public class EntityCtrl {

    @PostMapping("/doUpload")
    public Map<String,Object> handleFileUpload(
    		@RequestParam("file") MultipartFile file,
    		@RequestParam("pkg") String originPkg) {
    	
    	String fileToUpload = System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename();

    	try {
            Files.copy(file.getInputStream(), Paths.get( fileToUpload ), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
        	ex.printStackTrace();
            throw new RuntimeException("Could not store file " + fileToUpload + ". Please try again!");
        }

    	return ResponseUtil.buildResponse( fileToUpload, originPkg );
    	
    }

    @GetMapping("generateCSRF")
    public Map<String,String> generateCRFS(HttpServletRequest request, HttpServletResponse response) {

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");

        Map<String,String> tokenResponse = new HashMap<>();
        tokenResponse.put("X-CSRF-HEADER", token.getHeaderName());
        tokenResponse.put("X-CSRF-PARAM", token.getParameterName());
        tokenResponse.put("X-CSRF-TOKEN", token.getToken());

        return tokenResponse;
    }

}

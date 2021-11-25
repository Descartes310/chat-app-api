package com.chat.api.configurations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class FileController {
	
	public FileController() {}
	
	
	@GetMapping("/files/{filename:.+}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void serveFile(@PathVariable String filename) {}
		
	@GetMapping("/files/**")
	public void serveFileContent(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String requestURL = request.getRequestURL().toString();

	    String filename = requestURL.split("/files/")[1];
        
	    Path path = Paths.get(Utilities.UPLOAD_PATH, filename);
	    
        StreamUtils.copy(Files.readAllBytes(path), response.getOutputStream());
	}

}

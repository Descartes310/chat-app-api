package com.chat.api.configurations;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Service
public class UploadService {
	
	public UploadService() {}

	/**
	 * Get the file and save it somewhere
	 * @param file
	 * @param subUploadPath
	 * @return
	 */
	public String uploadFile(MultipartFile file, String subUploadPath) {
		try
		{			
			String fileExt = getExtension(file.getOriginalFilename()).get();
			byte[] bytes = file.getBytes();
			File repertoire = new File(subUploadPath);
			
			if(!repertoire.exists()){
				new File(Utilities.UPLOAD_PATH+System.getProperty("file.separator")+subUploadPath).mkdir();
			}
			String generatedFileName = generateUniqueFileName(subUploadPath, fileExt);
			Path filenamePath = Paths.get(subUploadPath, generatedFileName);
			Path filePath = Paths.get(Utilities.UPLOAD_PATH).resolve(filenamePath);
			
			File tmp = new File(Utilities.UPLOAD_PATH+System.getProperty("file.separator")+subUploadPath, generatedFileName);
			tmp.getParentFile().mkdirs();
			tmp.createNewFile();
			
			Files.write(filePath, bytes);

			String url = MvcUriComponentsBuilder
	          .fromMethodName(FileController.class, "serveFile", filenamePath.toString()).build().toString();
						
			return "files"+url.split("files")[1];

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while parsing file: " + file, e);
		}
	}
	
	private String generateUniqueFileName(String subUploadPath, String ext)
	{
		if(!ext.startsWith(".")) {
			ext = "." + ext;
		}

		Path uploadPath = Paths.get(Utilities.UPLOAD_PATH, subUploadPath);
		String end = "_" + String.valueOf(System.currentTimeMillis()) + ext;
		String name;
		do {
			name = UUID.randomUUID().toString().replace("-", "") + end;
		} while (Files.exists(Paths.get(uploadPath.toString(), name)));
		
		return name;
	}
	
	private Optional<String> getExtension(String filename) {
	    return Optional.ofNullable(filename)
	      .filter(f -> f.contains("."))
	      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
}

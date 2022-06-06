package com.ifcdpp.ifcdpp.controllers.api;

import com.ifcdpp.ifcdpp.service.FileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class DownloadController {

    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    private final FileService fileService;

    @GetMapping("/downloadFile/{link:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String link, HttpServletRequest request) {
        Resource resource = fileService.loadFileAsResource(link);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

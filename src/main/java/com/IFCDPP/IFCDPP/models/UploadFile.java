package com.ifcdpp.ifcdpp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UploadFile {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

}

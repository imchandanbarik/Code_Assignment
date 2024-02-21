package com.threeframes.versioncontrol.payload.vcs.request;

import lombok.Data;

@Data
public class CreateRequest {
    private String filepath;
    private String content;
}

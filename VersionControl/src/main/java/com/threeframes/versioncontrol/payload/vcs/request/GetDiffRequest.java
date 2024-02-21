package com.threeframes.versioncontrol.payload.vcs.request;

import lombok.Data;

@Data
public class GetDiffRequest {
    private String fileOne;
    private String fileTwo;
}

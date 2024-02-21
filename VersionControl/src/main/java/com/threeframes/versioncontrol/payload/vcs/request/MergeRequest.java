package com.threeframes.versioncontrol.payload.vcs.request;

import lombok.Data;

@Data
public class MergeRequest {
    private String baseContent;
    private String incomingContent;
    private String localContent;
}

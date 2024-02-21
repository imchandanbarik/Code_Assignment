package com.threeframes.versioncontrol.payload.vcs.request;

import lombok.Data;

@Data
public class ConflictResolveRequest {
    private String baseContent;
    private String incomingContent;
    private String localContent;
    private boolean isManualResolution;
}

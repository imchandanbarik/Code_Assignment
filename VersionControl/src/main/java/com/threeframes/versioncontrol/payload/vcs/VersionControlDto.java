package com.threeframes.versioncontrol.payload.vcs;

import lombok.Data;

@Data
public class VersionControlDto {
    private Folder root;

    public VersionControlDto() {
        root = new Folder();
    }
}

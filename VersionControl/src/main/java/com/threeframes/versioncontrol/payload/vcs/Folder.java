package com.threeframes.versioncontrol.payload.vcs;

import lombok.Data;

import java.util.Map;

@Data
public class Folder {
    private Map<String, File> files;
    private Map<String, Folder> folders;
    public void addFile(String fileName, File file) {
        files.put(fileName, file);
    }

    public void addFolder(String folderName, Folder folder) {
        folders.put(folderName, folder);
    }
}

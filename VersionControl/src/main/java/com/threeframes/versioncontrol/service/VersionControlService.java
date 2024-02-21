package com.threeframes.versioncontrol.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VersionControlService {
    String displayFileContent(String file);
    List<String> computeDiff(String file1, String file2);
    String merge(String localContent, String incomingContent, String baseContent);
}

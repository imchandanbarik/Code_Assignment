package com.threeframes.versioncontrol.controller;

import com.threeframes.versioncontrol.exception.BadRequestException;
import com.threeframes.versioncontrol.payload.vcs.File;
import com.threeframes.versioncontrol.payload.vcs.Folder;
import com.threeframes.versioncontrol.payload.vcs.request.ConflictResolveRequest;
import com.threeframes.versioncontrol.payload.vcs.request.CreateRequest;
import com.threeframes.versioncontrol.payload.vcs.request.GetDiffRequest;
import com.threeframes.versioncontrol.payload.vcs.request.MergeRequest;
import com.threeframes.versioncontrol.service.VersionControlService;
import com.threeframes.versioncontrol.utils.GlobalConstants;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@Slf4j
public class VersionController {
    private final VersionControlService versionControlService;
    public VersionController(VersionControlService versionControlService) {
        this.versionControlService = versionControlService;
    }

    @GetMapping("/display")
    @Operation(summary = "display content",
            description = "display content of a file")
    public ResponseEntity<String> displayContent(@RequestParam String filePath) {
        log.info("file path: {}", filePath);
        String[] parts = filePath.split("/");
        Folder currentFolder = new Folder();
        for (int i = 0; i < parts.length - 1; i++) {
            currentFolder = currentFolder.getFolders().get(parts[i]);
            if (currentFolder == null) {
                throw new BadRequestException(GlobalConstants.BAD_REQUEST,GlobalConstants.FOLDER_NOT_FOUND);
            }
        }
        File file = currentFolder.getFiles().get(parts[parts.length - 1]);
        if (file == null) {
            throw new BadRequestException(GlobalConstants.BAD_REQUEST,GlobalConstants.FILE_NOT_FOUND);
        }
        return ResponseEntity.ok(file.getContent());
    }

    @PostMapping("/create")
    @Operation(summary = "create file",
            description = "creation of a file")
    public ResponseEntity<String> createFile(@RequestBody CreateRequest createRequest) {
        String[] parts = createRequest.getFilepath().split("/");
        Folder currentFolder = new Folder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (!currentFolder.getFolders().containsKey(parts[i])) {
                currentFolder.addFolder(parts[i], new Folder());
            }
            currentFolder = currentFolder.getFolders().get(parts[i]);
        }
        currentFolder.addFile(parts[parts.length - 1], new File(createRequest.getContent()));
        return ResponseEntity.ok(GlobalConstants.CREATION_SUCCESS);
    }
    @PutMapping("/merge")
    @Operation(summary = "merge file",
            description = "merging of a file with remote and local changes")
    public ResponseEntity<String> mergeFile(@RequestBody MergeRequest mergeRequest) {
        String mergedContent = versionControlService.merge(mergeRequest.getLocalContent(), mergeRequest.getIncomingContent(), mergeRequest.getBaseContent());
        return ResponseEntity.ok(mergedContent);
    }
    @PostMapping("/resolve")
    @Operation(summary = "file resolution",
            description = "resolve merge conflicts having remote and local changes")
    public ResponseEntity<String> resolveConflicts(@RequestBody ConflictResolveRequest conflictResolveRequest) {
        if (conflictResolveRequest.isManualResolution()) {
            return ResponseEntity.ok(conflictResolveRequest.getIncomingContent());
        } else {
            String mergedContent = versionControlService.merge(conflictResolveRequest.getLocalContent(), conflictResolveRequest.getIncomingContent(), conflictResolveRequest.getBaseContent());
            return ResponseEntity.ok(mergedContent);
        }
    }
    @GetMapping("/differences")
    @Operation(summary = "diff finder",
            description = "finding diff between two files")
    public ResponseEntity<String> diffFinder(@RequestBody GetDiffRequest getDiffRequest) {
        String content1 = versionControlService.displayFileContent(getDiffRequest.getFileOne());
        String content2 = versionControlService.displayFileContent(getDiffRequest.getFileTwo());

        List<String> diffResult = versionControlService.computeDiff(content1, content2);

        StringBuilder diffStringBuilder = new StringBuilder();
        for (String line : diffResult) {
            diffStringBuilder.append(line).append("\n");
        }
        return ResponseEntity.ok(diffStringBuilder.toString());
    }
}

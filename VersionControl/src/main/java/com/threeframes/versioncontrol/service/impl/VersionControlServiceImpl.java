package com.threeframes.versioncontrol.service.impl;

import com.threeframes.versioncontrol.payload.vcs.File;
import com.threeframes.versioncontrol.payload.vcs.Folder;
import com.threeframes.versioncontrol.service.VersionControlService;
import com.threeframes.versioncontrol.utils.GlobalConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VersionControlServiceImpl implements VersionControlService {
    @Override
    public String displayFileContent(String filePath) {
        String[] parts = filePath.split("/");
        Folder currentFolder = new Folder();
        for (int i = 0; i < parts.length - 1; i++) {
            currentFolder = currentFolder.getFolders().get(parts[i]);
            if (currentFolder == null) {
                return GlobalConstants.FOLDER_NOT_FOUND;
            }
        }
        File file = currentFolder.getFiles().get(parts[parts.length - 1]);
        if (file == null) {
            return GlobalConstants.FILE_NOT_FOUND;
        }
        return file.getContent();
    }

    // Myers diff algorithm implementation
    @Override
    public List<String> computeDiff(String text1, String text2) {
        List<String> diffResult = new ArrayList<>();
        int[][] memo = new int[text1.length() + 1][text2.length() + 1];

        // Initialize memo table
        for (int i = 0; i <= text1.length(); i++) {
            memo[i][0] = i;
        }
        for (int j = 0; j <= text2.length(); j++) {
            memo[0][j] = j;
        }

        // Compute edit distance using Myers algorithm
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    memo[i][j] = memo[i - 1][j - 1];
                } else {
                    memo[i][j] = 1 + Math.min(Math.min(memo[i - 1][j], memo[i][j - 1]), memo[i - 1][j - 1]);
                }
            }
        }

        // Trace back to construct the edit script
        int i = text1.length();
        int j = text2.length();
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && text1.charAt(i - 1) == text2.charAt(j - 1)) {
                diffResult.add(0, "  " + text1.charAt(i - 1)); // No change
                i--;
                j--;
            } else if (j > 0 && (i == 0 || memo[i][j - 1] + 1 == memo[i][j])) {
                diffResult.add(0, "+ " + text2.charAt(j - 1)); // Insertion
                j--;
            } else if (i > 0 && (j == 0 || memo[i - 1][j] + 1 == memo[i][j])) {
                diffResult.add(0, "- " + text1.charAt(i - 1)); // Deletion
                i--;
            } else {
                diffResult.add(0, "* " + text1.charAt(i - 1)); // Substitution
                i--;
                j--;
            }
        }

        return diffResult;
    }

    @Override
    public String merge(String localContent, String incomingContent, String baseContent) {
        // Compute the differences between local and base contents
        List<String> diffLocalToBase = computeDiff(baseContent, localContent);
        // Compute the differences between incoming and base contents
        List<String> diffIncomingToBase = computeDiff(baseContent, incomingContent);

        // Apply the differences from diffIncomingToBase to localContent
        StringBuilder mergedContentBuilder = new StringBuilder();
        int localIndex = 0;
        for (String diff : diffIncomingToBase) {
            if (diff.startsWith("+")) {
                // Add lines from incomingContent that are not in localContent
                mergedContentBuilder.append(diff.substring(2)); // Append without the '+ '
            } else if (diff.startsWith(" ")) {
                // Add unchanged lines from localContent
                while (localIndex < diffLocalToBase.size() && !diffLocalToBase.get(localIndex).startsWith("+")) {
                    mergedContentBuilder.append(diffLocalToBase.get(localIndex).substring(2)); // Append without the '- '
                    localIndex++;
                }
            }
        }

        // Append remaining lines from localContent if any
        while (localIndex < diffLocalToBase.size()) {
            mergedContentBuilder.append(diffLocalToBase.get(localIndex).substring(2)); // Append without the '- '
            localIndex++;
        }

        return mergedContentBuilder.toString();
    }
}

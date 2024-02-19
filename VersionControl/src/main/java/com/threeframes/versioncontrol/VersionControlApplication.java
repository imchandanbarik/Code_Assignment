package com.threeframes.versioncontrol;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Version Control System",description = "keep track of all version of file and handles ops like mergeConflicts etc."))
public class VersionControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(VersionControlApplication.class, args);
    }

}

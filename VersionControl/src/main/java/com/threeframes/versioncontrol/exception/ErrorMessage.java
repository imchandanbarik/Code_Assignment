package com.threeframes.versioncontrol.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private int errCode;
    private int errStatus;
    private String errMessage;
    private String errDescription;
}

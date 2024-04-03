package com.projectdata.pipeline.textio.model.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Sink {
    private static final long serialVersionUID = 9067491750901730060L;

    private String successfulOutputFilePath;
    private String failedOutputFilePath;
}

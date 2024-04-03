package com.projectdata.pipeline.textio.model.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Source implements Serializable {
    private static final long serialVersionUID = -4945694214701869978L;
    private String inputFilePath;
}

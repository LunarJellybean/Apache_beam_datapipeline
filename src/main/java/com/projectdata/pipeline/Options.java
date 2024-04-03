package com.projectdata.pipeline;

import org.apache.beam.sdk.options.PipelineOptions;

public interface Options extends PipelineOptions {
    String getConfigPath();
    void setConfigPath(String configPath);
}

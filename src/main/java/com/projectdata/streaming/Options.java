package com.projectdata.streaming;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;

public interface Options extends PipelineOptions {
    @Description("Path of the input file")
    @Default.String("gs://apache-beam-samples/shakespeare/kinglear.txt")
    String getInputFile();
    void setInputFile(String value);

    @Description("Path of the output file")
    @Default.String("gs://projectdata-202403-bucket/dataflow/output.txt")
    String getOutputFile();
    void setOutputFile(String value);
}

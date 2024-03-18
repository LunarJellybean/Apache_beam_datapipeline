package com.projectdata.pipeline;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;

public class Datapipeline {
    public static void main(String[] args) {
        Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
        Pipeline p = Pipeline.create(options);

        //Reading from public dataset containing King Lear text
        PCollection<String> extractedData = p.apply(("Read"), TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"));
                            //Reading from a public bucket

        PCollection<String> transformedData = extractedData.apply("Transform", ParDo.of(new Transformation()));

//        PDone done = transformedData.apply(("Write"), TextIO.write().to("gs://projectdata-202403-bucket/dataflow/output.txt"));
        PDone done = transformedData.apply(("Write"), TextIO.write().to("gs://projectdata-202403-bucket/dataflow/transformed-output.txt"));
        //update the file name else it will override the existing file

        PipelineResult result = p.run();
        try {
            result.getState();
            result.waitUntilFinish();
        } catch (UnsupportedOperationException e) {
            //do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.projectdata.pipeline;

import com.projectdata.pipeline.textio.model.config.Config;
import com.projectdata.pipeline.textio.util.ConfigUtil;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.PDone;
import org.apache.beam.sdk.values.TupleTagList;

public class Datapipeline {
    public static void main(String[] args) {
        Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
        Pipeline p = Pipeline.create(options);

        Config config = ConfigUtil.loadConfig(options.getConfigPath());


//        //Reading from public dataset containing King Lear text
//        PCollection<String> extractedData = p.apply(("Read"), TextIO.read().from("gs://apache-beam-samples/shakespeare/kinglear.txt"));
//                            //Reading from a public bucket

//        //We are dropping a file to the bucket
//        PCollection<String> extractedData = p.apply(("Extract"),
//                TextIO.read().from("gs://projectdata-202403-bucket/dataflow/zillow.csv"));

        PCollection<String> extractedData = p.apply("Extract", TextIO.read().from(config.getSource().getInputFilePath()));



//        PCollection<String> transformedData = extractedData.apply("Transform", ParDo.of(new Transformation()));

        //execute transform to calculate the price per sqft for hourses
        PCollectionTuple transformedTuple = extractedData.apply("Transform", ParDo.of(new Transformation())
                .withOutputTags(Transformation.VALID_DATA_TAG, TupleTagList.of(Transformation.FAILURE_DATA_TAG)));

        //save data for known sized house
        transformedTuple.get(Transformation.VALID_DATA_TAG)
                .setCoder(StringUtf8Coder.of())
                .apply("Save Result", TextIO.write().to("gs://projectdata-202403-bucket/dataflow/zillow-result.txt"));

        //handle exception for unknown sized house
        transformedTuple.get(Transformation.FAILURE_DATA_TAG)
                .setCoder(StringUtf8Coder.of())
                .apply("Unknown Sqft data", TextIO.write().to("gs://projectdata-202403-bucket/dataflow/zillow-unknown.txt"));



//        PDone done = transformedData.apply(("Write"), TextIO.write().to("gs://projectdata-202403-bucket/dataflow/output.txt"));
//        PDone done = transformedData.apply(("Write"), TextIO.write().to("gs://projectdata-202403-bucket/dataflow/transformed-output.txt"));
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

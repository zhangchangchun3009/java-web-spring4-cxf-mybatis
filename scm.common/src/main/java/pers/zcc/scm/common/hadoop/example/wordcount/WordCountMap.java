package pers.zcc.scm.common.hadoop.example.wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class WordCountMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
    IntWritable one = new IntWritable(1); // value

    Text word = new Text(); // key

    @Override
    public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
            throws IOException {
        String line = value.toString();
        StringTokenizer stringTokenizer = new StringTokenizer(line);
        while (stringTokenizer.hasMoreTokens()) {
            String wordStr = stringTokenizer.nextToken();
            word.set(wordStr);
            output.collect(word, one); //collect --> makeCopyForPassByValue(key/value)
        }
    }

}

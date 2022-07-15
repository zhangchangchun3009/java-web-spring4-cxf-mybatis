package pers.zcc.scm.common.hadoop.example.wordcount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import pers.zcc.scm.common.hadoop.client.HadoopClient;

public class WordCount {

    public static void main(String[] args) {
        FileSystem fs = HadoopClient.getFsClient();
        try {
            fs.copyFromLocalFile(false, new Path("D://word.txt"), new Path("/hello/word.txt"));
            JobConf job = new JobConf(HadoopClient.getConfig());//behaves the same with new JobConf(),both reads configuration from xml resources.
            job.set("mapreduce.app-submission.cross-platform", "true"); // important!
            System.setProperty("HADOOP_USER_NAME", "root");
            String base = WordCount.class.getClassLoader().getResource("").getPath();
            job.set("mapred.jar", base + "../scm.common.jar");
            job.setJarByClass(WordCount.class);// jar main class
            job.setJobName("wordcount");
            job.setMemoryForMapTask(128);
            job.setMemoryForReduceTask(128);
            job.setMapperClass(WordCountMap.class);
            job.setCombinerClass(WordCountReduce.class);
            job.setReducerClass(WordCountReduce.class);
            job.setInputFormat(TextInputFormat.class);
            job.setOutputFormat(TextOutputFormat.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            FileInputFormat.setInputPaths(job, new Path("/hello/word.txt"));
            Path out = new Path("/hello/wordcountRes/");
            if (fs.exists(out)) {
                fs.delete(out, true);
            }
            FileOutputFormat.setOutputPath(job, out);
            JobClient.runJob(job).waitForCompletion(); // my job keep running and never stop here, something goes wrong
            RemoteIterator<LocatedFileStatus> wordcountResDir = fs.listFiles(out, false);
            LocatedFileStatus file = wordcountResDir.next();
            Path path = file.getPath();
            FSDataInputStream input = fs.open(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line = br.readLine();
            while (line != null) {
                System.out.println(path.getName() + ":" + line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

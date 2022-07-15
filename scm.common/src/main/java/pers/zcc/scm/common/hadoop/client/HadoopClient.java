package pers.zcc.scm.common.hadoop.client;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HadoopClient {

    private static final Configuration config;

    private static final FileSystem hdfs;

    static {
        config = new Configuration(true);
        hdfs = init();
    }

    static FileSystem init() {
        try {
            return FileSystem.get(config);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("hdfs init failed");
        }
    }

    public static Configuration getConfig() {
        return config;
    }

    public static FileSystem getFsClient() {
        return hdfs;
    }

    public static void main(String[] args) {
        try {
            FileSystem fs = getFsClient();
            Path f = new Path("/hello/test.text");
            FSDataInputStream in = fs.open(f);
            ByteBuffer b = ByteBuffer.allocate(1024);
            while (in.read(b) > 0) {
                b.flip();
                byte[] ba = new byte[b.remaining()];
                b.get(ba);
                b.rewind();
                System.out.println(new String(ba));
            }
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

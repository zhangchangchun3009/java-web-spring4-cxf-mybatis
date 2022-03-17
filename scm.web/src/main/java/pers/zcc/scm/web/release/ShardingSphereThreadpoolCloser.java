package pers.zcc.scm.web.release;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

import javax.inject.Named;

import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.executor.kernel.impl.ShardingSphereExecutorService;

import pers.zcc.scm.common.frame.IBeforeShutDownHandle;
import pers.zcc.scm.common.util.ApplicationContextManager;

@Named
public class ShardingSphereThreadpoolCloser implements IBeforeShutDownHandle {

    @Override
    public void process() {
        ShardingSphereDataSource sd = (ShardingSphereDataSource) ApplicationContextManager
                .getBean("shardingDataSource");
        try {
            sd.close();
            Thread.sleep(5500);
            ShardingSphereExecutorService executor = sd.getSchemaContexts().getExecutorKernel().getExecutorService();
            Class<? extends ShardingSphereExecutorService> clz = executor.getClass();
            Field f = clz.getDeclaredField("SHUTDOWN_EXECUTOR");
            f.setAccessible(true);
            ExecutorService shutdownT = (ExecutorService) f.get(executor);
            shutdownT.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getOrder() {
        return 998;
    }

}

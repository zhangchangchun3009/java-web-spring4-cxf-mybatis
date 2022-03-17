
package pers.zcc.scm.web.release;

import java.util.Collection;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.inject.Named;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import pers.zcc.scm.common.frame.IBeforeShutDownHandle;
import pers.zcc.scm.common.util.ApplicationContextManager;

/**
 * The Class ThreadPoolCloser.
 *
 * @author zhangchangchun
 * @Date 2022年1月14日
 */
@Named
public class ThreadPoolCloser implements IBeforeShutDownHandle {

    @Override
    public void process() {
        Collection<ThreadPoolTaskExecutor> taskExecutors = ApplicationContextManager
                .getBeanOfType(ThreadPoolTaskExecutor.class);
        taskExecutors.forEach(o -> o.shutdown());
        Collection<ScheduledThreadPoolExecutor> timerExecutors = ApplicationContextManager
                .getBeanOfType(ScheduledThreadPoolExecutor.class);
        timerExecutors.forEach(o -> o.shutdown());
    }

    @Override
    public int getOrder() {
        return 998;
    }

}

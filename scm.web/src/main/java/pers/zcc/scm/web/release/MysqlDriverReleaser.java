package pers.zcc.scm.web.release;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.inject.Named;

import pers.zcc.scm.common.frame.IBeforeShutDownHandle;

@Named
public class MysqlDriverReleaser implements IBeforeShutDownHandle {

    @Override
    public void process() {
        try {
            Class<?> cls = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
            Method method = cls.getMethod("checkedShutdown");
            method.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
        Enumeration<Driver> divers = DriverManager.getDrivers();
        while (divers.hasMoreElements()) {
            Driver driver = divers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getOrder() {
        return 999;
    }

}

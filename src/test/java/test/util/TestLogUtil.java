package test.util;
import com.ascend.data.util.LogUtil;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.Logger;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import test.UnitTest;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TestLogUtil {

    @Test
    public void TestLogGetInstance() throws Exception{
        LogUtil log = LogUtil.getInstance();
        assertNotNull(log);
    }

    @Test
    public void TestLogInfo() throws Exception{
        Logger logger = mock(Logger.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).info(Matchers.anyString());

        UnitTest.setFinalStatic(LogUtil.class.getDeclaredField("log"), logger);

        LogUtil logClass = LogUtil.getInstance();
        logClass.logInfo(Class.class, "Test");
    }

    @Test
    public void TestLogDebug() throws Exception{
        Logger logger = mock(Logger.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).info(Matchers.anyString());

        UnitTest.setFinalStatic(LogUtil.class.getDeclaredField("log"), logger);

        LogUtil logClass = LogUtil.getInstance();
        logClass.logDebug(Class.class, "Test");
    }

    @Test
    public void TestLogError() throws Exception{
        Logger logger = mock(Logger.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).info(Matchers.anyString());

        UnitTest.setFinalStatic(LogUtil.class.getDeclaredField("log"), logger);

        LogUtil logClass = LogUtil.getInstance();
        logClass.logError(Class.class, "Test");
    }

}

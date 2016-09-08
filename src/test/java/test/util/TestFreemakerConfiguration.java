package test.util;
import com.ascend.data.util.AutoReloadProperty;
import com.ascend.data.util.FreemakerConfiguration;
import com.ascend.data.util.LogUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TestFreemakerConfiguration {

    @Test
    public void TestGetEmailTemplateSuccess() throws Exception{
        Template template = Mockito.mock(Template.class);
        when(template.getName()).thenReturn("TestTemplate");

        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("temp");

        Field fieldFile = FreemakerConfiguration.class.getDeclaredField("file");
        fieldFile.setAccessible(true);
        fieldFile.set(null, file);

        Configuration config = mock(Configuration.class);
        when(config.getTemplate(Matchers.anyString())).thenReturn(template);

        Field fieldConfig = FreemakerConfiguration.class.getDeclaredField("config");
        fieldConfig.setAccessible(true);
        fieldConfig.set(null, config);


        LogUtil logger = mock(LogUtil.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).logInfo(Mockito.any(), Mockito.anyString());

        Field log = FreemakerConfiguration.class.getDeclaredField("log");
        log.setAccessible(true);
        log.set(null, logger);

        FreemakerConfiguration freemakerConfiguration = new FreemakerConfiguration();
        Template result = freemakerConfiguration.getEmailTemplate();
        assertEquals("TestTemplate", result.getName());
    }

    @Test
    public void TestGetEmailTemplateFail() throws Exception{
        Template template = Mockito.mock(Template.class);

        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.getAbsolutePath()).thenReturn("temp");

        Field fieldFile = FreemakerConfiguration.class.getDeclaredField("file");
        fieldFile.setAccessible(true);
        fieldFile.set(null, file);

        LogUtil logger = mock(LogUtil.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).logInfo(Mockito.any(), Mockito.anyString());
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(logger).logError(Mockito.any(), Mockito.anyString());

        Field log = FreemakerConfiguration.class.getDeclaredField("log");
        log.setAccessible(true);
        log.set(null, logger);

        IOException ioException = mock(IOException.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(ioException).printStackTrace();

        FreemakerConfiguration freemakerConfiguration = new FreemakerConfiguration();

        try{
            Configuration config = mock(Configuration.class);
            when(config.getTemplate(Matchers.anyString())).thenThrow(ioException);

            Field fieldConfig = FreemakerConfiguration.class.getDeclaredField("config");
            fieldConfig.setAccessible(true);
            fieldConfig.set(null, config);

            Template result = freemakerConfiguration.getEmailTemplate();
        }catch(IOException ex){
            assertNotNull(ex);
        }
    }

    @Test
    public void TestGetEmailTemplateNull() throws Exception{
        Field fieldConfig = FreemakerConfiguration.class.getDeclaredField("config");
        fieldConfig.setAccessible(true);
        fieldConfig.set(null, null);

        FreemakerConfiguration freemakerConfiguration = new FreemakerConfiguration();
        Template result = freemakerConfiguration.getEmailTemplate();

        assertNull(result);
    }

    @Test
    public void TestSetFileTrue(){
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        FreemakerConfiguration freemakerConfiguration = new FreemakerConfiguration();
        freemakerConfiguration.setFile(file);
    }

    @Test
    public void TestSetFileFalse(){
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(file).mkdirs();
        FreemakerConfiguration freemakerConfiguration = new FreemakerConfiguration();
        freemakerConfiguration.setFile(file);
    }

    @Test
    public void TestSetConfigSuccess() throws IOException{
        File file = mock(File.class);

        Configuration cfg = mock(Configuration.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(cfg).setDirectoryForTemplateLoading(Matchers.any());

        FreemakerConfiguration freemakerConfiguration = new FreemakerConfiguration();
        freemakerConfiguration.setConfig(file, cfg);
    }

    @Test
    public void TestSetConfigFail() throws IOException{
        File file = mock(File.class);
        IOException ioException = mock(IOException.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(ioException).printStackTrace();

        Configuration cfg = mock(Configuration.class);
        doThrow(ioException).when(cfg).setDirectoryForTemplateLoading(Matchers.any());

        FreemakerConfiguration freemakerConfiguration = new FreemakerConfiguration();
        freemakerConfiguration.setConfig(file, cfg);
    }
}

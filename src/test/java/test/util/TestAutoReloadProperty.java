package test.util;
import com.ascend.data.util.AutoReloadProperty;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TestAutoReloadProperty {

    @Test
    public void testSetFile() throws Exception{
        PropertiesConfiguration configuration = mock(PropertiesConfiguration.class);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(configuration).setFile(Mockito.any());

        Field field = AutoReloadProperty.class.getDeclaredField("configuration");
        field.setAccessible(true);
        field.set(null, configuration);


        AutoReloadProperty autoReloadProperty = new AutoReloadProperty();
        autoReloadProperty.setFile("test");
    }

    @Test
    public void testGetPropertyConfigNull() throws Exception{
        Field field = AutoReloadProperty.class.getDeclaredField("configuration");
        field.setAccessible(true);
        field.set(null, null);

        AutoReloadProperty autoReloadProperty =  new AutoReloadProperty();
        String key = autoReloadProperty.getProperty("Test");

        assertEquals("", key);
    }

    @Test
    public void testGetPropertyConfigNoKey() throws Exception{
        PropertiesConfiguration configuration = mock(PropertiesConfiguration.class);

        Field field = AutoReloadProperty.class.getDeclaredField("configuration");
        field.setAccessible(true);
        field.set(null, configuration);

        AutoReloadProperty autoReloadProperty =  new AutoReloadProperty();
        String key = autoReloadProperty.getProperty("Test");

        assertEquals("", key);
    }

    @Test
    public void testGetPropertyConfigSuccess() throws Exception{
        PropertiesConfiguration configuration = mock(PropertiesConfiguration.class);
        when(configuration.getProperty("key")).thenReturn("value");

        Field field = AutoReloadProperty.class.getDeclaredField("configuration");
        field.setAccessible(true);
        field.set(null, configuration);

        AutoReloadProperty autoReloadProperty =  new AutoReloadProperty();
        String key = autoReloadProperty.getProperty("key");

        assertEquals("value", key);
    }

    @Test
    public void TestSetPropertiesFileNotNull() throws Exception{
        Field field = AutoReloadProperty.class.getDeclaredField("configuration");
        field.setAccessible(true);
        field.set(null, null);

        PropertiesConfiguration propertiesConfiguration = mock(PropertiesConfiguration.class);
        AutoReloadProperty autoReloadProperty =  new AutoReloadProperty();
        autoReloadProperty.setPropertiesFile(propertiesConfiguration);
    }

    @Test
    public void TestSetPropertiesFileNull() throws Exception{
        PropertiesConfiguration configuration = mock(PropertiesConfiguration.class);
        Field field = AutoReloadProperty.class.getDeclaredField("configuration");
        field.setAccessible(true);
        field.set(null, configuration);

        PropertiesConfiguration propertiesConfiguration = mock(PropertiesConfiguration.class);
        AutoReloadProperty autoReloadProperty =  new AutoReloadProperty();
        autoReloadProperty.setPropertiesFile(propertiesConfiguration);
    }
}

package ru.unn.agile.WeightConverter.infrastructure;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
import static junit.framework.TestCase.assertNotNull;

public class WeightConverterTxtLoggerTests {
    private WeightConverterTxtLogger weightConverterTxtLogger;
    private static final String NAME_LOG_FILE = "./TxtLogger_Tests-lab3-weight-convertor.log";
    private static final String TEST_MESSAGE = "Test message";

    @Before
    public void setUp() {
        weightConverterTxtLogger = new WeightConverterTxtLogger(NAME_LOG_FILE);
    }

    @Test
    public void canCreateTxtLoggerWithFileName() {
        assertNotNull(weightConverterTxtLogger);
    }

    @Test
    public void canCreateLogFileOnDisk() {
        assertTrue(new File(NAME_LOG_FILE).isFile());
    }

    @Test
    public void canWriteMessageToLog() {
        weightConverterTxtLogger.log(TEST_MESSAGE);

        assertEquals(1, weightConverterTxtLogger.getLog().size());
    }

    @Test
    public void canWriteSeveralLogMessage() {
        String[] messages = {"First test message", "Second test message"};

        weightConverterTxtLogger.log(messages[0]);
        weightConverterTxtLogger.log(messages[1]);

        assertEquals(2, weightConverterTxtLogger.getLog().size());
    }

    @Test
    public void isLineLogContainDateAndTime() {
        weightConverterTxtLogger.log(TEST_MESSAGE);
        String line = weightConverterTxtLogger.getLog().get(0);

        assertTrue(line.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} > .*"));
    }
}
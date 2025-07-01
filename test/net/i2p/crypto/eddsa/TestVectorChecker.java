package net.i2p.crypto.eddsa;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <pre>
 * |---------------+---+---+---+---+---+---+---+---+---+---+---+---|
 * |Library        | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| 11|
 * |---------------+---+---+---+---+---+---+---+---+---+---+---+---|
 * |original-3.0   | V | V | V | V | X | X | V | V | X | X | V | X |
 * |---------------+---+---+---+---+---+---+---+---+---+---+---+---|
 * |fork-3.1       | V | V | V | V | X | X | X | X | X | X | V | X |
 * |---------------+---+---+---+---+---+---+---+---+---+---+---+---|
 * </pre>
 */
public class TestVectorChecker {

    private static final boolean[] EXPECTED_VECTOR_CHECK = {true, true, true, true, false, false, false, false, false, false, true, false};

    @Test
    public void testVectors() throws IOException {
        Ed25519TestCase[] testCases = getTestCases();

        // For i2p ed25519-java
        System.out.println("|Library        | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| 11|");
        System.out.print("|ed25519-java   |");
        // | V | V | V | V | X | X | X | X | X | X | V | X |
        for (Ed25519TestCase testCase : testCases) {
            if (testCase.verify_i2p()) {
                System.out.print(" V |");
            } else {
                System.out.print(" X |");
            }
        }
        System.out.println();

        Assert.assertEquals(EXPECTED_VECTOR_CHECK.length, testCases.length);
        for (int i = 0; i < testCases.length; i++) {
            Assert.assertEquals("Test case at [" + i + "]", EXPECTED_VECTOR_CHECK[i], testCases[i].verify_i2p());
        }
        System.out.println("Everything as expected");
    }

    private static Ed25519TestCase[] getTestCases() throws IOException {
        try (InputStream is = TestVectorChecker.class.getResourceAsStream("cases.json")) {
            if (is == null) {
                throw new IOException("Resource not found: cases.json");
            }
            JsonReader reader = new JsonReader(new InputStreamReader(is));

            return new Gson().fromJson(reader, Ed25519TestCase[].class);
        }
    }
}

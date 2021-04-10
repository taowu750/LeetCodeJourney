package util;

import java.io.*;

public class StdIOTestUtil {

    public static InputStream stdIn = System.in;
    public static PrintStream stdOut = System.out;

    public static void test(Runnable method, String inPath, String expectPath) {
        InputStream redirectIn = StdIOTestUtil.class.getClassLoader().getResourceAsStream(inPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream redirectOut = new PrintStream(out);

        System.setIn(redirectIn);
        System.setOut(redirectOut);
        redirectOut.flush();

        method.run();
        //noinspection ConstantConditions
        try (BufferedReader expectReader = new BufferedReader(new InputStreamReader(
                StdIOTestUtil.class.getClassLoader().getResourceAsStream(expectPath)));
             BufferedReader actualReader = new BufferedReader(new InputStreamReader(
                     new ByteArrayInputStream(out.toByteArray())))) {
            String expect, actual;
            while ((expect = expectReader.readLine()) != null
                    && (actual = actualReader.readLine()) != null) {
                if (!expect.equals(actual)) {
                    throw new AssertionError("expect=" + expect + ", but actual=" + actual);
                }
            }
            if (expect != null)
                throw new AssertionError("The actual is already null");
            else if (actualReader.readLine() != null)
                throw new AssertionError("The expect is already null");
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        System.setIn(stdIn);
        System.setOut(stdOut);
    }
}

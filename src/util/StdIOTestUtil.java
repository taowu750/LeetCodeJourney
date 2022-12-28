package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

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
            int lineCnt = 1;
            while ((expect = expectReader.readLine()) != null
                    && (actual = actualReader.readLine()) != null) {
                if (!expect.equals(actual)) {
                    throw new AssertionError(expectPath + " => line " + lineCnt +
                            ":\nexpect=" + expect + "\nactual=" + actual);
                }
                lineCnt++;
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

    public static void test(Runnable method, String inPath, BiConsumer<String, ByteArrayInputStream> resultChecker) {
        InputStream redirectIn = StdIOTestUtil.class.getClassLoader().getResourceAsStream(inPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream redirectOut = new PrintStream(out);

        System.setIn(redirectIn);
        System.setOut(redirectOut);
        redirectOut.flush();

        method.run();
        //noinspection ConstantConditions
        try (ByteArrayInputStream actualInput = new ByteArrayInputStream(out.toByteArray())) {
            resultChecker.accept(new String(Files.readAllBytes(Paths.get(StdIOTestUtil.class.getClassLoader().getResource(inPath).getPath())), StandardCharsets.UTF_8), actualInput);
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        System.setIn(stdIn);
        System.setOut(stdOut);
    }

    public static void output(Runnable method, String inPath, String outputPath) {
        try (InputStream redirectIn = StdIOTestUtil.class.getClassLoader().getResourceAsStream(inPath);
             PrintStream redirectOut = new PrintStream(StdIOTestUtil.class.getClassLoader().getResource(outputPath).getFile())) {
            System.setIn(redirectIn);
            System.setOut(redirectOut);
            method.run();
            redirectOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

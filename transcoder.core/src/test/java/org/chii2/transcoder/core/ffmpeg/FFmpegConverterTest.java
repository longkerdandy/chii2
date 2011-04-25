package org.chii2.transcoder.core.ffmpeg;

import org.testng.annotations.Test;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class FFmpegConverterTest {

    @Test
    public void FakeTest() {
        File inputFile = new File("/home/longkerdandy/Videos/The Clinic/The.Clinic.2010.DVDRiP.XViD-aAF.avi");
        FFmpegConverter converter = new FFmpegConverter(inputFile, "mpeg4", "949K", "copy", null);
        ExecutorService executor = new ScheduledThreadPoolExecutor(5);
        Future<Process> future = executor.submit(converter);
        try {
            Process process = future.get();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            System.out.println(reader.readLine());
            Thread.sleep(1000);
            process.destroy();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

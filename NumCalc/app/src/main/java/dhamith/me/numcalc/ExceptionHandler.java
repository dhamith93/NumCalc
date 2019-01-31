package dhamith.me.numcalc;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;

    private String TAG = "EXCEPTION_HANDLER";

    public ExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e(TAG, "uncaughtException: TEST");
        final Writer stringBuffSync = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringBuffSync);
        e.printStackTrace(printWriter);
        String stacktrace = stringBuffSync.toString();
        printWriter.close();

        writeToFile(stacktrace);

        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String currentStacktrace) {
        try {

            File dir = new File(Environment.getExternalStorageDirectory(),
                    "numcalc_crash_logs");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Log.e(TAG, "writeToFile: " + dir.getAbsolutePath());

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            String filename = dateFormat.format(date) + ".STACKTRACE";

            File reportFile = new File(dir, filename);
            FileWriter fileWriter = new FileWriter(reportFile);

            fileWriter.append(currentStacktrace);
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            Log.e("ExceptionHandler", e.getMessage());
        }
    }
}

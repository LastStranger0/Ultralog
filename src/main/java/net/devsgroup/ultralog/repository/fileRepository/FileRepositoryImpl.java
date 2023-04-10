package net.devsgroup.ultralog.repository.fileRepository;

import static net.devsgroup.ultralog.constants.Constants.TAG;
import static net.devsgroup.ultralog.constants.Constants.d;
import static net.devsgroup.ultralog.constants.Constants.e;
import static net.devsgroup.ultralog.constants.Constants.i;
import static net.devsgroup.ultralog.constants.Constants.v;
import static net.devsgroup.ultralog.constants.Constants.w;
import static net.devsgroup.ultralog.constants.Constants.wtf;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import net.devsgroup.ultralog.constants.Constants;
import net.devsgroup.ultralog.preferences.PreferenceRepository;
import net.devsgroup.ultralog.repository.UltralogRepository;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import kotlin.io.ByteStreamsKt;

public class FileRepositoryImpl implements UltralogRepository {
    private Uri writeFile;
    private ContentResolver resolver;

    private File oldFile;

    @RequiresApi(Build.VERSION_CODES.Q)
    public static FileRepositoryImpl create(Context applicationContext) {
        return new FileRepositoryImpl(applicationContext);
    }

    public static FileRepositoryImpl create() {
        return new FileRepositoryImpl();
    }

    private FileRepositoryImpl() {
        File downloadsDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File mediaSubDirectory = new File(downloadsDirectory, "Logs");
        if (!mediaSubDirectory.exists()) {
            mediaSubDirectory.mkdir();
        }
        oldFile = new File(mediaSubDirectory, Constants.getFileName());
        writeFile = null;
        resolver = null;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private FileRepositoryImpl(Context applicationContext) {
        this.resolver = applicationContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, Constants.getFileName());
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Logs");
        String filename = PreferenceRepository.getFileUri(applicationContext);
        if (filename.equals("")) {
            writeFile = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            PreferenceRepository.putFileUri(applicationContext, writeFile);
        } else {
            writeFile = Uri.parse(filename);
            try {
                resolver.openFileDescriptor(writeFile, "r").close();
            } catch (Exception e) {
                writeFile = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                PreferenceRepository.putFileUri(applicationContext, writeFile);
            }
        }
        oldFile = null;
    }

    @Override
    public void log(String log, int level) {
        StringBuilder builder = new StringBuilder();
        builder.append("/////// ");
        switch (level) {
            case v:
                builder.append("VERBOSE ");
                break;
            default:
            case d:
                builder.append("DEBUG ");
                break;
            case i:
                builder.append("INFO ");
                break;
            case w:
                builder.append("WARN ");
                break;
            case e:
                builder.append("ERROR ");
                break;
            case wtf:
                builder.append("WTF ");
                break;
        }
        builder.append("/////// ");
        builder.append(DateTime.now().toString("yyyyMMdd-HHmmss"));
        builder.append("\n");
        builder.append(log);
        builder.append("\n");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            try (OutputStream out = getNewStream()) {
                out.write(builder.toString().getBytes(StandardCharsets.UTF_8));
                out.flush();
            } catch (Exception e) {
                err(e);
            }
        } else {
            try (OutputStream out = getStream()) {
                out.write(builder.toString().getBytes(StandardCharsets.UTF_8));
                out.flush();
            } catch (Exception e) {
                err(e);
            }
        }
    }

    @Override
    public void err(Throwable e) {
        StringBuilder builder = new StringBuilder();
        builder.append("/////// ");
        builder.append(e.getLocalizedMessage());
        builder.append("///////");
        builder.append(DateTime.now().toString("yyyyMMdd-HHmmss"));
        builder.append("\n");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        builder.append(stringWriter);
        builder.append("\n");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            try (OutputStream out = getNewStream()) {
                out.write(builder.toString().getBytes(StandardCharsets.UTF_8));
                out.flush();
            } catch (Exception e1) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
        } else {
            try (OutputStream out = getStream()) {
                out.write(builder.toString().getBytes(StandardCharsets.UTF_8));
                out.flush();
            } catch (Exception e1) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public void clear() {
        resolver = null;
        writeFile = null;
        oldFile = null;
    }

    @Override
    public File getLog(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                File temporaryFile = File.createTempFile("log", DateTime.now().toString(), context.getCacheDir());
                FileOutputStream tempLogStream = new FileOutputStream(temporaryFile);
                InputStream logInputStream = getNewReadStream();
                ByteStreamsKt.copyTo(logInputStream, tempLogStream, 8 * 1024);// This shit cannot work in java project, will rewrote later
                return temporaryFile;
            } catch (IOException ex) {
                Log.e(TAG, ex.getLocalizedMessage(), ex);
            }
        } else {
            return oldFile;
        }
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private OutputStream getNewStream() throws FileNotFoundException {
        return resolver.openOutputStream(writeFile, "wa");
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private InputStream getNewReadStream() throws FileNotFoundException {
        return resolver.openInputStream(writeFile);
    }

    private OutputStream getStream() throws IOException {
        return new FileOutputStream(oldFile, true);
    }

    private InputStream getReadStream() throws IOException {
        return new FileInputStream(oldFile);
    }
}

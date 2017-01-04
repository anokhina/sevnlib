/*
 * Copyright 2016 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.org.sevn.alib.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;

public class ExternalStorageUtil {
    public static String FILE_ENCODING = "UTF-8";
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    
    public static String getExternalFileName(final File storeDir, final String extDirName, final String fileName, final boolean createIt) {
        if (createIt) {
            File dir = new File(storeDir.getAbsolutePath()+File.separator + extDirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return storeDir.getAbsolutePath()+File.separator + extDirName + File.separator + fileName;
    }
    
    public static FileOutputStream getExternalFileOutputStream(final int modeWr, final String extDirName, final String fileName) throws FileNotFoundException {
        FileOutputStream outputStream = null;
        File externalFile = new File(getExternalFileName(Environment.getExternalStorageDirectory(), extDirName, fileName, true));
        if (externalFile != null) {
            if (externalFile.exists() && modeWr < 0) {
                externalFile.delete();
            }
            outputStream = new FileOutputStream(externalFile, true);
        }
        return outputStream;
    }
    
    public static int getInternalStoreWriteMode(final int modeWr) {
        int mode = Context.MODE_WORLD_READABLE;
        if (modeWr >= 0) {
            mode = modeWr;
        }
        return mode;
    }
    
    public static FileOutputStream getInternalFileOutputStream(final int modeWr, final Context context, final String fileName) throws FileNotFoundException {
        return context.openFileOutput(fileName, getInternalStoreWriteMode(modeWr));
    }
    
    public static FileOutputStream getFileOutputStream(final int modeWr, final Context context, final String extDirName, final String fileName) throws FileNotFoundException {
        FileOutputStream outputStream = null;
        if (isExternalStorageWritable()) {
            outputStream = getExternalFileOutputStream(modeWr, extDirName, fileName);
        }
        if (outputStream == null) {
            outputStream = getInternalFileOutputStream(modeWr, context, fileName);
        }
        return outputStream;
    }
    
    public static void writeFileExtOrInt(final FileOutputStream outputStream, final String msg, final boolean close) throws IOException {
        try {
            outputStream.write(msg.getBytes(FILE_ENCODING));
        } finally {
            if (close && outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {}
            }
        }
    }
    
    public static void writeFileExtOrInt(final int modeWr, final Context context, final String extDirName, final String fileName, final String msg) throws FileNotFoundException, IOException {
        writeFileExtOrInt(getFileOutputStream(modeWr, context, extDirName, fileName), msg, true);
    }
    
    public static void appendFileExtOrInt(final Context context, final String extDirName, final String fileName, final String msg) throws FileNotFoundException, IOException {
        writeFileExtOrInt(Context.MODE_APPEND, context, extDirName, fileName, msg);
    }
    public static void writeFileExtOrInt(final Context context, final String extDirName, final String fileName, final String msg) throws FileNotFoundException, IOException {
        writeFileExtOrInt(-1, context, extDirName, fileName, msg);
    }

    public static interface FileLineProcessor {
        boolean processLine(String s);
    }
    
    public static FileInputStream getExternalFileInputStream(final String extDirName, final String fileName) throws FileNotFoundException {
        FileInputStream fisFileName = null;
        File externalFile = null;
        if (isExternalStorageReadable()) {
            externalFile = new File(getExternalFileName(Environment.getExternalStorageDirectory(), extDirName, fileName, false));
        }
        if (externalFile != null && externalFile.exists() && externalFile.canRead()) {
            fisFileName = new FileInputStream(externalFile);
        }
        return fisFileName;
    }
    
    public static FileInputStream getFileInputStream(final Context context, final String extDirName, final String fileName) throws FileNotFoundException {
        FileInputStream fisFileName = getExternalFileInputStream(extDirName, fileName);
        if (fisFileName == null) {
            fisFileName = context.openFileInput(fileName);
        }
        return fisFileName;
    }
    
    public static long importFile(final Context context, final String extDirName, final String fileName, final FileLineProcessor fileLineProcessor) throws IOException {
        return importFile(getFileInputStream(context, extDirName, fileName), fileLineProcessor, true);
    }
    
    public static long importFile(final InputStream fisFileName, final FileLineProcessor fileLineProcessor, boolean closeIt) throws IOException {
        long ret = 0;
        try {
            if (fileLineProcessor != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(fisFileName, FILE_ENCODING));
                for (String lOrig=br.readLine(); lOrig != null;  lOrig=br.readLine()) {
                    ret++;
                    if (!fileLineProcessor.processLine(lOrig)) {
                        break;
                    }
                }
            }
        } finally {
            if (closeIt && fisFileName != null) {
                try {
                    fisFileName.close();
                } catch (Exception e) {}
            }
        }
        return ret;
    }
    
}

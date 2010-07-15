package org.openmrs.module.infopathconverter;

import org.apache.commons.io.IOUtils;
import org.openmrs.util.OpenmrsConstants;

import java.io.*;

public class CabFile {
    private File location;

    public CabFile(InputStream stream) throws IOException {
        location = expandXsnContents(IOUtils.toByteArray(stream));
    }

    public void forEachEntry(Action<CabEntry> action) throws Exception {
        for (File file : location.listFiles()) {
            action.execute(new CabEntry(file));
        }
    }


    private File expandXsnContents(byte[] xsnFileContents)
            throws IOException {
        // copy the xsn contents to a temporary directory
        File tempXsnFromDatabaseDir = createTempDirectory("XSN-db-file");
        if (tempXsnFromDatabaseDir == null)
            throw new IOException(
                    "Failed to create temporary content directory");

        // copy the xsn contents to a new file
        File tmpXsnFromDatabaseFile = new File(tempXsnFromDatabaseDir,
                "tempContent.xsn");
        OutputStream out = new FileOutputStream(tmpXsnFromDatabaseFile);
        out.write(xsnFileContents);
        out.flush();
        out.close();

        String xsnFilePath = tmpXsnFromDatabaseFile.getAbsolutePath();

        File expandedContentsDir = null;
        try {
            expandedContentsDir = expandXsn(xsnFilePath);
        } finally {
        }

        return expandedContentsDir;
    }

    private File expandXsn(String xsnFilePath) throws IOException {
        File xsnFile = new File(xsnFilePath);
        if (!xsnFile.exists())
            return null;

        File tempDir = createTempDirectory("XSN");
        if (tempDir == null)
            throw new IOException("Failed to create temporary directory");

        StringBuffer cmdBuffer = new StringBuffer();

        if (OpenmrsConstants.UNIX_BASED_OPERATING_SYSTEM) {

            // retrieve the cabextract path from the runtime properties
            String cabextLocation = null;
            if (cabextLocation == null)
                cabextLocation = "/usr/local/bin/cabextract";

            File cabextractExecutable = new File(cabextLocation);

            cmdBuffer.append(cabextLocation + " -d ").append(
                    tempDir.getAbsolutePath()).append(" ").append(xsnFilePath);
            execCmd(cmdBuffer.toString(), tempDir);
        } else {
            cmdBuffer.append("expand -F:* \"").append(xsnFilePath).append(
                    "\" \"").append(tempDir.getAbsolutePath()).append("\"");
            execCmd(cmdBuffer.toString(), null);
        }

        return tempDir;
    }

    private static String execCmd(String cmd, File wd) {
        StringBuffer out = new StringBuffer();
        try {
            // Needed to add support for working directory because of a linux
            // file system permission issue.
            // Could not create lcab.tmp file in default working directory
            // (jmiranda).
            Process p = (wd != null) ? Runtime.getRuntime().exec(cmd, null, wd)
                    : Runtime.getRuntime().exec(cmd);

            // get the stdout
            out.append("Normal cmd output:\n");
            Reader reader = new InputStreamReader(p.getInputStream());
            BufferedReader input = new BufferedReader(reader);
            int readChar = 0;
            while ((readChar = input.read()) != -1) {
                out.append((char) readChar);
            }
            input.close();
            reader.close();

            // get the errout
            out.append("ErrorStream cmd output:\n");
            reader = new InputStreamReader(p.getErrorStream());
            input = new BufferedReader(reader);
            readChar = 0;
            while ((readChar = input.read()) != -1) {
                out.append((char) readChar);
            }
            input.close();
            reader.close();

            // wait for the thread to finish and get the exit value
            int exitValue = p.waitFor();


        } catch (Exception e) {
        }

        return out.toString();
    }

    private File createTempDirectory(String prefix) throws IOException {
        String dirname = System.getProperty("java.io.tmpdir");
        if (dirname == null)
            throw new IOException("Cannot determine system temporary directory");

        File directory = new File(dirname);
        if (!directory.exists())
            throw new IOException("System temporary directory "
                    + directory.getName() + " does not exist.");
        if (!directory.isDirectory())
            throw new IOException("System temporary directory "
                    + directory.getName() + " is not really a directory.");

        File tempDir;
        do {
            String filename = prefix + System.currentTimeMillis();
            tempDir = new File(directory, filename);
        } while (tempDir.exists());

        if (!tempDir.mkdirs())
            throw new IOException("Could not create temporary directory '"
                    + tempDir.getAbsolutePath() + "'");

        tempDir.deleteOnExit();
        return tempDir;
    }

}

package com.bank.service;

import com.bank.service.time.TimeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class BackupAndRestoreService {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    private static final String DUMP_NAME = "bankbackup.sql";
    private static final String DIRECTORY_DUMP = System.getProperty("user.dir") + File.separator + DUMP_NAME;

    public boolean backup() throws InterruptedException, IOException {
        String[] executeDumpCommand = new String[]{"mysqldump", "--user=" + username, "--password=" + password, "--databases", "bank"};
        File dumpFile = new File(DIRECTORY_DUMP);

        if (dumpFile.exists() && !dumpFile.isDirectory()) {
            PrintWriter writer = new PrintWriter(dumpFile);
            writer.close();
        }

        Process proc = Runtime.getRuntime().exec(executeDumpCommand);

        OutputStream outputStream = new FileOutputStream(dumpFile);
        IOUtils.copy(proc.getInputStream(), outputStream);
        outputStream.close();

        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        String s;
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            stringBuilder.append(s + "\n");
        }

        int processComplete = proc.waitFor();
        if (processComplete == 0) {
            return true;
        } else {
            System.err.println(stringBuilder.toString());
            return false;
        }
    }

    public boolean restore() throws IOException, InterruptedException {
        String[] executeCmd = new String[]{"mysql", "--user=" + username, "--password=" + password, "-e", "source " + DIRECTORY_DUMP};
        File dumpFile = new File(DIRECTORY_DUMP);

        if (!(dumpFile.exists() && !dumpFile.isDirectory())) {
            throw new FileNotFoundException();
        }
        Process proc = Runtime.getRuntime().exec(executeCmd);

        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        String s;
        StringBuilder stringBuilder = new StringBuilder();
        while ((s = stdError.readLine()) != null) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }

        int processComplete = proc.waitFor();
        if (processComplete == 0) {
            TimeService.TIMESIMULATOR.reset();
            return true;
        } else {
            System.err.println(stringBuilder.toString());
            return false;
        }

    }




}

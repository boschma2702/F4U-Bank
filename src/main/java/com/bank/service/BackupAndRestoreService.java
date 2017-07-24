package com.bank.service;

import com.bank.service.time.TimeService;
import com.bank.util.Logger;
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
        Logger.info("Creating database backup");
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
        while ((s = stdError.readLine()) != null) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }

        int processComplete = proc.waitFor();
        if (processComplete == 0) {
            Logger.info("Successfully created backup of database");
            return true;
        } else {
            Logger.error("Could not create database backup, error output=%s", stringBuilder.toString());
            return false;
        }
    }

    public boolean restore() throws IOException, InterruptedException {
        Logger.info("Restoring database backup");
        String[] executeCmd = new String[]{"mysql", "--user=" + username, "--password=" + password, "-e", "source " + DIRECTORY_DUMP};
        File dumpFile = new File(DIRECTORY_DUMP);

        if (!(dumpFile.exists() && !dumpFile.isDirectory())) {
            Logger.info("Could not find backup file");
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
            Logger.info("Successfully restored backup of database");
            return true;
        } else {
            Logger.error("Could not restore database backup, error output=%s", stringBuilder.toString());
            return false;
        }

    }




}

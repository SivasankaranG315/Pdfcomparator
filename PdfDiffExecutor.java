import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class PdfDiffExecutor {

    public static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
        }
        return properties;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            // Get the path to the configuration file from the user
            System.out.println("Enter the path for the configuration file:");
            String configFilePath = scanner.nextLine().trim();

            // Verify file existence
            File configFile = new File(configFilePath);
            if (!configFile.exists()) {
                System.err.println("Configuration file does not exist: " + configFilePath);
                return;
            }

            // Load the properties from the configuration file
            Properties properties = loadProperties(configFilePath);

            // Debug: Print loaded properties
            System.out.println("Loaded properties:");
            properties.forEach((key, value) -> System.out.println(key + " = " + value));

            // Get the paths and filenames from the properties
            String diffpdfPath = properties.getProperty("diffpdfPath");
            String pdf1Path = properties.getProperty("pdf1Path");
            String pdf2Path = properties.getProperty("pdf2Path");
            String outputDir = properties.getProperty("outputDir");
            String outputFile = properties.getProperty("outputFile");

            // Debug: Print retrieved property values
            System.out.println("diffpdfPath: " + diffpdfPath);
            System.out.println("pdf1Path: " + pdf1Path);
            System.out.println("pdf2Path: " + pdf2Path);
            System.out.println("outputDir: " + outputDir);
            System.out.println("outputFile: " + outputFile);

            // Check if properties are null
            if (diffpdfPath == null || pdf1Path == null || pdf2Path == null || outputDir == null || outputFile == null) {
                System.err.println("One or more properties are null. Please check the configuration file.");
                return;
            }

            // Create the batch file content
            String batchContent = String.format(
                    "@echo off\n" +
                    "set DIFFPDF_PATH=\"%s\"\n" +
                    "set PDF1_PATH=\"%s\"\n" +
                    "set PDF2_PATH=\"%s\"\n" +
                    "set OUTPUT_DIR=\"%s\"\n" +
                    "set OUTPUT_FILE=\"%s\"\n\n" +
                    "echo Running comparison command...\n\n" +
                    "\"%s\" -v -s -r \"%s\\%s\" \"%s\" \"%s\"\n\n" +
                    "echo Comparison completed.\n\n" +
                    "pause",
                    diffpdfPath.replace("\\", "\\\\"), 
                    pdf1Path.replace("\\", "\\\\"), 
                    pdf2Path.replace("\\", "\\\\"), 
                    outputDir.replace("\\", "\\\\"), 
                    outputFile,
                    diffpdfPath.replace("\\", "\\\\"), 
                    outputDir.replace("\\", "\\\\"), 
                    outputFile, 
                    pdf1Path.replace("\\", "\\\\"), 
                    pdf2Path.replace("\\", "\\\\"));

            // Print the batch file content for debugging
            System.out.println("Batch file content:");
            System.out.println(batchContent);

            // Write the batch file
            File batchFile = new File("runComparison.bat");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(batchFile))) {
                writer.write(batchContent);
            } catch (IOException e) {
                System.err.println("Error writing batch file: " + e.getMessage());
                return;
            }

            // Execute the batch file using ProcessBuilder
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "/wait", "runComparison.bat");
                processBuilder.inheritIO();  // This makes the new process use the same console
                Process process = processBuilder.start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                System.err.println("Error executing batch file: " + e.getMessage());
            }
        }
    }
}

package org.example;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*; // Import java.util package for List and Timer

public class PdfClassSchedulerApp extends JFrame {

    private JTextArea textArea;
    private JButton choosePdfButton;
    private java.util.Timer timer; // Specify java.util.Timer to avoid ambiguity

    public PdfClassSchedulerApp() {
        setTitle("Class Schedule Notification App");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // UI components
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        choosePdfButton = new JButton("Choose PDF File");
        choosePdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseAndProcessPdf();
            }
        });

        // Add components to the frame
        add(choosePdfButton, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void chooseAndProcessPdf() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String extractedText = extractTextFromPdf(selectedFile.getAbsolutePath());
            textArea.setText(extractedText);

            java.util.List<String> classTimes = parseClassTimes(extractedText);  // Specify java.util.List
            scheduleNotifications(classTimes);
        }
    }

    // Extract text from the chosen PDF file
    private String extractTextFromPdf(String filePath) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading PDF file!";
        }
    }

    // Parse the class times from the extracted text
    private java.util.List<String> parseClassTimes(String pdfText) {
        java.util.List<String> classTimes = new ArrayList<>();  // Use java.util.List
        String[] lines = pdfText.split("\n");
        for (String line : lines) {
            if (line.matches("\\d{2}:\\d{2} (AM|PM) - .+")) {
                classTimes.add(line);
            }
        }
        return classTimes;
    }

    // Schedule notifications for the parsed class times
    private void scheduleNotifications(java.util.List<String> classTimes) {  // Use java.util.List
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        Calendar now = Calendar.getInstance();

        for (String classTime : classTimes) {
            String[] parts = classTime.split(" - ");
            String timeString = parts[0];
            String className = parts[1];

            try {
                Date classDate = dateFormat.parse(timeString);
                Calendar classCalendar = Calendar.getInstance();
                classCalendar.setTime(classDate);

                // Set the notification for the class
                if (classCalendar.after(now)) {
                    long delay = classCalendar.getTimeInMillis() - now.getTimeInMillis();
                    scheduleNotification(className, delay);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to schedule notification with a delay using java.util.Timer
    private void scheduleNotification(String className, long delay) {
        timer = new java.util.Timer();  // Explicitly use java.util.Timer
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "Time for: " + className);
            }
        }, delay);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PdfClassSchedulerApp app = new PdfClassSchedulerApp();
            app.setVisible(true);
        });
    }
}

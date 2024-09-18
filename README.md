 Java desktop app that reads a PDF containing class times, extracts calendar data, and generates pop-up notifications for each class, using following approach:

Reading the PDF: Use a PDF library like Apache PDFBox or iText to extract text from the PDF. These libraries allow you to read and parse the content of a PDF file.

Extracting Calendar Data: Since the PDF contains a monthly calendar with class schedules, you need to implement logic to parse and extract the class times. Regular expressions or string manipulation techniques can be helpful in identifying class times.

Scheduling Notifications: Use the java.util.Timer or ScheduledExecutorService to schedule pop-up notifications. You can trigger a notification before each class based on the extracted data.

Displaying Notifications:use the Swing or JavaFX library to create pop-up messages or desktop notifications.


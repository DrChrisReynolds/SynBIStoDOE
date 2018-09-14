import java.awt.*;
import java.io.*;

public class SaveFile {
	
    public boolean  saveData(String dataToSave) {

		// Bring up a file save dialog box
        FileDialog fileDialog = new FileDialog(new Frame(), "Save", FileDialog.SAVE);
        fileDialog.setFilenameFilter(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        fileDialog.setFile("DoE_data.csv");
        fileDialog.setVisible(true);
        
        //Write the file
        boolean successfulSave=false;
        String fileName =fileDialog.getDirectory()+ fileDialog.getFile(); 
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(dataToSave);
			printWriter.close();
		} catch (IOException ioe) {
			System.err.println("Caught IOException: " + ioe.getMessage() + "\nCould not write file " + fileName);
		} finally {
			System.out.println("Successfully written file: " + fileName);
			successfulSave = true;
		}
		return successfulSave;
	}
}
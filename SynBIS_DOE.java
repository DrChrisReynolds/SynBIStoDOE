import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

// An AWT program inherits from the top-level container java.awt.Frame
public class SynBIS_DOE extends Frame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Label myLabel;			// Declare a Label component
	private TextField myTextBox;	// Declare a TextField component
	private Button myButton,exportButton;		// Declare a Button component

	private TreeMap<Double,ArrayList<SynBISdata>> dataList=new TreeMap<Double,ArrayList<SynBISdata>>();
	
/*
	public static void main(String[] args) {
	 Test_HTTP_XML oobj_Test_HTTP_XML=new Test_HTTP_XML();
	 oobj_Test_HTTP_XML.get_response();
	}
	*/
	
	public boolean get_response(String partID, SynBISdata sb) {

		//SynBISdata sb = new SynBISdata();
		
		for (int j = 0; j < 2; j++) {
			
			try {
				String url = "http://synbis.bg.ic.ac.uk/synbisapi/datasheet/"+partID;
				sb.setDisplayID(partID);
				
				//http://synbis.bg.ic.ac.uk/synbisapi/datasheet/
				System.out.println("URL:\t"+url);
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				int responseCode = con.getResponseCode();
				System.out.println("Response Code:\t" + responseCode);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				// print in String
				
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.parse(new InputSource(new StringReader(response.toString())));
				
				String nativeFrom=doc.getElementsByTagName("synbis:nativeFrom").item(0).getTextContent();
				sb.setNativeFrom(nativeFrom);
				
				
				String s = "synbis:MetricsTable";
				if (j == 1)
					s = "sbol:Sequence";
				
				NodeList errNodes = doc.getElementsByTagName(s);
								
				/*	
				to get:
				 
	<sbol:displayId>
	<synbis:nativeFrom>
	<sbol:elements>
	<synbis:rpu3>
	
				 */
				System.out.println("About:\t"+errNodes.item(0).getAttributes().getNamedItem("rdf:about").getNodeValue());
				
				System.out.println(errNodes.getLength()+ " nodes");
				for (int i = 0; i < errNodes.getLength(); i++) {
					Element err = (Element) errNodes.item(i);
					
					String s2 = "synbis:rpu3";
					if (j == 1)
						s2 = "sbol:elements";
					
					NodeList n = err.getElementsByTagName(s2);
					System.out.println(n.item(0).getTextContent());
										
					if (j == 0)
						sb.setRPU(Double.parseDouble(n.item(0).getTextContent()));
					else
						sb.setElements(n.item(0).getTextContent());
					
					//System.out.println(n.item(0).getTextContent());
					//n = err.getElementsByTagName("sbol:elements");
					//System.out.println(n.item(0).getTextContent());
					
				}
	
			} catch (Exception e) {
				System.out.println(e);
				return false;
			} // end try
			
		} // next j
			
		return true;
		
	}
	
	// Constructor to setup GUI components and event handlers
	public SynBIS_DOE() {
		
		setLayout(new FlowLayout());
		
		// Default large window size
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});		

        // "super" Frame, which is a Container, sets its layout to FlowLayout to arrange
        // the components from left-to-right, and flow to next row from top-to-bottom.
       
		// Open up the XML of SynBIS

        myLabel = new Label("Enter an ID");  // construct the Label component
        add(myLabel);                    // "super" Frame container adds Label component
 
        myTextBox = new TextField("J23108", 10); // construct the TextField component with initial text
        myTextBox.setEditable(true);       // set to read-only
        add(myTextBox);                     // "super" Frame container adds TextField component
 
        myButton = new Button("Add a part");   // construct the Button component
        add(myButton, BorderLayout.SOUTH);                    // "super" Frame container adds Button component
		myButton.addActionListener(this);
		myButton.setActionCommand("adding");
				
        exportButton = new Button("Export");   // construct the Button component
        add(exportButton, BorderLayout.SOUTH);
		exportButton.addActionListener(this);
		exportButton.setActionCommand("exporting");
        
        // "btnCount" is the source object that fires an ActionEvent when clicked.
        // The source add "this" instance as an ActionEvent listener, which provides
        // an ActionEvent handler called actionPerformed().
        // Clicking "btnCount" invokes actionPerformed().
 
		setTitle("SynBIS to DOE");	// "super" Frame sets its title
		setSize(250, 100); 			// "super" Frame sets its initial window size

		// For inspecting the Container/Components objects
		// System.out.println(this);
		// System.out.println(lblCount);
		// System.out.println(tfCount);
		// System.out.println(btnCount);

		setVisible(true); // "super" Frame shows

		// System.out.println(this);
		// System.out.println(lblCount);
		// System.out.println(tfCount);
		// System.out.println(btnCount);
	}
 
   // The entry main() method
   public static void main(String[] args) {
		// Invoke the constructor to setup the GUI, by allocating an instance
		SynBIS_DOE app = new SynBIS_DOE();
		// or simply "new AWTCounter();" for an anonymous instance
	}
 
	// ActionEvent handler - Called back upon button-click.
	public void actionPerformed(ActionEvent evt) {
		System.out.println(evt.getActionCommand());

		if (evt.getActionCommand().equals("adding")) {

			String partID = myTextBox.getText();
			System.out.println(partID);

			SynBISdata myData = new SynBISdata();
			
			boolean response=get_response(partID ,  myData);
			
			if (response){
				double myRPU = myData.getRPU();
				ArrayList<SynBISdata> tempArray;
				if (dataList.containsKey(myRPU)) {
					tempArray = dataList.get(myRPU);
				} else {
					tempArray = new ArrayList<SynBISdata>();
				}
				tempArray.add(myData);
				dataList.put(myRPU, tempArray);				
			}

			// Display the counter value on the TextField tfCount
			myTextBox.setText(""); // Reset the text box

		} else {

			// Initialise the data to write
			String header = "JMP Rank,DisplayID,nativeFrom,elements,rpu3";
			System.out.println(header);
			StringBuilder dataToSave = new StringBuilder(header + "\n");

			// Loop through dataList
			int rank = 0;
			for (double d : dataList.keySet()) {
				for (SynBISdata sb : dataList.get(d)) {
					rank++;
					String csvLine = rank + "," + sb.toString() + "\n";
					System.out.print(csvLine);
					dataToSave.append(csvLine);
				}
			}
			new SaveFile().saveData(dataToSave.toString());
		}
	}
}
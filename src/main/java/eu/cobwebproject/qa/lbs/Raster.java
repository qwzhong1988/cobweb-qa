package eu.cobwebproject.qa.lbs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Raster {
	private final Parameters params;		// The parameters of the data (e.g., rows, cols) 
	private final double[][] surfaceModel;	// The actual surface model data
	private final String fileName;			// the fileName if we did the parseing

	/**
	 * Construct a raster with the parameters and data already parsed.
	 * note: The String file constructor is preferred. 
	 * 
	 * @param p A parsed parsed Parameter object
	 * @param surfaceModel The surface model as a 2d double array
	 */
	public Raster(Parameters p, double[][] surfaceModel) {
		this.fileName = null;
		this.params = p;
		this.surfaceModel = surfaceModel;
	}
	
	/**
	 * Constructor returns a Raster parsed from the file path specified as string
	 * 
	 * @param file A String of the path to the file to parse
	 * @throws IOException If the file can't be found or anything else goes wrong whilst reading
	 */
	public Raster(String file) throws IOException {
		this.fileName = file;	
		// read and parse header from filename to parameters
		double[] headerData = readRasterHeader();	
		this.params = new Parameters((int) headerData[4], 
									 (int) headerData[0], 
									 (int) headerData[1], 
									 headerData[2], 
									 headerData[3], 
									 headerData[5]);
		// read and parse surface data from filename
		this.surfaceModel = readAsciiData();	
	}
	
	/**
	 * Getter for the params
	 * @return the parameters
	 */
	public Parameters getParams() {
		return params;
	}
	
	/**
	 * Getter for the surface model
	 * @return the surface model
	 */
	public double[][] getSurfaceModel() {
		return surfaceModel;
	}
	
	/**
	 * Private function to read the raster header data and return
	 * it as a double array (number of columns, number of rows, 
	 * X lower corner, Y lower corner, the cell size, no data value)
	 * 
	 * @return an array of doubles containing the ascii header data 
	 * @throws IOException if there is a problem reading the file
	 */
	private double[] readRasterHeader() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));		
	
		double[] headerData = new double[6];
		
		try {
			
			for(int i = 0; i < 6; i++) {				
				int skip = 0;	// chars to skip before value
				
				switch (i) {
				case 0:
					skip = 6;
					break;	
				case 1:
					skip = 6;
					break;
				case 2: 	
					skip = 9;
					break;
				case 3:
					skip = 9;
					break;
				case 4: 	
					skip = 8;
					break;
				case 5: 	
					skip = 12;
					break;						
				}
			
				String DSMline = br.readLine();
				char[] buffer = new char[DSMline.length()];
				DSMline.getChars(skip,DSMline.length(), buffer, 0);
				headerData[i] = Double.parseDouble(String.valueOf(buffer));
			}
			
		} finally {
			br.close();
		}
		
		return headerData;
	}
	
	/**
	 * Private function to read the surface model raster from the ascii file
	 * 
	 * @return a 2d array of doubles representing the height field
	 * @throws IOException If there is a problem reading from the file
	 */
	private double[][] readAsciiData() throws IOException {
		String DSMline;
		
		// read the header data and instantiate array for surface model data
		double[] headerData = readRasterHeader();
		double[][] ASCIIData = new double[(int) headerData[0]][(int) headerData[1]];
		
		// open the file and skip the header
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		for(int i = 0; i < 6; i++) 
			br.readLine();	// skip	
		
		try { // read the data	
			for(int i = 0;i < headerData[0];i++) {
				DSMline = br.readLine();
				String [] temp = new String[(int) headerData[1]];
				temp = DSMline.split("[ ]+");
				for(int j = 0;j < headerData[1];j++) {
					ASCIIData[i][j] = Double.parseDouble(temp[j]);				
			 	}
			}
		} finally {	// close even if error thrown
			br.close();
		}
		
		return ASCIIData;
	}
	
	public String toString() {
		if(fileName != null)
			return fileName;
		return "Manually parsed raster";
	}
}

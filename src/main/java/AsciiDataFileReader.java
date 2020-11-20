
import cern.colt.list.IntArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;


public class AsciiDataFileReader {


    private static Logger logger  = Logger.getLogger(AsciiDataFileReader.class);
    private static int bufferSize = 256000;

    private File file;
    private int nRows;
    private int nHeaderRows;
    private int nDataRows;
    private int nBlankRows;
    private int nDataCols;
    private String[] header;
    private String[] dataLines;
    private String[][] data;



    //  Constructors
    public AsciiDataFileReader(File file) throws AsciiDataFileFormatException, IOException  {

	this.file = file;
	readDataFile();
    }

    public AsciiDataFileReader(String filename) throws AsciiDataFileFormatException, IOException {

	this.file = new File(filename);
	readDataFile();
    }



    //  Public "get" methods
    public int getNRows() { return this.nRows; }

    public int getNDataRows() { return this.nDataRows; }

    public int getNHeaderRows() { return this.nHeaderRows; }

    public int getNDataCols() { return this.nDataCols; }

    public String[] getHeader() { return Arrays.copyOf(this.header, this.header.length); }

    private String[] getCol(int columnIndex) {

	String[] col = new String[nDataRows];

	for ( int i=0; i < this.nDataRows; i++ ) {
	    
	    col[i] = this.data[i][columnIndex];
	}

	return col;
    }

    public double[] getDblCol(int columnIndex) {

	String[] col = getCol(columnIndex);
	double[] dblCol = new double[this.nDataRows];

	for ( int i=0; i < this.nDataRows; i++ ) {

	    try {
		dblCol[i] = (Double.valueOf(col[i])).doubleValue();
	    }
	    catch ( NumberFormatException e ) {
		
		dblCol[i] = Double.NaN;
	    }
	}

	return dblCol;
    }

    public float[] getFltCol(int columnIndex) {

	String[] col = getCol(columnIndex);
	float[] fltCol = new float[this.nDataRows];

	for ( int i=0; i < this.nDataRows; i++ ) {

	    try {
		fltCol[i] = (Float.valueOf(col[i])).floatValue();
	    }
	    catch ( NumberFormatException e ) {
		
		fltCol[i] = Float.NaN;
	    }
	}
	    
	return fltCol;
    }

    public String[] getStrCol(int columnIndex) {

	return getCol(columnIndex);
    }

    public int[] getIntCol(int columnIndex) {

	String[] col = getCol(columnIndex);
	int[] intCol = new int[this.nDataRows];

	for ( int i=0; i < this.nDataRows; i++ ) {

	    try {
		intCol[i] = (Integer.valueOf(col[i])).intValue();
	    }
	    catch ( NumberFormatException e ) {
		
		intCol[i] = (int) Float.NaN;
	    }
	}

	return intCol;
    }



    //  Read the data from the file

    private void readDataFile() throws AsciiDataFileFormatException, IOException  {


	//  Initialize the buffered reader
	BufferedReader br = new BufferedReader(new FileReader(this.file), bufferSize);

	//  Get the data from the file
	ArrayList headerLinesArrayList = new ArrayList();
	ArrayList dataLinesArrayList = new ArrayList();
	IntArrayList nColsPerDataRowIntList = new IntArrayList();
	while ( true ) {  	

	    //  Read the line While there is data to be read
	    if ( ! br.ready() ) break;
	    String line = br.readLine();

	    //  Store the information contained in the file
	    try {
		StringTokenizer st = new StringTokenizer(line);
		String token = st.nextToken();
		double element = (Double.valueOf(token)).doubleValue();
		dataLinesArrayList.add(line);
		this.nDataRows++;
		
		//  Count the number of data elements in this row
		int nDataCols = 1;
		while ( st.hasMoreTokens() ) {
		    nDataCols++;
		    st.nextToken();
		}
		nColsPerDataRowIntList.add(nDataCols);
	    }
	    catch (NumberFormatException e) {
		
		headerLinesArrayList.add(line);
		this.nHeaderRows++;
	    }
	    catch (NoSuchElementException e) {
		
		this.nBlankRows++;
	    }
	}

	//  Close the stream
	try {
	    if ( br != null ) {
		br.close();
	    }
	}
	catch (IOException e) {
	    logger.error("Could not close BufferedReader \n"+e.getMessage());
	}


	//   Set the number of rows
	this.nRows = this.nHeaderRows + this.nDataRows + this.nBlankRows;
	if ( this.nRows == 0 ) {
	    throw new NoSuchElementException("File is empty");
	}
	if ( this.nDataRows == 0 ) {
	    throw new AsciiDataFileFormatException("There are 0 data lines");
	}
	logger.info("File "+file.getPath()+" contains "+this.nRows+" total lines:");
	logger.info("  "+this.nHeaderRows+" header lines");
	logger.info("  "+this.nDataRows+" data lines");
	logger.info("  "+this.nBlankRows+" blank lines");


	//  Set the number of data columns 
	nColsPerDataRowIntList.trimToSize();
	int[] nColsPerDataRow = nColsPerDataRowIntList.elements();
	double var = getVariance(nColsPerDataRow);
	if ( var > 1e-10 ) {
	    throw new AsciiDataFileFormatException("Data rows do not have the same number of columns");
	}
	this.nDataCols = nColsPerDataRow[0];
	logger.info("File contains "+ this.nDataCols+" data columns");


	//  Set the header
	headerLinesArrayList.trimToSize();
	Object[] h = headerLinesArrayList.toArray();
	this.header = new String[h.length];
	for ( int i=0; i < h.length; i++ ) {
	    this.header[i] = (String) h[i];
	}


	//  Set the data lines
	dataLinesArrayList.trimToSize();
	Object[] d = dataLinesArrayList.toArray();
	this.dataLines = new String[d.length];
	for ( int i=0; i < d.length; i++ ) {
	    this.dataLines[i] = (String) d[i];
	}
	

	//  Set the data
	this.data = new String[this.nDataRows][this.nDataCols];
	for ( int i=0; i < this.nDataRows; i++ ) {

	    StringTokenizer st = new StringTokenizer(dataLines[i]);
	    for ( int j=0; j < this.nDataCols; j++ ) {

		this.data[i][j] = st.nextToken();
	    }
	}

    }

    public static double getVariance(int[] data) {
		
	double mean = getMean(data);
	double sum = 0;
	int n = 0;
	for ( int i=0;  i < data.length; i++ ) {
	    if ( !Double.isNaN(data[i]) ) { //&& data[i] != 0.0 ) {
		sum += Math.pow(data[i] - mean, 2);
		n++;
	    }
	}
	double variance = sum/(n-1);
	return variance;
    }

    public static double getMean(int[] data) {
		
	double sum = 0;
	int n = 0;
	for ( int i=0; i < data.length; i++ ) {
			
	    if ( !Double.isNaN(data[i]) ) { //&& data[i] != 0 ) {
		sum += data[i];
		n++;
	    }
	}
	double mean = sum/n;
	return mean;
    }

}

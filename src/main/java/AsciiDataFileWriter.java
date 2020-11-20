
//import hep.aida.IAxis;
//import hep.aida.IHistogram1D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import org.apache.log4j.Logger;



/**
 * The class <code>AsciiDataFileWriter</code> is used to write data as ASCI files in QDP format.
 *
 * @author <a href="mailto: guilaume.belanger@esa.int">Guillaume Belanger</a>
 * @version 1.0 (June 2010, ESAC)
 */
public class AsciiDataFileWriter {


    private static Logger logger  = Logger.getLogger(AsciiDataFileWriter.class);

    private PrintWriter printWriter;
    private static int bufferSize = 256000;

    private static DecimalFormat num = new DecimalFormat("0.0000000000#E00");


    //  Constructor
    public AsciiDataFileWriter(String filename) throws IOException {
  	printWriter = new PrintWriter(new BufferedWriter(new FileWriter(filename), bufferSize));
    }


//     public void writeHisto(IHistogram1D histo, String xAxisLabel) {

// 	//  Get data from histo
// 	IAxis axis = histo.axis();
// 	int nBins = axis.bins();
// 	double[] binHeights = new double[nBins];
// 	double[] binCentres = new double[nBins];
// 	double[] halfWidths = new double[nBins];
// 	for ( int i=0; i < nBins; i++ ) {
// 	    binHeights[i] = histo.binHeight(i);
// 	    binCentres[i] = axis.binCenter(i);
// 	    halfWidths[i] = axis.binWidth(i)/2;
// 	}

// 	// Define QDP header
// 	String[] header = new String[] {
// 	    "DEV /XS",
// 	    "READ SERR 1",
// 	    "LAB T", "LAB F",
// 	    "TIME OFF",
// 	    "LINE STEP",
// 	    "LW 3", "CS 1.3",
// 	    "LAB X "+xAxisLabel,
// 	    "LAB Y Entries per bin",
// 	    "VIEW 0.2 0.1 0.8 0.9",
// 	    "!"
// 	};

// 	//  Write to file
// 	for ( int i=0; i < header.length; i++ ) {
// 	    printWriter.println(header[i]);
// 	}
// 	for ( int i=0; i < nBins; i++ ) {
// 	    printWriter.println((binCentres[i]) +"\t"+ (halfWidths[i]) +"\t"+ (binHeights[i]) +"\t");
// 	}
// 	printWriter.flush();
// 	printWriter.close();
//     }


    public void writeData(String[] header, double[] x, double[] y) throws IOException {

	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int nbins = (new Double(Math.min(x.length, y.length))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((x[i]) +"\t"+ (y[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, int[] x, double[] y) throws IOException {

	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int nbins = (new Double(Math.min(x.length, y.length))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((x[i]) +"\t"+ (y[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, int[] x, int[] y) throws IOException {

	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int nbins = (new Double(Math.min(x.length, y.length))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println(x[i] +"\t"+ y[i] +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, int[] x, double[] y, double[] y2) throws IOException {

	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {x.length, y.length, y2.length};
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((x[i]) +"\t"+ (y[i]) +"\t"+ (y2[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, int[] col1, int[] col2, double[] col3) throws IOException {

	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {col1.length, col2.length, col3.length};
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((col1[i]) +"\t"+ (col2[i]) +"\t"+ (col3[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, String[] col1, int[] col2, double[] y) throws IOException {

	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]); 
	for ( int i=0; i < col1.length; i++ ) {
	    printWriter.println(col1[i] +"\t"+ (col2[i]) +"\t"+ (y[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, double[] c1, double[] c2, double[] c3) throws IOException {

	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {c1.length, c2.length, c3.length};
	double var = getVariance(lengths);
	if ( var != 0 ) {
	    logger.warn("input column data of different lengths. Using min.");
	}
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((c1[i]) +"\t"+ (c2[i]) +"\t"+ (c3[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, double[] c1, double[] c2, double[] c3, double[] c4) 
	throws IOException {

	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {c1.length, c2.length, c3.length, c4.length};
	double var = getVariance(lengths);
	if ( var != 0 ) {
	    logger.warn("input column data of different lengths. Using min.");
	}
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((c1[i]) +"\t"+ (c2[i]) +"\t"+ 
		       (c3[i]) +"\t"+ (c4[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }
    
    public void writeData(String[] header, double[] c1, double[] c2, double[] c3, double[] c4, double[] c5) 
	throws IOException {
	
	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {c1.length, c2.length, c3.length, c4.length, c5.length};
	double var = getVariance(lengths);
	if ( var != 0 ) {
	    logger.warn("input column data of different lengths. Using min.");
	}
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((c1[i]) +"\t"+ (c2[i]) +"\t"+ 
		       (c3[i]) +"\t"+ (c4[i]) +"\t"+ 
		       (c5[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, double[] c1, double[] c2, double[] c3, double[] c4, double[] c5, double[] c6) 
	throws IOException {
	
	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {c1.length, c2.length, c3.length, c4.length, c5.length, c6.length};
	double var = getVariance(lengths);
	if ( var != 0 ) {
	    logger.warn("input column data of different lengths. Using min.");
	}
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((c1[i]) +"\t"+ (c2[i]) +"\t"+ 
		       (c3[i]) +"\t"+ (c4[i]) +"\t"+
		       (c5[i]) +"\t"+ (c6[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }


	public void writeData(String[] header, double[] c1, double[] c2, double[] c3, double[] c4, double[] c5, double[] c6, double[] c7) 
	throws IOException {
	
	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {c1.length, c2.length, c3.length, c4.length, c5.length, c6.length, c7.length};
	double var = getVariance(lengths);
	if ( var != 0 ) {
	    logger.warn("input column data of different lengths. Using min.");
	}
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((c1[i]) +"\t"+ (c2[i]) +"\t"+ 
		       (c3[i]) +"\t"+ (c4[i]) +"\t"+
		       (c5[i]) +"\t"+ (c6[i]) +"\t"+
		       (c7[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }


    public void writeData(String[] header, double[] c1, double[] c2, double[] c3, double[] c4, double[] c5, double[] c6, double[] c7, double[] c8) 
	throws IOException {
	
	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {c1.length, c2.length, c3.length, c4.length, c5.length, c6.length, c7.length, c8.length};
	double var = getVariance(lengths);
	if ( var != 0 ) {
	    logger.warn("input column data of different lengths. Using min.");
	}
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((c1[i]) +"\t"+ (c2[i]) +"\t"+ 
		       (c3[i]) +"\t"+ (c4[i]) +"\t"+
		       (c5[i]) +"\t"+ (c6[i]) +"\t"+
		       (c7[i]) +"\t"+ (c8[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
    }

    public void writeData(String[] header, double[] c1, double[] c2, double[] c3, double[] c4, double[] c5, double[] c6, double[] c7, double[] c8, double[] c9, double[] c10, double[] c11) 
	throws IOException {
	
	
	for ( int i=0; i < header.length; i++ )  printWriter.println(header[i]);
	int lengths[] = new int[] {c1.length, c2.length, c3.length, c4.length, c5.length, c6.length, c7.length, c8.length, c9.length, c10.length, c11.length};
	double var = getVariance(lengths);
	if ( var != 0 ) {
	    logger.warn("input column data of different lengths. Using min.");
	}
	int nbins = (new Double(getMin(lengths))).intValue();
	for ( int i=0; i < nbins; i++ ) {
	    printWriter.println((c1[i]) +"\t"+ (c2[i]) +"\t"+ 
		       (c3[i]) +"\t"+ (c4[i]) +"\t"+
		       (c5[i]) +"\t"+ (c6[i]) +"\t"+
		       (c7[i]) +"\t"+ (c8[i]) +"\t"+
		       (c9[i]) +"\t"+ (c10[i]) +"\t"+
		       (c11[i]) +"\t");
	}
	printWriter.flush();
	printWriter.close();
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

    public static double getMin(int[] data) {
		
	double min = Integer.MAX_VALUE;
	for ( int i=0; i < data.length; i++ )
	    min = (new Double(Math.min(min, data[i]))).intValue();
	return min;
    }


}
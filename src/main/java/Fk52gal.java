import java.awt.geom.Point2D;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import jsky.coords.wcscon;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Fk52gal{

    private static Logger logger  = Logger.getLogger(Fk52gal.class);
    private static String logFilename= "logger.config";
    private static File logFile = new File(logFilename);

    public static void main (String[] args) throws IOException, AsciiDataFileFormatException {

	DecimalFormat coord = new DecimalFormat("0.0000");

	//  Configure the logger
	InputStream is = ClassLoader.getSystemResourceAsStream(logFilename);
   	inputStreamToFile(is, logFilename);
	PropertyConfigurator.configure(logFilename);

	wcscon convert = new wcscon();
	if ( args.length == 1 ) {
	    String raDecFilename = args[0];
	    AsciiDataFileReader input = new AsciiDataFileReader(raDecFilename);
	    double[] ras = input.getDblCol(0);
	    double[] decs = input.getDblCol(1);
	    int nPoints = ras.length;
	    double[] l = new double[nPoints];
	    double[] b = new double[nPoints];
	    for ( int i=0; i < nPoints; i++ ) {
		Point2D.Double raDec = new Point2D.Double(ras[i], decs[i]);
		Point2D.Double lb = convert.fk52gal(raDec);
		l[i] = lb.getX();
		b[i] = lb.getY();
	    }
	    String lbFilename = "galCoords.txt";
	    AsciiDataFileWriter output = new AsciiDataFileWriter(lbFilename);
	    output.writeData(new String[] {}, l, b);
	}
	else if ( args.length == 2 ) {
	    double ra = (Double.valueOf(args[0])).doubleValue();
	    double dec = (Double.valueOf(args[1])).doubleValue();
	    Point2D.Double raDec = new Point2D.Double(ra, dec);
	    Point2D.Double lb = convert.fk52gal(raDec);
	    logger.info(coord.format(lb.getX())+" "+coord.format(lb.getY()));
	}
	else {
	    logger.info("Usage: java Fk52gal ra dec (in degrees)");
	    logger.info("Usage: java Fk52gal raDecList.txt");
	    logFile.delete();
	    System.exit(-1);
	}
	logFile.delete();
    }

    public static void inputStreamToFile(InputStream io, String fileName) throws IOException {
	
	FileOutputStream fos = new FileOutputStream(fileName);
	byte[] buf = new byte[256];
	int read = 0;
	while ((read = io.read(buf)) > 0) {
	    fos.write(buf, 0, read);
	}
	fos.flush();
	fos.close();
    }

}

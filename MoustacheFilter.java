import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_highgui.CvCapture;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacpp.opencv_imgproc.CvMoments;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.VideoInputFrameGrabber;
//import org.bytedeco.javacpp.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

public class MoustacheFilter {
	static BufferedImage moustache;
	static int x = 0, y = 0;
	public static void main(String[] args) throws Exception {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		grabber.setImageWidth(640);
		grabber.setImageHeight(480);
		try {
			moustache = ImageIO
					.read(new File("C:\\Users\\Vaibhav\\Desktop\\Java\\hi\\OCR\\bin\\moustache.png")); // Load Image You want diplayed under nose
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Cvdisplay(filter, "Input");
		// ("C:\\Users\\Vaibhav\\Desktop\\Java\\hi\\bbnm\\bin\\images.png");
		grabber.start();
		CanvasFrame cFrame = new CanvasFrame("Capture Preview",
				CanvasFrame.getDefaultGamma() / grabber.getGamma());

		IplImage capturedFrame = null;

		// While we are capturing...
		while ((capturedFrame = grabber.grab()) != null) {
			if (cFrame.isVisible()) {
				// filter.alphaChannel(0);
				// Show our frame in the preview
				cFrame.showImage(detect(capturedFrame));

			}
		}
	}

	public static BufferedImage detect(IplImage src) {

		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
				cvLoad("C:\\Users\\Vaibhav\\Desktop\\Java\\hi\\OCR\\bin\\Nariz.xml"));// Path of Haar Classifier Cascade
		CvMemStorage storage = CvMemStorage.create();
		CvSeq sign = cvHaarDetectObjects(src, cascade, storage, 1.5, 3, 
				CV_HAAR_DO_CANNY_PRUNING);  // Using Haar Classifier for Nose Detetcion

		cvClearMemStorage(storage);

		int total_Faces = sign.total();
		//int x = 0, y = 0;
		for (int i = 0; i < total_Faces; i++) {
			CvRect r = new CvRect(cvGetSeqElem(sign, i));
			//cvRectangle(src, cvPoint(r.x() - 20, r.y() + 50),
				//	cvPoint(r.width() + r.x() + 50, r.height() + r.y() - 20),
				//	CvScalar.RED, 2, CV_AA, 0);
			// Graphics g = IplImageToBufferedImage(src).getGraphics();
			x = (cvPoint(r.x() - 20, r.y() + 50).x() + cvPoint(
					r.width() + r.x() + 50, r.height() + r.y() - 20).x()) / 2; // x co-ordinate
			y = (cvPoint(r.x() - 20, r.y() + 50).y() + cvPoint(
					r.width() + r.x() + 50, r.height() + r.y() - 20).y()) / 2; // y co-ordinate
			/*
			 * g.drawImage( moustache, cvPoint(r.x() - 20, r.y() + 50).x(),
			 * cvPoint(r.width() + r.x() + 50, r.height() + r.y()).y() - 20,
			 * null);
			 */
		}
		BufferedImage I = src.getBufferedImage();
		return drawFilter(I, x-100, y); // Drawing Moustache at x and y co-ordinates
		// cvShowImage("Result", src);
		// cvWaitKey(0);

	}

	public static BufferedImage IplImageToBufferedImage(IplImage src) {
		return src.getBufferedImage();
	}

	static BufferedImage drawFilter(BufferedImage src, int x, int y) {

		// create one and only one BufferedImage object.
		// If this fails, the exception will bubble up the call chain
		// BufferedImage bufferedImage =
		// ImageIO.read(getClass().getResource(resourcePath));

		// get the Graphics context for this single BufferedImage object
		Graphics g = src.getGraphics();

		g.drawImage(moustache, x, y, null);

		g.dispose(); // get rid of the Graphics context to save resources

		return src;
	}
}


import java.awt.AWTException;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.helper.opencv_core.CvArr;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvOr;
import static org.bytedeco.javacpp.opencv_core.cvReleaseImage;
import static org.bytedeco.javacpp.opencv_core.cvReleaseMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_highgui.CV_CAP_ANY;
import static org.bytedeco.javacpp.opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT;
import static org.bytedeco.javacpp.opencv_highgui.CV_CAP_PROP_FRAME_WIDTH;
import org.bytedeco.javacpp.opencv_highgui.CvCapture;
import static org.bytedeco.javacpp.opencv_highgui.cvCreateCameraCapture;
import static org.bytedeco.javacpp.opencv_highgui.cvQueryFrame;
import static org.bytedeco.javacpp.opencv_highgui.cvReleaseCapture;
import static org.bytedeco.javacpp.opencv_highgui.cvSetCaptureProperty;
import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_highgui.cvWaitKey;
import org.bytedeco.javacpp.opencv_imgproc.CvMoments;

public class VirtualMouse {

	public static void main(String[] args) throws AWTException {

		IplImage img1, imgbinG, imgbinB;
		IplImage imghsv;

	/*	 CvScalar Gminc = cvScalar(20, 150, 75, 0), Gmaxc = cvScalar(40, 255,
		 255, 0); // Blue
		 CvScalar Bminc = cvScalar(40, 50, 60, 0), Bmaxc = cvScalar(80, 255,
		 255, 0); // Green
*/
		CvScalar Bminc = cvScalar(95, 150, 75, 0), Bmaxc = cvScalar(145, 255,
			255, 0); // Blue
		 CvScalar Gminc = cvScalar(40, 150, 75, 0), Gmaxc = cvScalar(80, 255,
		 255, 0); // Green
	//	CvScalar Gminc = cvScalar(168, 175, 0, 0);// RED wide dabur birko
	//	CvScalar Gmaxc = cvScalar(256, 256, 256, 0);
		// img1 = cvLoadImage("Pic.jpg");
		CvArr mask;
		int w = 640, h = 480;
		imghsv = cvCreateImage(cvSize(w, h), 8, 3);
		imgbinG = cvCreateImage(cvSize(w, h), 8, 1);
		IplImage imgC = cvCreateImage(cvSize(w, h), 8, 1);
		CvSeq contour1 = new CvSeq(), contour2 = null;
		CvMemStorage storage = CvMemStorage.create();
		CvMoments moments = new CvMoments(Loader.sizeof(CvMoments.class));
		imgbinB = cvCreateImage(cvSize(w, h), 8, 1);

		CvCapture capture1 = cvCreateCameraCapture(CV_CAP_ANY);
		cvSetCaptureProperty(capture1, CV_CAP_PROP_FRAME_WIDTH, w);
		cvSetCaptureProperty(capture1, CV_CAP_PROP_FRAME_HEIGHT, h);

		// int i=1;
		while (true) {

			img1 = cvQueryFrame(capture1);
			if (img1 == null) {
				System.err.println("No Image");
				break;
			}

			imgbinB = CcmFilter.Filter(img1, imghsv, imgbinB, Bmaxc, Bminc,
					contour1, contour2, storage, moments, 1, 0);
			imgbinG = CcmFilter.Filter(img1, imghsv, imgbinG, Gmaxc, Gminc,
					contour1, contour2, storage, moments, 0, 1);
			cvShowImage("hi", imgbinB);
			cvShowImage("hi2", imgbinG);

			cvOr(imgbinB, imgbinG, imgC, mask = null);
			// cvFlip(img1, img1, 1);
			// cvFlip(imgC, imgC, 1);
			cvShowImage("Combined", imgC);
			cvShowImage("Camera", img1);
			char c = (char) cvWaitKey(15);
			if (c == 'q') {
				break;
			}

		}
		cvReleaseImage(imghsv);
		cvReleaseImage(imgbinG);
		cvReleaseImage(imgbinB);
		cvReleaseImage(imghsv);
		cvReleaseMemStorage(storage);
		cvReleaseCapture(capture1);

	}
}

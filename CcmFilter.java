package bbnm;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import org.bytedeco.javacpp.Loader;
import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.CV_FILLED;
import static org.bytedeco.javacpp.opencv_core.CV_WHOLE_SEQ;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvDrawContours;
import static org.bytedeco.javacpp.opencv_core.cvInRangeS;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.CV_LINK_RUNS;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_LIST;
import org.bytedeco.javacpp.opencv_imgproc.CvMoments;
import static org.bytedeco.javacpp.opencv_imgproc.cvContourArea;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetCentralMoment;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetSpatialMoment;
import static org.bytedeco.javacpp.opencv_imgproc.cvMoments;
public class CcmFilter {

	public static int t;

	public static opencv_core.IplImage Filter(opencv_core.IplImage img,
			opencv_core.IplImage imghsv, opencv_core.IplImage imgBin,
			opencv_core.CvScalar maxc, opencv_core.CvScalar minc,
			opencv_core.CvSeq contour1, opencv_core.CvSeq contour2,
			CvMemStorage storage, CvMoments moments, int blue, int green)
			throws AWTException {

		double moment10, moment01, areaMax, areaC = 0, m_area;
		int posX = 0, posY = 0;
		Robot rbt = new Robot();

		cvCvtColor(img, imghsv, CV_BGR2HSV);
		cvInRangeS(imghsv, minc, maxc, imgBin);

		areaMax = 100;
/*
		cvFindContours(imgBin, storage, contour1,
				Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_LINK_RUNS,
				cvPoint(0, 0));

		contour2 = contour1;

		while (contour1 != null && !contour1.isNull()) {
			areaC = cvContourArea(contour1, CV_WHOLE_SEQ, 1);

			if (areaC > areaMax) {
				areaMax = areaC;
			}

			contour1 = contour1.h_next();

		}

		while (contour2 != null && !contour2.isNull()) {
			areaC = cvContourArea(contour2, CV_WHOLE_SEQ, 1);

			if (areaC < areaMax) {
				cvDrawContours(imgBin, contour2, CV_RGB(0, 0, 0),
						CV_RGB(0, 0, 0), 0, CV_FILLED, 8, cvPoint(0, 0));
			}

			contour2 = contour2.h_next();
		}
*/
		cvMoments(imgBin, moments, 1);

		moment10 = cvGetSpatialMoment(moments, 1, 0);
		moment01 = cvGetSpatialMoment(moments, 0, 1);
		m_area = cvGetCentralMoment(moments, 0, 0);

		posX = (int) (moment10 / m_area);
		posY = (int) (moment01 / m_area);

		if (blue == 1) {
			if (posX > 0 && posY > 0) {

				rbt.mouseMove((1366 / 640) * posX, (768 / 480) * posY); // Display
																		// Resolution
																		// =
																		// 1280
				// x 720 convert to windows
				// 320 x 240 = 1280/320 = 4
				// and 720/240 = 3

			}
		}

		if (green == 1) {
			if (posX > 0 && posY > 0) {
				rbt.mousePress(InputEvent.BUTTON1_MASK);
				t++;
			} else if (t > 0) {
				rbt.mouseRelease(InputEvent.BUTTON1_MASK);
				t = 0;
			}

		}

		return imgBin;

	}

}

/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.embroidery;

import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.ScreenModes;
import org.catrobat.catroid.content.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

public class DSTFileGenerator {
	private static final String TAG = DSTFileGenerator.class.getSimpleName();
	private static final int MAXDISTANCE = 121;
	private static final int HEADERMAXBYTE = 512;
	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	private char byte0;
	private char byte1;
	private char byte2;
	private File file;
	private FileOutputStream fileStream;
	ArrayList<PointF> stitchPoints = null;

	public DSTFileGenerator() {
		file = new File(Environment.getExternalStorageDirectory(), ProjectManager.getInstance().getCurrentProject().getName() + ".dst");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		stitchPoints = new ArrayList<>();
	}

	public void createDSTFile(ArrayList<PointF> stitchPoints) {
		if (stitchPoints.size() > 1) {
			prepareFileWriting(stitchPoints);
			writeDSTFile();
			reset();
		}
	}

	private void prepareFileWriting(ArrayList<PointF> stitchPoints) {
		createStitchPointsUnitDotOneMm(stitchPoints);
		validateStitches();
		calculateBoundingBox();
	}

	private void writeDSTFile() {
		try {
			writeHeader();
			writeStitches();
			writeEndFile();
		} catch (IllegalArgumentException ex) {
			Log.e(TAG, "Remaining stitch distance should be zero!");
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	private void createStitchPointsUnitDotOneMm(ArrayList<PointF> stitchPoints) { // TODO: Change name, unit is not mm it  is 0.1mm
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		Project project = ProjectManager.getInstance().getCurrentProject();
		Float inchMmFactor = 25.4f;
		Float dotOneMmFactor = 10.0f;
		Float aspectRatioMultiplierX = (float) metrics.widthPixels / project.getXmlHeader().getVirtualScreenWidth();
		Float aspectRatioMultiplierY = (float) metrics.heightPixels / project.getXmlHeader().getVirtualScreenHeight();

		if (project.getScreenMode() == ScreenModes.MAXIMIZE) {
			if (aspectRatioMultiplierX > aspectRatioMultiplierY) {
				aspectRatioMultiplierX = aspectRatioMultiplierY;
			}
			else {
				aspectRatioMultiplierY = aspectRatioMultiplierX;
			}
		}

		for (PointF stitch: stitchPoints) {
			PointF stitchUnitMm = new PointF();
			stitchUnitMm.x = stitch.x * inchMmFactor * dotOneMmFactor * aspectRatioMultiplierX / metrics.densityDpi;
			stitchUnitMm.y = stitch.y * inchMmFactor * dotOneMmFactor * aspectRatioMultiplierY / metrics.densityDpi;
			this.stitchPoints.add(stitchUnitMm);
		}
	}

	private void validateStitches() {
		int xDistance;
		int yDistance;

		for (int index = 0; index < stitchPoints.size() - 1; index++) {
			PointF currentPoint = stitchPoints.get(index);
			PointF nextPoint = stitchPoints.get(index + 1);

			xDistance = (int) (nextPoint.x - currentPoint.x);
			yDistance = (int) (nextPoint.y - currentPoint.y);

			if (xDistance < -MAXDISTANCE || xDistance > MAXDISTANCE || yDistance < -MAXDISTANCE || yDistance > MAXDISTANCE) {
				splitStitchAtIndex(index);
			}
		}
	}

	private void splitStitchAtIndex(int index) {
		PointF lastValidPoint = stitchPoints.get(index);
		PointF nextPoint = stitchPoints.get(index + 1);

		float xDistance = nextPoint.x - lastValidPoint.x;
		float yDistance = nextPoint.y - lastValidPoint.y;
		float maxDistance = Math.max(Math.abs(xDistance), Math.abs(yDistance));

		int splitCount = (int) Math.ceil(maxDistance / MAXDISTANCE);

		for (int count = 1; count < splitCount; count++) {
			float splitFactor = (float) count / splitCount;

			PointF newStitch = new PointF();
			newStitch.x = interpolate(nextPoint.x, lastValidPoint.x, splitFactor);
			newStitch.y = interpolate(nextPoint.y, lastValidPoint.y, splitFactor);
			this.stitchPoints.add(index + 1, newStitch);
		}
	}

	private float interpolate(float endValue, float startValue, float percentage) {
		return endValue + percentage * (startValue - endValue);
	}

	private void calculateBoundingBox() {
		boolean init = true;

		for (PointF point: stitchPoints) {
			if (init) {
				initBoundingBox(point);
				init = false;
			}

			if (minX > point.x) {
				minX = point.x;
			}
			if (maxX < point.x) {
				maxX = point.x;
			}
			if (minY > point.y) {
				minY = point.y;
			}
			if (maxY < point.y) {
				maxY = point.y;
			}
		}
	}

	private void initBoundingBox(PointF point) {
		minX = point.x;
		maxX = point.x;
		minY = point.y;
		maxY = point.y;
	}

	private void writeHeader() throws IOException{
		String header = createHeaderString();

		String headerFill = "";
		for (int index = header.getBytes().length; index < HEADERMAXBYTE; index++) {
			headerFill += " ";
		}

		fileStream = new FileOutputStream(file);
		fileStream.write(header.getBytes());
		fileStream.write(headerFill.getBytes());
	}

	private String createHeaderString() {
		final char substituteChar = 0x1A;
		final int mx = 0;
		final int my = 0;
		final String pd = "*****";
		int colorChanges = 1;

		PointF firstStitch = stitchPoints.get(0);
		PointF lastStitch = stitchPoints.get(stitchPoints.size() - 1);

		String header;
		header = String.format("LA:%-15s\n" + substituteChar, ProjectManager.getInstance().getCurrentProject().getName());
		header += formatString("ST:%-6d\n" + substituteChar, stitchPoints.size());
		header += formatString("CO:%-2d\n" + substituteChar, colorChanges);
		header += formatString("+X:%-4d\n" + substituteChar, (int) maxX);
		header += formatString("-X:%-4d\n" + substituteChar, (int) minX);
		header += formatString("+Y:%-4d\n" + substituteChar, (int) maxY);
		header += formatString("-Y:%-4d\n" + substituteChar, (int) minY);
		header += formatString("AX:%-5d\n" + substituteChar, (int) (lastStitch.x - firstStitch.x));
		header += formatString("AY:%-5d\n" + substituteChar, (int) (lastStitch.y - firstStitch.y));
		header += formatString("MX:%-5d\n" + substituteChar, mx);
		header += formatString("MY:%-5d\n" + substituteChar, my);
		header += String.format("PD:%-5s\n" + substituteChar, pd).replace(' ', '\0');

		return header;
	}

	private String formatString(String format, int value) {
		return String.format(format, value).replace(' ', '\0');
	}

	private void writeStitches() throws IllegalArgumentException, IOException{
		int xChange;
		int yChange;

		ListIterator<PointF> iterator = stitchPoints.listIterator();
		PointF currentPoint = stitchPoints.get(0);
		while (iterator.hasNext()) {
			byte0 = 0;
			byte1 = 0;
			byte2 = 0x03;

			PointF nextPoint = iterator.next();
			xChange = (int) (nextPoint.x - currentPoint.x);
			yChange = (int) (nextPoint.y - currentPoint.y);

			// CHECKSTYLE DISABLE OneStatementPerLineCheck FOR 30 LINES
			// CHECKSTYLE DISABLE LeftCurlyCheck FOR 29 LINES
			if (xChange >= +41) { byte2 += setBit(2); xChange -= 81; }
			if (xChange <= -41) { byte2 += setBit(3); xChange += 81; }
			if (xChange >= +14) { byte1 += setBit(2); xChange -= 27; }
			if (xChange <= -14) { byte1 += setBit(3); xChange += 27; }
			if (xChange >= +5) { byte0 += setBit(2); xChange -= 9; }
			if (xChange <= -5) { byte0 += setBit(3); xChange += 9; }
			if (xChange >= +2) { byte1 += setBit(0); xChange -= 3; }
			if (xChange <= -2) { byte1 += setBit(1); xChange += 3; }
			if (xChange >= +1) { byte0 += setBit(0); xChange -= 1; }
			if (xChange <= -1) { byte0 += setBit(1); xChange += 1; }
			if (xChange != 0) {
				throw new IllegalArgumentException("Remaining stitch distance should be zero");
			}

			if (yChange >= +41) { byte2 += setBit(5); yChange -= 81; }
			if (yChange <= -41) { byte2 += setBit(4); yChange += 81; }
			if (yChange >= +14) { byte1 += setBit(5); yChange -= 27; }
			if (yChange <= -14) { byte1 += setBit(4); yChange += 27; }
			if (yChange >= +5) { byte0 += setBit(5); yChange -= 9; }
			if (yChange <= -5) { byte0 += setBit(4); yChange += 9; }
			if (yChange >= +2) { byte1 += setBit(7); yChange -= 3; }
			if (yChange <= -2) { byte1 += setBit(6); yChange += 3; }
			if (yChange >= +1) { byte0 += setBit(7); yChange -= 1; }
			if (yChange <= -1) { byte0 += setBit(6); yChange += 1; }
			if (yChange != 0) {
				throw new IllegalArgumentException("Remaining stitch distance should be zero");
			}

			writeBytes();

			currentPoint = nextPoint;
		}
	}

	private void writeEndFile() throws IOException{
		byte0 = 0;
		byte1 = 0;
		byte2 = 0xF3;

		writeBytes();
		fileStream.flush();
		fileStream.close();
	}

	private void writeBytes() throws IOException{
		fileStream.write(byte0);
		fileStream.write(byte1);
		fileStream.write(byte2);
	}

	private char setBit(int bit) {
		return (char) (1 << bit);
	}

	private void reset() {
		stitchPoints = new ArrayList<>();
		this.fileStream = null;
	}
}

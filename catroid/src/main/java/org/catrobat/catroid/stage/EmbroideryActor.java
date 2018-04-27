/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
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

package org.catrobat.catroid.stage;

import android.graphics.PointF;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.XmlHeader;

public class EmbroideryActor extends Actor {
	private FrameBuffer buffer;

	public EmbroideryActor() {
		XmlHeader header = ProjectManager.getInstance().getCurrentProject().getXmlHeader();
		buffer = new FrameBuffer(Pixmap.Format.RGBA8888, header.virtualScreenWidth, header.virtualScreenHeight, false);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		buffer.begin();
		for (Sprite sprite : ProjectManager.getInstance().getSceneToPlay().getSpriteList()) {
			drawStitchPoints(sprite);
		}
		buffer.end();

		batch.end();
		TextureRegion region = new TextureRegion(buffer.getColorBufferTexture());
		region.flip(false, true);
		Image image = new Image(region);
		image.setPosition(-buffer.getWidth() / 2, -buffer.getHeight() / 2);
		batch.begin();
		image.draw(batch, parentAlpha);
	}

	private void drawStitchPoints(Sprite sprite) {
		float x = sprite.look.getXInUserInterfaceDimensionUnit();
		float y = sprite.look.getYInUserInterfaceDimensionUnit();
		Sprite.EmbroideryConfiguration embroidery = sprite.embroideryConfiguration;

		if (embroidery.stitch) {
			if (embroidery.previousPoint == null) {
				embroidery.previousPoint = new PointF(x, y);
				return;
			}

			ShapeRenderer renderer = StageActivity.stageListener.shapeRenderer;
			renderer.setColor(Color.BLACK);
			renderer.begin(ShapeRenderer.ShapeType.Filled);

			if (embroidery.stitch && (embroidery.previousPoint.x != sprite.look.getX() || embroidery.previousPoint.y != sprite.look.getY())) {
				renderer.circle(embroidery.previousPoint.x, embroidery.previousPoint.y, 5.25f);
				renderer.rectLine(embroidery.previousPoint.x, embroidery.previousPoint.y, x, y, 3.5f);
				renderer.circle(x, y, 5.25f);
			}

			renderer.end();
			embroidery.previousPoint.x = x;
			embroidery.previousPoint.y = y;
			embroidery.stitch = false;
		}
	}

	public void reset() {
		XmlHeader header = ProjectManager.getInstance().getCurrentProject().getXmlHeader();
		buffer.dispose();
		buffer = new FrameBuffer(Pixmap.Format.RGBA8888, header.virtualScreenWidth, header.virtualScreenHeight, false);
	}

	public void dispose() {
		if (buffer != null) {
			buffer.dispose();
			buffer = null;
		}
	}
}

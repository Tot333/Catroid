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

package org.catrobat.catroid.uiespresso.content.brick.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.AskBrick;
import org.catrobat.catroid.content.bricks.CameraBrick;
import org.catrobat.catroid.content.bricks.ChangeBrightnessByNBrick;
import org.catrobat.catroid.content.bricks.ChangeColorByNBrick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeTransparencyByNBrick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.ChangeYByNBrick;
import org.catrobat.catroid.content.bricks.ChooseCameraBrick;
import org.catrobat.catroid.content.bricks.ClearBackgroundBrick;
import org.catrobat.catroid.content.bricks.ClearGraphicEffectBrick;
import org.catrobat.catroid.content.bricks.FlashBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.GoToBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.MoveNStepsBrick;
import org.catrobat.catroid.content.bricks.NextLookBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick;
import org.catrobat.catroid.content.bricks.PointToBrick;
import org.catrobat.catroid.content.bricks.PreviousLookBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundAndWaitBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetColorBrick;
import org.catrobat.catroid.content.bricks.SetRotationStyleBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.SetTransparencyBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.content.bricks.ShowBrick;
import org.catrobat.catroid.content.bricks.StitchBrick;
import org.catrobat.catroid.content.bricks.TurnLeftBrick;
import org.catrobat.catroid.content.bricks.TurnRightBrick;
import org.catrobat.catroid.content.bricks.VibrationBrick;
import org.catrobat.catroid.content.bricks.WhenBackgroundChangesBrick;
import org.catrobat.catroid.physics.content.bricks.SetBounceBrick;
import org.catrobat.catroid.physics.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.physics.content.bricks.SetGravityBrick;
import org.catrobat.catroid.physics.content.bricks.SetMassBrick;
import org.catrobat.catroid.physics.content.bricks.SetPhysicsObjectTypeBrick;
import org.catrobat.catroid.physics.content.bricks.SetVelocityBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.uiespresso.testsuites.Cat;
import org.catrobat.catroid.uiespresso.testsuites.Level;
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
import org.catrobat.catroid.uiespresso.util.matchers.BrickCategoryListMatchers;
import org.catrobat.catroid.uiespresso.util.matchers.BrickPrototypeListMatchers;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_SHOW_EMBROIDERY_BRICKS;
import static org.catrobat.catroid.uiespresso.util.UiTestUtils.getResourcesString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class BrickValueBackgroundParameterTest {

	private String nameSpriteOne = "testSpriteOne";

	@Rule
	public BaseActivityInstrumentationRule<SpriteActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(SpriteActivity.class, SpriteActivity.EXTRA_FRAGMENT_POSITION, SpriteActivity.FRAGMENT_SCRIPTS);

	private List<String> allPeripheralCategories = new ArrayList<>(Arrays.asList(SETTINGS_SHOW_EMBROIDERY_BRICKS));
	private List<String> enabledByThisTestPeripheralCategories = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
		createProject("brickDefaultValueParameterTest ");
		baseActivityTestRule.launchActivity();

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());

		for (String category : allPeripheralCategories) {
			boolean categoryEnabled = sharedPreferences.getBoolean(category, false);
			if (!categoryEnabled) {
				sharedPreferences.edit().putBoolean(category, true).commit();
				enabledByThisTestPeripheralCategories.add(category);
			}
		}
	}

	@After
	public void tearDown() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());
		for (String category : enabledByThisTestPeripheralCategories) {
			sharedPreferences.edit().putBoolean(category, false).commit();
		}
		enabledByThisTestPeripheralCategories.clear();
	}

	@Category({Cat.AppUi.class, Level.Smoke.class})
	@Test
	public void testMotionBricksDefaultValues() {
		openCategory(R.string.category_motion);

		//Place at
		checkIfBrickShowsText(PlaceAtBrick.class, R.string.brick_place_at);
		checkIfBrickShowsText(PlaceAtBrick.class, R.string.x_label);
		checkIfBrickShowsText(PlaceAtBrick.class, R.string.y_label);
		checkIfBrickShowsEditTextWithText(PlaceAtBrick.class, R.id.brick_place_at_edit_text_x, "100 ");
		checkIfBrickShowsEditTextWithText(PlaceAtBrick.class, R.id.brick_place_at_edit_text_y, "200 ");

		//Set X to
		checkIfBrickShowsText(SetXBrick.class, R.string.brick_set_x);
		checkIfBrickShowsText(SetXBrick.class, "100 ");

		//Set Y to
		checkIfBrickShowsText(SetYBrick.class, R.string.brick_set_y);
		checkIfBrickShowsText(SetYBrick.class, "200 ");

		//Change X by
		checkIfBrickShowsText(ChangeXByNBrick.class, R.string.brick_change_x_by);
		checkIfBrickShowsText(ChangeXByNBrick.class, "10 ");

		//Change Y by
		checkIfBrickShowsText(ChangeYByNBrick.class, R.string.brick_change_y_by);
		checkIfBrickShowsText(ChangeYByNBrick.class, "10 ");

		//Go to
		checkIfBrickShowsText(GoToBrick.class, R.string.brick_go_to);
		checkIfBrickShowsSpinnerWithEditTextOverlayWithText(GoToBrick.class, R.id.brick_go_to_spinner, R.string.brick_go_to_touch_position);

		//Move  steps
		checkIfBrickShowsText(MoveNStepsBrick.class, R.string.brick_move);
		checkIfBrickShowsText(MoveNStepsBrick.class, "10 ");

		//Turn left degrees
		checkIfBrickShowsText(TurnLeftBrick.class, R.string.brick_turn_left);
		checkIfBrickShowsText(TurnLeftBrick.class, R.string.degrees);
		checkIfBrickShowsText(TurnLeftBrick.class, "15 ");

		//Turn right degrees
		checkIfBrickShowsText(TurnRightBrick.class, R.string.brick_turn_right);
		checkIfBrickShowsText(TurnRightBrick.class, R.string.degrees);
		checkIfBrickShowsText(TurnRightBrick.class, "15 ");

		//Point in direction degrees
		checkIfBrickShowsText(PointInDirectionBrick.class, R.string.brick_point_in_direction);
		checkIfBrickShowsText(PointInDirectionBrick.class, R.string.degrees);
		checkIfBrickShowsText(PointInDirectionBrick.class, "90 ");

		//Point towards
		checkIfBrickShowsText(PointToBrick.class, R.string.brick_point_to);
		checkIfBrickShowsSpinnerWithText(PointToBrick.class,
				R.id.brick_point_to_spinner,
				nameSpriteOne);

		//Set rotation style
		checkIfBrickShowsText(SetRotationStyleBrick.class, R.string.brick_set_rotation_style);
		checkIfBrickShowsSpinnerWithEditTextOverlayWithText(SetRotationStyleBrick.class,
				R.id.brick_set_rotation_style_spinner,
				R.string.brick_set_rotation_style_lr);

		//Glide second
		checkIfBrickShowsText(GlideToBrick.class, R.string.brick_glide);
		checkIfBrickShowsText(GlideToBrick.class, R.string.brick_glide_to_x);
		checkIfBrickShowsText(GlideToBrick.class, R.string.y_label);
		checkIfBrickShowsText(GlideToBrick.class, "1 ");
		checkIfBrickShowsText(GlideToBrick.class, "100 ");
		checkIfBrickShowsText(GlideToBrick.class, "200 ");

		//Vibrate for second
		checkIfBrickShowsText(VibrationBrick.class, R.string.brick_vibration);
		checkIfBrickShowsText(VibrationBrick.class, "1 ");

		//Set motion type to
		checkIfBrickShowsText(SetPhysicsObjectTypeBrick.class, R.string.brick_set_physics_object_type);
		checkIfBrickShowsSpinnerWithText(SetPhysicsObjectTypeBrick.class,
				R.id.brick_set_physics_object_type_spinner,
				R.string.brick_set_physics_object_type_dynamic);

		//Set velocity
		checkIfBrickShowsText(SetVelocityBrick.class, R.string.brick_set_velocity_to);
		checkIfBrickShowsText(SetVelocityBrick.class, R.string.x_label);
		checkIfBrickShowsText(SetVelocityBrick.class, R.string.y_label);
		checkIfBrickShowsText(SetVelocityBrick.class, R.string.brick_set_velocity_unit);

		//Rotate left
		checkIfBrickShowsText(TurnLeftBrick.class, R.string.brick_turn_left);
		checkIfBrickShowsText(TurnLeftBrick.class, R.string.degrees);
		checkIfBrickShowsText(TurnLeftBrick.class, "15 ");

		//Rotate right
		checkIfBrickShowsText(TurnRightBrick.class, R.string.brick_turn_right);
		checkIfBrickShowsText(TurnRightBrick.class, R.string.degrees);
		checkIfBrickShowsText(TurnRightBrick.class, "15 ");

		//Set gravity
		checkIfBrickShowsText(SetGravityBrick.class, R.string.brick_set_gravity_to);
		checkIfBrickShowsText(SetGravityBrick.class, R.string.x_label);
		checkIfBrickShowsText(SetGravityBrick.class, R.string.y_label);
		checkIfBrickShowsText(SetGravityBrick.class, R.string.brick_set_gravity_unit);
		checkIfBrickShowsText(SetGravityBrick.class, "0 ");
		checkIfBrickShowsText(SetGravityBrick.class, "- 10 ");

		//Set mass
		checkIfBrickShowsText(SetMassBrick.class, R.string.brick_set_mass);
		checkIfBrickShowsText(SetMassBrick.class, "1 ");
		checkIfBrickShowsText(SetMassBrick.class, R.string.brick_set_mass_unit);

		//Set bounce
		checkIfBrickShowsText(SetBounceBrick.class, R.string.brick_set_bounce_factor);
		checkIfBrickShowsText(SetBounceBrick.class, "80 ");
		checkIfBrickShowsText(SetBounceBrick.class, R.string.percent_symbol);

		//Set friction
		checkIfBrickShowsText(SetFrictionBrick.class, R.string.brick_set_friction);
		checkIfBrickShowsText(SetFrictionBrick.class, "20 ");
		checkIfBrickShowsText(SetFrictionBrick.class, R.string.percent_symbol);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class})
	@Test
	public void testLooksBricksDefaultValues() {
		openCategory(R.string.category_looks);

		checkIfBrickShowsText(NextLookBrick.class, R.string.brick_next_background);

		checkIfBrickShowsText(PreviousLookBrick.class, R.string.brick_previous_background);

		checkIfBrickShowsText(SetSizeToBrick.class, R.string.brick_set_size_to);
		checkIfBrickShowsText(SetSizeToBrick.class, R.string.percent_symbol);
		checkIfBrickShowsText(SetSizeToBrick.class, "60 ");

		checkIfBrickShowsText(ChangeSizeByNBrick.class, R.string.brick_change_size_by);
		checkIfBrickShowsText(ChangeSizeByNBrick.class, "10 ");

		checkIfBrickShowsText(HideBrick.class, R.string.brick_hide);

		checkIfBrickShowsText(ShowBrick.class, R.string.brick_show);

		checkIfBrickShowsText(AskBrick.class, R.string.brick_ask_label);
		checkIfBrickShowsText(AskBrick.class, "'" + getResourcesString(R.string.brick_ask_default_question) + "' ");
		checkIfBrickShowsText(AskBrick.class, R.string.brick_ask_store);
		checkIfBrickShowsSpinnerWithEditTextOverlayWithText(AskBrick.class, R.id.brick_ask_spinner,
				R.string.new_option);

		checkIfBrickShowsText(SetTransparencyBrick.class, R.string.brick_set_transparency);
		checkIfBrickShowsText(SetTransparencyBrick.class, R.string.percent_symbol);
		checkIfBrickShowsText(SetTransparencyBrick.class, "50 ");

		checkIfBrickShowsText(ChangeTransparencyByNBrick.class, R.string.brick_change_ghost_effect);
		checkIfBrickShowsText(ChangeTransparencyByNBrick.class, "25 ");

		checkIfBrickShowsText(SetBrightnessBrick.class, R.string.brick_set_brightness);
		checkIfBrickShowsText(SetBrightnessBrick.class, "50 ");
		checkIfBrickShowsText(SetBrightnessBrick.class, R.string.percent_symbol);

		checkIfBrickShowsText(ChangeBrightnessByNBrick.class, R.string.brick_change_brightness);
		checkIfBrickShowsText(ChangeBrightnessByNBrick.class, "25 ");

		checkIfBrickShowsText(SetColorBrick.class, R.string.brick_set_color);
		checkIfBrickShowsText(SetColorBrick.class, "0 ");

		checkIfBrickShowsText(ChangeColorByNBrick.class, R.string.brick_change_color);
		checkIfBrickShowsText(ChangeColorByNBrick.class, "25 ");

		checkIfBrickShowsText(ClearGraphicEffectBrick.class, R.string.brick_clear_graphic_effect);

		checkIfBrickShowsText(WhenBackgroundChangesBrick.class, R.string.brick_when_background);
		checkIfBrickShowsSpinnerWithEditTextOverlayWithText(WhenBackgroundChangesBrick.class,
				R.id.brick_when_background_spinner,
				R.string.new_option);

		checkIfBrickAtPositionShowsText(SetBackgroundBrick.class, 0, R.string.brick_set_background);
		checkIfBrickAtPositionShowsSpinnerWithEditTextOverlayWithText(SetBackgroundBrick.class, 0,
				R.id.brick_set_look_spinner,
				R.string.new_option);

		checkIfBrickAtPositionShowsText(SetBackgroundAndWaitBrick.class, 0, R.string.brick_set_background_and_wait);
		checkIfBrickAtPositionShowsSpinnerWithEditTextOverlayWithText(SetBackgroundAndWaitBrick.class, 0,
				R.id.brick_set_look_spinner,
				R.string.new_option);

		checkIfBrickShowsText(CameraBrick.class, R.string.brick_video);
		checkIfBrickShowsSpinnerWithEditTextOverlayWithText(CameraBrick.class,
				R.id.brick_video_spinner,
				R.string.video_brick_camera_on);

		checkIfBrickShowsText(ChooseCameraBrick.class, R.string.brick_choose_camera);
		checkIfBrickShowsSpinnerWithText(ChooseCameraBrick.class,
				R.id.brick_choose_camera_spinner,
				R.string.choose_camera_front);

		checkIfBrickShowsText(FlashBrick.class, R.string.brick_flash);
		checkIfBrickShowsSpinnerWithText(FlashBrick.class,
				R.id.brick_flash_spinner,
				R.string.brick_flash_on);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class})
	@Test
	public void testPenBricksDefaultValues() {
		openCategory(R.string.category_pen);

		checkIfBrickShowsText(ClearBackgroundBrick.class, R.string.brick_clear_background);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.Gadgets.class})
	@Test
	public void testEmbroideryBricksDefaultValues() {
		openCategory(R.string.category_embroidery);

		checkIfBrickShowsText(StitchBrick.class, R.string.brick_stitch);
	}

	private void openCategory(int categoryNameStringResourceId) {
		onView(withId(R.id.button_add))
				.perform(click());

		onData(allOf(is(instanceOf(String.class)), is(UiTestUtils.getResourcesString(categoryNameStringResourceId))))
				.inAdapterView(BrickCategoryListMatchers.isBrickCategoryView())
				.perform(click());
	}

	private void checkIfBrickShowsText(Class brickClass, String text) {
		onData(instanceOf(brickClass)).inAdapterView(BrickPrototypeListMatchers.isBrickPrototypeView())
				.onChildView(withText(text))
				.check(matches(isDisplayed()));
	}

	private void checkIfBrickShowsText(Class brickClass, int stringResourceId) {
		checkIfBrickShowsText(brickClass, UiTestUtils.getResourcesString(stringResourceId));
	}

	private void checkIfBrickAtPositionShowsText(Class brickClass, int position, int stringResourceId) {
		onData(instanceOf(brickClass)).inAdapterView(BrickPrototypeListMatchers.isBrickPrototypeView())
				.atPosition(position)
				.onChildView(withText(stringResourceId))
				.check(matches(isDisplayed()));
	}

	private void checkIfBrickShowsEditTextWithText(Class brickClass, int editTextResourceId, String text) {
		onData(instanceOf(brickClass)).inAdapterView(BrickPrototypeListMatchers.isBrickPrototypeView())
				.onChildView(withId(editTextResourceId))
				.check(matches(withText(text)));
	}

	private void checkIfBrickShowsSpinnerWithText(Class brickClass, int spinnerResourceId, int stringResourceId) {
		checkIfBrickShowsSpinnerWithText(brickClass, spinnerResourceId,
				UiTestUtils.getResourcesString(stringResourceId));
	}

	private void checkIfBrickShowsSpinnerWithText(Class brickClass, int spinnerResourceId, String text) {
		onData(instanceOf(brickClass)).inAdapterView(BrickPrototypeListMatchers.isBrickPrototypeView())
				.onChildView(withId(spinnerResourceId))
				.check(matches(withSpinnerText(text)));
	}

	//Spinners in Android sometimes contain a text directly (if they are generic ArrayAdapters of type <String>) and
	//sometimes the values are contained in an TextView within the Spinner. In the first case the methods above work,
	//and in the second the ones below.
	private void checkIfBrickShowsSpinnerWithEditTextOverlayWithText(Class brickClass, int spinnerResourceId,
			int stringResourceId) {
		checkIfBrickShowsSpinnerWithEditTextOverlayWithText(brickClass, spinnerResourceId,
				getResourcesString(stringResourceId));
	}

	private void checkIfBrickShowsSpinnerWithEditTextOverlayWithText(Class brickClass, int spinnerResourceId,
			String text) {
		onData(instanceOf(brickClass)).inAdapterView(BrickPrototypeListMatchers.isBrickPrototypeView())
				.onChildView(withId(spinnerResourceId))
				.onChildView(withId(android.R.id.text1)) //could be omitted, but just to make clear whats going on
				.check(matches(withText(text)));
	}

	private void checkIfBrickAtPositionShowsSpinnerWithEditTextOverlayWithText(Class brickClass, int position,
			int spinnerResourceId, int stringResourceId) {
		onData(instanceOf(brickClass)).inAdapterView(BrickPrototypeListMatchers.isBrickPrototypeView())
				.atPosition(position)
				.onChildView(withId(spinnerResourceId))
				.onChildView(withId(android.R.id.text1)) //could be omitted, but just to make clear whats going on
				.check(matches(withText(stringResourceId)));
	}

	private void createProject(String projectName) {
		Project project = new Project(InstrumentationRegistry.getTargetContext(), projectName);
		Sprite spriteOne = new Sprite(nameSpriteOne);
		project.getDefaultScene().addSprite(spriteOne);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(project.getDefaultScene().getBackgroundSprite());
	}
}

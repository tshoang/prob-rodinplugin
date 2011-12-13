/** 
 * (c) 2009 Lehrstuhl fuer Softwaretechnik und Programmiersprachen, 
 * Heinrich Heine Universitaet Duesseldorf
 * This software is licenced under EPL 1.0 (http://www.eclipse.org/org/documents/epl-v10.html) 
 * */

package de.bmotionstudio.gef.editor.observer;

import java.util.ArrayList;
import java.util.List;

import de.bmotionstudio.gef.editor.AttributeConstants;
import de.bmotionstudio.gef.editor.animation.AnimationMove;
import de.bmotionstudio.gef.editor.internal.Animation;
import de.bmotionstudio.gef.editor.model.BControl;
import de.bmotionstudio.gef.editor.observer.wizard.WizardObserverSwitchCoordinates;

public class SwitchCoordinates extends Observer {

	private List<ToggleObjectCoordinates> toggleObjects;

	// private transient AnimationListener animationListener;

	// private transient Boolean checked;

	public SwitchCoordinates() {
		toggleObjects = new ArrayList<ToggleObjectCoordinates>();
	}

	public void check(final Animation animation, final BControl control) {

		boolean set = false;

		// if (checked == null)
		// checked = true;

		// if (animationListener == null) {
		// animationListener = new AnimationListener() {
		// public void animationStopped(AnimationEvent evt) {
		// setCallBack(true);
		// checked = true;
		// // System.out
		// // .println("Animation stopped ---> Set callback to TRUE!");
		// }
		//
		// public void animationStarted(AnimationEvent evt) {
		// setCallBack(false);
		// checked = false;
		// // System.out
		// // .println("Animation started ---> Set callback to FALSE!");
		// }
		// };
		// }

		// Collect evaluate predicate objects in list
		for (ToggleObjectCoordinates obj : toggleObjects) {

			obj.setHasError(false);

			// First evaluate predicate (predicate field)
			String bolValue = "true";
			if (obj.getEval().length() > 0) {
				bolValue = parsePredicate(obj.getEval(), control, animation,
						obj);
			}

			if (!obj.hasError() && Boolean.valueOf(bolValue)) {

				int parsedX = 0;
				int parsedY = 0;
				// Handle X field
				try {
					parsedX = Integer.valueOf(parseExpression(obj.getX(),
							false, control, animation, obj, false));
				} catch (NumberFormatException n) {
					obj.setHasError(true);
					addError(control, animation, "x is not a valid integer: "
							+ n.getMessage());
				}
				// Handle Y field
				try {
					parsedY = Integer.valueOf(parseExpression(obj.getY(),
							false, control, animation, obj, false));
				} catch (NumberFormatException n) {
					obj.setHasError(true);
					addError(control, animation, "y is not a valid integer: "
							+ n.getMessage());
				}

				int currentX = Integer.valueOf(control.getAttributeValue(
						AttributeConstants.ATTRIBUTE_X).toString());
				int currentY = Integer.valueOf(control.getAttributeValue(
						AttributeConstants.ATTRIBUTE_Y).toString());

				if (currentX != parsedX || currentY != parsedY) {

					// setCallBack(false);

					// If true
					if (obj.getAnimate()) {

						// if (!checked)
						// return;

						AnimationMove aMove = new AnimationMove(5000, true,
								control, parsedX, parsedY);
						// aMove.addAnimationListener(animationListener);
						aMove.start();

					} else {
						control.setAttributeValue(
								AttributeConstants.ATTRIBUTE_X, parsedX);
						control.setAttributeValue(
								AttributeConstants.ATTRIBUTE_Y, parsedY);
						// setCallBack(true);
						// checked = false;
					}

				}
				// else {
				// setCallBack(true);
				// }

				set = true;

			}

		}

		if (!set) {
			control.restoreDefaultValue(AttributeConstants.ATTRIBUTE_X);
			control.restoreDefaultValue(AttributeConstants.ATTRIBUTE_Y);
		}

	}

	public ObserverWizard getWizard(final BControl bcontrol) {
		return new WizardObserverSwitchCoordinates(bcontrol, this);
	}

	public List<ToggleObjectCoordinates> getToggleObjects() {
		return this.toggleObjects;
	}

	public void setToggleObjects(final List<ToggleObjectCoordinates> list) {
		this.toggleObjects = list;
	}

	public Observer clone() throws CloneNotSupportedException {
		SwitchCoordinates clonedObserver = (SwitchCoordinates) super.clone();
		List<ToggleObjectCoordinates> list = new ArrayList<ToggleObjectCoordinates>();
		for (ToggleObjectCoordinates obj : getToggleObjects()) {
			list.add(obj.clone());
		}
		clonedObserver.setToggleObjects(list);
		return clonedObserver;
	}

}
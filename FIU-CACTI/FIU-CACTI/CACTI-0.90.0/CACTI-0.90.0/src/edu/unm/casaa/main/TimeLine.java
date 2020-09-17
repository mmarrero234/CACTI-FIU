/*
This source code file is part of the CASAA Treatment Coding System Utility
    Copyright (C) 2009  UNM CASAA
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.unm.casaa.main;

import edu.unm.casaa.misc.MiscCode;
import edu.unm.casaa.utterance.Utterance;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;

/**
 * Animated timeline of utterances
 */
public class TimeLine extends Group {

	private int pixelsPerSecond = 50; // sort of a framerate for the animation
	private TranslateTransition animation = null; // animation for moving timeline
	private TimeLineMarker selectedMarker = null; // store currently selected marker, if any
	private SessionData.UtteranceList utteranceList = null; //
	private double height = 55.0; // projected height of timeline. forces Group dimensions early
	private double thickness = 2.0; // thickness of line that represents time :)

	/*
	 * Add change property support to TimeLine This allows TimeLine to broadcast a
	 * request for utterance annotation. The controller will listen for these
	 * requests and handle the annotation editor. There are probably better ways to
	 * do this but i chose to avoid the pub/sub model as that will be deprecated in
	 * Java 9
	 * 
	 * annotateMarkerId is the property with change support. When these changes it
	 * is a request for annotation.
	 */
	private String annotateMarkerId = null;

	/* add property change support to timeline */
	final PropertyChangeSupport mPcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		mPcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		mPcs.removePropertyChangeListener(listener);
	}

	public String getAnnotateMarkerId() {
		return annotateMarkerId;
	}

	/**
	 * TimeLine member called by TimeLineMarker when a marker context menu selection
	 * indicates an annotation event.
	 * 
	 * @param annotateMarkerId
	 */
	public void setAnnotateMarker(String annotateMarkerId) {

		/* update property being listened to */
		this.annotateMarkerId = annotateMarkerId;

		/*
		 * We want to fire the event and have it seen as change even if user selects the
		 * same utterance again. Therefore, instead of sending the previous
		 * annotateMarkerId always set the old value to 0 and the listener will always
		 * hear this as a new change event.
		 */
		mPcs.firePropertyChange("annotateMarkerId", 0, annotateMarkerId);
	}

	/**
	 * @param audioDuration   Time span of timeline
	 * @param pixelsPerSecond Temporal resolution of timeline
	 * @param center          Horizontal center is beginning of timeline
	 * @param utteranceList   A reference to the utterance list that will be
	 *                        rendered on the timeline as TimeLineMarkers
	 */
	public TimeLine(Duration audioDuration, int pixelsPerSecond, double center,
			SessionData.UtteranceList utteranceList) {

		this.pixelsPerSecond = pixelsPerSecond;
		this.utteranceList = utteranceList;

		/*
		 * horizontal center is beginning of timeline We position both cursor and start
		 * of timeline in center of screen Manual layout is ok as i don't want cursor to
		 * move when window is resized Manual layout is sort of required because the
		 * Node type is Group
		 */
		// where does timeline begin; center
		this.setTranslateX(center);

		/*
		 * the center line of the timeline
		 */
		double end = audioDuration.toSeconds() * pixelsPerSecond;
		Line h = new Line(0, 0, end, 0);
		h.setStroke(Color.rgb(255, 0, 0, 1.0));
		h.setStrokeWidth(thickness);
		h.setTranslateY((height / 2.0) + (thickness / 2.0));
		this.getChildren().add(h);

		renderUtterances();

		/*
		 * initialize the animation of timeline add 2 seconds to duration of timeline.
		 * We want to timeline to outlast the media playback until we figure out how to
		 * recover timeline after onFinished
		 */
		animation = new TranslateTransition(audioDuration.add(new Duration(2000.0)), this);
		// interpolator needs to be linear like the audio playback
		animation.setInterpolator(Interpolator.LINEAR);

		/*
		 * translation is negative of the entire timeline width Here, subtract 2 seconds
		 * worth of line to compensate for duration extension above
		 */
		double byX = -audioDuration.toSeconds() * pixelsPerSecond - (2 * pixelsPerSecond);
		animation.setByX(byX);

		/*
		 * Define listeners for change to utterance list This links timeline markers to
		 * changes in utterance list
		 */
		ObservableMap<String, Utterance> observableMap = utteranceList.getObservableMap();
		observableMap.addListener(new MapChangeListener() {
			public void onChanged(MapChangeListener.Change change) {
				if (change.wasAdded()) {
					Utterance utr = (Utterance) change.getValueAdded();
					addMarker(utr);

				} else if (change.wasRemoved()) {
					Utterance utr = (Utterance) change.getValueRemoved();
					removeMarker(utr);
				}
			}
		});

	}

	/**
	 * Add or update utterance on timeline and model
	 *
	 * @param newUtterance Utterance to add
	 */
	public void addMarker(Utterance newUtterance) {
		// insert updated marker
		// TimeLineMarker newMarker = new TimeLineMarker(newUtterance.toString(),
		// newUtterance.getMiscCode().name, newUtterance.getStartTime().toSeconds(),
		// newUtterance.getMiscCode().getSpeaker());
		TimeLineMarker newMarker = new TimeLineMarker(newUtterance);
		this.getChildren().add(newMarker);
	}

	/**
	 * Add or update utterance on timeline and model
	 *
	 * @param newUtterance
	 */
	public String add(Utterance newUtterance) throws SQLException {
		/* check for selected timeline marker */
		TimeLineMarker activeMarker = getSelectedMarker();

		/*
		 * if the timeline's active marker is not null we are in edit mode and will
		 * update code and speaker
		 */
		if (activeMarker != null) {
			/* Edit active marker */
			String prevId = activeMarker.getMarkerID();
			double prevPos = activeMarker.posSeconds;

			/* remove marker from timeline and model */
			utteranceList.remove(prevId);

			/* create new id with previous time and new code value */
			String newID = Utils.formatID(Duration.seconds(prevPos), newUtterance.getMiscCode().value);

			/* prevent flipping onto existing marker */
			Node r = this.lookup("#" + newUtterance.getID());
			if (r == null) {
				/*
				 * update model, id and time = PREVIOUS, the other members can be updated
				 */
				newUtterance.setID(newID);
				newUtterance.setStartTime(prevPos);
				utteranceList.add(newUtterance);
			}

		} else {
			/*
			 * check if exact utterance is already in list. This to prevent timeline from
			 * adding duplicates.
			 */
			Node r = this.lookup("#" + newUtterance.getID());
			if (r == null) {
				/*
				 * sync timeline with player time to marker appears lined up. Do this only for
				 * new markers not edited above here
				 */
				this.getAnimation().jumpTo(newUtterance.getStartTime());

				// new utterance in storage
				utteranceList.add(newUtterance);
			}
		}
		return newUtterance.getID();

	}

	/**
	 * Call me when you want to delete an utterance from model
	 */
	public void remove(String markerID) throws SQLException {
		Node r = this.lookup("#" + markerID);
		if (r != null) {
			utteranceList.remove(markerID);
		}
	}

	/**
	 * Call me when you want to delete an utterance marker from the timeline
	 */
	public void removeMarker(Utterance utr) {
		Node r = this.lookup("#" + (utr.getID()));
		if (r != null) {
			this.getChildren().remove(r);
			this.setSelectedMarker(null);
		}
	}

	/**
	 * adds a marker to the timeline for each utterance in the model
	 */
	public void renderUtterances() {
		this.getChildren().remove(1, this.getChildren().size());
		utteranceList.values().forEach(this::addMarker);
	}

	/**
	 *
	 * @return user selected timeline marker or null
	 */
	public TimeLineMarker getSelectedMarker() {
		return selectedMarker;
	}

	/**
	 * set user selected timeline marker
	 * 
	 * @param selectedMarker TimeLineMarker to set as selected
	 */
	public void setSelectedMarker(TimeLineMarker selectedMarker) {
		this.selectedMarker = selectedMarker;
	}

	/**
	 * access the animation portion of this timeline
	 * 
	 * @return animation
	 */
	public TranslateTransition getAnimation() {
		return animation;
	}

	/**
	 * @param utterance_id
	 * @return
	 */
	public TimeLineMarker getTimeLineMarker(String utterance_id) {
		return (TimeLine.TimeLineMarker) this.lookup("#" + utterance_id);
	}

	/**
	 * Represents a marker on the timeline. Inline so that it has access to TimeLine
	 * properties
	 */
	public class TimeLineMarker extends VBox {

		private String markerID; // how to find record in data
		private double posSeconds; // start position in bytes
		private MiscCode.Speaker speaker; //
		private Text markerCode; // displays utterance code for this marker
		private Polygon indicatorShape; // displays arrow on timeline
		private int indicatorWidth = 4; // size of arrow
		// TODO: i need this only if i want to immediately update tooltip after editing
		// annotation
		private Tooltip annotationToolTip;
		private StackPane codeBubble;

		public String getAnnotationToolTipText() {
			return annotationToolTip.getText();
		}

		/**
		 * Provide access for tooltip text so that changes in annotation text are
		 * immediately reflected in the mouseover popup. Use this setter to avoid empty
		 * tooltip popups.
		 * 
		 * @param annotationToolTipText
		 */
		public void setAnnotationToolTipText(String annotationToolTipText) {

			// update the tooltip text
			this.annotationToolTip.setText(annotationToolTipText);
			// remove any existing tooltip from marker
			Tooltip.uninstall(codeBubble, annotationToolTip);
			// if text is available, reinstall tooltip
			if (!annotationToolTipText.isEmpty()) {
				Tooltip.install(codeBubble, annotationToolTip);
			}
		}

		/**
		 * An indicator rendered on the timeline to represent an utterance
		 * 
		 * @param utterance
		 */
		public TimeLineMarker(Utterance utterance) {

			// Set spacing between nodes inside marker. specify spacing as CSS doesn't
			// appear to work for this
			this.setSpacing(0.0);
			this.markerID = utterance.toString();
			this.posSeconds = utterance.getStartTime().toSeconds();
			this.speaker = utterance.getMiscCode().getSpeaker();

			// where does marker point on timeline
			double tipPos = (posSeconds * pixelsPerSecond);

			// initialize utterance code
			markerCode = new Text(utterance.getMiscCode().name);
			markerCode.setTextAlignment(TextAlignment.CENTER);
			double markerCodeWidth = markerCode.getBoundsInLocal().getWidth();
			double markerCodeHeight = markerCode.getBoundsInLocal().getHeight();

			// stack code and surrounding bubble
			codeBubble = new StackPane();
			// bubble outline
			Rectangle codeBubbleOutline = new Rectangle((markerCodeWidth + 4.0), (markerCodeHeight));
			codeBubbleOutline.setArcHeight(5.0);
			codeBubbleOutline.setArcWidth(5.0);
			codeBubbleOutline.setFill(Color.WHITE);
			codeBubbleOutline.setStroke(Color.GRAY);
			codeBubbleOutline.setStrokeLineCap(StrokeLineCap.ROUND);
			codeBubbleOutline.setStrokeLineJoin(StrokeLineJoin.ROUND);
			codeBubbleOutline.setStrokeType(StrokeType.OUTSIDE);
			codeBubble.getChildren().addAll(codeBubbleOutline, markerCode);
			codeBubble.autosize();

			// define tooltip for codeBubble. Assign text with setter member.
			annotationToolTip = new Tooltip();
			annotationToolTip.setWrapText(true);
			annotationToolTip.setTextOverrun(OverrunStyle.ELLIPSIS);
			annotationToolTip.setMaxWidth(300.0);
			setAnnotationToolTipText(utterance.getAnnotation());

			// formatting of marker varies on speaker (above/below timeline)
			if (speaker.equals(MiscCode.Speaker.Therapist)) {
				// tick mark between code bubble and timeline
				this.indicatorShape = new Polygon(0, -indicatorWidth, indicatorWidth, 0, indicatorWidth * 2,
						-indicatorWidth);
				// add code bubble and indicator to create marker
				this.getChildren().addAll(codeBubble, indicatorShape);
				// vertical placement on timeline
				this.setTranslateY(5.5);
			} else {
				// speaker 2
				this.indicatorShape = new Polygon(0, indicatorWidth, indicatorWidth, 0, indicatorWidth * 2,
						indicatorWidth);
				this.getChildren().addAll(indicatorShape, codeBubble);
				this.setTranslateY((height / 2.0) + thickness);
			}

			// horizontal placement on timeline
			this.setTranslateX(tipPos - codeBubbleOutline.getBoundsInParent().getWidth() / 2.0);
			this.setAlignment(Pos.CENTER);

			/*
			 * Filter the TimeLineMarker MouseEvent.MOUSE_CLICKED to control event order. If
			 * i simply assign setOnContextMenuRequested() this gets handled before the
			 * MouseEvent.MOUSE_CLICKED. This interferes with setting the selected marker
			 * prior to wanting to use it in context menu.
			 */
			this.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {

				// Stop the timeline animation while user works with utterance code
				if (animation.getStatus() == Animation.Status.RUNNING) {
					animation.pause();
				}

				// get marker clicked on
				TimeLineMarker timeLineMarker = (TimeLineMarker) e.getSource();

				// select or deselect that marker
				if (timeLineMarker != null) {

					if (timeLineMarker.equals(getSelectedMarker())) {
						/*
						 * selected marker clicked again. If left-click, delect this marker, if
						 * right-click, don't.
						 */
						if (e.getButton().equals(MouseButton.PRIMARY)) {
							// timeLineMarker.getIndicatorShape().setFill(Color.BLACK);
							timeLineMarker.selected(false);
							setSelectedMarker(null);
						}
					} else {
						/*
						 * different marker selected, deselect any previous and select this one
						 * regardless of mouse button
						 */
						if (getSelectedMarker() != null) {
							// getSelectedMarker().getIndicatorShape().setFill(Color.BLACK);
							getSelectedMarker().selected(false);
						}

						// SELECT
						// timeLineMarker.getIndicatorShape().setFill(Color.INDIANRED);
						timeLineMarker.selected(true);
						// set active
						setSelectedMarker(timeLineMarker);
					}
				}

				/*
				 * finished with accounting. Now, provide a popup menu on right-click that will
				 * use selectedMarker "|| e.isControlDown()" is for OSX Ctrl+Click provided in
				 * addition to two finger click
				 */
				if (e.getButton().equals(MouseButton.SECONDARY) || e.isControlDown()) {
					getContextMenu().show(this, Side.BOTTOM, 0, 0);
				}

			});

			// set parent container to id that will be used to pull utterance;
			this.setId(markerID);
		}

		/**
		 * generate right-click pop-up menu
		 * 
		 * @return ContextMenu
		 */
		private ContextMenu getContextMenu() {

			// context menu to return
			ContextMenu contextMenu = new ContextMenu();

			// menu item for deleting marker
			MenuItem mniRemoveMarker = new MenuItem("Remove Marker");
			mniRemoveMarker.setOnAction(e -> {
				try {
					if (getSelectedMarker() != null) {
						remove(getSelectedMarker().getMarkerID());
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});

			MenuItem mniAnnotateUtterance = new MenuItem("Annotate");
			mniAnnotateUtterance.setOnAction(e -> {
				if (getSelectedMarker() != null) {
					setAnnotateMarker(getMarkerID());
				}
			});

			if (getSelectedMarker() != null) {
				if (MainController.getSpeakerState() == null || getSelectedMarker().speaker == MainController.getSpeakerState()) {
					mniRemoveMarker.setDisable(false);
				} else {
					mniRemoveMarker.setDisable(true);
				}
				contextMenu.getItems().addAll(mniRemoveMarker);
				contextMenu.getItems().addAll(mniAnnotateUtterance);
				contextMenu.getStyleClass().add("contextMenu");
			}

			return contextMenu;
		}

		public Polygon getIndicatorShape() {
			return indicatorShape;
		}

		public void setIndicatorShape(Polygon indicatorShape) {
			this.indicatorShape = indicatorShape;
		}

		public String getMarkerID() {
			return markerID;
		}

		public void setMarkerID(String markerID) {
			this.markerID = markerID;
		}

		public String getCode() {
			return markerCode.getText();
		}

		public void setCode(String code) {
			markerCode.setText(code);
		}

		public MiscCode.Speaker getSpeaker() {
			return speaker;
		}

		public void setSpeaker(MiscCode.Speaker newSpeaker) {
			this.speaker = newSpeaker;
		}

		public void selected(boolean b) {
			this.getIndicatorShape().setFill(b ? Color.INDIANRED : Color.BLACK);
		}
	}

}

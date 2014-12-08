package gamePlayer.guiFeatures.earthquake;

import gamePlayer.mainClasses.guiBuilder.GuiConstants;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseEarthquakeStrategy extends EarthquakeStrategy {

	EventHandler<MouseEvent> swiper;
	
	@Override
	public void start(EarthquakeController listener) {
		super.start(listener);
		swiper = event -> swipe(0.6);
		GuiConstants.GUI_MANAGER.addEventFilter(MouseEvent.MOUSE_PRESSED, swiper);
	}

	public void stop() {
		GuiConstants.GUI_MANAGER.removeEventFilter(MouseEvent.MOUSE_PRESSED, swiper);
	}

}

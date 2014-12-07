package gamePlayer.guiItemsListeners;

import gamePlayer.guiItems.messageDisplay.MessageDisplay;

public interface MessageDisplayListener {
	public void registerMessageDisplayListener(MessageDisplay display);
	public void displayMessage(String message, boolean error);
	public void clearMessageDisplay();
}

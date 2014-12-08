package gamePlayer.guiItemsListeners;

import gameEngine.Data.TowerInfoObject;
import gamePlayer.guiItems.store.Store;

import java.util.Collection;

public interface StoreListener {
    public void registerStore(Store store);
    public void fillStore(Collection<TowerInfoObject> towersAvailable);
    public void refreshStore();
	public void placeTower(String towerName);
	public void escapePlace();
}

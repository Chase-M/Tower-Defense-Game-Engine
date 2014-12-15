// This entire file is part of my masterpiece.
// GREG LYONS

package gamePlayer.guiItems.towerUpgrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.scene.Node;

public class UpgradeItemCollection<UpgradePanelItem> implements Iterable<UpgradePanelItem>{

	private List<UpgradePanelItem> myItems;
	
	public UpgradeItemCollection() {
		myItems = new ArrayList<UpgradePanelItem>();
	}
	
	public void addItems(UpgradePanelItem...items) {
		for (UpgradePanelItem item: items) {
			myItems.add(item);
		}
	}
	
	public Collection<Node> getItemNodes(){
		List<Node> nodes = new ArrayList<Node>();
		for (UpgradePanelItem item: myItems) {
			nodes.add((Node)item);
		}
		return nodes;
	}

	@Override
	public Iterator iterator() {
		return myItems.iterator();
	}
	
}
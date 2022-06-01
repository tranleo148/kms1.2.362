importPackage(Packages.client.inventory);
function act(){
	rm.getPlayer().getMap().spawnItemDrop(rm.getReactor(), rm.getPlayer(), new Item(4032860, 0, 1, 0), rm.getPosition(), false, false);
	rm.getReactor().setState(0);
}
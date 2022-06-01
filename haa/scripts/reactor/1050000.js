function act() {
    if (Math.random() > 0.7) {
	rm.dropItems();
    } else {
	rm.warp(105090200);
    }
}
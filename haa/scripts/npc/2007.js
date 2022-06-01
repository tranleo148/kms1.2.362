function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		cm.dispose();
/*		if (!cm.getPlayer().isGM()) {
			cm.sendOk("경매장 점검중입니다. ");
			return;
		}*/
		Packages.handling.auction.handler.AuctionHandler.EnterAuction(cm.getPlayer(), cm.getClient());
	}
}
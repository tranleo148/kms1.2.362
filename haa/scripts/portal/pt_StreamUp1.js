function enter(pi) {
	switch(pi.getPlayer().getMapId())
	{
		case 450002015:
		pi.teleport(3);
		break;

		default:
		pi.teleport(9);
	}
}

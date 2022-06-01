//¾ÆÀÌÅÛÄÚµå, °¹¼ö
var item =[
[2432408, 1],
];

function enter(pi) {
	if (pi.canHold(4001198,1) && (pi.getMap().getAllMonstersThreadsafe().size() == 0 || pi.getMap().getMonsterById(9300183) != null) && (pi.getMap().getReactorByName("") == null || pi.getMap().getReactorByName("").getState() == 1)) {
		pi.warp(930000800,0);
		pi.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(pi.getPlayer().getLevel())/30);
		for(var i =0; i<item.length; i++){
			pi.gainItem(item[i][0], item[i][1]);
		}
	} else {
		pi.playerMessage(5, "Æ÷ÀÌÁð °ñ·½À» ÅðÄ¡ÇØÁÖ¼¼¿ä.");
	}
}
importPackage(java.lang);
importPackage(java.util);


var icon = "#fUI/UIWindow2.img/QuestIcon/4/0#"
//아이템코드, 갯수 
var item =[
[3994745, 3],
];


function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
			talk = "";
		if(cm.getPlayer().getMapId() != 921160600 && cm.getPlayer().getMapId() != 921160700){
			talk+= "이대로 포기하고 나가시겠습니까?\r\n";
			talk+= "#L0##b이곳에서 나가고 싶습니다.#k";
		} else if(cm.getPlayer().getMapId() == 921160700 && cm.getMonsterCount(cm.getMapId()) == 0){
			talk+= "도와주셔서 정말 감사드립니다. 덕분에 이곳에서 벗어날 수 있게 되었습니다.\r\n\r\n";
			talk +=icon+"\r\n";
			for(var i =0; i<item.length; i++){
				talk +="#i"+item[i][0]+"# #z"+item[i][0]+"# "+item[i][1]+"개\r\n";
			}
			talk +="#L1# 보상을 받고 나간다\r\n";
			cm.sendSimple(talk);
			talk+= "#L0##b이곳에서 나가시겠습니까?#k";
		} else {
			talk+= "이대로 포기하고 나가시겠습니까?\r\n";
			talk+= "#L0##b이곳에서 나가고 싶습니다.#k";
		}
		cm.sendSimple(talk);
	} else if(St == 1) {
		if(S == 0){
			cm.warp(910002000, 0);
			cm.dispose();
		} else if(S == 1){
			cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(cm.getPlayer().getLevel())/30);
			for(var i =0; i<item.length; i++){
				cm.gainItem(item[i][0], item[i][1]);
			}
			cm.warp(910002000, 0);
			cm.dispose();
		}
	}
}
importPackage(Packages.constants);
importPackage(Packages.packet.creators);

var status = -1;

function start() {
status = -1;
action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
	cm.dispose();
	} else {
	if (mode == 0) {
	cm.dispose();            
	return;        
	}        
	if (mode == 1)            
	status++;        
	else           
	status--;    

	if (status == 0) {
	sT = cm.getPlayer().getKeyValue("damageskin");
	showN = sT ==  1 ? "디지털라이즈" :
		sT ==  2 ? "크리티아스" :
		sT ==  3 ? "파티 퀘스트" :
		sT ==  4 ? "임팩티브" :
		sT ==  5 ? "달콤한 전통 한과" :
		sT ==  6 ? "클럽 헤네시스" :
		sT ==  7 ? "메리 크리스마스" :
		sT ==  8 ? "눈 꽃송이" :
		sT ==  9 ? "알리샤의" :
		sT == 10 ? "도로시의" :
		sT == 11 ? "키보드 워리어" :
		sT == 12 ? "살랑살랑 봄바람" :
		sT == 13 ? "솔로부대" : 
		sT == 14 ? "레미너선스" :
		sT == 15 ? "주황버섯" :
		sT == 16 ? "왕관" : "";

	sS = cm.getPlayer().getKeyValue2("DS_SAVE");
	showS = sS ==  1 ? "디지털라이즈" :
		sS ==  2 ? "크리티아스" :
		sS ==  3 ? "파티 퀘스트" :
		sS ==  4 ? "임팩티브" :
		sS ==  5 ? "달콤한 전통 한과" :
		sS ==  6 ? "클럽 헤네시스" :
		sS ==  7 ? "메리 크리스마스" :
		sS ==  8 ? "눈 꽃송이" :
		sS ==  9 ? "알리샤의" :
		sS == 10 ? "도로시의" :
		sS == 11 ? "키보드 워리어" :
		sS == 12 ? "살랑살랑 봄바람" :
		sS == 13 ? "솔로부대" :
		sS == 14 ? "레미너선스" :
		sS == 15 ? "주황버섯" :
		sS == 16 ? "왕관" : "";

	xX = sS != 0 ? "은" : "이";
	yY = sS != 0 ? " 데미지 스킨#k입니다." : "#k없습니다.";
	Xx = sT != 0 ? "은" : "이";
	Yy = sT != 0 ? " 데미지 스킨#k입니다." : "#k없습니다.";

		if(sT == null) { cm.getPlayer().setKeyValue("damageskin", "0") }
		if(sS == null) { cm.getPlayer().setKeyValue2("DS_SAVE", "0") }
	var text = "원하시는 기능을 선택해 주세요.\r\n\r\n";
	text += "#L1##b 현재 적용 중인 데미지 스킨을 저장한다.#k\r\n";
	text += "　 - 현재 적용되어 있는 데미지 스킨"+Xx+" #b"+showN+""+Yy+"\r\n\r\n";
	text += "#L2##b 저장되어 있는 데미지 스킨을 적용한다.#k\r\n";
	text += "　 - 현재 저장된 데미지 스킨"+xX+" #b"+showS+""+yY+"";
	cm.sendSimple(text);
		
	} else if (status == 1) {
		Sort = selection == 1 ? 1 : 2
		switch(selection) {
			case 1:
			sT != 0 ? cm.sendYesNo("정말 #b"+showN+" 데미지 스킨#k을 저장하시겠습니까? 기존에 저장되어 있던 데미지 스킨의 정보는 삭제됩니다.") :
				  cm.sendNext("기본 데미지 스킨은 저장할 수 없습니다.");
			return;

			case 2:
			sS != 0 ? cm.sendYesNo("저장된 데미지 스킨을 적용하려 하시는군요!\r\n#b현재 적용된 "+showN+" 데미지 스킨#k 대신 #b"+showS+" 데미지 스킨#k을 적용시키시겠습니까?") :
				  cm.sendNext("현재 저장된 데미지 스킨이 없습니다.");
			return;
		}
	} else if (status == 2) {
		switch(Sort) {
			case 1:
			if(sT != 0) {
			saveDS(sT);
			cm.sendNext("#b"+showN+" 데미지 스킨#k이 저장되었습니다.");
			}
			cm.dispose();
			return;

			case 2:
			if(sS != 0) {
			ReloadDS();
			cm.sendNext("#b"+showS+" 데미지 스킨#k이 적용되었습니다.");
			}
			cm.dispose();
			return;
		}
	}
}
}







function saveDS(i) {
	cm.getPlayer().setKeyValue2("DS_SAVE", i);
}

function ReloadDS() {
	switch(sS) {
		case 0:
		cm.getPlayer().setKeyValue("damageskin", 0);
		return;

		case 1:
		cm.getPlayer().setKeyValue("damageskin", 1);
		return;

		case 2:
		cm.getPlayer().setKeyValue("damageskin", 2);
		return;

		case 3:
		cm.getPlayer().setKeyValue("damageskin", 3);
		return;

		case 4:
		cm.getPlayer().setKeyValue("damageskin", 4);
		return;

		case 5:
		cm.getPlayer().setKeyValue("damageskin", 5);
		return;

		case 6:
		cm.getPlayer().setKeyValue("damageskin", 6);
		return;

		case 7:
		cm.getPlayer().setKeyValue("damageskin", 7);
		return;

		case 8:
		cm.getPlayer().setKeyValue("damageskin", 8);
		return;

		case 9:
		cm.getPlayer().setKeyValue("damageskin", 9);
		return;

		case 10:
		cm.getPlayer().setKeyValue("damageskin", 10);
		return;

		case 11:
		cm.getPlayer().setKeyValue("damageskin", 11);
		return;

		case 12:
		cm.getPlayer().setKeyValue("damageskin", 12);
		return;

		case 13:
		cm.getPlayer().setKeyValue("damageskin", 13);
		return;

		case 14:
		cm.getPlayer().setKeyValue("damageskin", 14);
		return;

		case 15:
		cm.getPlayer().setKeyValue("damageskin", 15);
		return;

		case 16:
		cm.getPlayer().setKeyValue("damageskin", 16);
		return;

	}
}
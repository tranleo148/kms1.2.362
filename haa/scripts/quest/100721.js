importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;

function start(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
		return;
	}
	if (mode == 0) {
		if (status == 5) {
			qm.sendOkS("마음이 바뀌면 언제든지 다시 말을 걸어줘.", 4, 9062474);
		}
		status--;
		qm.dispose();
		return;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		qm.sendNextS("#b#e#h0##n#k, 안녕?\r\n네 덕분에 네오 캐슬이 오늘도 아름답게 빛나고 있어.\r\n초대받은 사람들도 정말 즐거워하고 있단다.", 4, 9062474);
	} else if (status == 1) {
		qm.sendNextPrevS("그래, 혹시 이곳에 올 때 탔던 눈꽃 순록을 기억하니?\r\n그 아이들에게도 이 즐거움을 나눠주고 싶어.", 4, 9062474);
	} else if (status == 2) {
		qm.sendNextPrevS("물론 나눠주는 건 나겠지?", 0x02, 9062474, 9062474);
	} else if (status == 3) {
		qm.sendNextPrevS("후후후, 눈치 빠른 사람은 정말 좋다니까.", 4, 9062474);
	} else if (status == 4) {
		qm.sendYesNoS("눈꽃 순록들이 마음껏 바깥세상을 달릴 수 있도록 도와주렴.\r\n내가 만든 #b#e마법의 종#n#k을 받아주겠니?", 4, 9062474);
	} else if (status == 5) {
		qm.sendNextS("고마워.\r\n네오 캐슬을 유지하기 위해 모았던 에르다로 만든\r\n#b#e마법의 종#n#k을 받으렴.", 4, 9062474);
		qm.getPlayer().changeSkillLevel(80003025, 1, 1);
		qm.getPlayer().setKeyValue(100722, "cnt", "0");
		qm.giveBuff(80003025);
		qm.forceCompleteQuest();
		qm.getPlayer().dropMessage(5, "지금부터 레벨 범위 몬스터가 등장하는 곳에서 <르네와 마법의 종> 스킬을 사용할 수 있습니다.")
	} else if (status == 6) {
		qm.sendNextPrevS("#r#e레벨 범위 몬스터#n#k를 사냥하다 보면 가끔 에르다를 얻을 수 있어.\r\n에르다를 모을 때마다 네오 캐슬의 보석,\r\n#b#e#i4310306:# #t4310306##n#k을 1개씩 줄게.\r\n\r\n\r\n#r※ 레벨 범위 몬스터는 캐릭터의 레벨을 기준으로 #e-20레벨에서 +20레벨 범위#n에 해당하는 몬스터를 의미합니다.#k", 4, 9062474);
	} else if (status == 7) {
		qm.sendNextPrevS("그리고 마법의 종에 에르다가 가득 차면 #b#e커다란 마법의 종#n#k이 나타날 거야.\r\n그 마법의 종을 쳐서 깨트리면 #b#e#i4310306:# #t4310306##n#k 20개를 받을 수 있어.", 4, 9062474);
	} else if (status == 8) {
		qm.sendNextPrevS("종을 치다 보면 가끔 #b#e눈꽃 순록#n#k들이 달려올 거야.\r\n눈꽃 순록들이 나타나면 #b#e#i4310306:# #t4310306##n#k 20개를 더 받을 수 있어.", 4, 9062474);
	} else if (status == 9) {
		qm.sendNextPrevS("눈꽃 순록들은 처음에는 낯설어 할지도 모르지만 꾸준히\r\n불러주면 분명 너를 따르게 될 거야.\r\n\r\n#r* 이벤트 진행에 따라 눈꽃 순록 등장 확률이 증가합니다.", 4, 9062474);
	} else if (status == 10) {
		qm.sendNextPrevS("그럼 #r#e레벨 범위 몬스터#n#k를 어서 사냥하러 가보자.\r\n눈꽃 순록들이 바깥세상을 뛰놀 수 있도록 도와주렴.", 4, 9062474);
		qm.dispose();
	}
}

function statusplus(millsecond) {
	qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
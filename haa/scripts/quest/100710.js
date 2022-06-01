importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);
importPackage(Packages.scripting);

var status = -1;
var sel = 0;
function start(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
		return;
	}
	if (mode == 0) {
		status--;
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		var talk = "#h #!\r\n궁금한 것이 있어??\r\n\r\n#b"
		talk += "#L0##e<네오 캐슬>#n에 대해 알려줘.\r\n";
		talk += "#L1##e<네오 코어>#n에 대해 알려줘.\r\n";
		talk += "#L2##e<네오 젬>#n에 대해 알려줘.\r\n";
		talk += "#L3##e<네오 스톤>#n에 대해 알려줘.\r\n";
		talk += "#L4##e<르네와 마법의 종>#n에 대해 알려줘.\r\n";
		talk += "#L5##e<리오의 탐험일지>#n에 대해 알려줘.\r\n";
		qm.sendSimpleS(talk, 0x04, 9062474);
	} else if (status == 1) {
		sel = selection;
		switch (sel) {
			case 0:
				qm.sendNextS("#e#r네오 캐슬#k#n은 차원이 겹쳐진 곳에 비치는 #b#e환상#n#k같은 거야.", 0x04, 9062474);
				break;
			case 1:
				qm.sendNextS("#r#e네오 코어#n#k는 아주 #b#e강한 몬스터의 힘#n#k이 응축되어 만들어진 보석이야.", 0x04, 9062474);
				break;
			case 2:
				qm.sendNextS("#b#e#i4310307# #t4310307##n#k은 아주 #r#e순수한 즐거움#n#k이 응축되어 만들어진 보석이야.", 0x04, 9062474);
				break;
			case 3:
				qm.sendNextS("#b#e#i4310306# #t4310306##n#k은 네오 캐슬과 같은 힘을 지닌 보석이야.", 0x04, 9062474);
				break;
			case 4:
				qm.sendNextS("#b#e마법의 종#n#k은 네오 캐슬을 유지하기 위해 모았던 에르다로 만든 종이야.\r\n이 종을 가지고 #r#e레벨 범위 몬스터#n#k를 사냥하면 낮은 확률로 종에 에르다가 모일 거야.\r\n에르다가 모일 때마다 #b#e#i4310306:# #t4310306##n#k을 1개씩 줄게.", 0x04, 9062474);
				break;
			case 5:
				qm.sendNextS("음... 이건 #b#e리오#n#k한테 직접 물어보는 게 좋겠어.\r\n리오를 불러줄게.", 0x04, 9062474);
				break;
		}
	} else if (status == 2) {
		switch (sel) {
			case 0:
				qm.sendNextPrevS("#b#e그란디스#n#k와 #r#e메이플 월드#n#k가 #e하나의 차원#n이라는 것 알지? \r\n서로 다른 두 차원이 합쳐지면서 잠시 생겨난 공간인 거지.", 0x04, 9062474);
				break;
			case 1:
				qm.sendNextPrevS("#r#e네오 코어#n#k에 대해 설명할 수 있는 전문가를 불러줄게.", 0x04, 9062474);
				break;
			case 2:
				qm.sendNextPrevS("#r#e순수한 즐거움#n#k은 신나게 게임을 즐기는 어린아이들에게 강하게 나타나지.", 0x04, 9062474);
				break;
			case 3:
				qm.sendNextPrevS("네오 캐슬이 생기면서 유지되는 동안 계속 생겨날 거야.", 0x04, 9062474);
				break;
			case 4:
				qm.sendNextPrevS("그리고 마법의 종에 에르다가 가득 차면 #b#e커다란 마법의 종#n#k이 나타날 거야.\r\n그 마법의 종을 쳐서 깨트리면 #b#e#i4310306:# #t4310306##n#k 20개를 받을 수 있어.", 0x04, 9062474);
				break;
			case 5:
				qm.sendNextS("#b#e#h0##k#n님! \r\n제 탐험일지에 대해 궁금하신가요?", 0x04, 9062475);
				break;
		}
	} else if (status == 3) {
		switch (sel) {
			case 0:
				qm.sendNextPrevS("메이플 월드와 그란디스의 #b#e바다가 만나는 곳#n#k에 에르다의 힘으로 생겨난 거야.", 0x04, 9062474);
				break;
			case 1:
				qm.sendNextPrevS("하하! #r#e네오 코어#n#k에 대해 궁금하다고?", 0x04, 9062459);
				break;
			case 2:
				qm.sendNextPrevS("#b#e그란디스#n#k와 #r#e메이플 월드#n#k의 #e게임 대표#n로 놀러 온 아이들과 게임을 즐겨봐!", 0x04, 9062474);
				break;
			case 3:
				qm.sendNextPrevS("나와 리오를 도와주면 우리가 #b#e#i4310306# #t4310306##n#k을 줄게!", 0x04, 9062474);
				break;
			case 4:
				qm.sendNextPrevS("종을 치다 보면 가끔 #b#e눈꽃 순록#n#k들이 달려올 거야.\r\n눈꽃 순록들이 나타나면 #b#e#i4310306:# #t4310306##n#k 20개를 더 받을 수 있어.", 0x04, 9062474);
				break;
			case 5:
				qm.sendNextPrevS("제가 네오 캐슬과 함께 태어난 정령인 것은 말씀드렸죠...?", 0x04, 9062475);
				break;
		}
	} else if (status == 4) {
		switch (sel) {
			case 0:
				qm.sendNextPrevS("두 차원이 안정을 찾으면 네오 캐슬은 다시 사라질 거야. ", 0x04, 9062474);
				break;
			case 1:
				qm.sendNextPrevS("이 성은 신비한 힘을 가지고 있다.\r\n바로 #b#e다양한 힘#n#k을 #r#e보석의 형태#n#k로 만드는 힘!", 0x04, 9062459);
				break;
			case 2:
				qm.sendNextPrevS("아이들을 정말 즐겁게 해준다면 #b#e#i4310307# #t4310307##n#k을 얻을 수 있을 거야.", 0x04, 9062474);
				break;
			case 3:
				qm.sendNextPrevS("헤헤. 저를 도와주시면 #b#e#i4310306# #t4310306##n#k을 드리겠습니다!", 0x04, 9062475);
				break;
			case 4:
				qm.sendNextPrevS("눈꽃 순록들은 처음에는 낯설어 할지도 모르지만 꾸준히\r\n불러주면 분명 너를 따르게 될 거야.\r\n\r\n#r* 이벤트 진행에 따라 눈꽃 순록 등장 확률이 증가합니다.", 0x04, 9062474);
				break;
			case 5:
				qm.sendNextPrevS("지금은 인간의 모습을 하고 있지만 시간이 흐르면 다시 에르다의 흐름으로 되돌아가게 된답니다... \r\n그러니까 에르다로 돌아가기 전에 이 세계를 잔뜩 살펴보고, 실컷 즐기고 싶어요!", 0x04, 9062475);
				break;
		}
	} else if (status == 5) {
		switch (sel) {
			case 0:
				qm.sendNextPrevS("사라진다니...", 0x02, 9062474, 9062474);
				break;
			case 1:
				qm.sendNextPrevS("그중 #i4310308# #r#e#t4310308##n#k는 아주 강한 #b#e몬스터의 힘#n#k으로만 만들 수 있지.", 0x04, 9062459);
				break;
			case 2:
				qm.dispose();
				NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062474, 100710);
				break;
			case 3:
				qm.sendNextPrevS("#b#e#i4310306# #t4310306##n#k을 사용해서 이곳에 놀러 온 상인들에게 #r#e다양한 물건#n#k을 구매할 수 있을 거야.", 0x04, 9062474);
				break;
			case 4:
				qm.sendNextPrevS("그럼 #r#e[NEO] 르네와 마법의 종 퀘스트#n#k를 완료하고\r\n#b마법의 종#k에 에르다를 모아주렴.", 0x04, 9062474);
				break;
			case 5:
				qm.sendNextPrevS("궁금한 것들을 써둔 #r#e탐험일지#n#k를 가지고, 네오 캐슬 밖으로 나갈 수 없는 저 대신 조사해 주세요!", 0x04, 9062475);
				break;
		}
	} else if (status == 6) {
		switch (sel) {
			case 0:
				qm.sendNextPrevS("후후. 그래서 사람들을 초대한 거야. \r\n네오 캐슬이 있는 동안 다양한 사람들과 재미있게 놀아봐!", 0x04, 9062474);
				break;
			case 1:
				qm.sendNextPrevS("#b#e강한 몬스터#n#k를 처치하면 그 힘이 처치한 사람에게 잠시 유지된다. 그 힘이 사라지기 전에 나에게 오면 #r#e네오 코어#n#k를 만드는 것을 도와주지. \r\n\r\n\r\n#r ※ #e주간 보스#n를 처치하고 콜렉터에게 말을 걸면 네오 \r\n    코어를 받을 수 있습니다. ", 0x04, 9062459);
				break;
			case 3:
				qm.sendNextPrevS("그리고 누적으로 획득한 #b#e#i4310306# #t4310306##n#k의 양이 많아지면 구매할 수 있는 물건이 많아져요!\r\n\r\n\r\n#r ※ 캐릭터가 누적 네오 스톤 획득량을 달성하면, 월드 내 \r\n    모든 캐릭터가 다음 단계 상점을 이용할 수 있습니다.", 0x04, 9062475);
				break;
			case 4:
				qm.dispose();
				NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062474, 100710);
				break;
			case 5:
				qm.sendNextPrevS("탐험일지는 #r#e4가지#n#k를 쓸 거예요! \r\n\r\n#e#b[리오의 탐험일지]#k\r\n\r\n 첫 번째 - 레벨 범위 몬스터 처치\r\n 두 번째 - 엘리트 몬스터&챔피언 처치\r\n 세 번째 - 룬 사용\r\n 네 번째 - 폴로&프리토 클리어", 0x04, 9062475);
				break;
		}
	} else if (status == 7) {
		switch (sel) {
			case 0:
				qm.dispose();
				NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062474, 100710);
				break;
			case 1:
				qm.sendNextPrevS("몬스터를 처치한 힘은 #b#e매주 수요일#n#k까지만 유지되니, \r\n강한 몬스터를 처치하면 바로 오는 게 좋아.\r\n\r\n\r\n#r ※ 주간 보스 클리어 기록은 매주 #e목요일 0시#n에 리셋됩니다.", 0x04, 9062459);
				break;
			case 3:
				qm.sendNextPrevS("그럼 잘 부탁해!", 0x04, 9062474);
				break;
			case 5:
				qm.sendNextPrevS("탐험일지를 완료하면 #b#e#i4310306# #t4310306##n#k을 드릴게요!\r\n\r\n#e[리오의 탐험일지]\r\n 첫 번째 - #b#e#i4310306# #t4310306##k 100개\r\n 두 번째 - #b#e#i4310306# #t4310306##k 100개\r\n 세 번째 - #b#e#i4310306# #t4310306##k 200개\r\n 네 번째 - #b#e#i4310306# #t4310306##k 400개", 0x04, 9062475);
				break;
		}
	} else if (status == 8) {
		switch (sel) {
			case 1:
				qm.sendNextPrevS("그리고 #i4310308# #r#e#t4310308##n#k는 일주일에 #b#e월드 당 최대 400개#n#k까지만 만들 수 있다.\r\n\r\n\r\n#r ※ 네오 코어는 월드 내 공유되며, 월드 당 주간 400개까지 \r\n    획득할 수 있습니다.", 0x04, 9062459);
				break;
			case 3:
				qm.dispose();
				NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062474, 100710);
				break;
			case 5:
				qm.sendNextPrevS("참! #b#e매주 목요일 오전 10시#n#k에 새로운 탐험일지를 쓸 거니까 매주 도와주셔야 해요!", 0x04, 9062475);
				break;
		}
	} else if (status == 9) {
		switch (sel) {
			case 1:
				qm.sendNextPrevS("당연한 말이지만 #b#e강한 몬스터#n#k일수록 더 많은 #r#e네오 코어#n#k를 만들 수 있다는 것도 알아두도록.\r\n\r\n#e[주간 보스]#n#k#e\r\n\r\n#r#i4310308# #t4310308# 5개#k#e\r\n 하드 힐라#e\r\n 카오스 핑크빈#e\r\n 이지 시그너스#e\r\n 노멀 시그너스#e\r\n\r\n#r#i4310308# #t4310308# 10개#k#e\r\n 카오스 자쿰#e\r\n 카오스 피에르#e\r\n 카오스 반반#e\r\n 카오스 블러디퀸#e\r\n\r\n#r#i4310308# #t4310308# 20개#k#e\r\n 하드 매그너스#e\r\n 카오스 벨룸#e\r\n\r\n#r#i4310308# #t4310308# 30개#k#e\r\n 카오스 파풀라투스#e\r\n 노멀 스우#e\r\n 노멀 데미안#e\r\n 이지 루시드#e\r\n\r\n#r#i4310308# #t4310308# 40개#k#e\r\n 노멀 루시드#e\r\n 노멀 윌#e\r\n 노멀 더스크#e\r\n 노멀 듄켈#e\r\n\r\n#r#i4310308# #t4310308# 60개#k#e\r\n 하드 데미안 #e\r\n 하드 스우#e\r\n 하드 루시드#e\r\n 하드 윌#e\r\n\r\n#r#i4310308# #t4310308# 70개#k#e\r\n 카오스 더스크#e\r\n 하드 듄켈#e\r\n 진 힐라", 0x04, 9062459);
				break;
			case 5:
				qm.dispose();
				NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062474, 100710);
				break;
		}
	} else if (status == 10) {
		switch (sel) {
			case 1:
				qm.sendNextPrevS("그리고 몬스터를 처치하다가 실패한 경우에는 #r#e네오 코어#n#k를 받을 수 없다는 것 명심해!\r\n\r\n #b ※ 처치 시점에 #e사망 상태인 경우#n 네오 코어를 받을 수 \r\n     없습니다.\r\n\r\n  ※ 처치 전에 #e퇴장하는 경우#n 경우 네오 코어를 받을 수 \r\n     없습니다.\r\n\r\n  ※ 카오스 피에르, 블러디퀸은 처치 후 #e보물상자#n를 \r\n     오픈해야 네오 코어를 받을 수 있습니다. \r\n\r\n  ※ 이지, 노멀, 하드 루시드는 #e오르골#n까지 처치해야 \r\n     네오 코어를 받을 수 있습니다.", 0x04, 9062459);
				break;
		}
	} else if (status == 11) {
		switch (sel) {
			case 1:
				qm.sendNextPrevS("또 네오 코어를 많이 얻을수록 #r#e네오 포스#n#k라는 특별한 힘을 얻게 된다.\r\n\r\n\r\n#r ※ 네오 포스는 월드 내 모든 캐릭터가 받을 수 있습니다.", 0x04, 9062459);
				break;
		}
	} else if (status == 12) {
		switch (sel) {
			case 1:
				qm.sendNextPrevS("네오 코어를 많이 모으면 나에게 찾아와!\r\n자네의 네오 포스를 업그레이드해 주지.\r\n\r\n\r\n#r ※ 누적 네오 코어 개수에 따라 콜렉터에게 네오 포스 \r\n    스킬을 받을 수 있습니다.", 0x04, 9062459);
				break;
		}
	} else if (status == 13) {
		switch (sel) {
			case 1:
				qm.sendNextPrevS("그럼 네오 코어에 대해 더 자세하게 알고 싶다면 나를 찾아와.", 0x04, 9062459);
				break;
		}
	} else if (status == 14) {
		switch (sel) {
			case 1:
				qm.dispose();
				NPCScriptManager.getInstance().startQuest(qm.getClient(), 9062474, 100710);
				break;
		}
	}
}

function statusplus(millsecond) {
	qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}
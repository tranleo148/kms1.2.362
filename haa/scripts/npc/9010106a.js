
var enter = "\r\n";
var seld = -1;

var c = 4310229;

var require = -1;
var reqlevel = -1;

var union, main, sub;
var unionStr = "";
var unionNext = "";
var canAdd = -1;
var nextAdd = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}

function getRequire() {
	switch (main) {
		case 1:
			require = sub == 1 ? 120 : sub == 2 ? 140 : sub == 3 ? 150 : sub == 4 ? 160 : sub == 5 ? 170 : -1;
		break;
		case 2:
			require = sub == 1 ? 430 : sub == 2 ? 450 : sub == 3 ? 470 : sub == 4 ? 490 : sub == 5 ? 510 : -1;
		break;
		case 3:
			require = sub == 1 ? 930 : sub == 2 ? 960 : sub == 3 ? 1000 : sub == 4 ? 1030 : sub == 5 ? 1060 : -1;
		break;
		case 4:
			require = sub == 1 ? 2200 : sub == 2 ? 2300 : sub == 3 ? 2350 : sub == 4 ? 2400 : -1;
		break;
	}
}

function getUnionStr() {
	var temp = main == 1 ? "노비스" : main == 2 ? "베테랑" : main == 3 ? "마스터" : main == 4 ? "그랜드 마스터" : "";
	return temp + " 유니온 "+sub+"단계";
}

function getUnionNext() {
	var adv = sub == 5;
	if (!adv) {
		nextAdd = (9 * (main)) + (sub + 1);
		var temp = main == 1 ? "노비스" : main == 2 ? "베테랑" : main == 3 ? "마스터" : main == 4 ? "그랜드 마스터" : "";
		return temp + " 유니온 "+(sub + 1)+"단계";
	} else {
		nextAdd = (9 * (main + 1));
		var temp = (main + 1) == 1 ? "노비스" : (main + 1) == 2 ? "베테랑" : (main + 1) == 3 ? "마스터" : (main + 1) == 4 ? "그랜드 마스터" : "";
		return temp + " 유니온 1단계";
	}
}

function getUnion() {
	union = cm.getPlayer().getKeyValue(18771, "rank");
	main = Math.floor(union / 100);
	sub = union % 10;
	reqlevel = (((main == 1 ? 0 : main == 2 ? 1 : main == 3 ? 2 : 3) * 5) + sub) * 500;
	canAdd = (9 * (main)) + sub;
}

function getUnionChrSize() {
	return cm.getPlayer().getUnions().getUnions().size();
}

function sex() {
	var adv = sub == 5;
	var newunion = "";
	if (adv) {
		newmain = (main + 1);
		newsub = 1;
		newunion = newmain+"0"+newsub;
	} else {
		newsub = (sub + 1);
		newunion = main+"0"+newsub;
	}
	cm.getPlayer().setKeyValue(18771, "rank", newunion);
}

function action(mode, type, sel) {
	ac = 0;
	outmap = 921172200
	outmap2 = 921172201
	coin = cm.getPlayer().getUnionCoin();
	ncoin = cm.getPlayer().getUnionCoinNujuk();
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
    	}
	if (status == 0) {
		if (cm.getPlayer().getMapId() == outmap || cm.getPlayer().getMapId() == outmap2) {
            if (coin == 0) {
                talk = "음~ 아직 유니온 코인을 하나도 얻지 못하셨군요? 획득이 너무 어려우시다면 시간을 조금 가진 뒤 들어와 보세요. 유니온의 구성원들이 열심히 코인을 모아 둘 거에요."
            } else {
				talk = "#i4310229# #b#z4310229##k을 #b" + coin + "개#k나 모으셨군요? 대단해요~\r\n"
				talk += "#b주간 누적 유니온 코인 랭킹#k을 갱신해 드릴게요!\r\n\r\n"
				talk += "#b금주의 누적 코인#k#e:"+(cm.getPlayer().getUnionCoinNujuk() + coin)+"#n\r\n\r\n"
				talk += "그럼 왔던 곳으로 돌려 보내 드릴게요. 안녕히 가세요~" 
            }
            cm.sendNextS(talk,0x04,9010106);
		} else {
			getUnion();
			unionStr = getUnionStr();
			unionNext = getUnionNext();
			getRequire();
			var msg = "이것 참 용잡으러 가기 좋은 날이군요!"+enter;
			msg += "어떤 #b메이플 유니온#k 업무를 도와드릴까요?#b\r\n"+enter;
			msg += "#L1#<나의 메이플 유니온 정보를 확인한다.>"+enter;
			msg += "#L2#<메이플 유니온 등급을 올린다.>"+enter;
			msg += "#L3#<메이플 유니온에 대해 설명을 듣는다.>"+enter;
			//msg += "#L4#<주간 획득 코인 랭킹>";
			cm.sendSimpleS(msg, 4, 9010108);
		}
	} else if (status == 1) {
		if (cm.getPlayer().getMapId() == outmap || cm.getPlayer().getMapId() == outmap2) {
            if (coin == 0) {
				cm.warp(cm.getPlayer().getSkillCustomValue0(232471));
				cm.dispose();				
            } else {
				cm.getPlayer().AddAllUnionCoin(coin);
				cm.getPlayer().setUnionCoinNujuk(ncoin + coin);
				cm.warp(cm.getPlayer().getSkillCustomValue0(232471));
				cm.getPlayer().setUnionCoin(0);
				//cm.getPlayer().saveUnionRanks(cm.getPlayer().getClient());
				cm.dispose();
				return;
            }
		} else {
			seld = sel;
			switch (sel) {
				case 1:
					var msg = "용사님의 #e메이플 유니온#n 정보를 알려드릴까요?"+enter+enter;
					msg += "#e메이플 유니온 등급: #b<"+unionStr+">#k"+enter;
					msg += "유니온 레벨: #b<"+cm.getPlayer().getAllUnion()+">#k"+enter;
					msg += "보유 유니온 캐릭터: #b<"+getUnionChrSize()+">#k"+enter;
					msg += "투입 가능 공격대원:#b<"+canAdd+"명>#k";
					cm.sendOkS(msg, 4, 9010108);
					cm.dispose();
				break;
				case 2:
					if (main == 4 && sub == 5) {
						cm.sendOkS("당신은 더 이상 유니온 등급을 올릴 수 없답니다!", 4, 9010108);
						cm.dispose();
						return;
					}
					var msg = "#e메이플 유니온 승급#n을 하고 싶으신가요?"+enter;
					msg += "#e현재등급: #b<"+unionStr+">#k"+enter;
					msg += "다음등급: #b<"+unionNext+">#k"+enter;
					msg += "승급 시 투입 가능 공격대원 증가: #b<"+canAdd+"→"+nextAdd+" 명>#k#n"+enter+enter;
					msg += "승급을 위해선 아래 조건을 충족하셔야 해요."+enter+enter;
					msg += "#e<유니온 레벨> #r"+reqlevel+"이상#k"+enter;
					msg += "<지불 코인> #b#z"+c+"# "+require+"개#k#n"+enter+enter;
					msg += "지금 메이플 유니온을 #e승급#k 시켜 드릴까요?";
					cm.sendYesNoS(msg, 4, 9010108);
				break;
				case 3:
					var msg = "#b메이플 유니온#k에 대해 궁금하시다고요?\r\n";
					msg += "무엇을 알려드릴까요?\r\n\r\n";
					msg += "#b#L6#메이플 유니온이란?\r\n"
					msg += "#L7#유니온 레벨\r\n"
					msg += "#L8#유니온 등급\r\n"
					msg += "#L9#공격대와 전투력\r\n"
					msg += "#L10#전투지도와 캐릭터 블록\r\n"
					msg += "#L11#주간 획득 코인 랭킹\r\n\r\n#k"
					msg += "#L12##e설명을 그만 듣는다#n\r\n"
					cm.sendOkS(msg, 4, 9010108);
				break;
				case 4:
					var msg = "#b주간 획득코인 랭킹#k 업무를 보고 싶으신가요?\r\n무엇을 도와 드릴까요?\r\n";
					msg += "#b#L3#현재 랭킹 확인\r\n";
					msg += "#L4#지난 주 랭킹 확인\r\n";
					msg += "#L5#지난 주 랭킹 보상 받기\r\n";
					cm.sendOkS(msg, 4, 9010108);
					break;
			}
		}
	} else if (status == 2) {
		switch (seld) {
			case 1:
			break;
			case 2:
				if (cm.getPlayer().getAllUnionCoin() < require) {
					cm.sendOkS("코인이 모자라신건 아닌가요?", 4, 9010108);
					cm.dispose();
					return;
				}
				if (cm.getPlayer().getAllUnion() < reqlevel) {
					cm.sendOkS("유니온 레벨이 모자라신건 아닌가요?", 4, 9010108);
					cm.dispose();
					return;
				}
				var msg = "짝짝짝!"+enter;
				msg += "#e메이플 유니온 등급#n이 올랐어요! 이제 더 많은 공격대원과 함께 더욱 빠르게 성장 하실 수 있어요!"+enter+enter;
				msg += "#e신규등급: #e<"+unionNext+">#k"+enter;
				msg += "투입가능 공격대원: #b"+nextAdd+"#k#n"+enter+enter;
				msg += "그럼 다음 등급까지 쭉쭉~ 성장하세요!";
				cm.getClient().setKeyValue("유니온코인", (cm.getPlayer().getAllUnionCoin() - require)+"");
				cm.getPlayer().setKeyValue(500629, "point", (cm.getPlayer().getKeyValue(500629, "point") - require) +"");
				sex();
				cm.sendOkS(msg, 4, 9010108);
				cm.dispose();
			break;
			case 3:
				cm.UnionRank();
				cm.dispose();
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				msg = "#e<메이플 유니온이란?>#n\r\n\r\n";
				msg += "#e메이플 유니온#n은 #b동일 월드 내 캐릭터들의 집단#k을 의미해요.\r\n"
				msg += "하지만 모든 캐릭터가 #b메이플 유니온#k에 소속될 순 없죠.\r\n"
				msg += "#r캐릭터 레벨 60이상/2차 전직을 마친 캐릭터#k여야 하죠.\r\n"
				msg += "그리고 #r동일 월드 내 40명이 넘는 캐릭터가 있는 경우,#k #b레벨 기준 상위 40명#k만 메이플 유니온 소속으로 인정 된답니다.\r\n\r\n"
				msg += "또한 #b제로#k의 경우 #r130레벨 이상의 레벨이 가장 높은 캐릭터\r\n1명#k만 메이플 유니온 소속으로 인정이 되죠."
				cm.sendNext(msg);
				break;
			case 7:
				msg = "#e<유니온 레벨>#n\r\n\r\n";
				msg += "#e유니온 레벨#n은 #b메이플 유니온#k에 소속된 캐릭터들의\r\n"
				msg += "#r레벨을 모두 합친 값#k을 의미해요.\r\n\r\n"
				msg += "유니온 레벨이 높아야 더 높은 #b유니온 등급#k으로 #r승급#k 할 수\r\n있고 더욱 강력한 메이플 유니온을 꾸릴 수 있게 되죠.\r\n\r\n"
				cm.sendNext(msg);
				break;
			case 8:
				msg = "#e<유니온 등급>#n\r\n\r\n";
				msg += "#e유니온 등급#n은 #b유니온 레벨#k이 특정 수치에 도달하면 올릴\r\n수 있는 #b성장의 척도#k예요.\r\n\r\n"
				msg += "#b유니온 등급#k이 높을수록 #r더 많은 공격대원#k을 #r더 넓은 전투지도#k에 배치 할 수 있게 되죠.\r\n"
				msg += "#b유니온 레벨#k을 일정 수준까지 달성하여 #b승급#k을 할 땐 #i4310229#\r\n#b#z4310229##k을 지불해야 한답니다.\r\n\r\n"
				msg += "#L8# #b등급별 필요 레벨 /지불 코인/ 공격대원 숫자 보기"
				cm.sendNext(msg);
				break;
			case 9:
				msg = "#e<공격대와 전투력>#n\r\n\r\n";
				msg += "#b공격대#k란 유니온 캐릭터 중#k #b전투지도에 배치된 캐릭터들의\r\n집단#k을 의미해요.\r\n\r\n"
				msg += "#b공격대원#k이 된 캐릭터들은 #r유니온 레이드#k에 참여하여 강력한#k\r\n적들과 전투를 벌여 #b유니온 코인#k을 수집하게 되죠.\r\n\r\n"
				msg += "또 공격대원들은 캐릭터 고유의 #b<공격대원 효과>#k와 전투지\r\n도 점령 상태에 따른 #b<공격대 점령효과>#k를 발동시켜 #r월드 내\r\n모든 캐릭터#k들에게 #b능력치 상승 효과를 가져다 준답니다."
				cm.sendNext(msg);
				break;
			case 10:
				msg = "#e<전투지도와 캐릭터 블록>#n\r\n\r\n";
				msg += "#b전투지도#k는 #r공격대원#k들을 배치하여 점령 할 수 있는 지도로\r\n"
				msg += "#b내부 8개#k, #r외부 8개#k #e총 16개의 지역#n으로 구성되어 있어요.\r\n"
				msg += "각 지역은 #b고유한 능력치#k를 보유하고 있어서 지역별 #r점령된 칸 수#k에 따라 능력치가 상승하게 되죠.\r\n\r\n"
				msg += "#b내부 8개 지역의 능력치#k는 원하는대로 #b변경 할 수 있고#k\r\n"
				msg += "#r외부 8개 지역의 능력치#k는 고정되어 있답니다."
				cm.sendNext(msg);
				break;
			case 11:
				msg = "#e<주간 획득 코인 랭킹>#n\r\n\r\n";
				msg += "#b주간 획득 코인 랭킹#k은 월드 내 모든 캐릭터가\r\n"
				msg += "#b매 주 월요일 00시 30분 부터#k #r일요일 23시 30분까지#k 획득한\r\n"
				msg += "#b유니온 코인#k의 수량을 기준으로 랭킹을 산출해요."
				cm.sendNext(msg);
				break;
			case 12:
				cm.dispose();
				break;
		}
	} else if (status == 3) {
		switch (seld) {
			case 6:
				msg = "#e<메이플 유니온이란?>#n\r\n\r\n";
				msg += "#e메이플 유니온#n에 소속된 캐릭터는 #r레벨#k에 따라 #b캐릭터 등급#k이 상승하게 된답니다.\r\n\r\n"
				msg += "#e<일반 캐릭터>\r\n"
				msg += "#bB(60)>A(100)>S(140)>SS(200)>SSS(250)#k\r\n"
				msg += "<제로>\r\n"
				msg += "#bB(130)>A(160)>S(180)>SS(200)>SSS(250)#k#n\r\n"
				cm.sendNextPrev(msg);
				break;
			case 8:
				msg = "#e<노비스 유니온>#n\r\n\r\n";
				msg += "#e1단계#n #bLv.   -    #r필요코인: 없음#k   #b공격대원: 9명#k\r\n"
				msg += "#e2단계#n #bLv.1000  #r필요코인: 120개#k   #b공격대원: 10명#k\r\n"
				msg += "#e3단계#n #bLv.1500  #r필요코인: 140개#k   #b공격대원: 11명#k\r\n"
				msg += "#e4단계#n #bLv.2000  #r필요코인: 150개#k   #b공격대원: 12명#k\r\n"
				msg += "#e5단계#n #bLv.2500  #r필요코인: 160개#k   #b공격대원: 13명#k\r\n"
				cm.sendNext(msg);
				break;
			case 9:
				msg = "#e<공격대와 전투력>#n\r\n\r\n";
				msg += "#b전투력#k은 캐릭터의 #r레벨#k과 #r스타포스 수치#k에 의해서 결정돼요.\r\n\r\n"
				msg += "특히 #b공격대원#k들의 전투력 총합을 #b공격대 전투력#k이라 하는데,"
				msg += "#b공격대 전투력#k이 높아야 #r유니온 레이드#k에서 적에게 더욱 많\r\n"
				msg += "은 데미지를 주게 되고 더욱 빨리 유니온 코인을 수집 할 수\r\n"
				msg += "있게 되죠."
				cm.sendNextPrev(msg);
				break;
			case 10:
				msg = "#e<전투지도와 캐릭터 블록>#n\r\n\r\n";
				msg += "#b전투지도#k에 캐릭터를 #e드래그 앤 드롭#n하면 캐릭터가 #b블록#k으로 표시되며 #b전투 지도#k의 일부분을 점령하게 돼요.\r\n\r\n"
				msg += "#e캐릭터 블록#n은 #b레벨#k과 #b직업 종류#k에 따라 #b기준 캐릭터 블록#k을 중심으로 그 모양이 각각 다르게 성장하죠."
				cm.sendNextPrev(msg);
				break;
			case 11:
				msg = "#e<주간 획득 코인 랭킹>#n\r\n\r\n";
				msg += "#b제일 마지막으로#k 주간 누적 코인을 갱신한 #b캐릭터 이름#k으로 랭킹이 등록이 되고, 그 다음 주가 되면 #r상위 1등부터\r\n100등 유니온에겐#k #b특별한 선물#k을 지급한답니다.\r\n"
				msg += "#b일일 퀘스트#k를 통해 획득한 코인도 누적되니까 자주 들어오는게 좋겠죠?"
				cm.sendNextPrev(msg);
				break;
			case 7:
				cm.dispose();
				cm.openNpc(9010108);
				break;
		}
	} else if (status == 4) {
		switch (seld) {
			case 8:
				msg = "#e<베테랑 유니온>#n\r\n\r\n";
				msg += "#e1단계#n #bLv.3000  #r필요코인: 170개#k   #b공격대원: 18명#k\r\n"
				msg += "#e2단계#n #bLv.3500  #r필요코인: 430개#k   #b공격대원: 19명#k\r\n"
				msg += "#e3단계#n #bLv.4000  #r필요코인: 450개#k   #b공격대원: 20명#k\r\n"
				msg += "#e4단계#n #bLv.4500  #r필요코인: 470개#k   #b공격대원: 21명#k\r\n"
				msg += "#e5단계#n #bLv.5000  #r필요코인: 490개#k   #b공격대원: 22명#k\r\n"
				cm.sendNextPrev(msg);
				break;
			case 10:
				msg = "#e<전투지도와 캐릭터 블록>#n\r\n\r\n";
				msg += "처음 #b전투지도#k에 캐릭터를 배치할 때는 #r기준 캐릭터 블록#k이 반드시 #b중앙 4곳 중 한 곳#k에 포함되어야 해요.\r\n\r\n"
				msg += "그 다음부터는 캐릭터 블록이 서로 닿아 있거나 중첩되게 다른 캐릭터들을 배치해 나갈 수 있죠. 캐릭터 블록을 #b뒤집거나 돌려서#k 원하는 모양으로 바꾸거나 #b우클릭으로 해제#k 할 수도 있답니다."
				cm.sendNextPrev(msg);
				break;
			case 6:
			case 9:
				cm.dispose();
				cm.openNpc(9010108);
				break;
		}
	} else if (status == 5) {
		switch (seld) {
			case 8:
				msg = "#e<마스터 유니온>#n\r\n\r\n";
				msg += "#e1단계#n #bLv.5500  #r필요코인: 510개#k   #b공격대원: 27명#k\r\n"
				msg += "#e2단계#n #bLv.6000  #r필요코인: 930개#k   #b공격대원: 28명#k\r\n"
				msg += "#e3단계#n #bLv.6500  #r필요코인: 960개#k   #b공격대원: 29명#k\r\n"
				msg += "#e4단계#n #bLv.7000  #r필요코인: 1000개#k   #b공격대원: 30명#k\r\n"
				msg += "#e5단계#n #bLv.7500  #r필요코인: 1030개#k   #b공격대원: 31명#k\r\n"
				cm.sendNextPrev(msg);
				break;
			case 10:
			case 11:
				cm.dispose();
				cm.openNpc(9010108);
				break;
		}
	} else if (status == 6) {
		switch (seld) {
			case 8:
				msg = "#e<그랜드 마스터 유니온>#n\r\n\r\n";
				msg += "#e1단계#n #bLv.8000  #r필요코인: 1060개#k   #b공격대원: 36명#k\r\n"
				msg += "#e2단계#n #bLv.8500  #r필요코인: 2200개#k   #b공격대원: 37명#k\r\n"
				msg += "#e3단계#n #bLv.9000  #r필요코인: 2300개#k   #b공격대원: 38명#k\r\n"
				msg += "#e4단계#n #bLv.9500  #r필요코인: 2350개#k   #b공격대원: 39명#k\r\n"
				msg += "#e5단계#n #bLv.10000  #r필요코인: 2400개#k   #b공격대원: 40명#k\r\n"
				cm.sendNextPrev(msg);
				break;
		}
	} else if (status == 7) {
		switch (seld) {
			case 8:
				cm.dispose();
				cm.openNpc(9010108);
				break;
		}
	}
}
var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	text = "#fc0xFFFF9933#Pixi 유저 #h 0##k님 어서오세요~ 언제나 환영합니다!\r\n\r\n" +
//	"#L99##d#e[서버 도움] : 서버의 기능을 알고싶어요!#l#n\r\n\r\n#k" +
	"#b#e[이동 기능] : 게임 진행을 위한 편리한 기능들#n\r\n#k" +
	"#L0#이동하기#l    #L6#보스이동#l    #L11#낚시하기#l    #L22#몬파하기#l\r\n\r\n" +
	"#b#e[추가 기능] : 5차 전직 시스템에 관련된 기능들#n\r\n#k" +
	"#L4#심볼얻기#l    #L5#매트릭스#l    #L9#오차전직#l    #L30#심볼확장#l\r\n\r\n" +
	"#b#e[상점 기능] : 여러가지 다양한 상점들#n\r\n#k" +
	"#L1#기본상점#l    #L12#환생상점#l    #L13#코인상점#l    #L14#후원상점#l\r\n\r\n" +
	"#b#e[기타 기능] : 그 외에 여러가지 유용한 것들#n\r\n#k" +
	"#L8#후원관련#l    #L7#보조무기#l    #L10#길드관련#l    #L2#믹스염색#l\r\n\r\n" +
	"#b#e[컨텐츠 기능] : 즐길 수 있는 다양한 재미#n\r\n#k" +
	"#L26#전구제거#l    #L15#끈기의숲#l    #L16#성형헤어#l    #L17#환생하기#l\r\n" +
	"#L18#인내의숲#l    #L19#어빌리티#l    #L20#무릉도장#l    #L21#닉변하기#l\r\n" +
	"#L3#검색캐시#l    #L23#도박하기#l    #L24#포켓슬롯#l    #L25#성배뽑기#l\r\n" +
	"#L28#훈장관련#l    #L27#랭킹관련#l    #L31#변생하기#l    #L32#조합하기#l\r\n";
	if (cm.getPlayer().isGM()) {
	text += "\r\n\r\n#b#e[운영자 기능] : 운영자 전용 추가 기능입니다.#n\r\n#k" +
	"#L100#환생변경#l    #L101#아이템값#l	#L102#키벨류값#l    #L103#후원결제#l" +
	"#L104#홍보보상#l";
	}
	cm.getPlayer().forceCompleteQuest(16013);
	cm.sendIllustSimple(text,0x24);
    } else if (status == 1) {
	cm.dispose();
	switch (selection) {
		case 99:
//		cm.openNpc("child00");
		break;
		case 100:
		cm.getPlayer().setKeyValue(1, "reborns", "1000000");
		break;
		case 101:
		cm.openNpc("test1");
		break;
		case 102:
		cm.openNpc(9900002);
		break;
		case 103:
		cm.openNpc(9900003);
		break;
		case 104:
		cm.openNpc(9900000);
		break;
		case 0:
		cm.openNpc("victoria_taxi");
		break;
		case 1:
		cm.openNpc("npc_2520001");
		break;
		case 2:
		cm.openNpc(9000161);
		break;
		case 3:
		cm.openNpc("levelUP2");
		break;
		case 4:
		cm.openNpc(1052232);
		break;
		case 5:
		cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.openUI(1131));
		break;
		case 6:
		cm.openNpc(9020011);
		break;
		case 7:
		cm.openNpc("2011Haloween");
		break;
		case 8:
		cm.openNpc(1540405);
		break;
		case 9:
		if (cm.getPlayer().getLevel() < 200) {
			cm.cm.sendOkS("아직 레벨이 부족합니다. 200레벨을 달성해주세요.", 1, true);
		} else if (cm.getPlayer().getQuest(Packages.server.quest.MapleQuest.getInstance(1465)).getStatus() == 2) {
			cm.cm.sendOkS("이미 5차전직을 완료하였습니다.", 1, true);
		} else {
			cm.forceCompleteQuest(1465);
			cm.cm.sendOkS("5차 전직이 완료되었습니다.", 3, true);
		}
		break;
		case 10:
		cm.openNpc(2010008);
		break;
		case 11:
		cm.warp(3000400, 0);
		break;
		case 12:
		cm.openNpc(9000110);
		break;
		case 13:
		cm.openNpc(1530060);
		break;
		case 14:
		cm.openShop(5);
		break;
		case 15:
		cm.warp(910530200, 0);
		break;
		case 16:
		cm.openNpc(1012001);
		break;
		case 17:
		cm.openNpc(3003278);
		break;
		case 18:
		cm.warp(910130100, 0);
		break;
		case 19:
		cm.openNpc(9000100);
		break;
		case 20:
		cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.closeUI(62));
		cm.warp(925020000, 0);
		break;
		case 21:
		cm.openNpc(1530040);
		break;
		case 22:
		cm.openNpc("mParkShuttle");
		break;
		case 23:
		cm.openNpc(2074019);
		break;
		case 24:
		cm.forceCompleteQuest(6500);
		cm.sendOk("포켓슬롯이 개방되었습니다.");
		break;
		case 25:
		cm.openNpc(9062000);
		break;
		case 26:
		cm.sendOk("각 전구를 클릭하시면 자동으로 퀘스트가 완료됩니다.");
		break;
		case 27:
		cm.openNpc(2007);
		break;
		case 28:
		cm.openNpc(9000066);
		break;
		case 29:
		cm.openNpc(9062002);
		break;
		case 30:
		if (cm.getPlayer().getLevel() < 225) {
			cm.cm.sendOkS("아직 레벨이 부족합니다. 225레벨을 달성해주세요.", 1, true);
		} else if (cm.getPlayer().getQuest(Packages.server.quest.MapleQuest.getInstance(1465)).getStatus() != 2) {
			cm.cm.sendOkS("5차전직을 완료해 주시기 바랍니다.", 1, true);
		} else if (cm.getPlayer().getQuest(Packages.server.quest.MapleQuest.getInstance(34478)).getStatus() == 2) {
			cm.cm.sendOkS("이미 확장되었습니다.", 1, true);
		} else {
			cm.forceCompleteQuest(34478);
			cm.cm.sendOkS("심볼 확장에 성공하였습니다.", 3, true);
		}
		break;
		case 31:
		cm.openNpc("gateKeeper");
		break;
		case 32:
		cm.openNpc(1530543);
		break;
	}
    }
}


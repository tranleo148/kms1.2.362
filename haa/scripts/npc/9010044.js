/*
제작자 : qudtlstorl79@nate.com
*/

importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);

파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";

펫 = "#fUI/CashShop.img/CashItem_label/5#";
펫1 = "#fUI/CashShop.img/CashItem_label/8#";
길드 = "#fUI/Basic.img/theblackcoin/14#";
젬스톤 = "#fUI/Basic.img/theblackcoin/19#";
이름변경 = "#fUI/Basic.img/theblackcoin/20#";
자유전직 = "#fUI/Basic.img/theblackcoin/21#";
창고 = "#fUI/Basic.img/theblackcoin/13#";
어빌리티 = "#fUI/Basic.img/theblackcoin/22#";
포인트 = "#fUI/Basic.img/theblackcoin/7#";
포켓 = "#fUI/Basic.img/theblackcoin/23#";
랭킹 = "#fUI/Basic.img/theblackcoin/24#";
템버리기 = "#fUI/Basic.img/theblackcoin/25#";
편의 = "#fUI/Basic.img/theblack/2#";
추천인 = "#fUI/Basic.img/theblackcoin/41#";
상점 = "#fUI/Basic.img/theblack/1#";
검은마법사 = "#fUI/Basic.img/theblackcoin/42#";
위장색 = "#fUI/Basic.img/theblackcoin/43#";
정령 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";
var status = -1;

function start() {
    action(1, 0, 0);
}

/*
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(),cm.getPlayer(), false); 캐시샵
*/

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var choose = "#fn나눔고딕##fs11##L0##fc0xFFFF3636#" + 템버리기 + " 아이템 버리기#l";
        choose += "#L1##fc0xFFFF5E00#" + 랭킹 + " 각종랭킹 확인#l";
        choose += "#L2##fc0xFFE0B94F#" + 창고 + " 아이템 보관함#l\r\n";
        choose += "#L3##fc0xFF626262#" + 길드 + " 길드 제작하기#l";
        choose += "#L4##fc0xFFF361A6#" + 어빌리티 + " 어빌리티 개방#l";
        choose += "#L5##fc0xFF6799FF#" + 포켓 + " 포켓슬롯 개방#l\r\n";
        choose += "#L6##fc0xFF6B66FF#" + 펫 + " 펫 분양 받기#l";
        choose += "#L10##fc0xFF8041D9#" + 추천인 + " 추천인 시스템#l";
        //choose += "#L12##fc0xFF00B700#" + 정령 + " 정령코인 교환#l\r\n";
        choose += "#L13##fc0xFF7112FF#" + 위장색 + " 딜량체크 시스템#l\r\n\r\n";
		
        choose += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        choose += "#L7##fc0xFF6B66FF#" + 젬스톤 + " V 코어 매트릭스#k를 이용하겠습니다.\r\n#l";
        choose += "#L8##fc0xFF8C8C8C#" + 자유전직 + " 자유전직 시스템#k을 이용하겠습니다.\r\n#l";
        choose += "#L9##fc0xFFF29661#" + 이름변경 + " 캐릭터 이름#k을 변경하겠습니다.\r\n#l";
        choose += "#L80##fc0xFFF361A6#" + 펫1 + " 코디시스템#k을 이용하겠습니다.\r\n#l";
        cm.sendOkS(choose, 0x4);

    } else if (status == 1) {
        if (selection == 0) { // 아이템 버리기
            cm.dispose();
            cm.openNpc(1012121);
        } else if (selection == 1) { // 각종 랭킹
            cm.dispose();
            cm.openNpc(9076004);
        } else if (selection == 2) { // 창고
            cm.dispose();
            cm.openNpc(1002005);
        } else if (selection == 80) { // 창고
            cm.dispose();
            cm.openNpc(9401408);
        } else if (selection == 3) { // 길드
            cm.dispose();
            cm.warp(200000301);
        } else if (selection == 4) { // 어빌리티
            if (cm.getPlayer().getInnerSkills().size() > 2) {
                cm.sendOk("#fs11#" + 검정 + "이미 어빌리티가 개방되어 있다네!#k", 9401232);
                cm.dispose();
                return;
            }
            cm.forceCompleteQuest(12394);
            cm.forceCompleteQuest(12395);
            cm.forceCompleteQuest(12396);
            cm.setInnerStats(1);
            cm.setInnerStats(2);
            cm.setInnerStats(3);
            cm.fakeRelog();
            cm.updateChar();
            cm.sendOk("#fs11#" + 검정 + "자네를 위해 어빌리티를 개방해주었다네!", 9401232);
            cm.dispose();
        } else if (selection == 5) { // 포켓
            cm.getPlayer().forceCompleteQuest(6500);
            cm.sendOk("#fs11#" + 검정 + "자네를 위해 포켓을 개방해주었다네!", 9401232);
            cm.dispose();
        } else if (selection == 6) { // 펫 교환
				cm.dispose();
				cm.openNpc(1530330);
        } else if (selection == 7) { //V 코어 매트릭스
            cm.dispose();
            cm.openNpc(1540945);
        } else if (selection == 8) { //자유 전직
            cm.dispose();
            cm.sendUI(164);
        } else if (selection == 9) { //캐릭터 이름 변경
            cm.dispose();
            cm.openNpc(9062010);
		} else if (selection == 555) { //후원 시스템
            cm.dispose();
            cm.openNpc(3003168);
		} else if (selection == 556) { //홍보 시스템
            cm.dispose();
            cm.openNpc(3003167);
        } else if (selection == 10) { //추천인 시스템
            cm.dispose();
            cm.openNpc(9062454);
        } else if (selection == 11) { //검은마법사 재료 교환
            cm.dispose();
            cm.openNpc(3003811);
        } else if (selection == 12) {
            cm.dispose();
            cm.openNpc(9062518);
        } else if (selection == 13) {
            cm.dispose();
            cm.openNpc(1105008);
        } else if (selection == 14) {
            if (cm.getPlayer().getLevel() >= 200) {
                cm.dispose();
                cm.openShop(33);
            }
        } else if (selection == 999) {
            cm.dispose();
            cm.openNpc(9250022);
        }
    } else if (status == 2) {
        if (selection == 0) {
            cm.dispose();
            cm.openNpc(2074149);
        } else if (selection == 1) {
            cm.dispose();
            cm.openNpc(2074150);
        }
    }
}
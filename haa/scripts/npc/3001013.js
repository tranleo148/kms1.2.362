


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	화이트 에 의해 만들어 졌습니다.

	엔피시아이디 : 2004

	엔피시 이름 : 토드

	엔피시가 있는 맵 : The Black : Night Festival (100000000)

	엔피시 설명 : MISSINGNO


*/
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
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
        if (cm.getClient().getQuestStatus(50004) == 1 && cm.getClient().getCustomKeyValue(50004, "1") != 1) {
            cm.getClient().setCustomKeyValue(50004, "1", "1");
        }
        cm.Entertuto(true);
    } else if (status == 1) {
        cm.sendScreenText("#fs25##fc0xFF6B66FF#황혼 시스템#k에 대해 설명드리겠습니다.", false);
    } else if (status == 2) {
        cm.sendScreenText("#fs17##i1162000# #z1162000#　　　　　　　　　　　　　　　　　　　　　　　　　#i1162001# #z1162001#　　　　　　　　　　　　　　　　　　　　　　　　  #i1162002# #z1162002#", false);
    } else if (status == 3) {
        cm.sendScreenText("#fc0xFF6B66FF#황혼 아이템#k은 총 #fc0xFF6B66FF#3가지#k로 나뉘어져 있습니다.", true);
    } else if (status == 4) {
        cm.sendScreenText("#fs25##i1162002# #fc0xFFF2CB61#황혼의 지배자란?#k", false);
    } else if (status == 5) {
        cm.sendScreenText("#fs17#황혼의 지배자는 #fc0xFF6799FF#포켓 아이템#k 입니다.", false);
    } else if (status == 6) {
        cm.sendScreenText("#fc0xFFFFBB00#황혼 옵션#k 이라는 고유 옵션을 가지고 있습니다.", false);
    } else if (status == 7) {
        cm.sendScreenText("#fc0xFF6B66FF#포켓 아이템#k으로 분류되며, #fc0xFFF15F5F#몬스터 리젠률#k과 #fc0xFFF15F5F#크리티컬 데미지 상승#k 효과를 가지고 있습니다.", true);
    } else if (status == 8) {
        cm.sendScreenText("#fs25##i1162000# #fc0xFF6B66FF##z1162000##k 제작에 대해 설명 드리겠습니다.", false);
    } else if (status == 9) {
        cm.sendScreenText("#fs17#우선, 지배자를 제작하기 위해선 #i4310007# #fc0xFF6B66FF##z4310007##k가 필요합니다.", false);
    } else if (status == 10) {
        cm.sendScreenText("#z4310006#을 #fc0xFFF29661#정제#k시, 일정 확률로 정수 획득이 가능합니다.", false);
    } else if (status == 11) {
        cm.sendScreenText("#i4310009# #fc0xFFFAED7D##z4310009##k으로 제작 확률을 상승시킬 수 있습니다.", true);
    } else if (status == 12) {
        cm.sendScreenText("#fs25##i1162002# #fc0xFF6B66FF##z1162002##k 제작에 대해 설명 드리겠습니다.", false);
    } else if (status == 13) {
        cm.sendScreenText("#fs17##fc0xFF6B66FF#지배자 (흑)#k 을 제작하기 위해선 #fc0xFFCC3D3D#지배자 (홍)#k 이 필요합니다.", false);
    } else if (status == 14) {
        cm.sendScreenText("#fc0xFFCC3D3D#지배자 (홍)#k 아이템은 #fc0xFFF2CB61#골드 애플#k 에서 일정 확률로 획득이 가능합니다.", false);
    } else if (status == 15) {
        cm.sendScreenText("#fc0xFF6B66FF#황혼의 지배자 (흑)#k은 #fc0xFFFFBB00#황혼 옵션#k이 한줄이 더 개방되어,", false);
    } else if (status == 16) {
        cm.sendScreenText("#fc0xFF8041D9#잠재능력 개방#k 옵션이 추가됩니다.", false);
    } else if (status == 17) {
        cm.sendScreenText("#fc0xFF6B66FF#지배자 (흑)#k 제작에 #r실패#k 시, #i4310008# #fc0xFFD5D5D5##z4310008##k을 획득합니다.", false);
    } else if (status == 18) {
        cm.sendScreenText("#fc0xFFD5D5D5##z4310008# 5개를 모아 #fc0xFF6B66FF##z1162002##k을 #r100%#k 확률로 제작이 가능합니다.", true);
    } else if (status == 19) {
        cm.Endtuto()
        cm.dispose();
    }
}

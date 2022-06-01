var status = -1;

var item = [
	[5068300, "위습의 원더베리", 1, 1000],
	[5068301, "블랙베리", 1, 10000],
	[5062006, "플래티넘 언리미티드 큐브", 1, 1500],
	[5069100, "루나 크리스탈", 1, 2000],
	[5060048, "골드애플", 1, 2000],
	[2049361, "놀라운 장비강화 주문서", 1, 3000],
	[2049136, "놀라운 블랙 혼돈 줌서", 1, 3000],
	[2046025, "블랙 한손 무기 공", 1, 3500],
	[2046026, "블랙 한손 무기 마", 1, 3500],
	[2046119, "블랙 두손 무기 공", 1, 3500],
	[2430030, "보스 입장 초기화+", 1, 4000],
	[2049704, "레전드리 잠재 부여 주문서", 1, 5000],
	[2430034, "안드로이드 각인서", 1, 8000],
	[4033449, "하양 마정석", 1, 15000],
	[4036661, "돌림판 고급 이용권", 1, 10000],
	[2430067, "클론아바타 벨류",1, 10000],
	[2439959, "아케인셰이드 방어구 선택", 1, 10000],
	[2630782, "아케인셰이드 무기 선택", 1, 15000],
	[4033450, "검정 마정석", 1, 15000],
	[2439960, "SPACE 머리 구매x수정중", 1, 15000],
	[1042300, "SPACE 상의 구매x수정중", 1, 25000],
	[1062194, "SPACE 하의 구매x수정중", 1, 35000],
	[2430028, "스네이크 헌터 악세서리 교환권", 1, 35000],
	[2430068, "클론아바타 스페셜 벨류팩", 1, 50000],
	[2633336, "어센틱심볼 : 아르크스 만랩 교환권", 1, 100000],
	[2633616, "어센틱심볼 : 세르니움 만랩 교환권", 1, 100000]];

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
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
    }
    if (status == 0) {
        chat = "#fs11#";
        chat = "#fs11#현재 #h0#님의 후원포인트 : #r" + cm.getPlayer().getitem() + "#k\r\n스페이스템 수정중이니 절대구매하지마세요 복구절대안해드립니다.\r\n";
        for (i = 0; i < item.length; i++) {
            chat += "#L" + i + "##i" + item[i][0] + "# #d" + item[i][1] + "#k, #b후원 포인트#k #e#r" + item[i][3] + "#k#n\r\n";
        }
        cm.sendSimple(chat);

    } else if (status == 1) {
        if (selection == 25) {
            cm.dispose();
            cm.openNpc(1031001);

        } else if (selection == 26) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 3003273, "iicashItemsearch");
        } else {
            if (cm.getPlayer().getDonationPoint() >= item[selection][3]) {
                cm.gainItem(item[selection][0], item[selection][2]);
                cm.getPlayer().gainDonationPoint(-item[selection][3]);
                cm.sendOk("아이템을 정상적으로 교환했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("후원 포인트가 부족합니다.");
                cm.dispose();
            }
        }
    }
}
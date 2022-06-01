var status = -1;

var item = [
    [5060048, "골드 애플", 1, 3000],
    [4310016, "블랙 페스티벌 주화 10개(10억)", 10, 3000],
	[2049136, "놀라운 블랙 혼돈의 주문서", 1, 3500],
    [2046025, "블랙 한손공 주문서", 1, 4500],
    [2046026, "블랙 한손마 주문서", 1, 4500],
    [2046119, "블랙 두손공 주문서", 1, 4500],
    [2450064, "경험치 2배 쿠폰", 1, 1000],
    [2450134, "경험치 3배 쿠폰", 1, 3000],
    [5121060, "경험치 뿌리기", 1, 3000],
    [2435719, "코어 젬스톤 50개", 50, 1000],
    [5068300, "위습의 원더베리", 1, 3500],
    [5068300, "위습의 원더베리 10개", 10, 35000],
    [5069100, "루나 크리스탈", 1, 2000],
    [5069100, "루나 크리스탈 5개", 5, 10000],
	[2630755, "강환불 패키지(10개)", 1, 3500],
    [2439653, "영환불 패키지(10개)", 1, 5000],
    [2630551, "블랙 큐브 100개 패키지", 1, 1000],
    [2049360, "놀라운 장비강화 주문서(10장)", 10, 5000],
    [5064400, "리턴 스크롤 30장", 30, 3000],
	[2439959, "아케인셰이드 방어구 선택 상자", 1, 15000],
	[2630782, "아케인셰이드 무기 선택 상자", 1, 20000],
	[2630548, "유니크 강화석", 1, 30000],
    [5539002, "놀라운 블랙 혼돈의 주문서 10장", 10, 30000],
    [4034803, "닉네임 변경권", 1, 20000]];

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
        chat = "#fs11#현재 #h0#님의 홍보포인트 : #r" + cm.getPlayer().getHPoint() + "#k\r\n";
        for (i = 0; i < item.length; i++) {
            chat += "#L" + i + "##i" + item[i][0] + "# #d" + item[i][1] + "#k, #b홍보 포인트#k #e#r" + item[i][3] + "#k#n\r\n";
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
            if (cm.getPlayer().getHPoint() >= item[selection][3]) {
                cm.gainItem(item[selection][0], item[selection][2]);
                cm.getPlayer().gainHPoint(-item[selection][3]);
                cm.sendOk("아이템을 정상적으로 교환했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("홍보 포인트가 부족합니다.");
                cm.dispose();
            }
        }
    }
}
importPackage(Packages.constants);


var status = -1;

var 별 = "#fUI/FarmUI.img/objectStatus/star/whole#";
var HOT = "#fUI/CashShop.img/CSEffect/hot/0#";
var NEW = "#fUI/CashShop.img/CSEffect/new/0#";


function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {

        var choose = "광장으로 돌아가시겠습니까?\r\n";
        choose += "#L1##e이동#n하겠습니다.#k#k\r\n";
        cm.sendSimple(choose);

    } else if (selection == 1) {//혼텔
        cm.dispose();
        cm.warp(100000000);
    } else if (selection == 2) {//힐라
        cm.dispose();
cm.warpParty(240040700);
    } else if (selection == 3) {//반 레온
	cm.dispose();
cm.warpParty(262000000);
    } else if (selection == 4) {//아카이럼
	cm.dispose();
cm.warpParty(211070000);
    } else if (selection == 5) {//시그너스
	cm.dispose();
cm.warpParty(272000000);
    } else if (selection == 6) {//매그너스
	cm.dispose();
cm.warpParty(270040000);
    } else if (selection == 7) {//칸스핑크빈
	cm.dispose();
cm.warpParty(271041000);
    } else if (selection == 8) {//칸스블러디퀸
	cm.dispose();
cm.warpParty(105200000);
    } else if (selection == 9) {//칸스벨룸
	cm.dispose();
cm.warpParty(220080000);
    } else if (selection == 10) {//스우
	cm.dispose();
cm.warpParty(401060000);
    } else if (selection == 11) {//자쿰
	cm.dispose();
cm.warpParty(350060300);
    } else if (selection == 12) {//카오스피에르
	cm.dispose();
cm.warpParty(105300303);
    } else if (selection == 13) {//카오스반반
	cm.dispose();
cm.warpParty(450004000);
    } else if (selection == 14) {//루시드
	cm.dispose();
cm.warpParty(450007240);
    } else if (selection == 24) {//도로시
	cm.dispose();
        cm.openNpc(2540010);
    } else if (selection == 25) {//불꽃늑대
	cm.dispose();
        cm.openNpc(9001059);
    } else if (selection == 26) {//데미안
	cm.dispose();
        cm.openNpc(1530621);  
    } else if (selection == 99) {//카웅
	cm.dispose();
        cm.openNpc(2050005);
    } else if (selection == 101) {//크로스
	cm.dispose();
        cm.openNpc(9073003); 
    }
}
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

        var choose = "#fs11##fc0xFF050099##k\r\n";
        choose += "  #fs12##e[EASY]#n\r\n";
        choose += "#fs11##L1##fUI/UIWindow2.img/MobGage/Mob/8800100##e 자쿰#n#l\r\n";
        choose += "#L99##fUI/UIWindow2.img/MobGage/Mob/8880200##e 카웅#n#l\r\n";
        choose += "#L2##fUI/UIWindow2.img/MobGage/Mob/8810122##e 혼테일#n#l\r\n";
        choose += "#L3##fUI/UIWindow2.img/MobGage/Mob/8870000##e 힐라#n#l\r\n";
        choose += "#L4##fUI/UIWindow2.img/MobGage/Mob/8840014##e 반레온#n#l\r\n";
        choose += "#L5##fUI/UIWindow2.img/MobGage/Mob/8860000##e 아카이럼#n#l\r\n";
        choose += "#L6##fUI/UIWindow2.img/MobGage/Mob/8820001##e 핑크빈#n#l\r\n";
        choose += "#L7##fUI/UIWindow2.img/MobGage/Mob/8850011##e 시그너스#n#l\r\n\r\n";
        choose += "  #fs12##b#e[NORMAL]#n#k\r\n";
        choose += "#fs11##L10##fUI/UIWindow2.img/MobGage/Mob/8880002##b#e 매그너스#n#l\r\n";
        choose += "#L8##fUI/UIWindow2.img/MobGage/Mob/8930100##e 루타비스#n#l\r\n";
        choose += "#L9##fUI/UIWindow2.img/MobGage/Mob/8500012##e 파풀라투스#n#l\r\n";
        choose += "#L11##fUI/UIWindow2.img/MobGage/Mob/8950102##e 스우#n#l\r\n";
        choose += "#L12##fUI/UIWindow2.img/MobGage/Mob/8880111##e 데미안#n#l\r\n";
        choose += "#L13##fUI/UIWindow2.img/MobGage/Mob/8880150##e 루시드#n#l\r\n";
        choose += "#L14##fUI/UIWindow2.img/MobGage/Mob/8880342##e 윌#n#l#k\r\n\r\n";
        choose += "  #fs12##r#e[HARD]#n#k\r\n";
        choose += "#L21##fUI/UIWindow2.img/MobGage/Mob/8800100##e 우르스#n#l\r\n";
        choose += "#L103##fUI/UIWindow2.img/MobGage/Mob/8880405##e 진힐라#n#l\r\n";
        choose += "#L105##fUI/UIWindow2.img/MobGage/Mob/8645009##e 듄켈#n#l\r\n";
        choose += "#L98##fUI/UIWindow2.img/MobGage/Mob/8644655##e 더스크#n#l\r\n";
        choose += "#L97##fUI/UIWindow2.img/MobGage/Mob/8880502##e 검은 마법사#n#l#k\r\n";
        choose += "#L106##fUI/UIWindow2.img/MobGage/Mob/8880600##e 세렌#n#l\r\n\r\n";
        choose += "  #fs12##fc0xFFF781D8##e[EXTREME]#n#k\r\n";
        //choose += "#fs11##L100##e#d 크로스#n#l\r\n";
       // choose += "#fs11##L101##e#d 도로시#n#l\r\n";
        cm.sendSimple(choose);

    } else if (selection == 1) {//혼텔
        cm.dispose();
cm.warpParty(211042300);
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
cm.warpParty(272020110);
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
    } else if (selection == 12) {//칸스피에르
	cm.dispose();
cm.warpParty(105300303);
    } else if (selection == 13) {//칸스반반
cm.openNpc(3003208);
	cm.dispose();
    } else if (selection == 14) {//루시드
	cm.dispose();
cm.warpParty(450007240);
    } else if (selection == 20) {//불꽃늑대
	cm.dispose();
             cm.openNpc(9120012);
    } else if (selection == 21) {//우르스
	cm.dispose();
cm.warpParty(970072200);
  } else if (selection == 103) {//진힐라
	cm.dispose();
cm.warpParty(450011990);
  } else if (selection == 100) {//크로스
	cm.dispose();
cm.warpParty(940500100);
  } else if (selection == 97) {//검은마법사
	cm.dispose();
cm.warpParty(450012500);
  } else if (selection == 101) {//도로시
	cm.dispose();
cm.warpParty(992049000);
  } else if (selection == 102) {//진격거
	cm.dispose();
cm.warpParty(814030000);
    } else if (selection == 22) {//자쿰
	cm.dispose();
cm.openNpc(9000192);
    } else if (selection == 23) {//칸스피에르
	cm.dispose();
cm.warpParty(910810000);
    } else if (selection == 24) {//칸스반반
cm.openNpc(3003208);
	cm.dispose();
    } else if (selection == 25) {//루시드
cm.warpParty(450004000);
	cm.dispose();
    } else if (selection == 105) {//듄켈
	cm.dispose();
cm.warpParty(450012200);
    } else if (selection == 106) {//듄켈
	cm.dispose();
cm.warpParty(410000670);
cm.openNpc(9000192);
   } else if (selection == 98) {//더스크
	cm.dispose();
cm.warpParty(450009301);
   } else if (selection == 99) {//카웅
cm.warpParty(221030900);
	cm.dispose();
} else if (selection == 26) {//루시드
	cm.dispose();
cm.openNpc(9000192);
    }
}
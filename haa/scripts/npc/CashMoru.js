


/*

   * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

   * (Guardian Project Development Source Script)

   해피 에 의해 만들어 졌습니다.

   엔피시아이디 : 9900006

   엔피시 이름 : 테스트녀2

   엔피시가 있는 맵 : 팡팡 터지는 재미! : 플레르 테마파크 (100000000)

   엔피시 설명 : MISSINGNO


*/
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
검정 = "#fc0xFF191919#";
보라색 = "#fc0xFF8041D9#"
프레스티지 = [1007002, 1108002, 1088802, 1058002, 1078002, 1118802, 1012802, 1022802, 1032802]

마라벨 = [1003955, 1050299, 1051366, 1082555, 1072860, 1000070, 1001093, 1050312, 1051384, 1082580, 1072908, 1000076, 1001098, 1050339, 1051408, 1102729, 1072978, 1004450, 1050356, 1051426, 1102809, 1073041, 1004591, 1004592, 1050385, 1051455, 1102858, 1070071, 1004777, 1050427, 1051495, 1102932, 1070080, 1071097, 1004897, 1004898, 1050445, 1051513, 1102988, 1070086, 1071103, 1005037, 1005038, 1050472, 1051539, 1103053, 1070091, 1071108, 1005209, 1005210, 1050497, 1051565, 1103126, 1103127, 1070100, 1071117, 1005356, 1050520, 1051589, 1103187, 1070111, 1071127, 1005495, 1050542, 1051613, 1103241, 1073425, 1005619, 1051638, 1050566, 1073484, 1103301, 1032799, 1032800, 1032801, 1032802, 1032803, 1012949, 1012950, 1012951, 1012952, 1012953, 1022699, 1022700, 1022701, 1022702, 1022703]
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        a = 0;
        구분 = 0;
        inz = cm.getInventory(6)
        txt = "#fs11#" + 검정 + "현재 자네가 보유하고 있는 #b치장 아이템#k" + 검정 + "이라네, #r모루#k" + 검정 + "하고 싶은 아이템을 선택해보게! 아참, 모루를 하기 위해선 #b3,000만 메소#k" + 검정 + "가 필요하다네 크크.\r\n#r#r마스터라벨 아이템과 클론 아바타만 모루가 가능하며, 무기는 모루 외형이 불가능합니다.#k#b#fs11#\r\n";
        txt += "#fc0xFFD5D5D5#────────────────────────────#k#fs11\r\n";
        for (w = 0; w < inz.getSlotLimit(); w++) {
            if (!inz.getItem(w)) {
                continue;
            }
            for (mb = 0; mb < 마라벨.length - 1; mb++) {
                mbitem = 마라벨[mb];
                if (inz.getItem(w).getItemId() == mbitem) {
                    if (inz.getItem(w).getMoru() > 0) {
                        itemid = ((Math.floor(inz.getItem(w).getItemId() / 10000)) * 10000) + inz.getItem(w).getMoru();
                        txt += "#L" + w + "# #i" + inz.getItem(w).getItemId() + "#  " + 보라색 + "[ #i" + itemid + "# ]  " + 보라색 + "#t" + inz.getItem(w).getItemId() + "# #r(모루)#k\r\n";
                        a++;
                        break;
                    } else {
                        txt += "#L" + w + "##i" + inz.getItem(w).getItemId() + ":# " + 보라색 + "#t" + inz.getItem(w).getItemId() + "##l\r\n";
                        a++;
                        break;
                    }
                }
            }
        }
        if (a == 0) {
            cm.sendOkS("#fs11#자네는 인벤토리 창에 아이템이 없는거 같군, 마스터라벨 아이템과 클론 아바타만 이용이 가능하다네! 크크.", 0x04, 9401232);
            cm.dispose();
        } else {
            cm.sendSimpleS(txt, 0x04, 9401232);
        }
    } else if (status == 1) {
        if (cm.getPlayer().getMeso() < 30000000) {
            cm.sendOkS("#fs11#" + 검정 + "모루를 하기 위한 비용 #r30,000,000 메소#k" + 검정 + "가 부족하다네.", 0x04, 9401232);
            cm.dispose();
            return;
        }
        선택한탬 = inz.getItem(selection);
        txt = "#fs11#" + 검정 + "모루할 아이템을 선택했군! 장비 아이템과 치장 아이템에서\r\n#r외형을 변경할 아이템#k" + 검정 + "을 선택해보게. 크크.\r\n"
        txt += "#fc0xFFD5D5D5#────────────────────────────#k#fs11\r\n";
        txt += "\r\n" + 보라색 + "선택한 아이템 [ #i" + 선택한탬.getItemId() + "# #t" + 선택한탬.getItemId() + "##l]\r\n#k\r\n\r\n#L1##b장비 인벤토리\r\n#L2#치장 인벤토리"
        cm.sendSimpleS(txt, 0x04, 9401232);
    } else if (status == 2) {
        if (selection == 1) {
            mb = 0;
            for (mb = 0; mb < 마라벨.length; mb++) {
                if (선택한탬.getItemId() == 마라벨[mb]) {
                    check = 1;
                    break;
                }
            }
            if (check == 0) {
                cm.sendOkS("#fs11##r마스터라벨 아이템과 클론 아바타#k만 가능하다네.", 0x04, 9401232);
                cm.dispose();
                return;
            }
        }
        선택 = selection;
        txt = "#fs11#" + 보라색 + "선택한 아이템 [ #i" + 선택한탬.getItemId() + "# #t" + 선택한탬.getItemId() + "##l]#fs11##b\r\n외형으로 등록할 아이템을 선택해주세요.\r\n\r\n"
        if (선택 == 2) {
            inz = cm.getInventory(6)
            for (w = 0; w < inz.getSlotLimit(); w++) {
                if (!inz.getItem(w)) {
                    continue;
                }
                if (Math.floor(inz.getItem(w).getItemId() / 10000) == Math.floor(선택한탬.getItemId() / 10000)) {
                    if (inz.getItem(w).getMoru() > 0) {
                        itemid = ((Math.floor(inz.getItem(w).getItemId() / 10000)) * 10000) + inz.getItem(w).getMoru();
                        txt += "#L" + w + "# #i" + inz.getItem(w).getItemId() + "#  " + 보라색 + " [ #i" + itemid + "#]  #t" + inz.getItem(w).getItemId() + "# #r(모루)#k\r\n";
                    } else {
                        txt += "#L" + w + "##i" + inz.getItem(w).getItemId() + ":# " + 보라색 + "#t" + inz.getItem(w).getItemId() + "##l\r\n";
                    }
                }
            }
        } else {
            구분 = 1
            inz = cm.getInventory(1)
            txt += "#fs11##r장비 아이템을 모루 할 경우엔 다른 아이템으로 장착을 한 후 그 아이템을 장착 해제 해주시길 바랍니다.#b\r\n\r\n"
            for (w = 0; w < inz.getSlotLimit(); w++) {
                if (!inz.getItem(w)) {
                    continue;
                }
                if (Math.floor(inz.getItem(w).getItemId() / 10000) == Math.floor(선택한탬.getItemId() / 10000)) {
                    if (inz.getItem(w).getMoru() > 0) {
                        itemid = ((Math.floor(inz.getItem(w).getItemId() / 10000)) * 10000) + inz.getItem(w).getMoru();
                        txt += "#L" + w + "# #i" + inz.getItem(w).getItemId() + "#  [ #i" + itemid + "# ]  #t" + inz.getItem(w).getItemId() + "# #r(모루)#k#b\r\n";
                    } else {
                        txt += "#L" + w + "##i" + inz.getItem(w).getItemId() + ":# #t" + inz.getItem(w).getItemId() + "##l\r\n";
                    }
                }
            }
        }
        cm.sendSimple(txt);
    } else if (status == 3) {
        모루탬 = inz.getItem(selection);
        txt = "";
        if (모루탬.getMoru() > 0) {
            itemid = ((Math.floor(모루탬.getItemId() / 10000)) * 10000) + 모루탬.getMoru();
            txt += "#fs11##b모루할 아이템 [ #i" + 선택한탬.getItemId() + "# #t" + 선택한탬.getItemId() + "##l]\r\n#r외형 아이템 [ #i" + itemid + "# #t" + itemid + "##l]\r\n\r\n"
        } else {
            txt += "#fs11##b모루할 아이템 [ #i" + 선택한탬.getItemId() + "# #t" + 선택한탬.getItemId() + "##l]\r\n#r외형 아이템 [ #i" + 모루탬.getItemId() + "# #t" + 모루탬.getItemId() + "##k#l]\r\n\r\n"
        }

        txt += "#k" + 검정 + "정말로 이 아이템을 모루로 하겠나?"
        cm.sendYesNo(txt);
    } else if (status == 4) {
        cm.moru(모루탬, 선택한탬);
        cm.gainMeso(-30000000);
        if (구분 == 1) {
            cm.getPlayer().fakeRelog();
        }
        if (cm.getClient().getQuestStatus(50009) == 1) {
            cm.getClient().setCustomKeyValue(50009, "1", "1");
        }
        cm.sendOk("#fs11#" + 검정 + "성공적으로 모루를 마쳤습니다.");
        cm.dispose();
    }
}
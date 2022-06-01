var status = -1;
isOk = false;

/*
   장비가 아닌 것 : [아이템코드, 개수, 확률]
   장비 : [아이템코드, 1, 확률, 올스탯최저, 올스탯최대, 공마최저, 공마최대] (중간에 1은 스크립트의 편의상을 위해 넣은 것)
*/
itemlist =     [[[4001551, 1, 400], [5060048, 1, 10], [5068304, 1, 10], [1003112, 1, 10, 1, 50, 1, 50], [1012478, 1, 10, 1, 50, 1, 50] ,[1662000, 1, 10, 1, 50, 1, 50] , [1022231, 1, 10, 1, 50, 1, 50], [1122076, 1, 10, 1, 50, 1, 50], [1113149, 1, 8, 50, 50, 10, 50]], // 0~99개
               [[5060048, 2, 30], [5068304, 2, 3], [1672022, 1, 30, 10, 50, 10, 50], [1032241, 1, 20, 1, 50, 1, 50], [1162009, 1, 20, 1, 50, 1, 50], [1032136, 1, 18, 50, 400, 10, 50], [1662115, 1, 18, 1, 50, 1, 50], [1662116, 1, 8, 1, 50, 1, 50]], // 100~199개
               [[5060048, 10, 20], [5068304, 2, 3], [1113282, 1, 30, 1, 70, 1, 70], [1132272, 1, 20, 1, 70, 1, 70], [1672027, 1, 20, 1, 70, 1, 70], [1662114, 1, 18, 1, 70, 1, 70], [1122150, 1, 18, 1, 70, 1, 70], [1113070, 1, 8, 200, 300, 200, 300]],
               [[5060048, 20, 20], [5068304, 10, 1], [1113055, 1, 30, 1, 100, 1, 100], [1152154, 1, 20, 1, 100, 1, 100], [1032200, 1, 20, 1, 100, 1, 100], [1182200, 1, 18, 1, 100, 1, 100], [1162013, 1, 18, 1, 100, 1, 100], [1022226, 1, 8, 1, 100, 10, 100]],
               [[5060048, 50, 400], [5068304, 50, 100], [1132308, 1, 30, 100, 500, 100, 500], [1022278, 1, 20, 100, 500, 40, 300], [1062158, 1, 5, 100, 500, 40, 300], [1042244, 1, 5, 100, 500, 40, 300], [1032316, 1, 5, 100, 500, 40, 300], [1113306, 1, 5, 100, 500, 40, 300], [1162083, 1, 20, 100, 500, 100, 500], [1162082, 1, 20, 100, 500, 100, 500], [1162081, 1, 20, 100, 500, 100, 500], [1162080, 1, 20, 100, 500, 100, 500], [1012632, 1, 20, 100, 500, 100, 500], [1672077, 1, 20, 100, 500, 100, 500], [1122430, 1, 18, 100, 500, 100, 500], [5062005, 50, 12], [4001168, 1, 2]],
]; 

name = ["노멀", "레어", "에픽", "유니크", "레전드리"] // 등급 이름
ccoin = 4033114; // 코인 코드
sum = 0; // 건들지 말것


function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == 1) {
        if (status == 4) {
	cm.dispose();
	cm.openNpc(1540326);
        }
        if (isOk) {
            status++;
        }
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        말 = "             #fs11##d 해피스토리 스페셜 가챠 뽑기 \r\n#fs11##Cgray#  당신의 운을 믿고 확률을 걸어 좋은 아이템을 얻어보세요!#k\r\n\r\n#fs11#";
        말 += "#L0##i4033114# 를 이용하여 아이템을 뽑는다#l\r\n";
        말 += "#L1#가챠 시스템에 대한 설명을 듣는다.#l\r\n\r\n";
        말 += "#L2##d등급별로 등장하는 아이템 및 확률 보기";
        cm.sendSimple(말);
    } else if (status == 1) {
        if (selection == 0) {
            말 = "#fs11# #fc0xFF000000# 소비할 #b#z" + ccoin + "##k#fs11# #fc0xFF000000# 의 개수를 설정해 주세요.\r\n\r\n"
            말 += "#r(현재 #z" + ccoin + "#을 #c" + ccoin + "#개 가지고 있습니다.)#k"
            if (cm.itemQuantity(ccoin) < 100 * itemlist.length) {
                limit = cm.itemQuantity(ccoin);
            } else {
                limit = itemlist.length * 100 - 1;
            }
            cm.sendGetNumber(말, 1, 1, limit);
        } else if (selection == 1) {
            말 = "#fs11##d#i4033114# 의 시스템은 기본적으로 #b" + itemlist.length + "단계#k#fs11# #d로 나뉘어져 있습니다.\r\n\r\n"
            for (i = 0; i < itemlist.length; i++) {
                if ((100 * i) == 0) {
                    s = 1;
                } else {
                    s  = (100 * i);
                }
                말 += "#b" + s + "개 ~ " + ((100 * i) + 99) + "개#k - #r" + name[i] + "등급#k\r\n"
            }
            말 += "\r\n으로 나뉘어 지며, #b100 단위의 개수를 제외한, 0~99개#k로는 #b원하는 아이템이 나올 확률을 상승#k시킬 수 있습니다.\r\n\r\n"
            cm.sendOk(말);
            cm.dispose();
        } else if (selection == 2) {
            말 = "#fs11##d 각 등급별 등장 가능한 아이템과 확률입니다.\r\n"
            말 += " #r (각 확률은 소숫점을 표시하지 않습니다.)#k\r\n"
            for (i = 0; i < itemlist.length; i++) {
                말 += "\r\n#fs15##r[" + name[i] + " 등급]#k#fs11##n\r\n"
                for (j = 0; j < itemlist[i].length; j++) {
                    sum += itemlist[i][j][2]
                }
                for (h = 0; h < itemlist[i].length; h++) {
                    if (itemlist[i][h][0] >= 2000000) {
                        말 += "#i" + itemlist[i][h][0] + "# #b#z" + itemlist[i][h][0] + "##r (x" + itemlist[i][h][1] + ")#k#fc0xFF000000# [" + Math.floor(itemlist[i][h][2] / sum * 100) + "%]\r\n";
                    } else {
                        말 += "#i" + itemlist[i][h][0] + "# #b#z" + itemlist[i][h][0] + "##k #fc0xFF000000# [" + Math.floor(itemlist[i][h][2] / sum * 100) + "%]\r\n#d(올스탯 : " + itemlist[i][h][3] + "~" + itemlist[i][h][4] + ", 공/마 : " + itemlist[i][h][5] + "~" + itemlist[i][h][6] + ")\r\n"
                    }
                }
                sum = 0;
            }
            cm.sendOk(말);
            cm.dispose();
        }
    } else if (status == 2) {
        if (cm.itemQuantity(ccoin) < selection) {
            cm.sendOk("#fs11##fc0xFF000000#오류가 발생하였습니다.");
            cm.dispose();
            return;
        }
        st = selection;
        geti = Math.floor(selection / 100);
        말 = "#fs11##fc0xFF000000##i" + ccoin + "# #b#z" + ccoin + "##r " + st + "#k#fc0xFF000000#개를 사용하여\r\n 얻을 수 있는 아이템 리스트는 아래와 같습니다\r\n\r\n"
        말 += "#r["+name[geti]+" 등급]#k#n\r\n"
        for (i = 0; i < itemlist[geti].length; i++) {
            if (i == 0) {
                for (j = 0; j < itemlist[geti].length; j++) {
                    sum += itemlist[geti][j][2]
                }
            }
            if (Math.floor(selection) % 100 != 0) {
                말 += "#L" + i + "# "
            }
            if (itemlist[geti][i][0] >= 2000000) {
                말 += "#i" + itemlist[geti][i][0] + "# #b#z" + itemlist[geti][i][0] + "##r (x" + itemlist[geti][i][1] + ")#k#fc0xFF000000# [" + (itemlist[geti][i][2] / sum * 100).toFixed(2) + "%]#l\r\n";
            } else {
                말 += "#i" + itemlist[geti][i][0] + "# #b#z" + itemlist[geti][i][0] + "##k#fc0xFF000000# [" + (itemlist[geti][i][2] / sum * 100).toFixed(2) + "%]\r\n#d(올스탯 : " + itemlist[geti][i][3] + "~" + itemlist[geti][i][4] + ", 공/마 : " + itemlist[geti][i][5] + "~" + itemlist[geti][i][6] + ")#l\r\n"
            }
        }
        if (Math.floor(selection) % 100 != 0) {
            말 += "\r\n\r\n#b#z" + ccoin + "# #r" + Math.floor(st % 100) + "개#k를 사용하여, 확률을 상승시킬 수 있습니다.\r\n"
            말 += "원하시는 아이템을 선택해 주세요."
            cm.sendSimple(말);
        } else {
            isOk = true;
            말 += "#b#z" + ccoin + "##k을 사용하시겠습니까?";
            cm.sendYesNo(말);
        }
    } else if (status == 3) {
        itemlist[geti][selection][2] = parseInt(itemlist[geti][selection][2]) + Math.floor(st%100);
        sum += Math.floor(st%100);

        말 = "#fs11##fc0xFF000000# 확률이 증가되어 재설정된 아이템 리스트입니다.\r\n\r\n";
        //cm.getPlayer().dropMessage(6,itemlist[geti].length);
        for (i = 0; i < itemlist[geti].length; i++) {
            if (itemlist[geti][i][0] >= 2000000) {
                말 += "#i" + itemlist[geti][i][0] + "# #b#z" + itemlist[geti][i][0] + "##r (x" + itemlist[geti][i][1] + ")#k#fc0xFF000000# [" + (itemlist[geti][i][2] / sum * 100).toFixed(2) + "%]\r\n";
            } else {
                말 += "#i" + itemlist[geti][i][0] + "# #b#z" + itemlist[geti][i][0] + "##k#fc0xFF000000# [" + (itemlist[geti][i][2] / sum * 100).toFixed(2) + "%]\r\n#d(올스탯 : " + itemlist[geti][i][3] + "~" + itemlist[geti][i][4] + ", 공/마 : " + itemlist[geti][i][5] + "~" + itemlist[geti][i][6] + ")\r\n"
            }
        }
        말 += "#b#z" + ccoin + "##k을 사용하시겠습니까?";
        cm.sendYesNo(말);
    } else if (status == 4) {
        rd = Math.floor(Math.random() * sum);
        //cm.getPlayer().dropMessage(6, "rd : "+rd+"")
        //cm.getPlayer().dropMessage(6, "sum : "+sum+"")
        sum2 = 0;
        for (i=0; i<=itemlist[geti].length; i++) {
            if (sum2 >= rd) {
                geti2 = i - 1;
                break;
            } else {
                sum2 += itemlist[geti][i][2];
               // cm.getPlayer().dropMessage(6, "sum2 : "+sum2);
            }
        }
        if (itemlist[geti][geti2][0] >= 2000000) {
            cm.gainItem(itemlist[geti][geti2][0], itemlist[geti][geti2][1]);
        } else {
            rd1 = Packages.server.Randomizer.rand(itemlist[geti][geti2][3],itemlist[geti][geti2][4])
            rd2 = Packages.server.Randomizer.rand(itemlist[geti][geti2][5],itemlist[geti][geti2][6])
            cm.gainSponserItem(itemlist[geti][geti2][0],''+name[geti]+'',rd1,rd2,0);
        }
        cm.sendYesNo("#fs11##fc0xFF000000#아래와 같은 아이템을 획득하였습니다.\r\n\r\n"
        +"#i"+itemlist[geti][geti2][0]+"# #b#z"+itemlist[geti][geti2][0]+"##k #r("+itemlist[geti][geti2][1]+"개)\r\n\r\n"
        +"한번 더 이용하시겠습니까?");
        cm.gainItem(ccoin, -st);
    }
}
/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

status = -1;
검정 = "#fc0xFF191919#"
파랑 = "#fc0xFF4641D9#"
남색 = "#fc0xFF4641D9#"
주황 = "#fc0xFFEDA900#"

importPackage(Packages.tools.packet);
importPackage(Packages.client.inventory);

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 9) {
            qm.sendOkS("이런.. 언제라도 마음이 바뀌면 다시 찾아오게나.", 4, 9401232);
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("이번에는 더 블랙의 #r핵심시스템#k인 "+파랑+"황혼과 여명 시스템#k에 대해서 설명해주겠네!", 0x04, 9401232);
    } else if (status == 1) {
        qm.sendNextPrevS("#r여명 시스템#k이란 자네가 흔히 알던 뱃지와는 #b차원이 다른 기능을 가진 뱃지#k라네.", 0x04, 9401232);
    } else if (status == 2) {
        말 = "우선, 여명의 뱃지는 #b총 6종류#k가 있다네.\r\n#r※ 1차원 뱃지부터는 옵션이 공개되지 않습니다.#k"+남색+"\r\n"
        말 += "#i1182000# #z1182000#\r\n"
        말 += "#i1182001# #t1182001# (1차원)\r\n"
        말 += "#i1182002# #t1182002# (2차원)\r\n"
        말 += "#i1182003# #t1182003# (3차원)\r\n"
        말 += "#i1182004# #t1182004# (4차원)\r\n"
        말 += "#i1182005# #t1182005# (5차원)\r\n"
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 3) {
        말 = "여명의 뱃지는 다른 뱃지와는 달리 #e"+주황+"스타포스#k#n와 #e"+파랑+"주문서 사용#n#k이 가능하다네.\r\n"
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 4) {
        말 = "#z1182000# 아이템을 해방하여 차원을 #r업그레이드#k를 "
        말 += "진행하면 #b아이템 옵션은 전승#k되며 #b계정 내 이동가능#k 아이템으로 변경이 된다네.\r\n\r\n"
        말 += "그러니 "+주황+"스타포스#k와 "+파랑+"주문서#k를 #r#z1182000##k에 강화해도 괜찮겠지? 크크."
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 5) {
        말 = "#b여명의 뱃지#k는 #r총 5단계#k로 이루어지며, 업그레이드를 할때마다"
        말 += "#r\"1차원\"#k이 오르며 "+주황+"고유 옵션#k을 획득할 수 있네.\r\n"
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 6) {
        말 = "자 이번엔 #b황혼 시스템#k에 대해서 설명해주겠네!\r\n"
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 7) {
        말 = "#b황혼 시스템#k이란 "
        말 += "특수한 "+주황+"고유 옵션#k을 지닌 #e#r포켓 아이템#n#k 이라네.\r\n"
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 8) {
        말 = "황혼의 지배자 아이템은 #b3가지#k로 나뉜다네."+남색+"\r\n"
        말 += "#i1162000# #z1162000#\r\n"
        말 += "#i1162001# #z1162001#\r\n"
        말 += "#i1162002# #z1162002#\r\n\r\n"
        말 += "모두 #b몬스터 리젠률#k을 올려주는 "+주황+"유니크한 아이템#k이지."
        qm.sendNextPrevS(말, 0x04, 9401232);
    } else if (status == 9) {
        qm.sendYesNoS("자! 어느정도 설명을 했지만 이해가 잘 안 가는 표정이구만. 크크. 어느정도 예상했다네  하여 이번에도 #e[#b컨텐츠 시스템#k -> #r황혼과 여명 시스템#k]#n에 #d\"황혼 시스템은 무엇인가요?\"#K를 진행 해보게나.", 4, 9401232);
    } else if (status == 10) {
        qm.sendOkS("잊지 말게나 #e[#b컨텐츠 시스템#k -> #r황혼과 여명 시스템#k]#n에 #d\"황혼 시스템은 무엇인가요?\"를 클릭해야 한다네!", 4, 9401232);
        qm.forceStartQuest();
        qm.dispose();
    }
}

status = -1;
function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOkS("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
            qm.dispose();
            return;
        } else {
            status--;
        }
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        qm.sendNextS("설명을 잘 읽어봤나? 원하는 #r지배자, 여명#k 아이템을 얻기 위해선 시간이 오래 걸릴수도 있다네. 하지만 포기하지 말고 천천히 하다보면 언젠간 자네의 것이 될거야 크크. 응원한다네.", 0x04, 9401232);
    } else if (status == 1) {
        말 = "자네를 위해 조그마한 보상을 준비해봤다네.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n";
        말 += "#i2450064:# #b#t2450064:# 1개\r\n";
        말 += "#i5200002:# #b50,000,000 메소#k\r\n";
        qm.sendOkS(말, 0x04, 9401232);
    } else if (status == 2) {
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        if (leftslot < 1) {
            qm.sendOkS("#r#h #! 소비창 한칸 이상을 비워두게!", 0x04, 9401232);
            qm.dispose();
            return;
        }
        var str = qm.getClient().getKeyValue("GrowQuest");
        var ab = str.split("");
        var fi = "";
        ab[4] = "1";
        for (var a = 0; a < ab.length; a++) {
            fi += ab[a];
        }
        qm.getClient().setKeyValue("GrowQuest", fi);
        qm.gainItem(2450064, 1);
        qm.gainMeso(50000000);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}

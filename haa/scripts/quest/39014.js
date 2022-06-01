importPackage(Packages.server.quest);
importPackage(Packages.client.inventory);
var status = -1;
var str, ab;
var replace = false, change = false;
var questName = "ArcQuest1"
var quest1 = 39017;//퀘스트최소대역
var quest2 = 39032;//퀘스트최대대역
var questcount = 3;//퀘스트 몇개줄건지
var questarray = [];
var practicearray = [];

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0 && !change) {
        if (status == 1) {
            qm.sendOk("핥? 마음이 변한 거야? 내 요리를 돕고 싶다면 언제든 얘기해.");
            qm.dispose();
            return;
        }
        status += 2;
    }
    if (mode == 1 && !change) {
        status++;
    }

    if (status == 0) {
        if (qm.getPlayer().getV(questName) == null) {
            qm.SelectQuest(questName, quest1, quest2, questcount);
        }
        txt = "핥! 오늘의 보조 업무도 빨리 처리해줘!\r\n\r\n"
        str = qm.getPlayer().getV(questName);
        ab = str.split(",");
        for (var a = 0; a < ab.length; a++) {
            txt += "#b#e#y" + ab[a] + "##k#n\r\n"
        }
        qm.askPraticeReplace(txt);
    } else if (status == 1) {
        //교체
        qm.sendYesNo("핥! 이번 보조 업무가 어려워 보여? 그럼 다른 보조 업무들도 보여줄까?\r\n\r\n#b(일부 임무 혹은 전체 임무를 제외시키고 목록을 재구성 합니다.)#k");
        replace = true;
    } else if (status == 2) {
        //수행 & 교체
        if (replace) {
            //교체
            if (!change) {
                for (var a = 0; a < ab.length; a++) {
                    questarray.push([ab[a], 0]);
                }
            }
            change = true;
            if (selection > 0) {
                for (var b = 0; b < questarray.length; b++) {
                    if (questarray[b][0] == selection) {
                        questarray[b][1] = 1;
                        break;
                    }
                }
            }
            txt = "바꾸고 싶은 보조 업무를 골라봐.\r\n\r\n"
            for (var a = 0; a < ab.length; a++) {
                txt += "#L" + ab[a] + "#"
                for (var b = 0; b < questarray.length; b++) {
                    if (ab[a] == questarray[b][0]) {
                        if (questarray[b][1] == 0) {
                            txt += "#b"
                            break;
                        }
                    }
                }
                txt += "#e#y" + ab[a] + "##k#n\r\n"
            }
            if (selection > 0 && selection != 999) {
                var next = true;
                for (var b = 0; b < questarray.length; b++) {
                    if (questarray[b][1] == 0) {
                        //3개 다선택됐으면 다음으로
                        next = false;
                        break;
                    }
                }
                if (next) {
                    change = false;
                }
            } else if (selection == 999) {
                var changecount = 0;
                for (var b = 0; b < questarray.length; b++) {
                    if (questarray[b][1] == 1) {
                        changecount++;
                        practicearray.push(qm.ReplaceQuest(questName, quest1, quest2, questarray[b][0]));
                    }
                }
                str = qm.getPlayer().getV(questName);
                ab = str.split(",");
                txt = "제외된 " + changecount + "개의 보조 업무 대신 새로운 업무 " + changecount + "개를 보여주지.\r\n핥! 오늘의 보조 업무는 모두 " + questcount + "가지야!\r\n\r\n"

                for (var a = 0; a < ab.length; a++) {
                    txt += "#b#e#y" + ab[a] + "##k"
                    for (var b = 0; b < practicearray.length; b++) {
                        if (ab[a] == practicearray[b]) {
                            txt += "#r[NEW]#k"
                            break;
                        }
                    }
                    txt += "#n\r\n";
                }
                qm.sendNext(txt);
                startQuest();
            }
            if (next) {
                //전부 다시설정
                qm.SelectQuest(questName, quest1, quest2, questcount);
                txt = "제외된 " + questcount + "개의 보조 업무 대신 새로운 업무 " + questcount + "개를 보여주지.\r\n핥! 오늘의 보조 업무는 모두 " + questcount + "가지야!\r\n\r\n"
                str = qm.getPlayer().getV(questName);
                ab = str.split(",");
                for (var a = 0; a < ab.length; a++) {
                    txt += "#b#e#y" + ab[a] + "##k#r[NEW]#k#n\r\n"
                }
                qm.sendNext(txt);
                startQuest();
            } else {
                txt += "\r\n#r#e#L999#더 이상 바꾸고 싶은 보조 업무는 없어요.#n#k\r\n"
                qm.sendSimple(txt);
            }
        } else {
            //수행
            qm.sendOk("모든 보조 업무를 완료하면 나에게 다시 얘기해! 특별 수당을 주지! 핥핥핥!\r\n꼭 #r#e자정 전#k#n에 돌아와! 핥!\r\n보조 업무에 대해 궁금한 것이 있으면 #b#e퀘스트 정보창#k#n을 통해 확인할 수 있으니 참고하고!");
            startQuest();
        }
    }
}


function startQuest() {
    str = qm.getPlayer().getV(questName);
    ab = str.split(",");
    for (var a = 0; a < ab.length; a++) {
        MapleQuest.getInstance(ab[a]).forceStart(qm.getPlayer(), 0, null);
    }
    qm.forceStartQuest();
    qm.dispose();
}

function end(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 1) {
            qm.sendOk("나는 항상 같은자리에 있으니 언제라도 다시 말을 걸어주게.");
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
        leftslot = qm.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot();
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot(); leftslot2 = qm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2  && leftslot2 < 2) {
            qm.sendOk("인벤토리에 자리가 없다네 핥핥!");
            qm.dispose();
            return;
        }
        qm.sendNext("아주 훌륭하군! 핥! #i1712002# #z1712002# 10개,\r\n#i2435719# #z2435719# 10개야. 핥핥!");
    } else if (status == 1) {
        for (a = 0; a < 5; a++) {
            qm.gainItem(1712002, 10);
        }
        qm.gainItem(2435719, 10);
        qm.getPlayer().removeKeyValue(39016);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
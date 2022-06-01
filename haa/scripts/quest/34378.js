importPackage(Packages.server.quest);
importPackage(Packages.client.inventory);
var status = -1;
var str, ab;
var replace = false, change = false;
var questName = "ArcQuest2"
var quest1 = 34381;//퀘스트최소대역
var quest2 = 34393;//퀘스트최대대역
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
            qm.sendOk("부탁이 조금 부담스러운가? 나를 돕고 싶다면 언제든 얘기해주게.");
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
        txt = "어서오게나 #h #. 오늘도 내 부탁을 들어줘서 고맙구먼.\r\n\r\n"
        str = qm.getPlayer().getV(questName);
        ab = str.split(",");
        for (var a = 0; a < ab.length; a++) {
            txt += "#b#e#y" + ab[a] + "##k#n\r\n"
        }
        qm.askPraticeReplace(txt);
    } else if (status == 1) {
        //교체
        qm.sendYesNo("부탁이 조금 부담스러운가? 그럼 다른 부탁을 함세.\r\n\r\n#b(일부 임무 혹은 전체 임무를 제외시키고 목록을 재구성 합니다.)#k");
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
            txt = "바꾸고 싶은 보조 업무를 골라봄세.\r\n\r\n"
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
                txt = "제외된 " + changecount + "개의 보조 업무 대신 새로운 업무 " + changecount + "개를 알려주겠네. 오늘의 부탁은 모두 " + questcount + "가지라네.\r\n\r\n"

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
                txt = "제외된 " + changecount + "개의 보조 업무 대신 새로운 업무 " + changecount + "개를 알려주겠네. 오늘의 부탁은 모두 " + questcount + "가지라네.\r\n\r\n"
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
            qm.sendOk("모든 부탁을 완료하면 나한테로 다시 돌아오면 된다네.\r\n꼭 #r#e자정 전#k#n에 돌아오는 것 잊지 말게나.\r\n부탁에 대해 궁금한 것이 있으면 #b#e퀘스트 정보창#k#n을 통해 확인할 수 있으니 참고하게나.");
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
        if (leftslot < 10 && leftslot1 < 5 && leftslot2 < 3) {
            qm.sendOk("인벤토리에 자리가 없다네.");
            qm.dispose();
            return;
        }
        qm.sendNext("이 늙은이의 부탁을 들어주어 고맙네. 모두를 대표해서\r\n#i1712003# #z1712003# 10개,\r\n#i2435719# #z2435719# 10개를 주고 싶네. 받아주게나.");
    } else if (status == 1) {
		qm.forceCompleteQuest();
        for (a = 0; a < 5; a++) {
            qm.gainItem(1712003, 10);
        }
        qm.gainItem(2435719, 10);
        qm.getPlayer().removeKeyValue(34380);
        qm.dispose();
    }
}
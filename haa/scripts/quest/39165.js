importPackage(Packages.server.quest);
importPackage(Packages.client.inventory);
var status = -1;
var str, ab;
var replace = false, change = false;
var questName = "ArcQuest6"
var quest1 = 39105;//퀘스트최소대역
var quest2 = 39113;//퀘스트최대대역
var questcount = 4;//퀘스트 몇개줄건지
var questarray = [];
var practicearray = [];

function start(mode, type, selection) {

    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0 && !change) {
        if (status == 1) {
            qm.sendOk("···.");
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
        txt = "이번 주 자네에게 부탁할 일은 아래와 같다네.\r\n\r\n"
        str = qm.getPlayer().getV(questName);
        ab = str.split(",");
        for (var a = 0; a < ab.length; a++) {
            txt += "#b#e#y" + ab[a] + "##k#n\r\n"
        }
        txt += "\r\n#e지금 바로 수행하시겠나?#n\r\n(마음에 들지 않는다면 교체하기 버튼을 눌러 다른 임무로 교체할 수도 있다네.)"
        qm.askPraticeReplace(txt);
    } else if (status == 1) {
        //교체
        qm.sendYesNo("목록에 있는 임무가 마음에 들지 않는가 보군? 그렇다면 다른 임무를 찾아볼 수도 있지. 교체하고 싶은 임무를 골라주게나.");
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
            txt = "바꾸고 싶은 임무를 골라보게나.\r\n\r\n"
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
                txt = "새로운 임무를 찾았네.\r\n부탁할 일은 아래와 같다네.\r\n\r\n"

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
                txt = "새로운 임무를 찾았네.\r\n부탁할 일은 아래와 같다네.\r\n\r\n"
                str = qm.getPlayer().getV(questName);
                ab = str.split(",");
                for (var a = 0; a < ab.length; a++) {
                    txt += "#b#e#y" + ab[a] + "##k#r[NEW]#k#n\r\n"
                }
                qm.sendNext(txt);
                startQuest();
            } else {
                txt += "\r\n#r#e#L999#더 이상 바꾸고 싶은 보조 업무는 없다.#n#k\r\n"
                qm.sendSimple(txt);
            }
        } else {
            //수행
            qm.sendPrev("꼭 #r#e일요일 자정#n#k까지 임무를 완수해야 한다는 점 잊지 말게.\r\n그럼 안녕히 다녀오게나");
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
    qm.forceCompleteQuest();
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
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 5) {
            qm.sendOk("인벤토리에 자리가 없군요.");
            qm.dispose();
            return;
        }
        qm.sendNext("오늘의 명령 " + questcount + "가지를 모두 완수하셨군요! 자, 여기\r\n#i1712006# #z1712006# 10개,\r\n#i2435719# #z2435719# 10개를 드리겠습니다.");
    } else if (status == 1) {
        for (a = 0; a < 5; a++) {
            qm.gainItem(1712006, 10);
        }
        qm.gainItem(2435719, 10);
        qm.getPlayer().removeKeyValue(34775);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
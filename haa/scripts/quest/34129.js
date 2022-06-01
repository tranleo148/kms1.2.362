importPackage(Packages.server.quest);
importPackage(Packages.client.inventory);
var status = -1;
var str, ab;
var replace = false, change = false;
var questName = "ArcQuest0"
var quest1 = 34130;//퀘스트최소대역
var quest2 = 34138;//퀘스트최대대역
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
            qm.sendOk("앗, 마음이 바뀌신 거군요. 임무를 받으시려면 제게 다시 말을 걸어주세요.");
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
        txt = "잘 오셨어요, #ho#님. 오늘 #ho#님에게 부탁 드릴 일은 이 "+questcount+"가지입니다. 지금 바로 수행하시겠어요? 마음에 들지 않는다면 교체하기 버튼을 눌러 다른 임무로 교체할 수도 있습니다.\r\n\r\n"
        str = qm.getPlayer().getV(questName);
        ab = str.split(",");
        for (var a = 0; a < ab.length; a++) {
            txt += "#b#e#y" + ab[a] + "##k#n\r\n"
        }
        qm.askPraticeReplace(txt);
    } else if (status == 1) {
        //교체
        qm.sendYesNo("목록에 있는 임무가 마음에 들지 않으세요? 그렇다면 다른 임무를 찾아볼 수도 있습니다. \r\n\r\n#b(일부 임무 혹은 전체 임무를 제외시키고 목록을 재구성 합니다.)#k");
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
            txt = "제외하고 싶은 임무를 골라주세요.\r\n\r\n"
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
                txt = "제외된 "+changecount+"개 대신 새로운 임무 "+changecount+"개를 찾았습니다. 오늘 부탁드릴 일은 이렇게 "+questcount+"가지입니다.\r\n\r\n"

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
                txt = "제외된 "+questcount+"개 대신 새로운 임무 "+questcount+"개를 찾았습니다. 오늘 부탁드릴 일은 이렇게 "+questcount+"가지입니다.\r\n\r\n"
                str = qm.getPlayer().getV(questName);
                ab = str.split(",");
                for (var a = 0; a < ab.length; a++) {
                    txt += "#b#e#y" + ab[a] + "##k#r[NEW]#k#n\r\n"
                }
                qm.sendNext(txt);
                startQuest();
            } else {
                txt += "\r\n#r#e#L999#더 이상 제외하고 싶은 임무는 없다.#n#k\r\n"
                qm.sendSimple(txt);
            }
        } else {
            //수행
            qm.sendOk("임무가 끝나시거든 제게 오셔서 완료하시면 돼요. 꼭 오늘 자정까지 오셔야 합니다. 그럼 안녕히 다녀오세요.");
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
        leftslot1 = qm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 10 && leftslot1 < 10) {
            qm.sendOk("#fs11##r장비창과 소비창 10 칸 이상을 확보주세요.");
            qm.dispose();
            return;
        }
        qm.sendNext("#h #님, " + questcount + "개의 임무를 모두 완수하셨군요. 자, 여기\r\n#i1712001# #z1712001# 5개,\r\n#i2435719# #z2435719# 3개를 드렸습니다.");
    } else if (status == 1) {
        for (a = 0; a < 5; a++) {
            qm.gainItem(1712001, 10);
        }
        if (qm.getPlayer().getKeyValue(34151, "clear") != 0) {
            qm.getPlayer().setKeyValue(34151, "clear", "0");
        }
        qm.getPlayer().setKeyValue(34151, "clear", (qm.getPlayer().getKeyValue(34151, "clear") + 1) + "");
        qm.gainItem(2435719, 10);
qm.getPlayer().setKeyValue(100592, "point", "" + (qm.getPlayer().getKeyValue(100592, "point") + 1));
        qm.getPlayer().removeKeyValue(34127);
        qm.forceCompleteQuest();
        qm.dispose();
    }
}
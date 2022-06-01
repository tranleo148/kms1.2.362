/*
MelonK
*/
importPackage(Packages.tools.packet);
importPackage(java.util);
var St = 0;

qnum = 4; // 퀘스트 고유번호 (겹치지만 않으면 됨);
questlist = [
[8643000, 200, "mob"],
[8643001, 200, "mob"],
[8643002, 200, "mob"],
[8643003, 200, "mob"],
[8643004, 200, "mob"],
[8643005, 200, "mob"],
[8643006, 200, "mob"],
[8643007, 200, "mob"],
[8643008, 200, "mob"],
[8643009, 200, "mob"],
[8643010, 200, "mob"],
[8643011, 200, "mob"],
[8643012, 200, "mob"],
[4036572, 50, "item"]
];



//Don't Touch :D
count1 = 0;
count2 = 0;
isnewed = true;
qarr = [];
questarray = [];
color = ["b", "b", "b", "b", "b"];
var Stplus = true;

function start() {
    St = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    a = cm.getPlayer().getV("arcane_quest_" + qnum);
    if (mode == -1 || mode == 0) {
        if (St == 0) {
            isnewed = false;
            St = 3;
        } else {
            cm.dispose();
            return;
        }
    }
    if (mode == 1) {
        if (St == -1) {
            b = a;
        }
        if (b < 0 && St == 2 && selection != 100) {
            if (color[selection] == "b") {
                color[selection] = "k";
            } else {
                color[selection] = "b";
            }
        } else {
            St++;
        }
    }
    if (b == null || b < 1) { // 일일퀘스트를 받지 않았을때
        if (St == 0) {
            for (i = 0; i < questlist.length; i++) {
                if (b == null || b < 0) {
                    clearquest(questlist[i][0]);
                }
                qarr.push(questlist[i]);
            }
            if (b == null || b < 0) {
                for (i = 0; i < 3; i++) {
                    rd = Math.floor(Math.random() * questlist.length)
                    cm.getPlayer().addKV("rehelen_" + questlist[rd][0] + "_count", "0");
                    cm.getPlayer().addKV("rehelen_" + questlist[rd][0] + "_" + questlist[rd][2] + "q", questlist[rd][1]);
                    cm.getPlayer().addKV("rehelen_" + questlist[rd][0] + "_isclear", "0");
                    questlist.splice(rd, 1); // 중복 방지
                }
            cm.getPlayer().addKV("arcane_quest_" + qnum, 0);
            } else {
                listed = 0;
                while (listed < 3) {
                    for (i = 0; i < questlist.length; i++) {
                        if (cm.getPlayer().getV("rehelen_" + qarr[i][0] + "_" + qarr[i][2]+"q") > 0) {
                            questlist.splice(i, 1);
                            listed++;
                        }
                    }
		    if (listed < 3) {
			break;
		    }
                }
            }
            dialogue = "어서오게나 #b#e#h ##k#n. 오늘도 내 부탁을 들어줘서 고맙구먼.\r\n\r\n"
            for (i = 0; i < qarr.length; i++) {
                if (cm.getPlayer().getV("rehelen_" + qarr[i][0] + "_mobq") > 0) {
                    dialogue += "#b#e[일일 퀘스트] #o" + qarr[i][0] + "# " + qarr[i][1] + "마리 퇴치 \r\n"
                    questarray.push(qarr[i]);
                } else if (cm.getPlayer().getV("rehelen_" + qarr[i][0] + "_itemq") > 0) {
                    dialogue += "#b#e[일일 퀘스트] #z" + qarr[i][0] + "# " + qarr[i][1] + "개 수집\r\n"
                    questarray.push(qarr[i]);
                }

            }
            cm.sendConductExchange(dialogue);
        } else if (St == 1) {
            cm.sendYesNo("부탁이 조금 부담스러운가? 그럼 다른 부탁을 함세.\r\n\r\n#b(일부 의뢰 혹은 전체 의뢰를 제외시키고 목록을 재구성 합니다.)#k");
        } else if (St == 2) {
            newcheck = true;
            dialogue = "바꾸고 싶은 부탁을 골라봄세.\r\n\r\n"
            for (i = 0; i < questarray.length; i++) {
                if (cm.getPlayer().getV("rehelen_" + questarray[i][0] + "_mobq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[일일 퀘스트] #o" + questarray[i][0] + "# " + questarray[i][1] + "마리 퇴치#k#n#l\r\n"
                } else if (cm.getPlayer().getV("rehelen_" + questarray[i][0] + "_itemq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[일일 퀘스트] #z" + questarray[i][0] + "# " + questarray[i][1] + "개 수집#k#n#l\r\n"
                }
            }
            dialogue += "\r\n#L100##r#e더 이상 제외하고 싶은 임무는 없어요."
            cm.sendSimple(dialogue);
        } else if (St == 3) {
            for (i = 0; i < qarr.length; i++) {
                clearquest(qarr[i][0]);
            }
            talk = "";
            if (isnewed) {
	    talk += "제외된 부탁 대신 새로운 부탁을 알려주겠네."
	}
	talk+= "오늘의 부탁은 모두 3가지라네.\r\n\r\n";
            for (i = 0; i < 3; i++) {
                if (color[i] == "k") { // 제외되었으면 (색으로 판단)
                    isnewed = true;
                    rd = Math.floor(Math.random() * questlist.length)
                    questarray[i] = questlist[rd];
                    questlist.splice(rd, 1); // 중복 방지 (questlist 배열의 rd번째를 제거)
                }
                isnew = color[i] == "k" ? "#r#e[NEW]#k#n" : "#k#n"
                if (questarray[i][2] == "mob") {
                    talk += "#b#e[일일 퀘스트] #o" + questarray[i][0] + "# " + questarray[i][1] + "마리 퇴치#k#n " + isnew + "\r\n"
                } else {
                    talk += "#b#e[일일 퀘스트] #z" + questarray[i][0] + "# " + questarray[i][1] + "개 수집#k#n " + isnew + "\r\n"
                }
                cm.getPlayer().addKV("rehelen_" + questarray[i][0] + "_count", 0);
                cm.getPlayer().addKV("rehelen_" + questarray[i][0] + "_" + questarray[i][2] + "q", questarray[i][1]);
                cm.getPlayer().addKV("rehelen_" + questarray[i][0] + "_isclear", 0);
            }
            cm.sendNext(talk);
	    cm.getPlayer().addKV("arcane_quest_" + qnum, "1");
            cm.dispose();
        }
    } else {
        if (St == 0) {
            dialogue = "안개를 뚫고 도시 밖으로 나갈 수 있다면...\r\n\r\n"
            dialogue2 = "";
            dialogue3 = "";
	 if (a >= 4) {
	    cm.sendOk("이 늙은이의 부탁을 들어주어 고맙네. 염치없지만 내일도 잘 부탁하네.");
                cm.dispose();
                return;
            }
            for (i = 0; i < questlist.length; i++) {
                if (cm.getPlayer().getV("rehelen_" + questlist[i][0] + "_mobq") > 0 && cm.getPlayer().getV("rehelen_" + questlist[i][0] + "_isclear") < 2) {
                    선택지 = "#L" + i + "##d[일일 퀘스트] #o" + questlist[i][0] + "# " + questlist[i][1] + "마리 퇴치#k"
                    if (cm.getPlayer().getV("rehelen_" + questlist[i][0] + "_count") >= cm.getPlayer().getV("rehelen_" + questlist[i][0] + "_mobq")) {
                        count1++;
                        dialogue2 += 선택지 + " (완료 가능)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 선택지 + " (진행 중)\r\n"
                    }
                } else if (cm.getPlayer().getV("rehelen_" + questlist[i][0] + "_itemq") > 0 && cm.getPlayer().getV("rehelen_" + questlist[i][0] + "_isclear") < 2) {
                    선택지 = "#L" + i + "##d[일일 퀘스트] #z" + questlist[i][0] + "# " + questlist[i][1] + "개 수집#k"
                    if (cm.itemQuantity(questlist[i][0]) >= cm.getPlayer().getV("rehelen_" + questlist[i][0] + "_itemq")) {
                        count1++;
                        dialogue2 += 선택지 + " (완료 가능)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 선택지 + " (진행 중)\r\n"
                    }
                }
            }
            if (count1 >= 1) {
                dialogue += "\r\n#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n" // 완료 가능한 퀘스트 UI
            }
            dialogue += dialogue2;
            dialogue += "\r\n"
            if (count2 >= 1) {
                dialogue += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n" // 진행중 퀘스트 UI
            }
            dialogue += dialogue3;
            cm.sendSimple(dialogue);
        } else if (St == 1) {
			
			if (!cm.canHold(1712003, 3)) {
				cm.sendOk("인벤토리에 공간이 부족합니다.");
				cm.dispose();
				return;
			}
            if ((cm.getPlayer().getV("rehelen_" + questlist[selection][0] + "_mobq") > 0 && cm.getPlayer().getV("rehelen_" + questlist[selection][0] + "_count") >= cm.getPlayer().getV("rehelen_" + questlist[selection][0] + "_mobq")) ||
                (cm.getPlayer().getV("rehelen_" + questlist[selection][0] + "_itemq") > 0 && cm.itemQuantity(questlist[selection][0]) >= cm.getPlayer().getV("rehelen_" + questlist[selection][0] + "_itemq"))) {
	    cm.gainItem(1712003,3); // 심볼
        if(cm.getPlayer().getKeyValue(100592, "point") < 100){
            cm.getPlayer().setKeyValue(100592, "point", cm.getPlayer().getKeyValue(100592, "point")+1);
        }
	    var text2 = "이 늙은이의 부탁을 들어주어 고맙네. 모두를 대표해서 #i1712003# #z1712003# 3개를 주고 싶네. 받아주게나.\r\n\r\n";
	    text2 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
	    text2 += "#i1712003# #b#z1712003# #r#e3 개#k#n\r\n";
                cm.sendOk(text2);
                if (cm.getPlayer().getV("rehelen_" + questlist[selection][0] + "_itemq") > 0) {
                    cm.gainItem(questlist[selection][0], -cm.getPlayer().getV("rehelen_" + questlist[selection][0] + "_itemq"));
                }
                cm.getPlayer().addKV("rehelen_" + questlist[selection][0] + "_isclear", 2);
                cm.getPlayer().addKV("arcane_quest_" + qnum, parseInt(cm.getPlayer().getV("arcane_quest_" + qnum)) + 1);
                cm.dispose();
            } else {
	cm.sendOk("아직 임무를 완수하지 못하셨습니다.");
            }
            cm.dispose();
            return;

        }
    }
}

function clearquest(paramint) {
    cm.getPlayer().addKV("rehelen_" + paramint + "_count", -1);
    cm.getPlayer().addKV("rehelen_" + paramint + "_mobq", -1);
    cm.getPlayer().addKV("rehelen_" + paramint + "_itemq", -1);
    cm.getPlayer().addKV("rehelen_" + paramint + "_isclear", -1);
}
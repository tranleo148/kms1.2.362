/*
MelonK
*/
importPackage(Packages.tools.packet);
importPackage(java.util);
var St = 0;

qnum = 2; // 퀘스트 고유번호 (겹치지만 않으면 됨);
questlist = [
[8641000, 200, "mob"],
[8641001, 200, "mob"],
[8641002, 200, "mob"],
[8641003, 200, "mob"],
[8641004, 200, "mob"],
[8641005, 200, "mob"],
[8641006, 200, "mob"],
[8641007, 200, "mob"],
[8641008, 200, "mob"],
[4034922, 50, "item"],
[4034923, 50, "item"],
[4034924, 50, "item"],
[4034925, 50, "item"],
[4034926, 50, "item"],
[4034927, 50, "item"],
[4034928, 50, "item"],
[4034929, 50, "item"],
[4034930, 50, "item"]
]


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
        if (St == 0 && Stplus == true) {
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
                for (i = 0; i < 5; i++) {
                    rd = Math.floor(Math.random() * questlist.length)
                    cm.getPlayer().addKV("river_" + questlist[rd][0] + "_count", "0");
                    cm.getPlayer().addKV("river_" + questlist[rd][0] + "_" + questlist[rd][2] + "q", questlist[rd][1]);
                    cm.getPlayer().addKV("river_" + questlist[rd][0] + "_isclear", "0");
                    questlist.splice(rd, 1); // 중복 방지
                }
            cm.getPlayer().addKV("arcane_quest_" + qnum, 0);
            } else {
                listed = 0;
                while (listed < 5) {
                    for (i = 0; i < questlist.length; i++) {
                        if (cm.getPlayer().getV("river_" + qarr[i][0] + "_" + qarr[i][2]+"q") > 0) {
                            questlist.splice(i, 1);
                            listed++;
                        }
                    }
		    if (listed < 5) {
			break;
		    }
                }
            }
            dialogue = "잘 오셨어요. #b#e#h ##k#n님. 오늘 #b#e#h ##k#n님에게 부탁 드릴 일은 이 5가지입니다. 지금 바로 수행하시겠어요? 마음에 들지 않는다면 교체하기 버튼을 눌러 다른 임무로 교체할 수도 있습니다.\r\n\r\n"
            for (i = 0; i < qarr.length; i++) {
                if (cm.getPlayer().getV("river_" + qarr[i][0] + "_mobq") > 0) {
                    dialogue += "#b#e[일일 퀘스트] #o" + qarr[i][0] + "# " + qarr[i][1] + "마리 퇴치 \r\n"
                    questarray.push(qarr[i]);
                } else if (cm.getPlayer().getV("river_" + qarr[i][0] + "_itemq") > 0) {
                    dialogue += "#b#e[일일 퀘스트] #z" + qarr[i][0] + "# " + qarr[i][1] + "개 수집\r\n"
                    questarray.push(qarr[i]);
                }

            }
            cm.sendConductExchange(dialogue);
        } else if (St == 1) {
            cm.sendYesNo("목록에 있는 임무가 마음에 들지 않으세요? 그렇다면 다른 임무를 찾아볼 수도 있습니다.\r\n\r\n#b(일부 임무 혹은 전체 임무를 제외시키고 목록을 재구성 합니다.)#k");
        } else if (St == 2) {
            newcheck = true;
            dialogue = "제외하고 싶은 임무를 골라주세요.\r\n\r\n"
            for (i = 0; i < questarray.length; i++) {
                if (cm.getPlayer().getV("river_" + questarray[i][0] + "_mobq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[일일 퀘스트] #o" + questarray[i][0] + "# " + questarray[i][1] + "마리 퇴치#k#n#l\r\n"
                } else if (cm.getPlayer().getV("river_" + questarray[i][0] + "_itemq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[일일 퀘스트] #z" + questarray[i][0] + "# " + questarray[i][1] + "개 수집#k#n#l\r\n"
                }
            }
            dialogue += "\r\n#L100##r#e더 이상 제외하고 싶은 임무는 없다."
            cm.sendSimple(dialogue);
        } else if (St == 3) {
            for (i = 0; i < qarr.length; i++) {
                clearquest(qarr[i][0]);
            }
            talk = "";
            if (isnewed) {
	        talk += "제외된 임무 대신 새로운 임무를 찾았습니다."
	    }
	talk+= "오늘 부탁드릴 일은 이렇게 5가지입니다.\r\n\r\n";
            for (i = 0; i < 5; i++) {
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
                cm.getPlayer().addKV("river_" + questarray[i][0] + "_count", 0);
                cm.getPlayer().addKV("river_" + questarray[i][0] + "_" + questarray[i][2] + "q", questarray[i][1]);
                cm.getPlayer().addKV("river_" + questarray[i][0] + "_isclear", 0);
            }
            cm.sendNext(talk);
	    cm.getPlayer().addKV("arcane_quest_" + qnum, "1");
            cm.dispose();
        }
    } else {
        if (St == 0) {
            dialogue = "#b#e<일일퀘스트 : 망각의 호수 '소멸의 여로'>#k#n\r\n\r\n"
            dialogue += "이곳을 조사하는 것이 여기 있는 저희 시간의 신관들의 일이죠.\r\n"
            dialogue2 = "";
            dialogue3 = "";
	    if (a >= 6) {
	        cm.sendOk("수고하셨습니다. 오늘의 임무를 모두 마치셨습니다.");
                cm.dispose();
                return;
            }
            for (i = 0; i < questlist.length; i++) {
                if (cm.getPlayer().getV("river_" + questlist[i][0] + "_mobq") > 0 && cm.getPlayer().getV("river_" + questlist[i][0] + "_isclear") < 2) {
                    선택지 = "#L" + i + "##d[일일 퀘스트] #o" + questlist[i][0] + "# " + questlist[i][1] + "마리 퇴치#k"
                    if (cm.getPlayer().getV("river_" + questlist[i][0] + "_count") >= cm.getPlayer().getV("river_" + questlist[i][0] + "_mobq")) {
                        count1++;
                        dialogue2 += 선택지 + " (완료 가능)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 선택지 + " (진행 중)\r\n"
                    }
                } else if (cm.getPlayer().getV("river_" + questlist[i][0] + "_itemq") > 0 && cm.getPlayer().getV("river_" + questlist[i][0] + "_isclear") < 2) {
                    선택지 = "#L" + i + "##d[일일 퀘스트] #z" + questlist[i][0] + "# " + questlist[i][1] + "개 수집#k"
                    if (cm.itemQuantity(questlist[i][0]) >= cm.getPlayer().getV("river_" + questlist[i][0] + "_itemq")) {
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
			
			if (!cm.canHold(1712001, 3)) {
				cm.sendOk("인벤토리에 공간이 부족합니다.");
				cm.dispose();
				return;
			}
            if ((cm.getPlayer().getV("river_" + questlist[selection][0] + "_mobq") > 0 && cm.getPlayer().getV("river_" + questlist[selection][0] + "_count") >= cm.getPlayer().getV("river_" + questlist[selection][0] + "_mobq")) ||
                (cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq") > 0 && cm.itemQuantity(questlist[selection][0]) >= cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq"))) {
	    cm.gainItem(1712001,3); // 심볼
        if(cm.getPlayer().getKeyValue(100592, "point") < 100){
            cm.getPlayer().setKeyValue(100592, "point", cm.getPlayer().getKeyValue(100592, "point")+1);
        }
	    var text2 = "자, 여기 #i1712001# #z1712001#를 3개 드렸습니다. 가져다 주신 샘플은 연구에 큰 도움이 될 거예요.\r\n\r\n";
	    text2 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
	    text2 += "#i1712001# #b#z1712001# #r#e3 개#k#n\r\n";
                cm.sendOk(text2);
                if (cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq") > 0) {
                    cm.gainItem(questlist[selection][0], -cm.getPlayer().getV("river_" + questlist[selection][0] + "_itemq"));
                }
                cm.getPlayer().addKV("river_" + questlist[selection][0] + "_isclear", 2);
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
    cm.getPlayer().addKV("river_" + paramint + "_count", -1);
    cm.getPlayer().addKV("river_" + paramint + "_mobq", -1);
    cm.getPlayer().addKV("river_" + paramint + "_itemq", -1);
    cm.getPlayer().addKV("river_" + paramint + "_isclear", -1);
}
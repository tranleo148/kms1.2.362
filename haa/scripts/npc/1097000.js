var status;
var enter = "\r\n";
var st = -1;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    talk = ""
    for (i = 1; i <= 10; i++) {
        if (i != 1)
            talk += "\r\n";
        talk += "#e- " + i + " ~ " + (i * 4) + "마리 : 포인트 " + (i * 10) + "개#n"
    }
    dialogue = [
        ["nextprev", 0, "어서오게나. 이 탕윤님으로 말할 것 같으면 해적들의 배 노틸러스의 주방을 책임지고 있는 주방장이지. 맛있고 영양도 훌륭한 요리의 대가라고 남들이 말해 주더군. 하하하하하! 누가 그랬냐고? 글쎄. 누구였더라? 그게 뭐 중요한가. 하하하하!"],
        ["nextprev", 0, "이렇게 훌륭한 나도 요즘 좀 곤란하다네. 먹어도 먹어도 마치 밑 빠진 독처럼 항상 배고파하는 해적들 때문에 손이 세 개라도 모자랄 지경이야. 이거야 원 단체로 다이어트를 시킬 수도 없고."],
        ["nextprev", 0, "그래서 이렇게 주방에서 요리를 도와줄 사람을 구하고 있다네. 요리 방법은 간단해. 지정된 요리에 맞는 재료를 소환해서 열심히 때려잡기만 하면 돼. 불 조절과 소금간도 좀 해야하긴 하지만 뭐, 어렵지는 않아."],
        ["nextprev", 0, "잘 요리하면 온갖 녀석들이 요리를 먹겠다고 달려들지만, 못만들면 쓰레기통행이지. 자네의 실력에 달려 있다네. 어떤가? 자네도 꽤 요리에 재능이 있어 보이는데 요리 한 번 해보는게 어떤가? 하루에 5회까지 가능하다네.\r\n- #e레벨#n : 60 이상 ( #r추천레벨 : 60 ~ 90#k )\r\n- #e제한시간#n : 20분\r\n- #e참가인원#n : 1~3명\\r\n- #e획득 아이템#n : \r\n#i1052578# #z1052578#\r\n#i1003762# #z1003762#"]
    ]
    if (mode == 1) {
        if(status == 0 && selection == 3){
            cm.sendOk("자네의 오늘 남은 도전 횟수는 5회라네.");
            cmdispose();
        } else if(status == 1 && (selection == 0 || selection == 1)){
            if((selection == 0 && !cm.haveItem(4033668, 2))){
                cm.sendOk("요리사 자격증을 2개 가지고 있는 것이 맞나? 아니면 인벤토리에 장비창이 꽉 찬 것 아니야? 확인을 해보고 다시 말을 걸어 주게.");
                cm.dispose();
                return;
            } else if((selection == 1 && !cm.haveItem(4033668, 3))){
                cm.sendOk("요리사 자격증을 3개 가지고 있는 것이 맞나? 아니면 인벤토리에 장비창이 꽉 찬 것 아니야? 확인을 해보고 다시 말을 걸어 주게.");
                cm.dispose();
                return;
            } else {
                if(selection == 0){
                cm.gainItem(1052578, 1);
                cm.gainItem(4033668, -2);
                cm.dispose();
                return;
                } else {
                cm.gainItem(1003762, 1);
                cm.gainItem(4033668, -3);
                cm.dispose();
                return;
                }
            }
        } else {
            status++;
        }
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        var msg = "#e<파티퀘스트 : 탕윤의 요리교실>#n" + enter;
        msg += "노틸러스의 선원들을 위해 맛있는 요리를 만들어 보겠나? 이 탕윤이 직접 요리를 알려주지." + enter;
        msg += "#L0##b탕윤의 요리교실에 입장한다." + enter;
        msg += "#L1##b탕윤의 요리사 복장을 받는다." + enter;
        msg += "#L2##b탕윤의 요리교실에 대한 설명을 듣는다." + enter;
        msg += "#L3##b오늘 남은 도전 횟수를 묻는다." + enter;

        cm.sendSimple(msg);
    } else {
        if (status == 1) {
            st = selection;
        }
        if (st == 2) {
            if (status <= dialogue.length) {
                sendByType(dialogue[status - 1][0], dialogue[status - 1][1], dialogue[status-1][2]);
            } else {
                cm.dispose();
            }
        } else {
            if (status == 1) {
                switch (selection) {
                    case 0:
                        if (cm.getParty() == null) {
                            cm.sendOk("1인 이상 파티를 맺어야만 입장할 수 있다네.");
                            cm.dispose();
                            return;
                        } else if (cm.getPlayer().getParty().getMembers().size() > 3) {
                            cm.sendOk("3인 이하 파티만 입장할 수 있다네.");
                            cm.dispose();
                            return;
                        } else if (!cm.isLeader()) {
                            cm.sendOk("파티장만이 입장을 신청할 수 있습니다.");
                            cm.dispose();
                            return;
                        } else if (!cm.allMembersHere()) {
                            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
                            cm.dispose();
                            return;
                        }
                        cm.resetMap(912080100);
                        cm.getPlayer().getMap().killAllMonsters(true);
                        cm.warpMap(912080100,0);
                        cm.dispose();
                        break;
                    case 1:
                        var msg = "오, 요리사 복장을 받을만큼 많이 요리를 만든 사람인가? 그렇다면 당연히 요리사 복장을 줘야지.";
                        msg += "요리사 자격증은 가져 온 거겠지?" + enter;
                        msg += "#L0#요리사 옷을 주세요(요리사 자격증 2개이상)" + enter;
                        msg += "#L1#요리사 모자을 주세요(요리사 자격증 3개이상)" + enter;
                        cm.sendSimple(msg);
                        break;
                }
            }
        }
    }
}

function sendByType(type, type2, text) {
    switch (type) {
        case "next":
            cm.sendNextS(text, type2);
            break;
        case "nextprev":
            cm.sendNextPrevS(text, type2);
            break;
    }
}
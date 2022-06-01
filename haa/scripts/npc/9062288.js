/*


	엔피시아이디 : 

	엔피시 이름 : 

	엔피시가 있는 맵 :  :  ()

	엔피시 설명 : 일일 퀘스트


*/
var status = -1;

var date = new Date();
var day = date.getDay();
switch (day) {
    case 0:
        week = "일"
        reqitem = [4034875,4034876]; //필요한 아이템
        reqitemea = [2000,2000]; //필요한 아이템 개수
        reward = [4001716, 4310266]; //클리어시 받을 아이템
        rewardea = [5,50]; //클리어시 받을 아이템 개수
        break;
    case 1:
        week = "월"
        reqitem = [40006,4034876]; //필요한 아이템
        reqitemea = [2000,2000]; //필요한 아이템 개수
        reward = [2632800, 4310266]; //클리어시 받을 아이템
        rewardea = [30,50]; //클리어시 받을 아이템 개수
        break;
    case 2:
        week = "화"
        reqitem = [4000685,4009433]; //필요한 아이템
        reqitemea = [2000,2000]; //필요한 아이템 개수
        reward = [4310308, 4310266]; //클리어시 받을 아이템
        rewardea = [30,50]; //클리어시 받을 아이템 개수
        break;
    case 3:
        week = "수"
        reqitem = [4000685,4009433]; //필요한 아이템
        reqitemea = [2000,2000]; //필요한 아이템 개수
        reward = [4031227, 4310266]; //클리어시 받을 아이템
        rewardea = [5,50]; //클리어시 받을 아이템 개수
        break;
    case 4:
        week = "목"
        reqitem = [4000644,4000645]; //필요한 아이템
        reqitemea = [2000,2000]; //필요한 아이템 개수
        reward = [4310237, 4310266]; //클리어시 받을 아이템
        rewardea = [100,50]; //클리어시 받을 아이템 개수
        break;
    case 5:
        week = "금"
        reqitem = [4000646,4000647]; //필요한 아이템
        reqitemea = [2000,2000]; //필요한 아이템 개수
        reward = [2435719, 4310266]; //클리어시 받을 아이템
        rewardea = [100,50]; //클리어시 받을 아이템 개수
        break;
    case 6:
        week = "토"
        reqitem = [4000646,4000647]; //필요한 아이템
        reqitemea = [2000,2000]; //필요한 아이템 개수
        reward = [5068305, 4310266]; //클리어시 받을 아이템
        rewardea = [1,60]; //클리어시 받을 아이템 개수
        break;
    default:
}

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
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        var text = ""
        if (cm.getPlayer().getKeyValue(20, "talak") != day) {
            cm.getPlayer().setKeyValue(20, "talak", day);
            cm.getPlayer().setKeyValue(20, "talak1", 0);
        }
        text += "안녕하세요 #b#h0##k님\r\n"
        text += "오늘은 #b" + week + "요일#k 이군요.\r\n#b"
        if (cm.getPlayer().getKeyValue(20, "talak1") == 2) {
            text += "#L0#퀘스트를 완료하시겠습니까?#l\r\n"
        } else {
            text += "#L0#요일에 맞는 퀘스트를 내드리겠습니다.#l\r\n"
        }
        text += "#L99#보상보기#l\r\n"
        cm.sendSimple(text);
    } else if (status == 1) {
        var text = ""
        if (selection == 0) {
            if (cm.getPlayer().getKeyValue(20, "talak1") == 1) {
                text += "#fs11#이미 #b" + week + "요일#k의 일일 퀘스트를 완료하였습니다."
                cm.sendOk(text);
                cm.dispose();
                return;
            } else {
                text += "#b"
                for (i = 0; i < reqitem.length; i++) {
                    text += "#fs11##i" + reqitem[i] + "# #z" + reqitem[i] + "# " + reqitemea[i] + "개\r\n"
                    if (i == (reqitem.length - 1)) {
                        text += "\r\n"
                    }
                }
                if (cm.getPlayer().getKeyValue(20, "talak1") == 2) {
                    text += "#fs11##k입니다. 지금 완료하시겠습니까?"
                } else {
                    text += "#fs11##k입니다. 지금 시작하시겠습니까?"
                }
                cm.sendYesNo(text);
            }
        } else if (selection == 99) {
        reward = [4310266,4001716,4310237,2435719]; //클리어시 받을 아이템
        rewardea = [1,5,20,30,40,50,50]; //클리어시 받을 아이템 개수
            text += "#fs11#보상목록입니다.\r\n\r\n"
            text += "#i4310266#,#i4001716#,#i4310237#,#i2435719#,#i2632800#\r\n"
            cm.sendOk(text);
            cm.dispose();
            return;
        }
    } else if (status == 2) {
        var text = ""
        var ready = true;
        if (cm.getPlayer().getKeyValue(20, "talak1") == 2) {
            for (i = 0; i < reqitem.length; i++) {
                if (cm.getPlayer().getItemQuantity(reqitem[i], false) < reqitemea[i]) {
                    ready = false;
                    text += "#fs11##t" + reqitem[i] + "##n\r\n"
                    text += "#fs11##r" + cm.getPlayer().getItemQuantity(reqitem[i], false) + "#k / " + reqitemea[i] + "개\r\n\r\n"
                }
            }
            if (ready == false) {
                text += "#fs11#퀘스트를 완료하기 위한 조건이 부족합니다.\r\n"
            } else {
                cm.getPlayer().setKeyValue(20, "talak1", 1);
                text += "#fs11#퀘스트를 완료하였습니다.\r\n\r\n"
                text += "#fUI/UIWindow.img/Quest/reward#\r\n"
                for (i = 0; i < reward.length; i++) {
                    text += "#i" + reward[i] + "# #b#t" + reward[i] + "##k #b" + rewardea[i] + "#k개\r\n";
                }
                for (i = 0; i < reqitem.length; i++) {
                    cm.gainItem(reqitem[i], -(reqitemea[i]));
                }
                for (i = 0; i < reward.length; i++) {
                    cm.gainItem(reward[i], rewardea[i]);
                }
            }
        } else {
            cm.getPlayer().setKeyValue(20, "talak1", 2);
            text += "#fs11#퀘스트를 수락하였습니다.\r\n"
        }
        cm.sendOk(text);
        cm.dispose();
        return;
    }
}

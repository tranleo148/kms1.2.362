importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.database);

importPackage(java.sql);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.math);
var status = -1;
//40656
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
t=0;
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
	talk = "안녕하신가, 자네에게 내가 재밌는 퀴즈를 하나 내 도록 하겠네. 이 퀴즈는 다음에 있을 업데이트와 관련이 될수도 있으니 잘 맞춰보게나..\r\n\r\n"
	talk+= "다음의 숫자는 자네에게 어떤것을 의미하는가?\r\n"
	talk+= "#fs25##fn맑은 고딕##e"+cm.getPlayer().getStat().dropBuff+"#n#fn##fs#\r\n\r\n"
	talk+= "#r(힌트를 주자면 착용하고 있는 아이템을 다른 아이템으로 바꿔 보면서 숫자의 변화를 확인해 보시게나..)#k\r\n"
	if (cm.getClient().getKeyValue(20190706, "quiz1") == -1) {
		cm.sendGetText(talk);
	} else {
		talk+="\r\n자네는 이미 퀴즈에 답했으니 다음에 오시게나!";
		cm.sendOk(talk);
		cm.dispose();
		return;
	}
    } else if (status == 1) {
	if (selection == 1) {
		cm.getClient().setKeyValue(20190706, "quiz1", -1);
		cm.dispose();
		return;
	} else {
		Packages.scripting.NPCConversationManager.writeLog("quiz/quiz1.log", cm.getClient().getAccountName() + " | " + cm.getPlayer().getName() + " | " + cm.getText()+"\r\n", true);
		cm.getClient().setKeyValue(20190706, "quiz1", 1);
		cm.sendOk("퀴즈에 참여해 주셔서 고맙네.. 정답을 맞춘 사람은 나중에 좋은 일이 생길수도 있을게야..");
		cm.dispose();
		return;
	}
    }
}
importPackage(Packages.handling.channel);
importPackage(Packages.handling.world);

var status = -1;
var select;
var bonus = 0;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	cm.sendGetNumber("보상할 양 입력.\r\n1번 후원마다 같이 계산하지 말고 1회씩 다로 처리할 것.", 0, 0, 100000);
    } else if (status == 1) {
	select = selection;
	cm.sendGetNumber("캐릭터 이름 입력. 캐릭터가 접속중이여야함.", 0, 0, 100000);
    } else if (status == 2) {
	cm.dispose();
	ch = World.Find.findChannel(cm.getText());
	if (ch != -1)
	{
	switch (cm.getPlayer().getKeyValue(6, "2435718_rate")) {
		case 8:
		bonus = 1.1;
		break;
		case 7:
		bonus = 1.3;
		break;
		case 6:
		bonus = 1.5;
		break;
		case 5:
		bonus = 1.7;
		break;
		case 4:
		case 3:
		case 2:
		case 1:
		bonus = 2.0;
		break;
		default:
		bonus = 1.0;
		break;
	}
	victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(cm.getText());
	victim.setKeyValue(6, "2435718_value", cm.getPlayer().getKeyValue(6, "2435718_value") + 1);
	victim.setKeyValue(5304, "2435718", cm.getPlayer().getKeyValue(5304, "2435718") + (select * bonus));
	victim.dropMessage(-1, "후원 보상이 지급되었습니다.");
	cm.sendOk("아이템이 지급 완료되었습니다.");
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 1) {
		cm.setKeyValue(6, "2435718_rate", "9");
	}
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 3) {
		cm.setKeyValue(6, "2435718_rate", "8");
	}
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 5) {
		cm.setKeyValue(6, "2435718_rate", "7");
	}
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 7) {
		cm.setKeyValue(6, "2435718_rate", "6");
	}
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 10) {
		cm.setKeyValue(6, "2435718_rate", "5");
	}
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 13) {
		cm.setKeyValue(6, "2435718_rate", "4");
	}
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 15) {
		cm.setKeyValue(6, "2435718_rate", "3");
	}
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 20) {
		cm.setKeyValue(6, "2435718_rate", "2");
	}
	if (cm.getPlayer().getKeyValue(6, "2435718_value") >= 30) {
		cm.setKeyValue(6, "2435718_rate", "1");
	}
	}
	else
	{
	cm.sendOk(cm.getText() + " 유저는 현재 서버에 접속중이지 않습니다.");
	}
    }
}
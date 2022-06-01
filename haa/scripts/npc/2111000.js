
/* 이 엔피시는 아침맵 전용 엔피시 입니다. */

importPackage(Packages.launch.world);
importPackage(Packages.packet.creators);
importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.launch.world);
importPackage(Packages.handling.world);
importPackage(Packages.packet.creators);
importPackage(Packages.tools.packet);
importPackage(java.util);
importPackage(java.lang);

var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
	if(cm.getPlayer().getMapId() != 234567899) {
	cm.dispose(); return;
	}
	map = cm.getClient().getChannelServer().getMapFactory().getMap(234567899);

		if(cm.getPlayer().isDead) {
			cm.dispose(); return;
		}
			if(cm.getPlayer().isVoting) {
			names = map.names.split(",");
				var text = map.names+"투표하기 원하는 사람을 선택 해 주세요.\r\n";
					for(i=0;i< names.length; i++) {
						try {
						text += "#L"+i+"##b"+names[i]+"#k";
						if(cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(names[i]).isDead) {
							text += "[사망]#l\r\n";
						} else {
							text += "#l\r\n";
						}
							}catch(e){
							}
					}
			} else {
				cm.sendOk("현재는 투표시간이 아닙니다.");
				cm.dispose(); return;
			}
			cm.sendSimple(text);
    } else if(status == 1) {
	sel = selection;
	if(cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(names[sel]).isDead) {
		cm.sendOk("사망한 사람을 투표할 수 없습니다."); cm.dispose(); return;
	} else {
		cm.sendYesNo("정말 #b"+names[sel]+"#k님 을(를) 선택하시겠습니까?");
	}
    } else if(status == 2) {
	if(cm.getPlayer().isVoting) {
		cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(names[sel]).voteamount += 1;
		cm.getPlayer().isVoting = false;
		
		map.broadcastMessage(CWvsContext.serverNotice(11, names[sel]+"님이 한표를 받으셨습니다."));
		cm.sendOk("투표를 완료하였습니다.");
		cm.dispose(); return;
	} else {
		cm.sendOk("당신은 투표권이 없습니다.");
		cm.dispose(); return;
	}
    }
}
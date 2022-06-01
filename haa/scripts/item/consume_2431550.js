importPackage(Packages.constants);

var status = -1, select;
var inz = new Array(1122372, 1122373, 1122374, 1122375, 1122376, 1122377);

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
    if (status == 1 && selection < 100) {
	status = 2;
    }
    if (status == 3 && selection == 1) {
	status = 1;
    }
    if (status == 0) {
	txt = "현재 직업이 착용 가능한 장비를 우선 추천해 드립니다.\r\n받으실 방어구를 선택해 주세요.\r\n#b";
	if ((GameConstants.isWarrior(cm.getPlayer().getJob()) && !GameConstants.isDemonAvenger(cm.getPlayer().getJob())) || (cm.getPlayer().getJob() >= 500 && cm.getPlayer().getJob() <= 512) || (cm.getPlayer().getJob() >= 530 && cm.getPlayer().getJob() <= 532)|| (cm.getPlayer().getJob() >= 1500 && cm.getPlayer().getJob() <= 1512) || (cm.getPlayer().getJob() >= 2500 && cm.getPlayer().getJob() <= 2512)) {
        	txt += "#L0##i" + inz[0] + ":##z" + inz[0] + ":#";
        } else if (GameConstants.isMagician(cm.getPlayer().getJob())) {
        	txt += "#L1##i" + inz[1] + ":##z" + inz[1] + ":#";
        } else if (GameConstants.isArcher(cm.getPlayer().getJob()) || (cm.getPlayer().getJob() >= 520 && cm.getPlayer().getJob() <= 522) || (cm.getPlayer().getJob() >= 3500 && cm.getPlayer().getJob() <= 3512) || (cm.getPlayer().getJob() >= 6500 && cm.getPlayer().getJob() <= 6512)) {
        	txt += "#L2##i" + inz[2] + ":##z" + inz[2] + ":#";
        } else if (GameConstants.isThief(cm.getPlayer().getJob())) {
        	txt += "#L3##i" + inz[3] + ":##z" + inz[3] + ":#";
        } else if (GameConstants.isDemonAvenger(cm.getPlayer().getJob())) {
        	txt += "#L4##i" + inz[4] + ":##z" + inz[4] + ":#";
        } else if (GameConstants.isXenon(cm.getPlayer().getJob())) {
        	txt += "#L5##i" + inz[5] + ":##z" + inz[5] + ":#";
        }
	txt += "\r\n\r\n#L100#전체 아이템 리스트를 본다.\r\n#L101#사용 취소";
	cm.sendSimple(txt);
    } else if (status == 1) {
	if (selection == 100) {
		txt = "원하시는 방어구를 선택해 주세요.\r\n#b";
		for (i = 0; i < inz.length; i++) {
			txt += "#L" + i + "##i" + inz[i] + ":##z" + inz[i] + ":#\r\n";
		}
		cm.sendSimple(txt);
	} else if (selection == 101) {
		cm.dispose();
	}
    } else if (status == 2) {
	select = selection;
	cm.sendSimple("받고 싶은 장비가 이 장비가 확실한가요?\r\n#i" + inz[selection] + ":# #z" + inz[selection] + ":#\r\n\r\n#e#r※주의※#k#n\r\n한 번 조각을 아이템으로 교환하면 #r상자가 삭제#k되며, 다시 다른 아이템으로 교환할 수 없습니다.\r\n\r\n#b#L0#네 맞습니다.\r\n#L1#아닙니다. 다시 선택하겠습니다.");
    } else if (status == 3) {
	cm.dispose();
	cm.gainItem(inz[select], 1);
	cm.gainItem(2431550, -1);
	cm.sendOk("아이템이 지급되었습니다.");
    }
}
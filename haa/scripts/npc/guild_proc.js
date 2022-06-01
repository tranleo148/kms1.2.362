/* guild creation npc */
var status = -1;
var sel;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;

    if (status == 0)
	cm.sendSimple("길드를 만들고 싶은가? 혹은 길드 관련 업무를 위해서 찾아왔는가? 원하는 것을 말해보게.\r\n\r\n#b#L0#길드를 만들고 싶습니다.#l\r\n#L1#길드를 해체합니다.#l\r\n#L2#길드 최대인원을 늘리고 싶습니다. (최대 100명)#l\r\n#L3#길드 최대인원을 늘리고 싶습니다. (최대 200명)#l#k");
    else if (status == 1) {
	sel = selection;
	if (selection == 0) {
	    if (cm.getPlayerStat("GID") > 0) {
		cm.sendOk("흐음.. 이미 길드에 가입되어 있는 것 같은데?");
		cm.dispose();
	    } else
		cm.sendYesNo("길드 제작 수수료는 #b1,500,000 메소#k라네, 정말 만들어 보고 싶은가?");
	} else if (selection == 1) {
	    if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
		cm.sendOk("길드장만이 길드를 해체할 수 있다네.");
		cm.dispose();
	    } else
		cm.sendYesNo("길드를 해체하고 싶은가....? 지금 해체하게 된다면 절대 되돌릴 수 없다네.. 또, 모아뒀던 GP는 모두 사라지게 된다네. 길드 해체는 신중하게 선택하도록 하게나. 다시 한번 생각해보기 바라네. 정말 길드를 해체하고 싶은가?");
	} else if (selection == 2) {
	    if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
		cm.sendOk("길드장만이 길드 인원을 늘릴 수 있다네.");
		cm.dispose();
	    } else
		cm.sendYesNo("길드 최대 인원 추가 비용은 #b50만#k 메소 라네. 지금 추가하면 최대 인원이 5명 만큼 더 늘어날걸세. 정말 최대 인원을 늘려보고 싶은가?");
	} else if (selection == 3) {
	    if (cm.getPlayerStat("GID") <= 0 || cm.getPlayerStat("GRANK") != 1) {
		cm.sendOk("길드장만이 길드 인원을 늘릴 수 있다네.");
		cm.dispose();
	    } else
		cm.sendYesNo("길드 최대 인원 추가 비용은 #b2,000#k 길드포인트 라네. 지금 추가하면 최대 인원이 5명 만큼 더 늘어날걸세. 정말 최대 인원을 늘려보고 싶은가?");
	}
    } else if (status == 2) {
	if (sel == 0 && cm.getPlayerStat("GID") <= 0) {
	    cm.genericGuildMessage(3);
	    cm.dispose();
	} else if (sel == 1 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
	    cm.disbandGuild();
	    cm.dispose();
	} else if (sel == 2 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
	    cm.increaseGuildCapacity(false);
	    cm.dispose();
	} else if (sel == 3 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
	    cm.increaseGuildCapacity(true);
	    cm.dispose();
	}
    }
}
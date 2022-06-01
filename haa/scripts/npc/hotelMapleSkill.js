var status = -1;
var idx, skill, req, sp;

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
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
	idx = cm.getNpc();
	skill = 80002927 + idx;
	req = parseInt(cm.getPlayer().getKeyValue(501046, idx)) + 1;
	sp = cm.getPlayer().getKeyValue(501045, "sp");
	if (parseInt(sp) < req) {
	   talk = "스킬 포인트가 부족합니다.";
           cm.sendOk(talk, 9062297);
	   cm.dispose();
	} else if (req > 3) {
	   talk = "이미 최고레벨에 도달한 스킬입니다.";
           cm.sendOk(talk, 9062297);
	   cm.dispose();
	} else {
           talk = "#b#e[#q" + skill + "#]#n#k 스킬을 올리시겠습니까?\r\n\r\n\r\n";
	   talk+= " - 필요한 스킬 포인트 : #r#e" + cm.getPlayer().getKeyValue(501046, idx) + "#n#k\r\n";
	   talk+= " - 보유한 스킬 포인트 : #e#b" + sp;
           cm.sendYesNo(talk, 9062297);
	}
    } else if (status == 1) {
	cm.dispose();
	cm.getPlayer().setKeyValue(501046, idx, "" + req);
	cm.getPlayer().setKeyValue(501045, "sp", "" + (sp - req));
	cm.getPlayer().getStat().recalcLocalStats(cm.getPlayer());
//	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.detailShowInfo("#q" + skill + "# 스킬이 " + req + " 레벨이 되었습니다!", false));
        cm.sendOk("#b#e#q" + skill + "##n#k 스킬이 #r#e" + req + " 레벨#n#k이 되었습니다.", 9062297);
    }
}
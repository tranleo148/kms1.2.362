importPackage(java.text);

var seld = -1;
var stage = 0;
var item = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        txt = "수고했어! 생각보다 대단한 실력을 가졌는걸?\r\n\r\n";
	if (cm.getPlayer().getFrittoEgg() != null) {
	    txt+= "#b#L1#드래곤의 알 훔치기 보상 받기#l\r\n";
	}
	if (cm.getPlayer().getFrittoEagle() != null) {
	    txt+= "#b#L2#독수리 사냥 보상 받기#l\r\n";
	}
	if (cm.getPlayer().getFrittoDancing() != null) {
	    txt+= "#b#L3#구애의 춤 보상 받기#l\r\n";
	}
	txt+= "#b#L4#원래 왔던 곳으로 돌려보내줘.";
	cm.sendSimple(txt);
    } else if (status == 1) {
	seld = sel;
	if (cm.getPlayer().getInventory(2).isFull()) {
	    cm.sendOk("소비창에 공간이 부족해. 1칸 이상 비우고 다시 말을 걸어줄래?");
	    cm.dispose();
	    return;
	}
	switch (seld) {
            case 1:
		stage = cm.getPlayer().getKeyValue(15042, "Stage");
		cm.getPlayer().setKeyValue(15042, "Stage", "0");
		cm.getPlayer().setFrittoEgg(null);
		cm.sendNextNoESC("휴~ 큰일 날 뻔 했군! 하마터면 #b드래곤#k에게 걸릴 뻔 했어!\r\n살금살금 올라가는 너의 모습을 보니 너도 나처럼 훌륭한 현상금 사냥꾼이 될 것 같더군 하핫!");
		break;
	   case 2:
		stage = cm.getPlayer().getKeyValue(15141, "point");
		cm.getPlayer().setKeyValue(15141, "point", "0");
		cm.getPlayer().setFrittoEagle(null);
		cm.sendNextNoESC("하하! #b독수리 사냥#k은 어땠어?\r\n멀리서 총으로 쏘아 잡으니 생각보다 쉽지?");
		break;
	   case 3: // 대사 불확실
		stage = cm.getPlayer().getKeyValue(15143, "score");
		cm.getPlayer().setKeyValue(15143, "score", "0");
		cm.getPlayer().setFrittoDancing(null);
		cm.sendNextNoESC("휴~ 큰일 날 뻔 했군! 하마터면 #b양치기#k에게 걸릴 뻔 했어!\r\n열심히 춤을 추는 모습을 보니 너도 나처럼 훌륭한 현상금 사냥꾼이 될 것 같더군 하핫!");
		break;
	   case 4:
		cm.sendYesNo("정말 보상을 받지 않고 그냥 돌아가겠어?");
		break;
	}
    } else if (status == 2) {
	switch (seld) {
            case 1:
		switch (stage) {
		    case 2:
			item = 2434634;
			break;
		    case 3:
		    case 4:
			item = 2434635;
			break;
		    case 5:
		    case 6:
			item = 2434636;
			break;
		    default:
			item = 2434633;
			break;
		}
		cm.sendNextNoESC("이번에는 #b#e" + stage + "#n#k단계 까지 올라갔군?\r\n자 여기 너의 몫으로#i" + item + ":# #b#t" + item + "##k와 #b경험치#k를 줄게 다음에 또 보자고!");
		break;
	    case 2:
		cm.sendNextNoESC("형처럼 정면에서 싸우는 것 만이 능사는 아니지! 나처럼 머리를 쓸 줄 아는 사냥꾼이야 말로 진정한 사냥꾼이라 할 수 있다고!");
		break;
            case 3:
		switch (stage) {
		    case 3:
		    case 4:
		    case 5:
			item = 2434634;
			break;
		    case 6:
		    case 7:
		    case 8:
			item = 2434635;
			break;
		    case 9:
		    case 10:
			item = 2434636;
			break;
		    default:
			item = 2434633;
			break;
		}
		cm.sendNextNoESC("이번에는 #b#e" + stage + "#n#k번 성공했군?\r\n자 여기 너의 몫으로#i" + item + ":# #b#t" + item + "##k와 #b경험치#k를 줄게 다음에 또 보자고!");
		break;
	    case 4:
		cm.getPlayer().setFrittoEgg(null);
		cm.getPlayer().setFrittoEagle(null);
		cm.warp(parseInt(cm.getPlayer().getV("poloFritto")));
		cm.dispose();
		break;
	}
    } else if (status == 3) {
	switch (seld) {
            case 1:
		cm.dispose();
		cm.gainItem(item, 1);
		cm.warp(parseInt(cm.getPlayer().getV("poloFritto")));
		break;
	    case 2:
		switch (parseInt(stage / 100)) {
		    case 3:
		    case 4:
		    case 5:
			item = 2434634;
			break;
		    case 6:
		    case 7:
		    case 8:
			item = 2434635;
			break;
		    case 9:
		    case 10:
			item = 2434636;
			break;
		    default:
			item = 2434633;
			break;
		}
		cm.sendNextNoESC("이번에 사냥에서 #b#e" + stage + "#n#k점을 획득 했군?\r\n자 여기 너의 몫으로#i2434633:# #b#t2434633##k와 #b경험치#k를 줄게 다음에 또 보자고!");
		break;
            case 3:
		cm.dispose();
		cm.gainItem(item, 1);
		cm.warp(parseInt(cm.getPlayer().getV("poloFritto")));
		break;
	}
    } else if (status == 4) {
	switch (seld) {
            case 1:
		break;
	    case 2:
		cm.dispose();
		cm.gainItem(item, 1);
		cm.warp(parseInt(cm.getPlayer().getV("poloFritto")));
		break;
	}
    }
}
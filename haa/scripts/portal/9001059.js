importPackage(java.text);

var enter = "\r\n";
var seld = -1;

var iskill = false;
var EXP = 0;
var Meso = 0;
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
        iskill = cm.getPlayer().isFWolfKiller();
        Meso = cm.getFWolfMeso()
        EXP = cm.getFWolfEXP();

        if (cm.getPlayer().getBountyHunting() != null || cm.getPlayer().getDefenseTowerWave() != null) {
	    cm.sendNextS("너 또한 훌륭한 사냥꾼이군 그래... 수고했다. 다음에 인연이 되면 또 만나도록 하지", 1);
        } else if (!iskill)
            cm.sendSimpleS("너 또한 훌륭한 사냥꾼이군 그래... 수고했다." + enter + enter + "#b#L1#불꽃늑대 사냥 보상 받기" + enter + "#L2#원래 왔던 곳으로 돌려보내줘.", 1);
        else
            cm.sendNextS("흉악한 #r#e불꽃늑대#k#n를 퇴치하다니! 대단한 실력이군.", 1);
    } else if (status == 1) {
	if (cm.getPlayer().getBountyHunting() != null || cm.getPlayer().getDefenseTowerWave() != null) {
	    if (cm.getPlayer().getQuestStatus(16406) == 1) {
	        quest = cm.getPlayer().getQuestNAdd(Packages.server.quest.MapleQuest.getInstance(16406));
		if (quest != null) {
		    cm.getPlayer().updateQuest(quest, true);
		}
	    }
	    cm.warp(parseInt(cm.getPlayer().getV("poloFritto")));
	    cm.getPlayer().setBountyHunting(null);
	    cm.getPlayer().setDefenseTowerWave(null);
	    cm.dispose();
	    return;
        }
        seld = sel;
        switch (sel) {
            case 2:
                cm.sendSimpleS("#r#e불꽃늑대#k#n와의 전투 보상을 받지 않았군. 지금 나가게 되면 보상을 받을 수 없네. 괜찮나?" + enter + enter + "#b#L1#네" + enter + "#L2#아니오", 1);
                break;
	    default:
                if (!iskill)
                    cm.sendNextS("흉악한 #r불꽃늑대#k 앞에서도 전혀 주늑들지 않고 싸우던 너의 모습 잘 보았다.", 1);
                else
                    cm.sendNextS("#r#e불꽃늑대#k#n는 우리 형제가 아주 오랜시간 추적하던 적이었지... 물론 녀석은 다시 나타나겠지만, 네 덕분에 한동안은 여행자들을 약탈할 수 없을 거다.", 1);
                break;
        }
    } else if (status == 2) {
       switch (seld) {
            case 1:
        	   damage = "적당한";
		item = 2434633;
        	   if (cm.getPlayer().getFWolfDamage() >= 75000000000) {
		damage = "치명적인";
		item = 2434636;
        	   } else if (cm.getPlayer().getFWolfDamage() >= 12500000000) {
		damage = "막대한";
		item = 2434635;
       	   } else if (cm.getPlayer().getFWolfDamage() >= 1250000000) {
		damage = "상당한";
		item = 2434634;
       	   }
	if (!cm.canHold(2434636, 1)) {
		cm.sendOk("인벤토리에 공간이 부족합니다.");
		cm.dispose();
		return;
	}
           if (!iskill) {
            	cm.sendNextS("너는 불꽃늑대에게 #e#b" + damage + "#k#n 데미지를 주었군!" + enter + "우선 너의 기여도에 걸맞은 #e#b경험치#k#n를 주겠다.", 1);
	    } else {
            cm.sendOkS("하지만 #r#e불꽃늑대#k#n를 영원히 없애는 건 불가능하다. 녀석이 또 나타나면 다시 만나지. 잘가라.", 1);
            cm.warp(parseInt(cm.getPlayer().getV("fireWolf")));
            cm.gainExp(EXP);
            cm.gainMeso(Meso);
			
	    cm.gainItem(2434636, 1);
            cm.dispose();
	   }
	   break;
	case 2:
                if (sel == 1) {
                    cm.warp(parseInt(cm.getPlayer().getV("fireWolf")));
                }
                cm.dispose();
                break;
       }
    } else if (status == 3) {
        if (!iskill) {
            var msg = "앞으로 너의 뒤를 이어 많은 용사들이 #r#e불꽃늑대#k#n에게 피해를 줄 것이다. #r#e불꽃늑대#k#n가 다른 용사들의  손에 죽게 된다면 너에게도 #b#e기여도#k#n에 따라 #b#e보상#k#n을 줄테니 나의 연락을 잘 기다리고 있도록.";
            cm.sendNextS(msg, 1);
        }
    } else if (status == 4) {
	cm.dispose();
	cm.warp(parseInt(cm.getPlayer().getV("fireWolf")));
	cm.gainItem(item, 1);
        }
}
var status;
var fatigue = 10;
var limited = false;
var limitedcount = 3;
importPackage(Packages.client.stats);
importPackage(java.lang);
function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    return;
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
	    if (cm.getPlayer().getKeyValue("Today_EnergyDrink") == null) {
                cm.getPlayer().setKeyValue("Today_EnergyDrink", "0");
            }
            if (limited) {
                var count = Integer.parseInt(cm.getPlayer().getKeyValue("Today_EnergyDrink"));
                if (count == limitedcount) {
                    cm.getPlayer().message(5, "피로 회복제를 이미 3번 모두 사용하셨습니다.");
                    cm.dispose();
                    return;
                }
                count++;
                cm.getPlayer().setKeyValue("Today_EnergyDrink", count+"");
                cm.dispose();
            }
            cm.getPlayer().getProfession().addFatigue(-fatigue);
            cm.getPlayer().updateSingleStat(PlayerStat.FATIGUE, cm.getPlayer().getProfession().getFatigue());
            cm.getPlayer().message("피로도가 "+fatigue+"만큼 회복되었습니다.");
            cm.gainItem(2430213, -1);
        } else { 
            cm.dispose();
        }
    }
}
importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server); 
importPackage(Packages.scripting);
importPackage(Packages.client);

var status = -1;
var index = 0;
var itemid = 0;
var count = 0;
var item = [[2430046, 1], [2430046, 1], [2430046, 3], [2430046, 1], [2430046, 1], [2430046, 3], [2049704, 3], [2048759, 100], [2631878, 1], [2049704, 3], [2048757, 50], [5062503, 100], [4310012, 2500], [2633352, 1000], [2631527, 5], [4310012, 2500], [2048768, 30], [2644002, 1]]

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
	}
	if (mode == 1) {
		d = status;
		status++;
	}

	if (status == 0) {
		index = cm.getPlayer().getSkillCustomValue0(501378);
		itemid = item[index][0]
		count = item[index][1]
		cm.sendYesNoS("\r\n지금 즉시 #e<블루밍 플라워> 보상#n을 받아 가시겠나여?\r\n\r\n#fUI/UIWindow2.img/Quest/quest_info/summary_icon/reward#\r\n#b#e#i"+itemid+":# #t"+itemid+":# : "+count+"개\r\n\r\n\r\n#r※ 보상은 월드당 1회만 받을 수 있습니다.\r\n※ 이벤트 기간 : 2021년 6월 16일(수) 23시 59분까지", 4, 9062508);
	} else if (status == 1) {
		cm.sendGetText("\r\n정말 현재 캐릭터로 받으시려면 아래 입력창에 \r\n'#r#e수령한다#k#n'를 입력해 주세여!!\r\n#r(예시) 수령한다#k\r\n\r\n\r\n - #e#fs16#<현재 캐릭터> #b"+cm.getPlayer().getLevel()+"레벨 #h0# #n#k#fs12#\r\n\r\n", 9062508);
	} else if (status == 2) {
		text = cm.getText().replaceAll(" ", "");;
        if (text.contains("수령한다")) {
			var s = cm.getPlayer().getKeyValueStr(501367, "getReward");
			var ab = s.split("");
			var fi = "";
			ab[index] = 1;
			for (var a = 0; a < ab.length; a++) {
				fi += ab[a];
			}
			cm.getPlayer().setKeyValue(501367, "getReward", fi);
			cm.getClient().setKeyValue("getReward", fi);
			cm.getPlayer().dropMessage(5, "<블루밍 플라워> 보상이 지급되었습니다. 메이플 보관함을 확인해주세요.");
			cm.getPlayer().gainCabinetItem(itemid, count);
			cm.dispose();
		} else {
			cm.sendOkS("정확히 입력하지 않으면 보상을 수령하실 수 없어여!!!", 4, 9062508);
			cm.dispose();
		}
	}
}
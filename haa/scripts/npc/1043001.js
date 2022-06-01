var enter = "\r\n";
var year, month, date2, date, day
var hour, minute;

var questt = "jump_2"; // jump_고유번호

var reward = [
	{'itemid' : 2432423, 'qty' : 100},
	{'itemid' : 4001715, 'qty' : 3}
	
]

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
		getData();
		if (cm.getClient().getKeyValue(date, questt) > 0) {
			cm.sendOk("#fs11#이미 오늘의 보상을 지급하였습니다..");
			cm.dispose();
			return;
		}
		var msg = "인내의 숲 클리어를 축하드립니다."+enter;
		msg += "클리어 보상을 받으시겠습니까?";
		cm.sendYesNo(msg);
	} else if (status == 1) {
		if (cm.getClient().getKeyValue(date, questt) > 0) {
			cm.sendOk("#fs11#오늘의 보상을 지급하였습니다..");
			cm.dispose();
			return;
		}
		
		cm.getClient().setKeyValue(date, questt, "1");
		for (i = 0; i < reward.length; i++)
			cm.gainItem(reward[i]['itemid'], reward[i]['qty']);
                                                cm.getPlayer().AddStarDustCoin(100);

		cm.sendOk("오늘의 보상이 지급되었습니다.");
		cm.dispose();
	}
}

function getData() {
   time = new Date();
   year = time.getFullYear();
   month = time.getMonth() + 1;
   if (month < 10) {
      month = "0"+month;
   }
   date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
   date = year+""+month+""+date2;
   day = time.getDay();
   hour = time.getHours();
   minute = time.getMinutes();
}
var status;
var enter = "\r\n";

var reward = [
	[2438681, 5],
	[5068300, 3],
	[2435719, 10],
	[2450064, 1],
        [2434584, 5],
        [2434585, 5],
        [2434586, 5],
        [2434587, 15]
]

function start() {
    status = -1;
    action(1, 1, 0);
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
		var msg = "#fs11#다음과 같은 아이템을 획득하셨습니다."+enter;
		for (i = 0; i < reward.length; i++) {
			cm.gainItem(reward[i][0], reward[i][1]);
			msg += "#i"+reward[i][0]+"##b#z"+reward[i][0]+"# "+reward[i][1]+"개#k"+enter;
		}
		cm.sendOk(msg);
		cm.gainItem(2438683, -1);
		cm.dispose();
    	}
}

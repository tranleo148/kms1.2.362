var status;
importPackage(Packages.server);
var rand = Randomizer.rand(1,1000);

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
	cm.gainItem(2434636, -1);
	if(rand >= 1 && rand < 310){cm.gainItem(4310266, 10);}
      else if(rand >= 310 && rand < 620){cm.gainItem(4310237, 10);}
      else if(rand >= 620 && rand < 930){cm.gainItem(4009005, 10);}
      else if(rand >= 930 && rand < 960){cm.gainItem(4001716, 1);}
      else if(rand >= 960 && rand < 990){cm.gainItem(2048717, 10);}
      else if(rand == 1000){cm.gainItem(5062006, 1);}
	cm.dispose();
	}
}

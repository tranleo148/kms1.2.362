﻿var s1;
var xy = [[-224, -380], [-58, -380], [124, -380], [300, -380], [530, -380], [2320, -380], [2510, -380], [2730, -380], [2860, -380], [3050, -380], [-1287, -380], [-1042, -380], [-939, -380], [-796, -380], [-583, -380], [3363, -380], [3511, -380], [3748, -380], [3964, -380], [4177, -380], [-1530, -890], [-1270, -890], [-980, -890], [-870, -890], [-700, -890], [3536, -890], [3711, -890], [3942, -890], [4189, -890], [4365, -890], [1120, -800], [1400, -800], [1730, -800], [1413, -970], [1413, -1100], [1113, -1650], [1422, -1650], [1720, -1650], [1370, -1830], [1646, -1950]];
var setting = [[9833245, 9833246, 9833247, 9833248, 9833249, 9833250, 9833251, 9833252, 9833253,
		 9833254, 9833255, 9833256, 9833257, 9833258, 9833259, 9833260, 9833261
        ],
        [9833030, 9833031, 9833032, 9833033, 9833034, 9833035, 9833036, 9833037, 9833038,
		 9833039, 9833040, 9833041, 9833042, 9833043, 9833044, 9833045, 9833046
        ],
        [9833050, 9833051, 9833052, 9833053, 9833054, 9833055, 9833056, 9833057, 9833058,
		 9833059, 9833060, 9833061, 9833062, 9833063, 9833064, 9833065, 9833066
        ]
]
var maps = [921171200, 921170050, 921170100];

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
        status++;
    }

    if (status == 0) {
	if (cm.getPlayer().getMapId() == 450002024) {
		cm.dispose();
		cm.openNpc(3003160);
	} else
		cm.sendSimple("#b#e<배고픈 무토>#n#k\r\n #b무토#k를 도와 #r굴라#k를 물리치는 걸 도와 주시겠어요?\r\n#r※10분간 플레이가 진행되며 중간에 이탈할 수 없습니다.#k\r\n#b#L0# <배고픈 무토>에 입장한다.#l\r\n#L1# 시미아 에게서 설명을 듣는다.#l\r\n#L2#오늘의 남은 도전횟수를 확인한다.#l");  
    } else if (status == 1) {
	s1 = selection;
	if (s1 == 0) {
		cm.sendSimple("#b#e<배고픈 무토>#n#k\r\n어떤 #b난이도#k로 도전 하시겠어요?#b\r\n#L0#쉬운 난이도#l\r\n#L1#일반 난이도#l\r\n#L2#어려운 난이도#l#k");
	} else {
	    cm.dispose();
	}
    } else if (status == 2) {
	cm.dispose();
	if (s1 == 0) {
		if (cm.getPlayer().getParty() == null) {
			cm.sendOk("파티를 맺어야 입장이 가능합니다.");
		} else if (!cm.isLeader()) {
			cm.sendOk("파티장이 입장을 시도할 수 있습니다.");
		} else if (!cm.allMembersHere()) {
			cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
		} else if (!cm.isBossAvailable("muto", 3)) {
            		talk = "파티원 중 "
            		for (i = 0; i < cm.BossNotAvailableChrList("muto", 3).length; i++) {
                		if (i != 0) {
                    		talk += ", "
                		}
                		talk += "#b#e" + cm.BossNotAvailableChrList("muto", 3)[i] + ""
            		}
            		talk += "#k#n님이 오늘 입장했습니다. 배고픈 무토는 하루에 3번만 도전하실 수 있습니다.";
            		cm.sendOk(talk);
		} else if (cm.getPlayerCount(maps[selection]) > 0) {
			cm.sendOk("이미 누군가가 입장했습니다.");
		} else {
			cm.getEventManager("Muto").startInstance_Party("" +maps[selection], cm.getPlayer());
		}
	}
    }
}
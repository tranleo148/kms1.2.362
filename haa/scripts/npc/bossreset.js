importPackage(Packages.packet.creators);
importPackage(Packages.client.items);
importPackage(Packages.server.items);
importPackage(Packages.launch.world);
importPackage(Packages.main.world);
importPackage(Packages.database);
importPackage(java.lang);
importPackage(Packages.server);
importPackage(Packages.handling.world);
importPackage(Packages.tools.packet);

var status = -1;

var choice = -1;

var k, key;

var icon;

var bosslist = [ //난이도, 보스 이름, 보스 키, 초기화권코드, 초기화권 소모 갯수
    //노멀
    ["Normal", "[Normal] 자쿰", "Normal_Zakum", 500, 1, 1],
    ["Normal", "[Normal] 혼테일", "Normal_Horntail", 500, 1, 2],
    ["Normal", "[Normal] 카웅", "Normal_Kawoong", 500, 1, 21],
    ["Normal", "[Normal] 힐라", "Normal_Hillah", 500, 1, 3],
    ["Normal", "[Normal] 반 레온", "Normal_VonLeon", 500, 1, 8],
    ["Easy", "[Easy] 아카이럼", "Easy_Arkarium", 500, 1, 9],
    ["Normal", "[Normal] 핑크빈", "Normal_Pinkbean", 500, 1, 11],
    ["Normal", "[Normal] 시그너스", "Normal_Cygnus", 500, 1, 12],
    ["Normal", "[Normal] 매그너스", "Normal_Magnus", 500, 1, 10],
    ["Normal", "[Normal] 파풀라투스", "Normal_Populatus", 500, 1, 22],
    ["Normal", "[Normal] 피에르", "Normal_Pierre", 500, 1, 4],
    ["Normal", "[Normal] 반반", "Normal_VonBon", 500, 1, 5],
    ["Normal", "[Normal] 블러디퀸", "Normal_BloodyQueen", 500, 1, 6],
    ["Normal", "[Normal] 벨룸", "Normal_Vellum", 500, 1, 7],
    ["Normal", "[Normal] 스우", "Normal_Lotus", 500, 1, 13],
    ["Normal", "[Normal] 데미안", "Normal_Demian", 500, 1, 15],
    ["Normal", "[Normal] 루시드", "Normal_Lucid", 500, 1, 19],
    ["Normal", "[Normal] 윌", "Normal_Will", 500, 1, 23],
    ["Normal", "[Normal] 듄켈", "Normal_Dunkel", 500, 1, 27],
    ["Normal", "[Normal] 더스크", "Normal_Dusk", 500, 1, 1],
    //하드, 카오스
    ["Chaos", "[Chaos] 자쿰", "Chaos_Zakum", 500, 1, 1],
    ["Chaos", "[Chaos] 혼테일", "Chaos_Horntail", 500, 1, 2],
    ["Hard", "[Hard] 힐라", "Hard_Hillah", 500, 1, 3],
    ["Hard", "[Hard] 반 레온", "Hard_VonLeon", 500, 1, 8],
    ["Hard", "[Normal] 아카이럼", "Normal_Arkarium", 500, 1, 9],
    //Chaos_Pinkbean
    ["Hard", "[Hard] 매그너스", "Hard_Magnus", 500, 1, 10],
    ["Chaos", "[Chaos] 파풀라투스", "Chaos_Populatus", 500, 1, 22],
    ["Chaos", "[Chaos] 피에르", "Chaos_Pierre", 500, 1, 4],
    ["Chaos", "[Chaos] 반반", "Chaos_VonBon", 500, 1, 5],
    ["Chaos", "[Chaos] 블리디퀸", "Chaos_BloodyQueen", 500, 1, 6],
    ["Chaos", "[Chaos] 벨룸", "Chaos_Vellum", 500, 1, 7],
    ["Hard", "[Hard] 스우", "Hard_Lotus", 500, 1, 13],
    ["Hard", "[Hard] 데미안", "Hard_Demian", 500, 1, 15],
    ["Hard", "[Hard] 루시드", "Hard_Lucid", 500, 1, 19],
    ["Hard", "[Hard] 윌", "Hard_Will", 500, 1, 23],
    ["Chaos", "[Chaos] 더스크", "Chaos_Dusk", 500, 1, 26],
    ["Hard", "[Hard] 진 힐라", "Normal_JinHillah", 500, 1, 24],
    ["Hard", "[Hard] 검은마법사", "Black_Mage", 500, 1, 25],
    ["Hard", "[Hard] 세렌", "Hard_Seren", 500, 1, 28],
];

function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var say = "일일보스 입장횟수를 1회씩 늘려주는 엔피시입니다.\r\n" +
        "해당 보스를 일일 1회 입장하신 이후에만 사용 가능합니다.\r\n" +
        "어떤 보스의 입장횟수를 1회 늘리시겠습니까?";
        say += "\r\n\r\n#b#e[Normal]#k#n\r\n";
        for (var i = 0; i < bosslist.length; i++) {
            if (bosslist[i][0] == "Normal") {
	k = cm.getPlayer().getV(bosslist[i][2]);
	key = k == null ? 0 : Integer.parseInt(cm.getPlayer().getV(bosslist[i][2]));
	icon = "#fUI/UIWindow2.img/UserList/Main/Boss/BossList/"+bosslist[i][5]+"/Icon/normal/0#";
                say += "#L" + i + "# " + icon + " | 오늘 입장한 횟수 : " + key + " |  \r\n";
            }
        }
        say += "\r\n\r\n#r#e[Hard] Or [Chaos]#k#n\r\n\r\n";
        for (var i = 0; i < bosslist.length; i++) {
            if (bosslist[i][0] == "Hard" || bosslist[i][0] == "Chaos") {
	k = cm.getPlayer().getV(bosslist[i][2]);
	key = k == null ? 0 : Integer.parseInt(cm.getPlayer().getV(bosslist[i][2]));
icon = "#fUI/UIWindow2.img/UserList/Main/Boss/BossList/"+bosslist[i][5]+"/Icon/normal/0#";
                say += "#L" + i + "# " + icon + " | 오늘 입장한 횟수 : " + key + " |  \r\n";
            }
        }
        cm.sendSimple(say);
    } else if (status == 1) {
        choice = selection;
        cm.sendYesNo("정말로 " + bosslist[selection][1] + "의 일일 입장횟수를 1회 초기화 하시겠습니까?");
    } else if (status == 2) {
        key = k == null ? 0 : Integer.parseInt(cm.getPlayer().getV(bosslist[choice][2]));
	var va =String(key-1);
        if (cm.getPlayer().getDonationPoint() >= bosslist[choice][3] && key > 0) {
            cm.getPlayer().gainDonationPoint(-bosslist[choice][3]);
            cm.getPlayer().addKV(bosslist[choice][2], va);
            cm.sendOk(bosslist[choice][1] + "보스의 일일 입장횟수를 1회 늘렸습니다.");
            cm.dispose();
            return;
        } else {
            cm.sendOk("후원포인트가 모자르거나 해당 보스를 오늘 입장한 기록이 없습니다.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}
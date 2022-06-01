/*
색변 캐시 상자
*/
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.provider);
importPackage(Packages.server);
importPackage(Packages.constants);

검정 = "#fc0xFF191919#"

var itemsz0 = new Array(3010000, 3010001, 3010007, 3010008, 3010009, 3010018, 3010021, 3010025, 3010049, 3010055, 3010060, 3010061, 3010062, 3010063, 3010064, 3010065, 3010066, 3010067, 3010080, 3010081, 3010082, 3010083, 3010084, 3010086, 3010092, 3010097, 3010107, 3010108, 3010109, 3010110, 3010113, 3010114, 3010115, 3010129, 3010130, 3010131, 3010132, 3010133, 3010134, 3010154, 3010157, 3010186, 3010191, 3010194, 3010202, 3010203, 3010205, 3010206, 3010207, 3010208, 3010211, 3010215, 3010216, 3010224, 3010225, 3010257, 3010279, 3010288, 3010297, 3010298, 3010307, 3010314, 3010315, 3010316, 3010317, 3010318, 3010319, 3010320, 3010321, 3010322, 3010354, 3010364, 3010365, 3010368, 3010369, 3010370, 3010371, 3010373, 3010374, 3010375, 3010376, 3010377, 3010383, 3010390, 3010397, 3010402, 3010403, 3010404, 3010405, 3010406, 3010421, 3010423, 3010429, 3010430, 3010431, 3010432, 3010433, 3010434, 3010435, 3010436, 3010437, 3010438, 3010439, 3010440, 3010441, 3010442, 3010443, 3010444, 3010445, 3010446, 3010447, 3010448, 3010449, 3010450, 3010451, 3010452, 3010455, 3010457, 3010458, 3010459, 3010464, 3010465, 3010493, 3010500, 3010501, 3010502, 3010503, 3010504, 3010505, 3010506, 3010512, 3010513, 3010514, 3010515, 3010516, 3010517, 3010518, 3010519, 3010520, 3010521, 3010522, 3010523, 3010524, 3010525, 3010526, 3010530, 3010531, 3010532, 3010533, 3010534, 3010535, 3010536, 3010537);
//1번의자

var itemsz1 = new Array(3010538, 3010539, 3010540, 3010541, 3010542, 3010543, 3010544, 3010545, 3010546, 3010547, 3010548, 3010549, 3010550, 3010551, 3010552, 3010553, 3010554, 3010555, 3010556, 3010557, 3010558, 3010559, 3010560, 3010561, 3010562, 3010563, 3010564, 3010565, 3010566, 3010567, 3010568, 3010569, 3010570, 3010571, 3010572, 3010573, 3010574, 3010575, 3010576, 3010577, 3010578, 3010579, 3010580, 3010581, 3010582, 3010584, 3010585, 3010587, 3010589, 3010590, 3010592, 3010593, 3010596, 3010597, 3010598, 3010600, 3010601, 3010611, 3010612, 3010613, 3010622, 3010623, 3010624, 3010636, 3010637, 3010638, 3010639, 3010640, 3010641, 3010642, 3010643, 3010644, 3010651, 3010652, 3010653, 3010654, 3010655, 3010656, 3010659, 3010662, 3010672, 3010673, 3010674, 3010675, 3010676, 3010677, 3010678, 3010679, 3010680, 3010681, 3010682, 3010683, 3010685, 3010690, 3010691, 3010692, 3010693, 3010694, 3010695, 3010697, 3010700, 3010702, 3010703, 3010704, 3010705, 3010708, 3010719, 3010720, 3010721, 3010722, 3010723, 3010733, 3010734, 3010735, 3010742, 3010743, 3010744, 3010757, 3010760, 3010761, 3010766, 3010767, 3010783, 3010797, 3010798, 3010800, 3010801, 3010802, 3010803, 3010804, 3010810, 3010811, 3010812, 3010814, 3010815, 3010835, 3010836, 3010837, 3010838, 3010844, 3010851, 3010852, 3010854, 3010862, 3010863, 3010867, 3010868, 3010869, 3010870, 3010871, 3010872, 3010873, 3010874, 3010875, 3010878, 3010941, 3010942, 3010943, 3010946, 3010956, 3010957, 3010958, 3010959, 3010960, 3010961, 3010962, 3010964, 3010965, 3010976, 3010978, 3010979, 3010980);
//2번의자
var itemsz2 = new Array(3012017, 3014000, 3014001, 3014002, 3014003, 3014004, 3014006, 3014011, 3014019, 3014026, 3014027, 3014037, 3015000, 3015003, 3015004, 3015015, 3015016, 3015017, 3015018, 3015019, 3015020, 3015021, 3015022, 3015023, 3015024, 3015025, 3015026, 3015030, 3015031, 3015032, 3015033, 3015041, 3015042, 3015043, 3015044, 3015048, 3015049, 3015049, 3015050, 3015061, 3015062, 3015063, 3015064, 3015075, 3015081, 3015082, 3015083, 3015089, 3015090, 3015091, 3015092, 3015107, 3015109, 3015111, 3015112, 3015114, 3015115, 3015116, 3015117, 3015118, 3015119, 3015120, 3015155, 3015156, 3015157, 3015159, 3015160, 3015161, 3015167, 3015168, 3015171, 3015172, 3015173, 3015174, 3015175, 3015210, 3015223, 3015234, 3015235, 3015236, 3015238, 3015239, 3015243, 3015244, 3015245, 3015246, 3015247, 3015248, 3015263, 3015264, 3015272, 3015276, 3015277, 3015278, 3015279, 3015295, 3015296, 3015297, 3015298, 3015299, 3015300, 3015301, 3015302, 3015303, 3015305, 3015306, 3015309, 3015310, 3015311, 3015312, 3015314, 3015315, 3015325, 3015326, 3015327, 3015330, 3015331, 3015332, 3015333, 3015339, 3015340, 3015341, 3015346, 3015350, 3015354, 3015367, 3015368, 3015369, 3015387, 3015388, 3015389, 3015390, 3015391, 3015392, 3015394, 3015404, 3015405, 3015408, 3015409, 3015410, 3015411, 3015412, 3015413, 3015414, 3015416, 3015420, 3015421, 3015422, 3015423, 3015429, 3015431, 3015432, 3015433, 3015434, 3015435, 3015437, 3015438, 3015447, 3015448, 3015468, 3015472, 3015473, 3015474, 3015475, 3015476, 3015481, 3015516, 3015517, 3015518, 3015521, 3015522, 3015523, 3015544, 3015545, 3015547, 3015564, 3015565, 3015566, 3015567, 3015568, 3015569, 3015570, 3015571);
//3번의자
var itemsz3 = new Array(3015572, 3015586, 3015587, 3015602, 3015603, 3015610, 3015611, 3015612, 3015613, 3015614, 3015615, 3015616, 3015617, 3015618, 3015633, 3015636, 3015637, 3015638, 3015642, 3015643, 3015644, 3015645, 3015646, 3015648, 3015649, 3015650, 3015651, 3015652, 3015653, 3015658, 3015663, 3015687, 3015688, 3015689, 3015690, 3015691, 3015692, 3015703, 3015704, 3015705, 3015706, 3015707, 3015708, 3015710, 3015711, 3015745, 3015747, 3015748, 3015749, 3015750, 3015753, 3015762, 3015764, 3015767, 3015768, 3015769, 3015770, 3015786, 3015787, 3015788, 3015789, 3015790, 3015791, 3015792, 3015801, 3015802, 3015803, 3015804, 3015810, 3015812, 3015813, 3015821, 3015822, 3015823, 3015841, 3015842, 3015844, 3015845, 3015846, 3015847, 3015856, 3015857, 3015858, 3015859, 3015860, 3015861, 3015862, 3015865, 3015873, 3015874, 3015888, 3015890, 3015891, 3015894, 3015896, 3015900, 3015906, 3015907, 3015917, 3015918, 3015919, 3015920, 3015921, 3015922, 3015923, 3015924, 3015925, 3015926, 3015927, 3015928, 3015929, 3015930, 3015931, 3015932, 3015933, 3015934, 3015935, 3015936, 3015953, 3015959, 3015962, 3015967, 3015968, 3015969, 3015974, 3015975, 3015977, 3015978, 3015993, 3015999, 3016200, 3016206, 3017000, 3017001, 3017002, 3017003, 3017004, 3017005, 3017006, 3017007, 3017008, 3017009, 3017010, 3017011, 3017012, 3017013, 3017014, 3017015, 3017016, 3017017, 3017018, 3017019, 3017020, 3017021, 3017022, 3017027, 3017028, 3017029, 3017030, 3017031, 3017032, 3017033, 3017037, 3017038, 3017039, 3017040, 3017041, 3017042, 3017043, 3017060, 3017061);
//4번의자

var itemsz4 = new Array(3018000, 3018001, 3018008, 3018009, 3018015, 3018017, 3018019, 3018020, 3018021, 3018022, 3018023, 3018038, 3018042, 3018043, 3018044, 3018045, 3018046, 3018054, 3018055, 3018056, 3018057, 3018058, 3018059, 3018067, 3018070, 3018075, 3018076, 3018078, 3018079, 3018080, 3018087, 3018090, 3018100, 3018101, 3018102, 3018103, 3018104, 3018105, 3018106, 3018107, 3018108, 3018109, 3018126, 3018127, 3018129, 3018134, 3018135, 3018149, 3018153, 3018167, 3018171, 3018172, 3018173, 3018174, 3018175, 3018177, 3018178, 3018179, 3018185, 3018186, 3018199, 3018200, 3018209, 3018210, 3018211, 3018212, 3018213, 3018216, 3018219, 3018221, 3018222, 3018223, 3018224, 3018232, 3018236, 3018238, 3018239, 3018240, 3018241, 3018242, 3018243, 3018250, 3018258, 3018268, 3018270, 3018272, 3018278, 3018279, 3018280, 3018281, 3018282, 3018283, 3018291, 3018292, 3018293, 3018294, 3018305, 3018306, 3018307, 3018308, 3018309, 3018310, 3018311, 3018314, 3018317, 3018318, 3018319, 3018320, 3018321, 3018328, 3018330, 3018333, 3018336, 3018337, 3018338, 3018340, 3018341, 3018342, 3018343, 3018345, 3018346, 3018347, 3018349);
//5번의자

var itemsz5 = new Array(3018350, 3018351, 3018353, 3018354, 3018355, 3018357, 3018378, 3018382, 3018383, 3018384, 3018385, 3018391, 3018395, 3018396, 3018397, 3018398, 3018399, 3018403, 3018418, 3018428, 3018429, 3018440, 3018442, 3018445, 3018447, 3018450, 3018451, 3018460, 3018461, 3018462, 3018467, 3018468, 3018469, 3018481, 3018486, 3018487, 3018488, 3018489, 3018496, 3018497, 3018500, 3018501, 3018502, 3018515, 3018519, 3018522, 3018523, 3018524, 3018525, 3018541, 3018542, 3018543, 3018544, 3018545, 3018546, 3018547, 3018548, 3018549, 3018556, 3018562, 3018574, 3018575, 3018576, 3018577, 3018579, 3018580, 3018581, 3018582, 3018583, 3018584, 3018590, 3018591, 3018598, 3018601, 3018602, 3018603, 3018604, 3018609, 3018610, 3018615, 3018616, 3018621, 3018634, 3018635, 3018651, 3018657, 3018658, 3018659, 3018662, 3018663, 3018664, 3018672, 3018673, 3018675, 3018680, 3018683, 3018686, 3018687, 3018688, 3018697, 3018698, 3018699, 3018700, 3018701, 3018702, 3018707, 3018708, 3018712, 3018718, 3018719, 3018720);
//6번의자

var itemsz6 = new Array();
//장갑

var itemsz7 = new Array();
//신발

var itemsz8 = new Array(); 
//망토

var itemsz9 = new Array();
//악세서리

var itemsz10 = new Array();
//무기

var itemsz11 = new Array(); 
//이펙트

var itemsz12 = new Array(); 
//무기

var itemsz13 = new Array(); 
//명찰

var itemCategorys = new Array(
"#fc0xFF3B36CF##fs11#1번 의자 리스트를 확인하고 싶습니다.#k",
"#fc0xFF3B36CF##fs11#2번 의자 리스트를 확인하고 싶습니다.#k",
"#fc0xFF3B36CF##fs11#3번 의자 리스트를 확인하고 싶습니다.#k",
"#fc0xFF3B36CF##fs11#4번 의자 리스트를 확인하고 싶습니다.#k",
"#fc0xFF3B36CF##fs11#5번 의자 리스트를 확인하고 싶습니다.#k",
"#fc0xFF3B36CF##fs11#6번 의자 리스트를 확인하고 싶습니다.#k");


var status = -1;
var menuSelect = -1;
var select = -1;


function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    
    if (status == 0) {
        var leaf = cm.itemQuantity(4310012);
        trade = "#fs11#"+검정+"저는 #fc0xFF000087#[블랙]#k "+검정+"컨텐츠 부서에서 #b의자 판매#k"+검정+"를 담당하고 있는 로밍이라고 합니다."
        trade += "\r\n선택하신 의자 하나 당 #r#z4310012# 10개#k"+검정+"로 구매가 가능합니다.\r\n\r\n";
        trade += "현재 보유중인 #d#z4310012##k 갯수 : #e#r"+leaf+"#k개#n\r\n"
        trade += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        for (var i = 0; i < itemCategorys.length; i++) {
            trade += "#L"+i+"#"+itemCategorys[i]+"#l\r\n";
        }
            trade += "#L30##r[HOT]#k #fc0xFF3B36CF#스페셜 의자를 뽑아보고 싶습니다.#k"
        cm.sendSimple(trade);
    } else if (status == 1) {
        menuSelect = selection;
        if (selection == 50 ) {
        cm.dispose(); 
        cm.openNpc(1540755);
        } else if (selection == 30) {
            cm.dispose();
            cm.openNpc(1540105, "SpecialChair");
        } else {
                var trade = "\r\n";
                var itemsArray = getArray(selection);
                for (var i = 0;i < itemsArray.length; i++) {
                    trade += "#L"+i+"##i"+itemsArray[i]+"##l";
                    if (i%5 == 4) {
                    trade +="\r\n";
                    }
                }
                cm.sendSimple(trade);
        }
    } else if (status == 2) {
        var itemsArray = getArray(menuSelect);
        select = selection;
        var check = 0;
        for (b = 0; b < itemsz0.length; b++) {
            if (itemsArray[select] == itemsz0[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz1.length; b++) {
            if (itemsArray[select] == itemsz1[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz2.length; b++) {
            if (itemsArray[select] == itemsz2[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz3.length; b++) {
            if (itemsArray[select] == itemsz3[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz4.length; b++) {
            if (itemsArray[select] == itemsz4[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz5.length; b++) {
            if (itemsArray[select] == itemsz5[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz6.length; b++) {
            if (itemsArray[select] == itemsz6[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz7.length; b++) {
            if (itemsArray[select] == itemsz7[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz8.length; b++) {
            if (itemsArray[select] == itemsz8[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz9.length; b++) {
            if (itemsArray[select] == itemsz9[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz10.length; b++) {
            if (itemsArray[select] == itemsz10[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz11.length; b++) {
            if (itemsArray[select] == itemsz11[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz12.length; b++) {
            if (itemsArray[select] == itemsz12[b]) {
                check = 1;
                break;
            }
        }
        for (b = 0; b < itemsz13.length; b++) {
            if (itemsArray[select] == itemsz13[b]) {
                check = 1;
                break;
            }
        }
        if (check == 0) {
         a = new Date();
         temp = Randomizer.rand(0,9999999);
         cn = cm.getPlayer().getName();
         fFile1 = new File("Log/Item/"+a.getDate() +"_"+a.getHours()+"_"+a.getMinutes()+"_"+a.getSeconds()+"_"+cn+".log");
         if (!fFile1.exists()) {
            fFile1.createNewFile();
         }
         out1 = new FileOutputStream("Log/Item/"+a.getDate() +"_"+a.getHours()+"_"+a.getMinutes()+"_"+a.getSeconds()+"_"+cn+".log",false);
         msg =  "'"+cm.getPlayer().getName()+"'이(가) 의심됨.\r\n";
         msg = "'"+a.getFullYear()+"년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일 "+a.getHours()+"시 "+a.getMinutes()+"분 "+a.getSeconds()+"초'\r\n";
         msg += "복사 시도 아이템코드(의자상점) : "+itemsArray[select]+"\r\n";
         msg += "사용자 캐릭터 아이디 : "+cm.getPlayer().getId()+"\r\n";
         msg += "사용자 어카운트 아이디 : "+cm.getPlayer().getAccountID()+"\r\n";
         out1.write(msg.getBytes());
         out1.close();
         cm.getPlayer().getWorldGMMsg(cm.getPlayer(), "의자 상점에서 복사를 시도");
         cm.sendOk(itemsArray[select] + "#fs11##r비정상 경로 확인.",9062004);
         cm.dispose();
          return;
        }
        var itemsArray = getArray(menuSelect);
        cm.sendYesNo("#fs11##r선택하신 아이템을 정말로 구매하시겠습니까?#k\r\n\r\n#b#i"+itemsArray[select]+"##z"+itemsArray[select]+"##k");
    } else if (status == 3) {
        var itemsArray = getArray(menuSelect);
        if (cm.haveItem(4310012, 10) && cm.canHold(itemsArray[select])) {
            cm.gainItem(4310012, -10);
	        cm.gainItem(itemsArray[select], 1);
            말 = "#fs11##b#h ##k님께 어울리는 의자를 선택하셨군요! 다양한 의자가 많으니 또 들러주세요.\r\n\r\n"
            말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            말 += "#i"+itemsArray[select]+"# #b#z"+itemsArray[select]+""
            cm.sendOk(말);
            cm.dispose();
        } else {
            cm.sendOk("#fs11##i4310018# #b#z4310018##k 아이템이 부족하거나 없는거 같네요.");
            cm.dispose();
            return;
        }
    }
}

function getArray(sel) {
if (sel==0)return itemsz0;
if (sel==1)return itemsz1;
if (sel==2)return itemsz2;
if (sel==3)return itemsz3;
if (sel==4)return itemsz4;
if (sel==5)return itemsz5;
if (sel==6)return itemsz6;
if (sel==7)return itemsz7;
if (sel==8)return itemsz8;
if (sel==9)return itemsz9;
if (sel==10)return itemsz10;
if (sel==11)return itemsz11;
if (sel==12)return itemsz12;
if (sel==13)return itemsz13;
}
var seld = -1;
var bjob = -1;
var cjob = -1;
var adv = false;

var map = 100000000;

h = "#fUI/UIMiniGame.img/starPlanetRPS/heart#";
a = "#i3801317#"
b = "#i3801313#"
c = "#i3801314#"
d = "#i3801315#"
p = "#fc0xFFF781D8#"

function start() {
   status = -1;
   action(1, 0, 0);
}

function action(mode, type, selection) {
   if (mode == 1) {
      status++;
   } else {
      cm.dispose();
      return;
       }
   if (status == 0) {
   var text = "#fs12##fc0xFF000000#새로운 모험가군? #fc0xFF990033#[ZERO Island]#fc0xFF000000#로 출발할 준비는 되었어?  \r\n\r\n\r\n\r\n";
   text += "";

  // text += "#fs 11#     1. 타 서버 #fc0xFFF781D8#비교발언#d 및 서버에 대한 #fc0xFFF781D8#비방#d, 공격성 발언 #fc0xFFF781D8#금지#d\r\n";
  // text += "     2. 비인가 프로그램, 클라이언트 #fc0xFFF781D8#변조#d, 매크로 등 #fc0xFFF781D8#금지#d\r\n        #fc0xFFF781D8#(적발시 즉시 밴)#d\r\n";
 //  text += "     3. 불건전한 닉네임, 타인 사칭 및 #fc0xFFF781D8#비방#d, 과도한 분쟁 #fc0xFFF781D8#금지#d\r\n";
  // text += "     4. #fc0xFFF781D8#GM사칭#d 및 아이템의 현금거래 #fc0xFFF781D8#금지#d\r\n";
  // text += "     5. 게임내 #fc0xFFF781D8#모든 정보는 로그가 기록#d될 수 있으며 필요에 \r\n        의해 #fc0xFFF781D8#증거로 활용#d될 수 있습니다.#d\r\n\r\n";
   text += "#fs12##fc0xFFFF9900#'예'#fc0xFF000000# 버튼을 누르시면 시작합니다.";
      cm.sendYesNo(text);
   } else if (status == 1) {
      FirstJob(cm.getJob());
   } else if (status == 2) {
      seld = selection;
      if (selection >= 1 && selection <= 5) {
         adv = true;
         cm.sendSimple(SecondJob(selection));
      } else {
         switch (cm.getJob()) {

            case 2000:
               cm.teachSkill(20001295, 1, 1);
            break;

            case 6000:
               cm.teachSkill(60000219, 1, 1);
               cm.teachSkill(60001217, 1, 1);
               cm.teachSkill(60001216, 1, 1);
               cm.teachSkill(60001218, 1, 1);
               cm.teachSkill(60001219, 1, 1);
               cm.teachSkill(60001225, 1, 1);
               cm.addEquip(-10, 1352500, 0, 0, 0, 0, 0, 0);
            break;

            case 6001:
               cm.addEquip(-10, 1352601, 0, 0, 0, 0, 0, 0);
            break;

            case 6002:
               cm.addEquip(-10, 1353300, 0, 0, 0, 0, 0, 0);
            break;

            case 2002:
               cm.addEquip(-10, 1352000, 0, 0, 0, 0, 0, 0);
            break;

            case 2003:
               cm.addEquip(-10, 1352100, 0, 0, 0, 0, 0, 0);
            break;

            case 2004:
               cm.teachSkill(27000106, 5, 5);
               cm.teachSkill(27000207, 5, 5);
               cm.teachSkill(27001201, 20, 20);
               cm.teachSkill(27001100, 20, 20);
               cm.addEquip(-10, 1352400, 0, 0, 0, 0, 0, 0);
            break;
            case 2005:
               cm.teachSkill(20051284,30,30);
               cm.teachSkill(20050285,30,30);
               cm.teachSkill(20050286,30,30);
               cm.teachSkill(25001000,30,30);
               cm.teachSkill(25001002,30,30);
               cm.teachSkill(25000003,30,30);
               cm.addEquip(-10, 1353100, 0, 0, 0, 0, 0, 0);
            break;
            case 1000:
               cm.teachSkill(10001251, 1, 1);
               cm.teachSkill(10001252, 1, 1);
               cm.teachSkill(10001253, 1, 1);
               cm.teachSkill(10001254, 1, 1);
               cm.teachSkill(10001255, 1, 1);
            break;
			case 13100:
			break;
            case 14000:
               cm.addEquip(-10, 1353200, 0, 0, 0, 0, 0, 0);
	    break;
            case 15000:
               cm.teachSkill(150000079, 1, 1);
               cm.teachSkill(150011005, 1, 1);
            break;
            case 15001:
               cm.addEquip(-10, 1353600, 0, 0, 0, 0, 0, 0);
            break;
			case 15002:
				cm.addEquip(-10,1354000,0,0,0,0,0,0);
				break;
            case 16000:
               cm.teachSkill(164001004, 1, 1);
               cm.teachSkill(164001075, 1, 1);
               cm.addEquip(-10, 1353800, 0, 0, 0, 0, 0, 0);
            break;

            case 16001: //라라
               cm.teachSkill(160011005, 1, 1);
               cm.addEquip(-10, 1354020, 0, 0, 0, 0, 0, 0);
            break;
         }
         switch (selection) {
            case 430: //듀블
               cjob = selection - 30;
               cm.getPlayer().setReborns(selection);
               cm.gainItem(1342000, 1);
               cm.teachSkill(80001829, 5, 5);
            break;

            case 530:
               cjob = 501;
            break;

            case 14000:
               cm.teachSkill(140000291, 6, 6);
            break;

            case 3100:
               cm.addEquip(-10, 1099000, 0, 0, 0, 0, 0, 0);
            break;

            case 3101:
               cm.getPlayer().setHair(36460);
               cm.getPlayer().setFace(20284);
               cm.addEquip(-5, 1050249, 0, 0, 0, 0, 0, 0);
               cm.addEquip(-7, 1070029, 0, 0, 0, 0, 0, 0);
               cm.addEquip(-9, 1102505, 0, 0, 0, 0, 0, 0);
               cm.addEquip(-10, 1099000, 0, 0, 0, 0, 0, 0);
               cm.addEquip(-11, 1232000, 0, 0, 0, 0, 0, 0);
            break;

            case 3300:
               cm.teachSkill(30001061, 1, 1);
            break;

            case 3500:
               cm.teachSkill(30001068, 1, 1);
            break;

            case 3700:
               cm.addEquip(-10, 1353400, 0, 0, 0, 0, 0, 0);
            break;

            case 6300:
               cm.addEquip(-10, 1354010, 0, 0, 0, 0, 0, 0);
            break;

            case 10112:
               cm.addEquip(-10, 1562000, 97, 0, 0, 0, 0, 0);
               cm.addEquip(-11, 1572000, 95, 0, 0, 0, 0, 0);
            break;
         }

         if (cjob == -1) {
            cjob = selection;
         }
                   cm.dispose();
         cm.changeJob(cjob);
                        cm.gainItem(2430917, 1);
                        cm.gainItem(2000005, 500);
                        cm.gainItem(3018129,1);
                        cm.gainMeso(2000000);
                        cm.setDamageSkin(2438871);
                        cm.teachSkill(80001829, 5, 5);
         cm.getPlayer().SkillMasterByJob();
         max = selection == 2500 ? 20 : 10;
         if (selection != 10112) {
                      for (var i = cm.getPlayer().getLevel(); i < max; i++) {
                         cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
            }
         }
         cm.warp(map);
        // cm.openNpc(1540100);
      }
   } else if (status == 3) {
      if (adv) {
         switch (selection) {
            case 110:
            case 120:
            case 130:
               cjob = 100;
            break;
            case 210:
            case 220:
            case 230:
               cjob = 200;
            break;
            case 310:
            case 320:
               cjob = 300;
	       break;
            case 330:
               cjob = 301;
               cm.addEquip(-10, 1353700, 0, 0, 0, 0, 0, 0);
               break;
            case 410:
            case 420:
               cjob = 400;
               break;

            case 510:
            case 520:
               cjob = 500;
               break;
         }
         cm.warp(map);
              cm.dispose();
         cm.changeJob(cjob);
         cm.getPlayer().setReborns(selection);
         cm.getPlayer().SkillMasterByJob();
         for (var i = cm.getPlayer().getLevel(); i < 10; i++) {
                      cm.gainExp(Packages.constants.GameConstants.getExpNeededForLevel(i));
         }
                        cm.gainItem(2430917, 1);
                        cm.gainItem(3018129,1);
                        cm.gainItem(2000005, 500);
                        cm.setDamageSkin(2438871);
                        cm.teachSkill(80001829, 5, 5);
                        cm.gainMeso(2000000);
      }
   }
}

function SecondJob(i) {
   var adventure = "#fs 12##fc0xFF000000#세부 직업을 선택해! 선택한 직업은 후에 충족 레벨이 되면 자동으로 전직이 된다구\r\n"
   switch (i) {
          case 1:
                      adventure += "#fc0xFF000000#현재 당신이 선택한 직업군은 #b전사#fc0xFF000000# 입니다.\r\n";
                      adventure += "#L110##fc0xFF000000# 저는 #fc0xFFFF3300#히어로#fc0xFF000000#로 시작하고 싶습니다.\r\n";
                      adventure += "#L120##fc0xFF000000# 저는 #fc0xFFFF3300#팔라딘#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
                      adventure += "#L130##fc0xFF000000# 저는 #fc0xFFFF3300#다크나이트#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
               case 2:
                      adventure += "#fc0xFF000000#현재 당신이 선택한 직업군은 #b마법사#fc0xFF000000# 입니다.\r\n";
                      adventure += "#L210##fc0xFF000000# 저는 #fc0xFFFF3300#불독#k로 시작하고 싶습니다.\r\n";
                      adventure += "#L220##fc0xFF000000# 저는 #fc0xFFFF3300#썬콜#k로 시작하고 싶습니다.\r\n";
                      adventure += "#L230##fc0xFF000000# 저는 #fc0xFFFF3300#비숍#k으로 시작하고 싶습니다.\r\n";
      break;
                case 3:
                      adventure += "#fc0xFF000000#현재 당신이 선택한 직업군은 #b궁수#fc0xFF000000# 입니다.\r\n";
                      adventure += "#L310##fc0xFF000000# 저는 #fc0xFFFF3300#보우마스터#fc0xFF000000#로 시작 하겠습니다.\r\n";
                      adventure += "#L320##fc0xFF000000# 저는 #fc0xFFFF3300#신궁#fc0xFF000000#으로 시작 하겠습니다.\r\n";
                      adventure += "#L330##fc0xFF000000# 저는 #fc0xFFFF3300#패스파인더#fc0xFF000000#로 시작 하겠습니다.\r\n";
      break;
               case 4:
                      adventure += "#fc0xFF000000#현재 당신이 선택한 직업군은 #b도적#k 입니다.\r\n";
                      adventure += "#L410##fc0xFF000000# 저는 #fc0xFFFF3300#나이트로드#fc0xFF000000#로 시작 하겠습니다.\r\n";
                      adventure += "#L420##fc0xFF000000# 저는 #r섀도어#fc0xFF000000#로 시작 하겠습니다.\r\n";
      break;
              case 5:
                      adventure += "#fc0xFF000000#현재 당신이 선택한 직업군은 #b해적#k 입니다.\r\n";
                      adventure += "#L510##fc0xFF000000# 저는 #fc0xFFFF3300#바이퍼#fc0xFF000000#로 시작 하겠습니다.\r\n";
                      adventure += "#L520##fc0xFF000000# 저는 #fc0xFFFF3300#캡틴#fc0xFF000000#으로 시작 하겠습니다.\r\n";
      break;
   }
   return adventure;
}

function FirstJob(i) {
   var chat = "#fs 12##fc0xFF000000#원하는 직업을 선택해! \r\n\r\n(#fc0xFF6600CC##h0##fc0xFF000000# 님이 현재 플레이 가능한 #fc0xFF6600CC#직업#fc0xFF000000#은 아래와 같습니다)\r\n";
   switch (i) {
      case 0:
         chat += "#L1##fc0xFF000000# 저는 #fc0xFFFF3300#전사#fc0xFF000000#로 시작하고 싶습니다.\r\n";
         chat += "#L2##fc0xFF000000# 저는 #fc0xFFFF3300#마법사#fc0xFF000000#로 시작하고 싶습니다.\r\n";
         chat += "#L3##fc0xFF000000# 저는 #fc0xFFFF3300#궁수#fc0xFF000000#로 시작하고 싶습니다.\r\n";
         chat += "#L4##fc0xFF000000# 저는 #fc0xFFFF3300#도적#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
         chat += "#L5##fc0xFF000000# 저는 #fc0xFFFF3300#해적#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
         chat += "#L430##fc0xFF000000# 저는 #fc0xFFFF3300#듀얼블레이드#fc0xFF000000#로 시작하고 싶습니다.\r\n";
         chat += "#L530##fc0xFF000000# 저는 #fc0xFFFF3300#캐논슈터#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 1000:
        chat += "#L1100##fc0xFF000000# 저는 #fc0xFFFF3300#소울마스터#fc0xFF000000#로 시작하고 싶습니다.\r\n";
        chat += "#L1200##fc0xFF000000# 저는 #fc0xFFFF3300#플레임위자드#fc0xFF000000#로 시작하고 싶습니다.\r\n";
        chat += "#L1300##fc0xFF000000# 저는 #fc0xFFFF3300#윈드브레이커#fc0xFF000000#로 시작하고 싶습니다.\r\n";
        chat += "#L1400##fc0xFF000000# 저는 #fc0xFFFF3300#나이트워커#fc0xFF000000#로 시작하고 싶습니다.\r\n";
        chat += "#L1500##fc0xFF000000# 저는 #fc0xFFFF3300#스트라이커#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 2000:
         chat += "#L2100##fc0xFF000000# 저는 #fc0xFFFF3300#아란#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
      break;
      case 2001:
         chat += "#L2200##fc0xFF000000# 저는 #fc0xFFFF3300#에반#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
      break;
      case 2002:
         chat += "#L2300##fc0xFF000000# 저는 #fc0xFFFF3300#메르세데스#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 2003:
         chat += "#L2400##fc0xFF000000# 저는 #fc0xFFFF3300#팬텀#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
      break;
      case 2004:
         chat += "#L2700##fc0xFF000000# 저는 #fc0xFFFF3300#루미너스#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 2005:
         chat += "#L2500##fc0xFF000000# 저는 #fc0xFFFF3300#은월#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 3000:
         chat += "#L3200##fc0xFF000000# 저는 #fc0xFFFF3300#배틀메이지#fc0xFF000000#로 시작하고 싶습니다.\r\n";
         chat += "#L3300##fc0xFF000000# 저는 #fc0xFFFF3300#와일드헌터#fc0xFF000000#로 시작하고 싶습니다.\r\n";
         chat += "#L3500##fc0xFF000000# 저는 #fc0xFFFF3300#메카닉#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
         chat += "#L3700##fc0xFF000000# 저는 #fc0xFFFF3300#블래스터#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 3001:
         chat += "#L3100##fc0xFF000000# 저는 #fc0xFFFF3300#데몬슬레이어#fc0xFF000000#로 시작하고 싶습니다.\r\n";
         chat += "#L3101##fc0xFF000000# 저는 #fc0xFFFF3300#데몬어벤져#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 3002:
         chat += "#L3600##fc0xFF000000# 저는 #fc0xFFFF3300#제논#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
      break;
      case 5000:
         chat += "#L5100##fc0xFF000000# 저는 #fc0xFFFF3300#미하일#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 6000:
         chat += "#L6100##fc0xFF000000# 저는 #fc0xFFFF3300#카이저#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 6001:
         chat += "#L6500##fc0xFF000000# 저는 #fc0xFFFF3300#엔젤릭버스터#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 6002:
         chat += "#L6400##fc0xFF000000# 저는 #fc0xFFFF3300#카데나#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 6003:
         chat += "#L6300##fc0xFF000000# 저는 #fc0xFFFF3300#카인#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
      break;
      case 10112:
         chat += "#L10112##fc0xFF000000# 저는 #fc0xFFFF3300#제로#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
	  case 13100:
		chat += "#L13100##fc0xFF000000# 저는 #fc0xFFFF3300#핑크빈#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
		break;
      case 14000:
         chat += "#L14200##fc0xFF000000# 저는 #fc0xFFFF3300#키네시스#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 15000:
         chat += "#L15200##fc0xFF000000# 저는 #fc0xFFFF3300#일리움#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
      break;
      case 15001:
         chat += "#L15500##fc0xFF000000# 저는 #fc0xFFFF3300#아크#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
	  case 15002:
         chat += "#L15100##fc0xFF000000# 저는 #fc0xFFFF3300#아델#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
      case 16000:
         chat += "#L16400##fc0xFF000000# 저는 #fc0xFFFF3300#호영#fc0xFF000000#으로 시작하고 싶습니다.\r\n";
      break;
      case 16001:
         chat += "#L16200##fc0xFF000000# 저는 #fc0xFFFF3300#라라#fc0xFF000000#로 시작하고 싶습니다.\r\n";
      break;
   }
   cm.sendSimple(chat);
}
function start() {
    status = -1;
    action(1, 0, 0);
}

var serverName = "해피"

function action(mode, type, selection) {
    dialogue = [
   [""],
   ["#fc0xFF990033#[랭크 승급 버프]#k\r\n"],
   ["#b랭크 승급마다 아래 버프가 중첩 됩니다."],
   ["#r메소 획득량 +10%\r\n아이템 드롭률 +10%\r\n크리데미지 +5%\r\n보스공격력 +5%"],
   ["#fc0xFF990033#[랭크 종류]#k\r\n"],
   ["#fc0xFF330000##fEtc/ZodiacEvent.img/0/icon/1/0# [Bronze] #fc0xFF999999##fEtc/ZodiacEvent.img/0/icon/2/0# [Silver] #fc0xFFCCCC33##fEtc/ZodiacEvent.img/0/icon/3/0# [Gold] #fc0xFF339966##fEtc/ZodiacEvent.img/0/icon/4/0# [Platinum] #fc0xFF00CCCC##fEtc/ZodiacEvent.img/0/icon/5/0# [Diamond] #fc0xFF990066##fEtc/ZodiacEvent.img/0/icon/6/0# [Master] #fc0xFFCC0066##fEtc/ZodiacEvent.img/0/icon/7/0# [Grand Master] #fc0xFFFF6600##fEtc/ZodiacEvent.img/0/icon/8/0# [Challenger]  #fc0xFF000000##fEtc/ZodiacEvent.img/0/icon/9/0# [Overload]"],
   ];
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
   cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.MakeBlind(0x01, 200, 0, 0, 0, 800, 0));
   cm.getPlayer().setInGameDirectionMode(true, true, false, false);
   cm.getPlayer().InGameDirectionEvent("", 1, 1000);
    } else if (status > 0) {
   if (status == (dialogue.length - 1)) {
       cm.getPlayer().InGameDirectionEvent(dialogue[status], 12, 1);
   } else if (status == dialogue.length) {
       cm.getPlayer().InGameDirectionEvent("", 1, 1000);
       cm.getPlayer().InGameDirectionEvent("", 23, 700);
   } else if (status == (dialogue.length + 1)) {
            cm.getPlayer().removeInGameDirectionMode();
       cm.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 800, 0));
            cm.dispose();
   } else {
       cm.getPlayer().InGameDirectionEvent(dialogue[status], 12, 0);
   }
    }
}
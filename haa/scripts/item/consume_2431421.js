/*

성 우 (mysqld@nate.com)

메이플 로얄 스타일 

*/
//imddddportPackage(java.lang);
importPackage(Packages.handling.world);
importPackage(Packages.packet.creators);

var status = -1;

var royalstyle = 5680157; //메이플 로얄 스타일 쿠폰





var item = new Array(new Array(1702456, 1), new Array(1050299, 1), new Array(1051366, 1), new Array(1072860, 1), new Array(1082555, 1), new Array(1003955, 1), new Array(1050300, 1), new Array(1051367,1), new Array(1003957, 1), new Array(1003958, 1), new Array(1702457, 1), new Array(1072862, 1), new Array(1003867, 1), new Array(1042264, 1), new Array(1060182, 1), new Array(1061206,1), new Array(1072823, 1), new Array(1082527, 1), new Array(1003909, 1), new Array(1050291,1), new Array(1051357,1), new Array(1072836,1), new Array(1102593,1), new Array(1702442,1), new Array(1003945,1), new Array(1050296,1), new Array(1051362,1), new Array(1072852,1), new Array(1102608,1), new Array(1003971,1), new Array(1003972,1), new Array(1050302,1), new Array(1072868,1), new Array(1702464,1), new Array(1004002,1), new Array(11026324,1), new Array(1070057,1), new Array(1071074,1), new Array(1702473,1), new Array(1702424,1), new Array(1702486,1), new Array(1001092,1), new Array(1072901,1));




function start(){
    action(1,0,0);
}

 

item.sort(function(){
    return Math.random() - Math.random();
});

 

function action(mode,type,selection){
    if(mode == 1){
        status++;
    }else{
        status--;
        cm.dispose();
    }
    if (status == 0){
        cm.sendSimple("안녕? 난 빅 헤드국의 둘째왕자 빅 헤드거라고 해.\r\n#i5680157##r#t5680157##k 쿠폰을 가지고 있다면 내가 최신 스타일의 캐시 아이템 1종을 바꿔줄게. 운이 좋다면 #r어메이징한 옵션#k이 붙어있는 캐시 의상 아이템도 받을 수가 있지!\r\n어때 지금바로 쿠폰을 사용할거야?\r\n\r\n#L1##b지금 바로 메이플 로얄 스타일 쿠폰을 사용!");
    }else if (status == 1){
        if (cm.haveItem(royalstyle, 1)){
            if (cm.canHold(item[0][0])){
                if (item[0][1] != 0) {
                    cm.gainItem(item[0][0], item[0][1]);
                    cm.gainItem(royalstyle, -1);
                    cm.sendOk("어때?#i" + item[0][0] + "# #r(#z" + item[0][0] + "#)#k 아이템은 잘 받았어? 정말 어메이징하지 않아? 다음번에 또 #b메이플 로얄 스타일#k 쿠폰이 생기면 나를 찾아줘!");
                    WorldBroadcasting.broadcast(MainPacketCreator.serverNotice(6,""+ cm.getPlayer().getName()+ " 님께서 메이플 로얄 스타일 쿠폰을 사용해, 캐시 아이템을 얻으셨습니다~ 축하드려요 !"));
                    cm.dispose();
                } else {
                    cm.sendOk("아쉽게도 아무것도, 뽑히지 않았네요.");
                    cm.gainItem(royalstyle, -1);
                    cm.dispose();
                }
            } else {
                cm.sendOk("인벤토리 공간부족이 부족합니다.");
                cm.dispose();
            }
        } else {
            cm.sendOk("#i"+royalstyle+"##r메이플 로얄 스타일#k 쿠폰을 가지고있지 않습니다.");
            cm.dispose();
        }    
    }    
}
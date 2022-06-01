var cchoice; 
var choice; 
var Frock = "#fUI/UIWindow.img/RpsGame/Frock#"; 
var Fpaper = "#fUI/UIWindow.img/RpsGame/Fpaper#"; 
var Fscissor = "#fUI/UIWindow.img/RpsGame/Fscissor#"; 
var rock = "#fUI/UIWindow.img/RpsGame/rock#"; 
var paper = "#fUI/UIWindow.img/RpsGame/paper#"; 
var scissor = "#fUI/UIWindow.img/RpsGame/scissor#";  
var win = "　　　#fUI/UIWindow.img/RpsGame/win#"; 
var lose = "　　　#fUI/UIWindow.img/RpsGame/lose#"; 
var draw = "　　　#fUI/UIWindow.img/RpsGame/draw#"; 
var money = 0;
var gamestatus = false;
var isdraw = false;


function start(){
status = -1;
action(1,0,0);
}

function action(mode,type,selection){
if(mode != 1) {
cm.dispose();
}
if(mode == 1)
status ++;
else
status--;

if(status == 0){

if(cm.getMeso() >= 30000000)
cm.sendSimple("#fs11##fn나눔고딕 Extrabold##d저랑 #b가위바위보 내기#k #d한번 해보시겟어요?\r\n\r\n#r주의 : 도박이 당신을 한순간에 거지로 만들 수 있습니다.\r\n도박이 당신 인생에 전부는 아니라는 점! 명심 해주세요.#k\r\n\r\n#d승리 : + 3000 만 / 패배 : - 3000 만 / 무승부 : - 500 만#k\r\n\r\n#L1#"+rock+"#l#L2#"+scissor+"#l#L3#"+paper+"#l"); 
else {
cm.sendOk("#fs11##fn나눔고딕 Extrabold# #r저랑 내기를 하기위해선 최소 3000 만 메소가 필요해요..#k");
cm.dispose();
}
} else if (status == 1){
if(selection == 1)
choice = "rock";
else if (selection == 2)
choice = "scissor";
else if (selection == 3)
choice = "paper";
var rand = Math.floor(Math.random() * 3);
if(rand == 0)
cchoice = "Frock";
else if (rand == 1)
cchoice = "Fpaper";
else if (rand == 2)
cchoice = "Fscissor";
else
cchoice = "Fscissor";

//가위바위보 시작
if(choice == "rock"){
choice = "#fUI/UIWindow.img/RpsGame/rock#"; 
if(cchoice == "Fscissor"){
money = 30000000
cchoice = "#fUI/UIWindow.img/RpsGame/Fscissor#"; 
gamestatus = true;
isdraw = false;
}else if (cchoice == "Fpaper"){
gamestatus = false;
isdraw = false;
cchoice = "#fUI/UIWindow.img/RpsGame/Fpaper#"; 
}else if (cchoice == "Frock"){
isdraw = true;
gamestatus = true;
cchoice = "#fUI/UIWindow.img/RpsGame/Frock#"; 
}

} else if(choice == "scissor"){
choice = "#fUI/UIWindow.img/RpsGame/scissor#"; 
if(cchoice == "Fpaper"){
money = 30000000
cchoice = "#fUI/UIWindow.img/RpsGame/Fpaper#"; 
gamestatus = true;
isdraw = false;
}else if (cchoice == "Frock"){
gamestatus = false;
isdraw = false;
cchoice = "#fUI/UIWindow.img/RpsGame/Frock#"; 
}else if (cchoice == "Fscissor"){
isdraw = true;
gamestatus = true;
cchoice = "#fUI/UIWindow.img/RpsGame/Fscissor#"; 
}

} else if(choice == "paper"){
choice = "#fUI/UIWindow.img/RpsGame/paper#"; 
if(cchoice == "Frock"){
money = 30000000
cchoice = "#fUI/UIWindow.img/RpsGame/Frock#"; 
gamestatus = true;
isdraw = false;
}else if (cchoice == "Fscissor"){
gamestatus = false;
isdraw = false;
cchoice = "#fUI/UIWindow.img/RpsGame/Fscissor#"; 
}else if (cchoice == "Fpaper"){
isdraw = true;
gamestatus = true;
cchoice = "#fUI/UIWindow.img/RpsGame/Fpaper#"; 
}
}
if(gamestatus == true){
 if(isdraw == true){
 cm.sendOk("#fn나눔고딕 Extrabold#           상대방"+(cchoice)+"vs"+(choice)+"자신\r\n"+draw);
 cm.gainMeso(-5000000);
 cm.dispose();
 } else {
 cm.sendOk("#fn나눔고딕 Extrabold#           상대방"+cchoice+"vs"+choice+"자신\r\n"+win);
 cm.gainMeso(money);
 cm.dispose();
 }
}else if (gamestatus == false){
 cm.sendOk("#fn나눔고딕 Extrabold#           상대방"+cchoice+"vs"+choice+"자신\r\n"+lose);
 cm.gainMeso(-30000000);
 cm.dispose();
}

}

}
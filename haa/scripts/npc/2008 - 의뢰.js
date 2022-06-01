var batting = 0;
var white = "#fc0xFFFFFFFF#";
var black = "#fc0xFF000000#";
var red = "#fc0xFFFF0000#";
var space = white+".#k";
var space2 = white+"----------------#k";
var enter ="\r\n";

var PlayerCard = [];
var PlayerCard2 = [];
var BankerCard = [];
var BankerCard2 = [];

var a = [];

var player = -1, banker = -1;

var cardNumbertext = [
""+black+"♠A#k",""+black+"♠2#k",""+black+"♠3#k",""+black+"♠4#k",""+black+"♠5#k",""+black+"♠6#k",""+black+"♠7#k",""+black+"♠8#k",""+black+"♠9#k",""+black+"♠J#k",""+black+"♠Q#k",""+black+"♠K#k",
""+red+"♥A#k",""+red+"♥2#k",""+red+"♥3#k",""+red+"♥4#k",""+red+"♥5#k",""+red+"♥6#k",""+red+"♥7#k",""+red+"♥8#k",""+red+"♥9#k",""+red+"♥J#k",""+red+"♥Q#k",""+red+"♥K#k",
""+black+"♣A#k",""+black+"♣2#k",""+black+"♣3#k",""+black+"♣4#k",""+black+"♣5#k",""+black+"♣6#k",""+black+"♣7#k",""+black+"♣8#k",""+black+"♣9#k",""+black+"♣J#k",""+black+"♣Q#k",""+black+"♣K#k",
""+red+"◆A#k",""+red+"◆2#k",""+red+"◆3#k",""+red+"◆4#k",""+red+"◆5#k",""+red+"◆6#k",""+red+"◆7#k",""+red+"◆8#k",""+red+"◆9#k",""+red+"◆J#k",""+red+"◆Q#k",""+red+"◆K#k"
];

var cardNumber = [
"♠A","♠2","♠3","♠4","♠5","♠6","♠7","♠8","♠9","♠J","♠Q","♠K",
"♥A","♥2","♥3","♥4","♥5","♥6","♥7","♥8","♥9","♥J","♥Q","♥K",
"♣A","♣2","♣3","♣4","♣5","♣6","♣7","♣8","♣9","♣J","♣Q","♣K",
"◆A","◆2","◆3","◆4","◆5","◆6","◆7","◆8","◆9","◆J","◆Q","◆K"
];

var data = [];
var target = 9; // 목표과 가장 가까운 값
var near = 0; 
var abs = 0;
var min = 8; // 해당 범위에서 가장 큰 값

function start()
{
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1)
	{
		cm.dispose();
		return;
	}

	if(M == 1)
	St++;

	if(St == 0)	{
	var talk ="어서오세요. 카드 도박에 오신걸 환영합니다.\r\n";
	talk +="배팅금을 입력해주세요."
	cm.sendGetNumber(talk, 1000, 1000, 99999999);
	} else if(St == 1)	{
		batting = S;
		Cardset();
		var talk ="[나의 배팅금 : "+batting+"]"+enter+enter;
		talk +="[Player]"+enter
		//PlayerCard
		talk +="[?] ";
		for(var i = 1; i<PlayerCard.length; i++){
			talk +=PlayerCard[i]+" ";
		}
		//BankerCard
		talk +=enter+enter+"[Banker]"+enter
		talk +="[?] ";
		for(var i = 1; i<BankerCard.length; i++){
			talk +=BankerCard[i]+" ";
		}
		talk +=enter+"#L0# 플레이어에 걸기 #L1# 뱅커에 걸기 #L2# 무승부에 걸기";
		cm.sendSimple(talk);
	} else if(St == 2)	{
		calculator();
		if(S == 0){
			if(near == player){
				var talk ="[나의 배팅금 : "+batting+"]"+enter+enter;
				talk += result();
				talk +=enter+"플레이어가 승리하였습니다.";
				cm.sendOk(talk);
				cm.dispose();
				return;
			} else {
				var talk ="[나의 배팅금 : "+batting+"]"+enter+enter;
				talk += result();
				talk +=enter+"플레이어가 패배하였습니다.";
				cm.sendOk(talk);
				cm.dispose();
				return;
			}
		} else if(S == 1){
			if(near == banker){
				var talk ="[나의 배팅금 : "+batting+"]"+enter+enter;
				talk += result();
				talk +=enter+"뱅커가 승리하였습니다.";
				cm.sendOk(talk);
				cm.dispose();
				return;
			} else {
				var talk ="[나의 배팅금 : "+batting+"]"+enter+enter;
				talk += result();
				talk +=enter+"뱅커가 패배하였습니다.";
				cm.sendOk(talk);
				cm.dispose();
				return;
			}
		} else if(S == 2){
			if(player == banker){
				var talk ="[나의 배팅금 : "+batting+"]"+enter+enter;
				talk += result();
				talk +=enter+"무승부 입니다.";
				cm.sendOk(talk);
				cm.dispose();
				return;
			} else {
				var talk ="[나의 배팅금 : "+batting+"]"+enter+enter;
				talk += result();
				talk +=enter+"무승부가 아닙니다.";
				cm.sendOk(talk);
				cm.dispose();
				return;
			}
		}
	}
}

function Cardset(){
	for(var i = 0; i<2; i++){
		var card = Math.floor(Math.random() * cardNumber.length);
		PlayerCard.push(cardNumbertext[card]);
		PlayerCard2.push(cardNumber[card]);
		trans(PlayerCard2[i]);
		cardNumbertext.splice(card,1);
		cardNumber.splice(card,1);
	}
	for(var i = 0; i<2; i++){
		var card = Math.floor(Math.random() * cardNumber.length);
		BankerCard.push(cardNumbertext[card]);
		BankerCard2.push(cardNumber[card]);
		trans(BankerCard2[i]);
		cardNumbertext.splice(card,1);
		cardNumber.splice(card,1);
	}
}

function trans(aa){
	switch(aa){
		case "♠A":
		case "♥A":
		case "♣A":
		case "◆A":
			a.push(1);
			break;
		case "♠2":
		case "♥2":
		case "♣2":
		case "◆2":
			a.push(2);
			break;
		case "♠3":
		case "♥3":
		case "♣3":
		case "◆3":
			a.push(3);
			break;
		case "♠4":
		case "♥4":
		case "♣4":
		case "◆4":
			a.push(4);
			break;
		case "♠5":
		case "♥5":
		case "♣5":
		case "◆5":
			a.push(5);
			break;
		case "♠6":
		case "♥6":
		case "♣6":
		case "◆6":
			a.push(6);
			break;
		case "♠7":
		case "♥7":
		case "♣7":
		case "◆7":
			a.push(7);
			break;
		case "♠8":
		case "♥8":
		case "♣8":
		case "◆8":
			a.push(8);
			break;
		case "♠9":
		case "♥9":
		case "♣9":
		case "◆9":
			a.push(9);
			break;
		case "♠J":
		case "♥J":
		case "♣J":
		case "◆J":
		case "♠Q":
		case "♥Q":
		case "♣Q":
		case "◆Q":
		case "♠K":
		case "♥K":
		case "♣K":
		case "◆K":
			a.push(0);
			break;
	}
}

function result(){
	var talk ="";
	talk +="[Player]"+enter
	//PlayerCard
	for(var i = 0; i<PlayerCard.length; i++){
		talk +=PlayerCard[i]+" ";
	}
	//BankerCard
	talk +=enter+enter+"[Banker]"+enter
	for(var i = 0; i<BankerCard.length; i++){
		talk +=BankerCard[i]+" ";
	}
	return talk;
}

function calculator(){
	player = a[0]+a[1];
	banker = a[2]+a[3];
	if(player > 10){
		player = 10-player;
	} else if(banker > 10){
		banker = 10-banker;
	}
	data.push(player);
	data.push(banker);
	minimum();
}


function minimum(){
	for(var i=0; i < data.length; i++){
	abs = ((data[i] - target) < 0) ? - ((data[i])-target) : (data[i] - target);
	if(abs < min){
	min = abs;
	near = data[i];
	}
	}
}

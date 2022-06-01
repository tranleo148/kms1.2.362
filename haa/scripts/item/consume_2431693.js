var status = -1;

var quest = new Array("노예 하요", "노가다는 제 일상이지만, 너무 많은 걸 바라지는 말아주세요.", 801000,
"꼴초 비너스", "금연하려 했는데, 벽돌 사이에 있는 그 담배는 무엇인가요… ….", 801001,
"고영익 (교도소에서)", "치킨이 좋을까? 로리가 좋을까? 전 항상 고민돼요! ㅠ_ㅠ", 801002,
"왕따 돌륜군", "많은 사람들이 예스할 때 저는 노를 외칩니다 네 왕따에요.", 801003,
"개발자 K", "롤하고 싶어요! 공부하기 싫어요! 로리♥", 801004,
"노예 하요", "방어구 상점 만들고 싶다고 생각한 1분이 제4시간을 훔쳐 갔습니다.", 801005,
"z1존 팬더", "저는 오늘도 노동을 합니다.", 801006,
"뿔테 안경 아이유", "으으… 편지 내용 받아내려고 팀원 한사람 한 사람에게 재촉하는 것도 이제 질렸어요~! 으앙…. 이게 작가를 재촉하는 편집자의 마음일까요…?", 801007,
"z1존 팬더", "맥스공은 도대체 어떻게 하면 나오는 건가요…", 802000,
"솔로 아이유", "오늘도 옆구리가 시리네요. 선풍기를 쐬서 그런가… ….", 802001,
"전교 1등 비너스", "전교 2등은 어떻게 하는 거죠?", 802002,
"노예 하요", "어떻게 하면 새벽까지 깬 상태로 있을 수 있는 거죠? 12시만 되면 잠이 오는데…", 802003,
"천둥 맞은 똥개", "할머니께서 말씀하셨지 길을 걸을 땐 왼발부터", 802004,
"KYI (교도소에서)", "로리를 보면 불끈거리네요 어떻게 해야 하죠?", 802005,
"졸린 K", "눈뜨고 자는 건 어떻게 하는 건가요?…", 802006);

var quest_num = new Array();
var quest_name = new Array();
var quest_con = new Array();

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
if (mode == 1) { status++;
} else { cm.dispose(); return; }

if (status == 0) {
if (cm.getQuestStatus(799999) == 0) {
	cm.completeQuest(799999);
	cm.gainItem(2432740, 1);
}
	cm.gainItem(2431693, -1);
	Math.floor(Math.random() * quest.length);
	for (var i = 2; i < quest.length; i+=3) {
		quest_num.push(quest[i]);
		quest_name.push(quest[i-2]);
		quest_con.push(quest[i-1]);
	}
	var ran = Math.floor(Math.random() * quest_num.length);
	cm.completeQuest(quest_num[ran]);
	cm.sendOk("#bTo. #e"+cm.getPlayer().getName()+"#n 님께..\r\n\r\n#k"+quest_con[ran]+"\r\n\r\n#bFrom. "+quest_name[ran]);
	cm.dispose();
}
}
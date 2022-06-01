/*
제작자 : 백란(vmfhvlfqhwlak@nate.com)
수정자 : 타임(time_amd@nate.com)
*/


var status = -1;

function start() {
 action(1, 0, 0);
}

function action(mode, type, selection) {
 if (mode == 1) {
  status++;
 } else {
  status--;
  cm.dispose();
 }
 if (status == 0) {
  cm.sendYesNo("#b#z2430728##k 를 정말로 사용하시겠습니까?");
 } else if (status == 1) {
cm.gainItem(2430728,-1);//아이템 사라지게
cm.teachSkill(80001149,1,1); // 스킬 주기
cm.sendOk("라이딩스킬이 성공적으로 적용 되었습니다."); //더블파워슈트
cm.dispose();
}
}
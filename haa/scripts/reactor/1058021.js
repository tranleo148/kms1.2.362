/*
벨룸 소환
*/
importPackage(java.lang);
importPackage(java.util);


function act() {
      var tick = 0;
      rm.getPlayer().getMap().startMapEffect("내 경고를 무시하고 다시 찾아온 것은 네놈이니 더 이상 자비를 베풀지는 않겠다.", 5120103, 5000);
      var sc = Packages.server.Timer.EtcTimer.getInstance().schedule(function () {
            rm.spawnMonster(8930000, 1);
            sc.cancel(true);
      },
            4000);
}
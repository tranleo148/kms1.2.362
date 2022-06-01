importPackage(Packages.tools.packet);

var status = -1;
var date = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        //cm.getPlayer().setKeyValue(18838, "count", "99");
        cm.getPlayer().getClient().getSession().writeAndFlush(CField.UIPacket.openUIOption(1112, 0));
        cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.OnYellowDlg(9070200, 2000, "신의 컨트롤에 도전하는거냐?", ""));
        cm.dispose();
    }

}
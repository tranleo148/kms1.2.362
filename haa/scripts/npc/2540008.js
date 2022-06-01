importPackage(Packages.database);
var status = -1;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
reqmeso = 100000;
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
	cm.sendGetText("오케이");
    } else if (status == 1) {
	if (cm.getText() == "") {
		cm.sendOk("공백 닉네임은 사용하실 수 없습니다.");
		cm.dispose();
		return;
	}
	var c = DatabaseConnection.getConnection();
	var con = c.prepareStatement("SELECT COUNT(*) FROM characters WHERE NAME = '"+cm.getText()+"'").executeQuery();
	con.next()
	if (con.getInt("COUNT(*)") == 0) {
		if (getByteB(cm.getText()) > 12) {
			cm.sendOk("닉네임은 총 12바이트까지 가능합니다. (한글은 2바이트, 영어는 1바이트로 계산됩니다.)");
			con.close();
			cm.dispose();
			return;
		}
	} else {
		cm.sendOk("이미 사용중인 닉네임입니다.");
		con.close();
		cm.dispose();
		return;
	}
	con.close();
	cm.getPlayer().setName(cm.getText());
	cm.getPlayer().fakeRelog();
	cm.dispose();
    }
}


function getByteB(str) {
	var byte = 0;
	for (var i=0; i<str.length; ++i) {
		(str.charCodeAt(i) > 127) ? byte += 2 : byte++ ;
	}
	return byte;
}
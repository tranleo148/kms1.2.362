var status;
var book  = new Array(1712001, 1712002, 1712003, 1712004, 1712005, 1712006);

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }
        if (status == 0) {
		item = book[Math.floor(Math.random() * book.length)];
		cm.gainItem(item, 1);
		cm.gainItem(2630281, -1);
		cm.dispose();
}
}
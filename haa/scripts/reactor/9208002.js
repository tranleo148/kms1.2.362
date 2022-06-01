function act() {
    var eim = rm.getPlayer().getEventInstance();
    if (eim != null) {
        var status = eim.getProperty("stage1status");
        if (status != null && !status.equals("waiting")) {
            var stage = parseInt(eim.getProperty("stage1phase"));
            if (status.equals("display")) {
                var prevCombo = eim.getProperty("stage1combo");
                prevCombo += rm.getReactor().getObjectId();
                eim.setProperty("stage1combo",prevCombo);
                if (prevCombo.length == (3 * (stage + 3))) {
                    eim.setProperty("stage1status","active");
                    rm.mapMessage(5, "The combo has been displayed; Proceed with caution.");
                    eim.setProperty("stage1guess","");
                }
            } else { 
                var prevGuess = eim.getProperty("stage1guess");
                if (prevGuess.length != (3 * stage + 9)) {
                    prevGuess += rm.getReactor().getObjectId();
                    eim.setProperty("stage1guess",prevGuess);
                }
            }
        }
    }
}
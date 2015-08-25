function sestej(x) {
    return x + 3;
}

function processTrap(trap) {
    var t = "ok " + trap.getCustomText();
    trap.setCustomText(t);
    trap.setSeverity(2);
    var response = new Object();
    var u = myctx.getSomeCtx();
    u = sestej(u);
    trap.setTimestamp(u);
    return trap;
}

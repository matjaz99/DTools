function sestej(x) {
    return x + 1;
}

function processTrap(trap) {
    var t = "ok " + trap.getCustomText();
    if (trap.getPeerIp() == "192.168.1.110") {
    	trap.setNodeName("CentOS 6");
    	trap.setLocation("Private network");
    } else {
    	trap.setNodeName("TestNode 1");
    	trap.setLocation("Testbed 1");
    }
    trap.setCustomText(t);
    trap.setSeverity(4);
    trap.setGroup("Alarm");
    
    if (trap.isV1) {
    	
    } else if (trap.isV2C) {
    }
    var response = new Object();
    var u = ctx.getSomeCtx();
    u = sestej(u);
    trap.setTimestamp(u);
    return trap;
}
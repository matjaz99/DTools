function sestej(x) {
    return x + 1;
}

function processTrap(trap) {
    
    if (trap.peerIp == "192.168.1.110") {
    	trap.nodeName = "CentOS 6";
    	trap.location = "Private network";
    } else {
    	trap.nodeName = "TestNode 1";
    	trap.location = "Testbed 1";
    }
    
    var t = trap.community + " ";
    var varbinds = trap.varbinds;
    for (var i = 0; i < varbinds.length; i++) {
    	var oid = varbinds[i].oid;
    	var value = varbinds[i].value;
    	t += oid + ":" + value + ", ";
	}
    
    trap.customText = t;
    trap.severity = 2;
    trap.group = "Alarm";
    
    if (trap.isV1) {
    	
    } else if (trap.isV2C) {
    	
    }
    var response = new Object();
    var u = ctx.getSomeCtx();
    u = sestej(u);
    // trap.setTimestamp(u);
    return trap;
}
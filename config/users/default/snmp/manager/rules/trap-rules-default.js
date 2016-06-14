function sestej(x) {
    return x + 1;
}

function processTrap(trap) {
	
    // set node name and location
    if (trap.peerIp == "192.168.1.110") {
    	trap.nodeName = "CentOS 6";
    	trap.location = "Private network";
    } else {
    	trap.nodeName = "TestNode 1";
    	trap.location = "New York";
    }
    
    // ignore non-public traps
    if (trap.community != "public") {
    	trap.ignore = true;
    	return trap;
    }
    
    // set custom trap group
    trap.group = "Alarm";
    
    var t = trap.community + " ";
    var varbinds = trap.varbinds;
    for (var i = 0; i < varbinds.length; i++) {
    	var oid = varbinds[i].oid;
    	var value = varbinds[i].value;
    	t += oid + ":" + value + ", ";
	}
    
    // trap.customText = t;
    
    // check version of trap
    if (trap.isV1 == true) {
    	
    	if (trap.enterpriseOid == "1.3.6.1.4.1.3183.1.1") {
    		// Shelf Manager
    		trap.severity = 4;
    		trap.customText = "Shelf Manager";
    	} else if (trap.enterpriseOid == "1.1.2.2.3.3.111") {
    		// example
    		trap.severity = 3;
    		trap.trapName = "hiTemp";
    		trap.name = "Temperature is too high";
    		trap.extDat1 = getValue(trap.varbinds, "1.1.2.2.3.3.112");
    		trap.customText = trap.name + " (" + trap.extDat1 + ")";
    	} else {
    		trap.severity = 6;
    		// trap.customText = "genericTrap: " + trap.genericTrap;
    		trap.customText = "specificTrap: " + trap.specificTrap;
    	}
    	
    } else if (trap.isV2C == true) {
    	
    	if (trap.snmpTrapOid == "1.3.6.1.6.3.1.1.5.3") {
    		// LinkDown
    		trap.severity = 1;
    		trap.trapName = "LinkDown";
    		trap.customText = trap.trapName;
    		trap.extDat1 = "ifAdminStatus=" + getValue(trap.varbinds, "1.3.6.1.2.1.2.2.1.7");
    		trap.extDat2 = "ifOperStatus=" + getValue(trap.varbinds, "1.3.6.1.2.1.2.2.1.8");
    		trap.customText += " " + trap.extDat1 + " " + trap.extDat2;
    	} else if (trap.snmpTrapOid == "1.3.6.1.6.3.1.1.5.4") {
    		// LinkUp
    		trap.severity = 5;
    		trap.trapName = "LinkUp";
    		trap.customText = trap.trapName;
    		trap.extDat1 = "ifAdminStatus=" + getValue(trap.varbinds, "1.3.6.1.2.1.2.2.1.7");
    		trap.extDat2 = "ifOperStatus=" + getValue(trap.varbinds, "1.3.6.1.2.1.2.2.1.8");
    		trap.customText += " " + trap.extDat1 + " " + trap.extDat2;
    	} else if (trap.snmpTrapOid == "1.3.6.1.6.3.1.1.5.5") {
    		// LinkUp
    		trap.severity = 6;
    		trap.trapName = "AuthenticationFailure";
    		trap.customText = trap.trapName;
    	} else if (trap.snmpTrapOid == "1.2.1.3.6.1.444.1.1") {
    		// example
    		trap.severity = 2;
    		trap.customText = "myTrap 1";
    	} else if (trap.snmpTrapOid == "1.2.1.3.6.1.444.1.2") {
    		// example
    		trap.severity = 5;
    		trap.customText = "myTrap 2";
    	} else {
    		trap.severity = 6;
    		trap.customText = trap.snmpTrapOid;
    	}
    	
    }
    var response = new Object();
    var u = ctx.getSomeCtx();
    u = sestej(u);
    // trap.setTimestamp(u);
    return trap;
}

// search varbinds for oid and return value
function getValue(varbinds, oid) {
    for (var i = 0; i < varbinds.length; i++) {
    	if (varbinds[i].oid == oid) {
    		return varbinds[i].value;
    	}
	}
	for (var i = 0; i < varbinds.length; i++) {
    	if (varbinds[i].oid.startsWith(oid)) {
    		return varbinds[i].value;
    	}
	}
	return "";
}


package si.matjazcerkvenik.dtools.tools.snmp.impl;


public class SnmpContext {

	int someCtx;
	
	public static final int UNDEFINED = 0;
	public static final int CRITICAL = 1;
	public static final int MAJOR = 2;
	public static final int MINOR = 3;
	public static final int WARNING = 4;
	public static final int CLEAR = 5;
	public static final int INFO = 6;

	public int getSomeCtx() {
		return someCtx;
	}

	public void setSomeCtx(int someCtx) {
		this.someCtx = someCtx;
	}
	

}

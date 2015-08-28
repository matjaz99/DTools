package si.matjazcerkvenik.dtools.tools.snmp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class TrapProcessor {
	
	private SimpleLogger logger;
	
	private ScriptEngineManager scriptManager; 
    private ScriptEngine engine;
    private SnmpContext myctx;
    
    public TrapProcessor() {
    	logger = DToolsContext.getInstance().getLogger();
	}
    
    public void init() {
    	scriptManager = new ScriptEngineManager();
        engine = scriptManager.getEngineByName("JavaScript");  
        myctx = new SnmpContext();
        myctx.setSomeCtx(1000);
        
        Reader reader;
		try {
			reader = new FileReader(DToolsContext.HOME_DIR + "/config/parser.js");
			engine.eval(reader);
	        reader.close();
		} catch (FileNotFoundException e) {
			logger.error("TrapProcessor.init(): FileNotFoundException", e);
		} catch (ScriptException e) {
			logger.error("TrapProcessor.init(): ScriptException", e);
		} catch (IOException e) {
			logger.error("TrapProcessor.init(): IOException", e);
		}
        
        engine.put("myctx", myctx);
        
	}
    
    public Object process(TrapNotification alarm) {
        alarm.setCustomText("to je tekst");
        Invocable inv = (Invocable) engine;
        Object result = null;
		try {
			result = inv.invokeFunction("processTrap", alarm);
		} catch (ScriptException e) {
			logger.error("TrapProcessor.init(): ScriptException", e);
			return alarm;
		} catch (NoSuchMethodException e) {
			logger.error("TrapProcessor.init(): NoSuchMethodException", e);
			return alarm;
		}
        return result;
	}
	
}

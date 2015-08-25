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

public class TrapProcessor {
	
	private ScriptEngineManager scriptManager; 
    private ScriptEngine engine;
    private MyContext myctx;
    
    public void init() {
    	scriptManager = new ScriptEngineManager();
        engine = scriptManager.getEngineByName("JavaScript");  
        myctx = new MyContext();
        myctx.setSomeCtx(1000);
        
        Reader reader;
		try {
			reader = new FileReader(DToolsContext.HOME_DIR + "/config/parser.js");
			engine.eval(reader);
	        reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
        System.out.println(result.toString());
        return result;
	}
	
}

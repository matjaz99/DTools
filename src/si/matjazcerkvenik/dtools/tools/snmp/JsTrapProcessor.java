/* 
 * Copyright (C) 2015 Matjaz Cerkvenik
 * 
 * DTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * DTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with DTools. If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package si.matjazcerkvenik.dtools.tools.snmp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

/**
 * This class calls javascript trap rules.
 * @author matjaz
 *
 */
public class JsTrapProcessor {
	
	private SimpleLogger logger;
	private boolean isInitialized = false;
	
	private ScriptEngineManager scriptManager; 
    private ScriptEngine engine;
    private SnmpContext ctx;
    
    public JsTrapProcessor() {
    	logger = DToolsContext.getInstance().getLogger();
	}
    
    public void init() {
    	scriptManager = new ScriptEngineManager();
        engine = scriptManager.getEngineByName("JavaScript");  
        ctx = new SnmpContext();
        ctx.setSomeCtx(1000);
        
        Reader reader;
		try {
			reader = new FileReader(DToolsContext.HOME_DIR + "/" + DProps.getProperty(DProps.SNMP_RECEIVER_RULES_FILE));
			engine.eval(reader);
	        reader.close();
	        engine.put("ctx", ctx);
	        isInitialized = true;
		} catch (FileNotFoundException e) {
			logger.error("TrapProcessor.init(): FileNotFoundException", e);
		} catch (ScriptException e) {
			logger.error("TrapProcessor.init(): ScriptException", e);
		} catch (IOException e) {
			logger.error("TrapProcessor.init(): IOException", e);
		}
        
	}
    
    public Object process(TrapNotification alarm) {
    	if (!isInitialized) {
    		logger.warn("TrapProcessor.process(): engine not initialized");
			return alarm;
		}
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

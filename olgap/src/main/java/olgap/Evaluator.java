package olgap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.eclipse.rdf4j.model.ValueFactory;

//import groovy.lang.GroovyShell;

public class Evaluator {
	static private final  Logger logger = LogManager.getLogger(Evaluator.class);
	static protected   HashMap<ValueFactory, Source > sources = new HashMap<ValueFactory, Source>();
	static protected ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	static protected HashMap<String, ScriptEngine> scriptEngines = new HashMap<String, ScriptEngine>();
	
	static private  MessageDigest digest;
	static protected  boolean trace = false;
	static protected  boolean initialized = false;

	public static final String NAMESPACE = "http://inova8.com/olgap/";

	public Evaluator() {
		super();
		if(!initialized) {
			try {
				digest = MessageDigest.getInstance("MD5");
				List<ScriptEngineFactory> engineFactories = scriptEngineManager.getEngineFactories();
				if (engineFactories.isEmpty()) {
					logger.error("No scripting engines were found");
					return;
				}
				String engineNames = "";
				for (ScriptEngineFactory engineFactory : engineFactories) {
					for (String engineName : engineFactory.getNames()) {
						ScriptEngine engine = scriptEngineManager.getEngineByName(engineName);
						scriptEngines.put(engineName, engine);
						engineNames = engineNames + engineName + ";";
					}
				}
				logger.info( engineFactories.size() + " scripting engines were found with the following short names:" + engineNames);
				setTrace(Evaluator.trace);
				
				initialized=true;
			} catch (NoSuchAlgorithmException e) {
				logger.error( e.getMessage());
			}
		}
	}


	public static String bytesToHex(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static boolean setTrace(boolean trace) {
		if(trace) {
			Configurator.setRootLevel(Level.TRACE);
			Evaluator.trace=true;
		}else {
			Configurator.setRootLevel(Level.DEBUG);
			Evaluator.trace=false;
		}	
		return Evaluator.trace;
	}
	protected static byte[] getDigest(String key) {
		return digest.digest(key.getBytes());
	}
	protected static String getHexKey(String key) {
		return bytesToHex(getDigest(key));
	}
}
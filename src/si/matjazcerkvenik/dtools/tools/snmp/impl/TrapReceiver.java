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

package si.matjazcerkvenik.dtools.tools.snmp.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.faces.event.ValueChangeEvent;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import si.matjazcerkvenik.dtools.context.DProps;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.xml.DAO;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class TrapReceiver implements Serializable, CommandResponder {
	
	private static final long serialVersionUID = 8047737749908071L;
	private SimpleLogger logger;
	private SimpleLogger trapsLogger;
	
	private String name;
	private String ip;
	private int port;
	
	private ThreadPool threadPool;
	private MessageDispatcher dispatcher;
	private Address listenAddress;
	private Snmp snmp;
	private boolean active = false;
	
	private int counterOfReceivedTraps = 0;
	private int queueSize = 100;
	private ConcurrentLinkedQueue<TrapNotification> receivedTrapNotifications = new ConcurrentLinkedQueue<TrapNotification>();
	
	public TrapReceiver() {
		init();
	}
	
	public TrapReceiver(String name, String ip, int port) {
		
		init();
		this.name = name;
		this.ip = ip;
		this.port = port;
		
	}
	
	public void init() {
		logger = DToolsContext.getInstance().getLogger();
		String size = DProps.getProperty(DProps.SNMP_RECEIVER_QUEUE_SIZE);
		try {
			queueSize = Integer.parseInt(size);
		} catch (NumberFormatException e) {
			queueSize = 100;
		}
		trapsLogger = new SimpleLogger(DToolsContext.HOME_DIR+ "/log/snmp-traps-" + name + ".log");
		trapsLogger.setVerbose(logger.isVerbose());
	}
	
	public void start() {

		try {
			threadPool = ThreadPool.create("Trap", 2);

			dispatcher = new MultiThreadedMessageDispatcher(threadPool,
					new MessageDispatcherImpl());

//		listenAddress = GenericAddress.parse("udp:0.0.0.0/6162");
			listenAddress = new UdpAddress(ip + "/" + port); // localhost/6162

			@SuppressWarnings("rawtypes")
			TransportMapping transport;

			if (listenAddress instanceof UdpAddress) {
				transport = new DefaultUdpTransportMapping(
						(UdpAddress) listenAddress);
			} else {
				transport = new DefaultTcpTransportMapping(
						(TcpAddress) listenAddress);
			}

			snmp = new Snmp(dispatcher, transport);
			snmp.addCommandResponder(this); // this class will process traps
			
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());

			USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(
					MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
			snmp.listen();
			
			logger.info("SnmpTrapReceiver.start(): listening on port " + port);
			
			active = true;
			
		} catch (IOException e) {
			logger.error("SnmpTrapReceiver.start(): IOException", e);
			active = false;
		}
		
		
		
	}
	
	public void stop() {
		try {
			snmp.close();
			threadPool.interrupt();
			snmp = null;
			active = false;
		} catch (IOException e) {
			logger.error("SnmpTrapReceiver.stop(): IOException", e);
		}
		logger.info("SnmpTrapReceiver.stop(): stop listening");
	}
	
	
	public void toggle() {
		if (snmp == null) {
			start();
		} else {
			stop();
		}
	}

	
	public boolean isActive() {
		return active;
	}

	@XmlTransient
	public void setActive(boolean active) {
		toggle();
	}

	/**
	 * This method will be called whenever a pdu is received on the given port
	 * specified in the listen() method
	 */
	public synchronized void processPdu(CommandResponderEvent cmdRespEvent) {
		PDU pdu = cmdRespEvent.getPDU();
		
		// create TrapNotification
		TrapNotification tn = new TrapNotification(counterOfReceivedTraps++, name, cmdRespEvent);
		
		JsTrapProcessor tProc = new JsTrapProcessor();
		tProc.init();
		tn = (TrapNotification) tProc.process(tn);
		
		if (!tn.isIgnore()) {
			if (receivedTrapNotifications.size() > queueSize) {
				receivedTrapNotifications.poll();
			}
			receivedTrapNotifications.add(tn);
			trapsLogger.info("SnmpTrapReceiver.processPdu(): " + tn.toStringRaw());
		}
		
		if (pdu != null) {

			logger.trace("SnmpTrapReceiver.processPdu(): PDU = " + pdu.toString());
			int pduType = pdu.getType();
			if ((pduType != PDU.TRAP) && (pduType != PDU.V1TRAP)
					&& (pduType != PDU.REPORT) && (pduType != PDU.RESPONSE)) {
				pdu.setErrorIndex(0);
				pdu.setErrorStatus(0);
				pdu.setType(PDU.RESPONSE);
				StatusInformation statusInformation = new StatusInformation();
				StateReference ref = cmdRespEvent.getStateReference();
				try {
					logger.trace("SnmpTrapReceiver.processPdu(): response PDU = " + cmdRespEvent.getPDU().toString());
					// response to INFORM
					cmdRespEvent.getMessageDispatcher().returnResponsePdu(
							cmdRespEvent.getMessageProcessingModel(),
							cmdRespEvent.getSecurityModel(),
							cmdRespEvent.getSecurityName(),
							cmdRespEvent.getSecurityLevel(), pdu,
							cmdRespEvent.getMaxSizeResponsePDU(), ref,
							statusInformation);
				} catch (MessageException e) {
					logger.error("SnmpTrapReceiver.processPdu(): MessageException: ", e);
				}
			}
		}
	}

	public ConcurrentLinkedQueue<TrapNotification> getReceivedTrapNotifications() {
		return receivedTrapNotifications;
	}
	
	public void clearReceivedTraps() {
		receivedTrapNotifications.clear();
	}
	
	
	
	

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	@XmlElement
	public void setIp(String ip) {
		System.out.println("setIP=" + ip);
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	@XmlElement
	public void setPort(int port) {
		this.port = port;
	}
	
	public void changedName(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		name = e.getNewValue().toString();
		DAO.getInstance().saveSnmpManager();
	}
	
	public void changedIp(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		ip = e.getNewValue().toString();
		DAO.getInstance().saveSnmpManager();
	}
	
	public void changedPort(ValueChangeEvent e) {
		if (e.getOldValue().toString().equalsIgnoreCase(e.getNewValue().toString())) {
			return;
		}
		port = Integer.parseInt(e.getNewValue().toString().trim());
		DAO.getInstance().saveSnmpManager();
	}
	
}

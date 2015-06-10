package si.matjazcerkvenik.dtools.tools.snmp;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

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

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class SnmpTrapReceiver implements CommandResponder {
	
	private SimpleLogger logger;
	private SimpleLogger trapsLogger;
	
	private ThreadPool threadPool;
	private MessageDispatcher dispatcher;
	private Address listenAddress;
	private Snmp snmp;
	
	private ConcurrentLinkedQueue<PDU> receivedTraps = new ConcurrentLinkedQueue<PDU>();
	
	public SnmpTrapReceiver() {
		logger = DToolsContext.getInstance().getLogger();
		
		trapsLogger = new SimpleLogger(DToolsContext.HOME_DIR+ "/log/snmp-traps.log");
		trapsLogger.setVerbose(logger.isVerbose());
	}
	
	public void start(String ip, int port) {

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
			
		} catch (IOException e) {
			logger.error("SnmpTrapReceiver.start(): IOException", e);
		}
		
	}
	
	public void stop() {
		try {
			snmp.close();
			threadPool.interrupt();
		} catch (IOException e) {
			logger.error("SnmpTrapReceiver.stop(): IOException", e);
		}
		logger.info("SnmpTrapReceiver.stop(): stop listening");
	}
	
	/**
	 * This method will be called whenever a pdu is received on the given port
	 * specified in the listen() method
	 */
	public synchronized void processPdu(CommandResponderEvent cmdRespEvent) {
		PDU pdu = cmdRespEvent.getPDU();
		
		if (receivedTraps.size() > 100) {
			receivedTraps.poll();
		}
		receivedTraps.add(pdu);
		
		trapsLogger.info("SnmpTrapReceiver.processPdu(): PDU = " + pdu.toString());
		
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

	public ConcurrentLinkedQueue<PDU> getReceivedTraps() {
		return receivedTraps;
	}
	
	public void clearReceivedTraps() {
		receivedTraps.clear();
	}
	
	
}

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

package si.matjazcerkvenik.dtools.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpAgent;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpClient;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpClients;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpManager;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpRow;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpSimulator;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTable;
import si.matjazcerkvenik.dtools.tools.snmp.SnmpTrap;
import si.matjazcerkvenik.dtools.tools.snmp.TrapsTable;
import si.matjazcerkvenik.dtools.tools.snmp.impl.TrapReceiver;
import si.matjazcerkvenik.dtools.xml.Commands;
import si.matjazcerkvenik.dtools.xml.FtpClient;
import si.matjazcerkvenik.dtools.xml.FtpClients;
import si.matjazcerkvenik.dtools.xml.FtpTransfer;
import si.matjazcerkvenik.dtools.xml.FtpTransfers;
import si.matjazcerkvenik.dtools.xml.NetworkNodes;
import si.matjazcerkvenik.dtools.xml.Node;
import si.matjazcerkvenik.dtools.xml.Note;
import si.matjazcerkvenik.dtools.xml.Notes;
import si.matjazcerkvenik.dtools.xml.SshClient;
import si.matjazcerkvenik.dtools.xml.SshClients;
import si.matjazcerkvenik.dtools.xml.SshResponse;
import si.matjazcerkvenik.dtools.xml.Todo;
import si.matjazcerkvenik.dtools.xml.Todos;
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class DAO {

	private static DAO instance;
	private SimpleLogger logger;

	private NetworkNodes networkNodes;
	private SshClients sshClients;
	private FtpClients ftpClients;
	private FtpTransfers ftpTransfers;
	private Commands commands;
	private SnmpManager snmpManager;
	private SnmpClients snmpClients;
	private SnmpSimulator snmpSimulator;
	private Notes notes;
	private Todos todos;
	
	private String XML_NETWORK_NODES = "/config/users/$DTOOLS_USER$/network/networkNodes.xml";
	
	private String XML_SSH_CLIENTS = "/config/users/$DTOOLS_USER$/ssh/sshClients.xml";
	private String XML_SSH_COMMANDS = "/config/users/$DTOOLS_USER$/ssh/sshCommands.xml";
	private String DIR_SAVE_SSH_RESPONSE = "/config/users/$DTOOLS_USER$/ssh/saved";
	private String XML_SAVE_SSH_RESPONSE = "/config/users/$DTOOLS_USER$/ssh/saved/$FILENAME$.xml";
	private String TXT_SAVE_SSH_RESPONSE = "/config/users/$DTOOLS_USER$/ssh/saved/$FILENAME$.txt";
	
	private String XML_FTP_CLIENTS = "/config/users/$DTOOLS_USER$/ftp/ftpClients.xml";
	private String XML_FTP_TRANSFERS = "/config/users/$DTOOLS_USER$/ftp/ftpTransfers.xml";
	
	private String XML_SNMP_MANAGER = "/config/users/$DTOOLS_USER$/snmp/manager/snmpManager.xml";
	private String XML_SNMP_CLIENTS = "/config/users/$DTOOLS_USER$/snmp/client/snmpClients.xml";
	private String DIR_SNMP_SIMULATOR = "/config/users/$DTOOLS_USER$/snmp/simulator";
	private String TXT_SAVE_RECEIVED_TRAPS = "/config/users/$DTOOLS_USER$/snmp/manager/temp/$FILENAME$.txt";
	
	private String XML_NOTES = "/config/users/$DTOOLS_USER$/misc/notes.xml";
	private String XML_TODOS = "/config/users/$DTOOLS_USER$/misc/todos.xml";
	

	private DAO() {
		// singleton
		logger = DToolsContext.getInstance().getLogger();
		
		XML_NETWORK_NODES = XML_NETWORK_NODES.replace("$DTOOLS_USER$", "default");
		
		XML_SSH_CLIENTS = XML_SSH_CLIENTS.replace("$DTOOLS_USER$", "default");
		XML_SSH_COMMANDS = XML_SSH_COMMANDS.replace("$DTOOLS_USER$", "default");
		DIR_SAVE_SSH_RESPONSE = DIR_SAVE_SSH_RESPONSE.replace("$DTOOLS_USER$", "default");
		XML_SAVE_SSH_RESPONSE = XML_SAVE_SSH_RESPONSE.replace("$DTOOLS_USER$", "default");
		TXT_SAVE_SSH_RESPONSE = TXT_SAVE_SSH_RESPONSE.replace("$DTOOLS_USER$", "default");
		
		XML_FTP_CLIENTS = XML_FTP_CLIENTS.replace("$DTOOLS_USER$", "default");
		XML_FTP_TRANSFERS = XML_FTP_TRANSFERS.replace("$DTOOLS_USER$", "default");
		
		XML_SNMP_MANAGER = XML_SNMP_MANAGER.replace("$DTOOLS_USER$", "default");
		XML_SNMP_CLIENTS = XML_SNMP_CLIENTS.replace("$DTOOLS_USER$", "default");
		DIR_SNMP_SIMULATOR = DIR_SNMP_SIMULATOR.replace("$DTOOLS_USER$", "default");
		TXT_SAVE_RECEIVED_TRAPS = TXT_SAVE_RECEIVED_TRAPS.replace("$DTOOLS_USER$", "default");
		
		XML_NOTES = XML_NOTES.replace("$DTOOLS_USER$", "default");
		XML_TODOS = XML_TODOS.replace("$DTOOLS_USER$", "default");
		
	}

	public static DAO getInstance() {
		if (instance == null) {
			instance = new DAO();
		}
		return instance;
	}
	
	
	public void resetAllDataToNull() {
		networkNodes = null;
		sshClients = null;
		ftpClients = null;
		ftpTransfers = null;
		commands = null;
		snmpManager = null;
		snmpClients = null;
		snmpSimulator = null;
		notes = null;
		todos = null;
	}
	
	
	
	
	
	

	/* NETWORK NODES */

	
	/**
	 * Load network nodes configuration
	 * @return networkNodes
	 */
	public NetworkNodes loadNetworkNodes() {

		if (networkNodes != null) {
			return networkNodes;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_NETWORK_NODES);
			if (!file.exists()) {
				networkNodes = new NetworkNodes();
				JAXBContext jaxbContext = JAXBContext
						.newInstance(NetworkNodes.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.marshal(networkNodes, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(NetworkNodes.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			networkNodes = (NetworkNodes) jaxbUnmarshaller.unmarshal(file);
			if (networkNodes.getNodesList() == null) {
				networkNodes.setNodesList(new ArrayList<Node>());
			}
			
			for (Node n : networkNodes.getNodesList()) {
				n.init();
			}
			
			logger.info("DAO:loadNetworkNodes(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadNetworkNodes(): JAXBException: ", e);
		}

		return networkNodes;

	}
	
	/**
	 * Find network nodes according to given name.
	 * @param name
	 * @return node
	 */
	public Node findNode(String name) {
		for (Node n : networkNodes.getNodesList()) {
			if (n.getName().equals(name)) {
				return n;
			}
		}
		return null;
	}

	/**
	 * Save network nodes configuration
	 */
	public void saveNetworkNodes() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_NETWORK_NODES);
			JAXBContext jaxbContext = JAXBContext.newInstance(NetworkNodes.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(networkNodes, file);
			
			logger.info("DAO:saveNetworkNodes(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveNetworkNodes(): JAXBException: ", e);
		}

	}

	/**
	 * Add new node and save configuration
	 * @param node
	 */
	public void addNode(Node node) {

		networkNodes.addNode(node);
		saveNetworkNodes();

	}

	/**
	 * Permanently delete node and save configuration
	 * @param node
	 */
	public void deleteNode(Node node) {

		networkNodes.deleteNode(node);
		saveNetworkNodes();

	}
	
	
	
	

	/* SSH CLIENTS */

	public SshClients loadSshClients() {

		if (sshClients != null) {
			return sshClients;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SSH_CLIENTS);
			if (!file.exists()) {
				sshClients = new SshClients();
				JAXBContext jaxbContext = JAXBContext
						.newInstance(SshClients.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.marshal(sshClients, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(SshClients.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			sshClients = (SshClients) jaxbUnmarshaller.unmarshal(file);
			if (sshClients.getSshClientList() == null) {
				sshClients.setSshClientList(new ArrayList<SshClient>());
			}
			
			logger.info("DAO:loadSshClients(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadSshClients(): JAXBException: ", e);
		}

		return sshClients;

	}
	
	public SshClient findSshClient(String sshUrl) {
		
		for (SshClient ssh : sshClients.getSshClientList()) {
			if (ssh.toUrlString().equals(sshUrl)) {
				return ssh;
			}
		}
		return null;
		
	}

	public void saveSshClients() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SSH_CLIENTS);
			JAXBContext jaxbContext = JAXBContext.newInstance(SshClients.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(sshClients, file);
			
			logger.info("DAO:saveSshClients(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveSshClients(): JAXBException: ", e);
		}

	}

	public void addSshClient(SshClient client) {

		sshClients.addSshClientAction(client);
		saveSshClients();

	}

	public void deleteSshClient(SshClient client) {

		sshClients.deleteSshClient(client);
		saveSshClients();

	}
	
	
	
	
	
	/* FTP CLIENTS */
	
	
	public FtpClients loadFtpClients() {

		if (ftpClients != null) {
			return ftpClients;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_FTP_CLIENTS);
			if (!file.exists()) {
				ftpClients = new FtpClients();
				JAXBContext jaxbContext = JAXBContext
						.newInstance(FtpClients.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.marshal(ftpClients, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(FtpClients.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ftpClients = (FtpClients) jaxbUnmarshaller.unmarshal(file);
			if (ftpClients.getFtpClientList() == null) {
				ftpClients.setFtpClientList(new ArrayList<FtpClient>());
			}
			
			logger.info("DAO:loadFtpClients(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadFtpClients(): JAXBException: ", e);
		}

		return ftpClients;

	}
	
	public FtpClient findFtpClient(String ftpUrl) {
		
		for (FtpClient ftp : ftpClients.getFtpClientList()) {
			if (ftp.toUrlString().equals(ftpUrl)) {
				return ftp;
			}
		}
		return null;
		
	}

	public void saveFtpClients() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_FTP_CLIENTS);
			JAXBContext jaxbContext = JAXBContext.newInstance(FtpClients.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(ftpClients, file);
			
			logger.info("DAO:saveFtpClients(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveFtpClients(): JAXBException: ", e);
		}

	}

	public void addFtpClient(FtpClient client) {

		ftpClients.addFtpClientAction(client);
		saveFtpClients();

	}

	public void deleteFtpClient(FtpClient client) {

		ftpClients.deleteFtpClient(client);
		saveFtpClients();

	}
	
	
	

	/* SSH COMMANDS */
	

	public Commands loadCommands() {

		if (commands != null) {
			return commands;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SSH_COMMANDS);
			if (!file.exists()) {
				commands = new Commands();
				JAXBContext jaxbContext = JAXBContext
						.newInstance(Commands.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.marshal(commands, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(Commands.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			commands = (Commands) jaxbUnmarshaller.unmarshal(file);
			if (commands.getCommands() == null) {
				commands.setCommands(new ArrayList<String>());
			}
			
			logger.info("DAO:loadCommands(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadCommands(): JAXBException: ", e);
		}

		return commands;

	}

	public void saveCommands() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SSH_COMMANDS);
			JAXBContext jaxbContext = JAXBContext.newInstance(Commands.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(commands, file);
			
			logger.info("DAO:saveCommands(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveCommands(): JAXBException: ", e);
		}

	}

	public void addCommand(String cmd) {

		commands.addCommand(cmd);
		saveCommands();

	}

	public void deleteCommand(String cmd) {

		commands.deleteCommand(cmd);
		saveCommands();

	}
	
	
	
	
	/* FTP TRANSFERS */
	

	public FtpTransfers loadTransfers() {

		if (ftpTransfers != null) {
			return ftpTransfers;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_FTP_TRANSFERS);
			if (!file.exists()) {
				ftpTransfers = new FtpTransfers();
				JAXBContext jaxbContext = JAXBContext
						.newInstance(FtpTransfers.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.marshal(ftpTransfers, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(FtpTransfers.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			ftpTransfers = (FtpTransfers) jaxbUnmarshaller.unmarshal(file);
			if (ftpTransfers.getTransfersList() == null) {
				ftpTransfers.setTransfersList(new ArrayList<FtpTransfer>());
			}
			
			logger.info("DAO:loadTransfers(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadTransfers(): JAXBException: ", e);
		}

		return ftpTransfers;

	}

	public void saveTransfers() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_FTP_TRANSFERS);
			JAXBContext jaxbContext = JAXBContext.newInstance(FtpTransfers.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(ftpTransfers, file);
			
			logger.info("DAO:saveTransfers(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveTransfers(): JAXBException: ", e);
		}

	}

	public void addFtpTransfer(FtpTransfer t) {

		ftpTransfers.addFtpTransfer(t);
		saveTransfers();

	}

	public void deleteFtpTransfer(FtpTransfer t) {

		ftpTransfers.deleteFtpTransfer(t);
		saveTransfers();

	}
	
	
	

	/* NOTES */
	

	public Notes loadNotes() {

		if (notes != null) {
			return notes;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_NOTES);
			if (!file.exists()) {
				notes = new Notes();
				JAXBContext jaxbContext = JAXBContext.newInstance(Notes.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.marshal(notes, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(Notes.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			notes = (Notes) jaxbUnmarshaller.unmarshal(file);
			if (notes.getNotesList() == null) {
				notes.setNotesList(new ArrayList<Note>());
			}
			
			logger.info("DAO:loadNotes(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadNotes(): JAXBException: ", e);
		}

		return notes;

	}

	public void saveNotes() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_NOTES);
			JAXBContext jaxbContext = JAXBContext.newInstance(Notes.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(notes, file);
			
			logger.info("DAO:saveNotes(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveNotes(): JAXBException: ", e);
		}

	}

	public void addNote(Note n) {

		notes.addNote(n);
		saveNotes();

	}

	public void deleteNote(Note n) {

		notes.deleteNote(n);
		saveNotes();

	}
	
	
	
	
	/* TODOS */
	
	
	public Todos loadTodos() {

		if (todos != null) {
			return todos;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_TODOS);
			if (!file.exists()) {
				todos = new Todos();
				JAXBContext jaxbContext = JAXBContext.newInstance(Todos.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(todos, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(Todos.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			todos = (Todos) jaxbUnmarshaller.unmarshal(file);
			if (todos.getTodoList() == null) {
				todos.setTodoList(new ArrayList<Todo>());
			}
			
			logger.info("DAO:loadTodos(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadTodos(): JAXBException: ", e);
		}

		return todos;

	}

	public void saveTodos() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_TODOS);
			JAXBContext jaxbContext = JAXBContext.newInstance(Todos.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(todos, file);
			
			logger.info("DAO:saveTodos(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveTodos(): JAXBException: ", e);
		}

	}

	public void addTodo(Todo t) {

		todos.addTodo(t);
		saveTodos();

	}

	public void deleteTodo(Todo t) {

		todos.deleteTodo(t);
		saveTodos();

	}
	
	
	
	
	
	/* SNMP MANAGER */
	
	
	public SnmpManager loadSnmpManager() {

		if (snmpManager != null) {
			return snmpManager;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SNMP_MANAGER);
			if (!file.exists()) {
				snmpManager = new SnmpManager();
				snmpManager.createDefaultTrapReceiver();
				JAXBContext jaxbContext = JAXBContext.newInstance(SnmpManager.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(snmpManager, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(SnmpManager.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			snmpManager = (SnmpManager) jaxbUnmarshaller.unmarshal(file);
			if (snmpManager.getTrapReceiversList() == null) {
				snmpManager.setTrapReceiversList(new ArrayList<TrapReceiver>());
			}
			
			logger.info("DAO:loadSnmpManager(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadSnmpManager(): JAXBException: ", e);
		}

		return snmpManager;

	}

	public void saveSnmpManager() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SNMP_MANAGER);
			JAXBContext jaxbContext = JAXBContext.newInstance(SnmpManager.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(snmpManager, file);
			
			logger.info("DAO:saveSnmpManager(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveSnmpManager(): JAXBException: ", e);
		}

	}

	public void addTrapReceiver(TrapReceiver r) {

		snmpManager.addTrapReceiver(r);
		saveSnmpManager();

	}

	public void deleteTrapReceiver(TrapReceiver r) {

		snmpManager.removeTrapReceiver(r);
		saveSnmpManager();

	}
	
	public TrapReceiver findtrapReceiver(String name) {
		for (TrapReceiver tr : snmpManager.getTrapReceiversList()) {
			if (tr.getName().equals(name)) {
				return tr;
			}
		}
		return null;
	}
	
	
	
	
	
	/* SNMP CLIENTS */
	

	public SnmpClients loadSnmpClients() {

		if (snmpClients != null) {
			return snmpClients;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SNMP_CLIENTS);
			if (!file.exists()) {
				snmpClients = new SnmpClients();
				JAXBContext jaxbContext = JAXBContext.newInstance(SnmpClients.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(snmpClients, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(SnmpClients.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			snmpClients = (SnmpClients) jaxbUnmarshaller.unmarshal(file);
			if (snmpClients.getSnmpClientsList() == null) {
				snmpClients.setSnmpClientsList(new ArrayList<SnmpClient>());
			}
			
			logger.info("DAO:loadSnmpClients(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadSnmpClients(): JAXBException: ", e);
		}

		return snmpClients;

	}
	
	public SnmpClient findSnmpClient(String sshUrl) {
		
		for (SnmpClient	 snmp : snmpClients.getSnmpClientsList()) {
			if (snmp.toUrlString().equals(sshUrl)) {
				return snmp;
			}
		}
		return null;
		
	}

	public void saveSnmpClients() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SNMP_CLIENTS);
			JAXBContext jaxbContext = JAXBContext.newInstance(SnmpClients.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(snmpClients, file);
			
			logger.info("DAO:saveSnmpClients(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveSnmpClients(): JAXBException: ", e);
		}

	}

	public void addSnmpClient(SnmpClient c) {

		snmpClients.addSnmpClient(c);
		saveSnmpClients();

	}

	public void deleteSnmpClients(SnmpClient c) {

		snmpClients.deleteSnmpClient(c);
		saveSnmpClients();

	}
	
	
	
	
	
	/* SNMP AGENT SIMULATOR */
	
	
	/**
	 * Load SNMP agents. <code>SnmpSimulator</code> class holds all agents 
	 * and handles all the operations on agents (adding, removing...)
	 * @return snmpSimulator
	 */
	public SnmpSimulator loadSnmpSimulator() {

		if (snmpSimulator != null) {
			return snmpSimulator;
		}
		
		snmpSimulator = new SnmpSimulator();
		
		// load agent directories
		File simDir = new File(DToolsContext.HOME_DIR + DIR_SNMP_SIMULATOR);
		File[] simFiles = simDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		
		
		// load agent configuration
		for (int i = 0; i < simFiles.length; i++) {
			
			File agentXmlFile = new File(simFiles[i].getAbsolutePath() + "/agent.xml");
			SnmpAgent agent = loadAgentMetadata(agentXmlFile);
			
			File trapsDir = new File(simFiles[i].getAbsolutePath() + "/traps");
			List<TrapsTable> snmpTraps = loadSnmpTraps(trapsDir);
			
			File tablesDir = new File(simFiles[i].getAbsolutePath() + "/tables");
			List<SnmpTable> snmpTablesList = loadSnmpTables(tablesDir);
			
			agent.setTrapsTableList(snmpTraps);
			agent.setSnmpTablesList(snmpTablesList);
			
			snmpSimulator.addSnmpAgent(agent);
			
		}

		return snmpSimulator;

	}
	
	/**
	 * Load agent.xml file.
	 * @param file
	 * @return snmpAgent
	 */
	public SnmpAgent loadAgentMetadata(File file) {
		
		SnmpAgent snmpAgent = null;
		
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(SnmpAgent.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			snmpAgent = (SnmpAgent) jaxbUnmarshaller.unmarshal(file);
			snmpAgent.setDirectoryPath(file.getParentFile().getAbsolutePath());
			snmpAgent.init();

			logger.info("DAO:loadAgentMetaFile(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadAgentMetaFile(): JAXBException: ", e);
		}
		
		return snmpAgent;
	}
	
	/**
	 * Save agent.xml file.
	 * @param agent
	 */
	public void saveAgentMetadata(SnmpAgent agent) {
		
		// TODO change also directory name
		
		try {
			
			File file = new File(agent.getDirectoryPath() + "/agent.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(SnmpAgent.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(agent, file);
			
			logger.info("DAO:saveAgentMetadata(): " + file.getAbsolutePath());
		} catch (JAXBException e) {
			logger.error("DAO:saveAgentMetadata(): JAXBException: ", e);
		}
		
	}
	
	
	/**
	 * Find SNMP agent according to name
	 * @param name
	 * @return agent
	 */
	public SnmpAgent findSnmpAgent(String name) {
		for (SnmpAgent a : snmpSimulator.getSnmpAgentsList()) {
			if (a.getName().equals(name)) return a;
		}
		return null;
	}
	
	
	
	public List<TrapsTable> loadSnmpTraps(File trapsDir) {

		List<TrapsTable> snmpTrapsList = new ArrayList<TrapsTable>();
		
		File[] trapsXml = trapsDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				return f.getAbsolutePath().endsWith(".xml");
			}
		});
		
		if (trapsXml == null || trapsXml.length == 0) {
			return snmpTrapsList;
		}
		
		for (int i = 0; i < trapsXml.length; i++) {
			
			try {
				
				JAXBContext jaxbContext = JAXBContext.newInstance(TrapsTable.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				TrapsTable snmpTraps = (TrapsTable) jaxbUnmarshaller.unmarshal(trapsXml[i]);
				if (snmpTraps.getTrapsList() == null) {
					snmpTraps.setTrapsList(new ArrayList<SnmpTrap>());
				}
				snmpTraps.setFilePath(trapsXml[i].getAbsolutePath());
				
				snmpTrapsList.add(snmpTraps);
				
				logger.info("DAO:loadSnmpTraps(): " + trapsXml[i].getAbsolutePath());

			} catch (JAXBException e) {
				logger.error("DAO:loadSnmpTraps(): JAXBException: ", e);
			}
			
		}
		
		return snmpTrapsList;

	}
	
	public void saveSnmpTraps(TrapsTable snmpTraps) {
		
		try {
			
			File file = new File(snmpTraps.getFilePath());
			JAXBContext jaxbContext = JAXBContext.newInstance(TrapsTable.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(snmpTraps, file);
			
			logger.info("DAO:saveSnmpTraps(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveSnmpTraps(): JAXBException: ", e);
		}
		
	}
	
	
	public void deleteTrapsTable(SnmpAgent agent, TrapsTable table) {
		for (int i = 0; i < snmpSimulator.getSnmpAgentsList().size(); i++) {
			if (snmpSimulator.getSnmpAgentsList().get(i).equals(agent)) {
				snmpSimulator.getSnmpAgentsList().get(i).getTrapsTableList().remove(table);
			}
		}
		delete(new File(table.getFilePath()));
		logger.info("DAO:deleteTrapsTable(): " + table.getFilePath());
	}
	
	
	
	
	
	public List<SnmpTable> loadSnmpTables(File trapsDir) {

		List<SnmpTable> snmpTableList = new ArrayList<SnmpTable>();
		
		File[] tablesXml = trapsDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				return f.getAbsolutePath().endsWith(".xml");
			}
		});
		
		if (tablesXml == null || tablesXml.length == 0) {
			return snmpTableList;
		}
		
		for (int i = 0; i < tablesXml.length; i++) {
			
			try {
				
				JAXBContext jaxbContext = JAXBContext.newInstance(SnmpTable.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				SnmpTable tbl = (SnmpTable) jaxbUnmarshaller.unmarshal(tablesXml[i]);
				
				tbl.setFilePath(tablesXml[i].getAbsolutePath());
				if (tbl.getRowsList() == null) {
					tbl.setRowsList(new ArrayList<SnmpRow>());
				}
				
				snmpTableList.add(tbl);
				
				logger.info("DAO:loadSnmpTables(): " + tablesXml[i].getAbsolutePath());

			} catch (JAXBException e) {
				logger.error("DAO:loadSnmpTables(): JAXBException: ", e);
			}
			
		}
		
		return snmpTableList;

	}
	
	
	/**
	 * Write table data to disc.
	 * @param table
	 */
	public void saveSnmpDataTable(SnmpTable table) {
		
		try {
			
			File file = new File(table.getFilePath());
			JAXBContext jaxbContext = JAXBContext.newInstance(SnmpTable.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(table, file);
			
			
			
			logger.info("DAO:saveSnmpTable(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveSnmpTable(): JAXBException: ", e);
		}
		
	}
	
	
	/**
	 * Delete table data xml file from disc.
	 * @param agent
	 * @param table
	 */
	public void deleteSnmpDataTable(SnmpAgent agent, SnmpTable table) {
		for (int i = 0; i < snmpSimulator.getSnmpAgentsList().size(); i++) {
			if (snmpSimulator.getSnmpAgentsList().get(i).equals(agent)) {
				snmpSimulator.getSnmpAgentsList().get(i).getSnmpTablesList().remove(table);
			}
		}
		delete(new File(table.getFilePath()));
		logger.info("DAO:deleteSnmpDataTable(): " + table.getFilePath());
	}
	
	
	public List<File> loadAgentMibs(SnmpAgent agent) {
		File mibsDir = new File(agent.getDirectoryPath() + "/mibs");
		File[] mibs = mibsDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				return f.getAbsolutePath().endsWith(".mib") || f.getAbsolutePath().endsWith(".txt");
			}
		});
		
		List<File> filesList = new ArrayList<File>();
		if (mibs == null || mibs.length == 0) {
			return filesList;
		}
		
		for (int i = 0; i < mibs.length; i++) {
			filesList.add(mibs[i]);
		}
		return filesList;
	}
	
	
	
	
	
	/**
	 * Add new agent, create directories and write agent.xml metadata
	 * @param a
	 */
	public void createNewSnmpAgent(SnmpAgent a) {
		
		a.setDirectoryPath(DToolsContext.HOME_DIR + DIR_SNMP_SIMULATOR + "/" + a.getName() + "-" + System.currentTimeMillis());
		
		File agentDir = new File(a.getDirectoryPath());
		agentDir.mkdirs();
		saveAgentMetadata(a);
		
		File trapsDir = new File(a.getDirectoryPath() + "/traps");
		trapsDir.mkdirs();
		
		File tablesDir = new File(a.getDirectoryPath() + "/tables");
		tablesDir.mkdirs();
		
		File mibsDir = new File(a.getDirectoryPath() + "/mibs");
		mibsDir.mkdirs();
		
		a.init();
		snmpSimulator.addSnmpAgent(a);

	}
	

	/**
	 * Delete agent and remove directory (including traps and tables subdirectories)
	 * @param a
	 */
	public void deleteSnmpAgent(SnmpAgent a) {
		
		File dir = new File(a.getDirectoryPath());
		delete(dir);
		
		snmpSimulator.removeSnmpAgent(a);

	}
	
	/**
	 * Delete directory including all files
	 * @param dir
	 * @return
	 */
	private boolean delete(File dir) {
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						delete(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return dir.delete();
	}
	
	
	
	
	
	
	
	/* SAVE RECEIVED TRAPS */
	
	/**
	 * Save received snmp traps in plain text file.
	 * @param filename
	 * @param trapsString
	 */
	public String saveReceivedTrapsAsTxt(String filename, String trapsString) {
		
		String file = filename + "-" + System.currentTimeMillis();
		file = DToolsContext.HOME_DIR + TXT_SAVE_RECEIVED_TRAPS.replaceFirst("$FILENAME$", file);
		writePlainTextFile(file, trapsString);
		return file;
		
	}
	
	
	
	
	/* SNMP TRAPS */
	
//	@Deprecated
//	public TrapsTable loadSnmpTraps() {
//		
//		if (snmpTraps != null) {
//			return snmpTraps;
//		}
//
//		try {
//
//			File file = new File(DToolsContext.HOME_DIR + XML_SNMP_TRAPS);
//			if (!file.exists()) {
//				snmpTraps = new TrapsTable();
//				JAXBContext jaxbContext = JAXBContext.newInstance(TrapsTable.class);
//				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//				jaxbMarshaller.marshal(snmpTraps, file);
//			}
//			JAXBContext jaxbContext = JAXBContext.newInstance(TrapsTable.class);
//			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//			snmpTraps = (TrapsTable) jaxbUnmarshaller.unmarshal(file);
//			if (snmpTraps.getTrapsList() == null) {
//				snmpTraps.setTrapsList(new ArrayList<SnmpTrap>());
//			}
//			
//			logger.info("DAO:loadSnmpTraps(): " + file.getAbsolutePath());
//
//		} catch (JAXBException e) {
//			logger.error("DAO:loadSnmpTraps(): JAXBException: ", e);
//		}
//
//		return snmpTraps;
//
//	}

//	@Deprecated
//	public void saveSnmpTraps2() {
//
//		try {
//
//			File file = new File(DToolsContext.HOME_DIR + XML_SNMP_TRAPS);
//			JAXBContext jaxbContext = JAXBContext.newInstance(TrapsTable.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			jaxbMarshaller.marshal(snmpTraps, file);
//			
//			logger.info("DAO:saveSnmpTraps(): " + file.getAbsolutePath());
//
//		} catch (JAXBException e) {
//			logger.error("DAO:saveSnmpTraps(): JAXBException: ", e);
//		}
//
//	}

//	public void addSnmpTrap(SnmpTrap t) {
//
//		snmpTraps.addTrap(t);
//		saveSnmpTraps();
//
//	}

//	public void deleteSnmpTrap(SnmpTrap t) {
//
//		snmpTraps.deleteTrap(t);
////		saveSnmpTraps();
//
//	}
	
	
	
	
	

	/* SSH RESPONSE */
	

	public void saveSshResponse(String filename, SshResponse sshResponse) {

		try {
			String fn = DToolsContext.HOME_DIR + XML_SAVE_SSH_RESPONSE.replace("$FILENAME$", filename);
			File file = new File(fn);
			JAXBContext jaxbContext = JAXBContext
					.newInstance(SshResponse.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(sshResponse, file);
			
			String fn2 = DToolsContext.HOME_DIR + TXT_SAVE_SSH_RESPONSE.replace("$FILENAME$", filename);
			writePlainTextFile(fn2, sshResponse.getResponse());
			
			logger.info("DAO:saveSshResponse(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveSshResponse(): JAXBException: ", e);
		}

	}

	public SshResponse loadSshResponse(String filename) {

		String resp = "";

		try {
			String fn = DToolsContext.HOME_DIR + TXT_SAVE_SSH_RESPONSE.replace("$FILENAME$", filename);
			File txtFile = new File(fn);
			FileReader fr = new FileReader(txtFile);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				resp += line + "\n";
			}
			br.close();
			logger.info("DAO:loadSshResponse(): " + txtFile.getAbsolutePath());
		} catch (Exception e) {
			logger.error("DAO:loadSshResponse(): Exception: ", e);
		}
		
		SshResponse sshResponse = new SshResponse();
		
		try {
			String fn = DToolsContext.HOME_DIR + XML_SAVE_SSH_RESPONSE.replace("$FILENAME$", filename);
			File file = new File(fn);
			JAXBContext jaxbContext = JAXBContext.newInstance(SshResponse.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			sshResponse = (SshResponse) jaxbUnmarshaller.unmarshal(file);
			
			logger.info("DAO:loadSshResponse(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadSshResponse(): JAXBException: ", e);
		}
		
		sshResponse.setResponse(resp);
		
		return sshResponse;

	}
	
	public List<SshResponse> loadAllSshResponses() {
		
		List<String> files = loadHistoryFilenames();
		
		List<SshResponse> sshResponses = new ArrayList<SshResponse>();
		
		for (int i = 0; i < files.size(); i++) {
			SshResponse r = loadSshResponse(files.get(i));
			sshResponses.add(r);
		}
		
		return sshResponses;
	}
	
	private List<String> loadHistoryFilenames() {
		
		String fn = DToolsContext.HOME_DIR + DIR_SAVE_SSH_RESPONSE;
		File dir = new File(fn);
		File[] files = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getAbsolutePath().endsWith(".xml");
			}
		});
		
		List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName().substring(0, files[i].getName().length() - 4);
			list.add(filename);
		}
		
		return list;
		
	}
	
	/**
	 * Delete SSH response (xml and txt)
	 * @param resp
	 */
	public void deleteSshResponse(SshResponse resp) {
		String txtFile = DToolsContext.HOME_DIR + TXT_SAVE_SSH_RESPONSE.replace("$FILENAME$", resp.getFilename());
		String xmlFile = DToolsContext.HOME_DIR + XML_SAVE_SSH_RESPONSE.replace("$FILENAME$", resp.getFilename());
		File txt = new File(txtFile);
		File xml = new File(xmlFile);
		txt.delete();
		xml.delete();
	}
	
	
	
	
	
	
	/* GENERIC METHODS */
	
	
	/**
	 * Write data to txt file.
	 * @param absoluteFilepath
	 * @param data
	 */
	public void writePlainTextFile(String absoluteFilepath, String data) {
		
		try {
			File txtFile = new File(absoluteFilepath);
			FileWriter fw = new FileWriter(txtFile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.close();
			logger.info("DAO:writePlainTextFile(): " + txtFile.getAbsolutePath());
		} catch (IOException e) {
			logger.error("DAO:writePlainTextFile(): IOException: ", e);
		}
		
	}
	

}

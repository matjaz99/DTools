package si.matjazcerkvenik.dtools.xml;

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
import si.matjazcerkvenik.simplelogger.SimpleLogger;

public class DAO {

	private static DAO instance;
	private SimpleLogger logger;

	private Servers servers;
	private SshClients sshClients;
	private FtpClients ftpClients;
	private FtpTransfers ftpTransfers;
	private Commands commands;
	private Notes notes;
	
	private final String XML_SERVERS = "/config/servers.xml";
	private final String XML_SSH_CLIENTS = "/config/sshClients.xml";
	private final String XML_SSH_COMMANDS = "/config/commands.xml";
	private final String XML_FTP_CLIENTS = "/config/ftpClients.xml";
	private final String XML_FTP_TRANSFERS = "/config/ftpTransfers.xml";
	private final String XML_NOTES = "/config/notes.xml";

	private DAO() {
		// singleton
		logger = DToolsContext.getInstance().getLogger();
	}

	public static DAO getInstance() {
		if (instance == null) {
			instance = new DAO();
		}
		return instance;
	}

	/* SERVERS */

	public Servers loadServers() {

		if (servers != null) {
			return servers;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SERVERS);
			if (!file.exists()) {
				servers = new Servers();
				JAXBContext jaxbContext = JAXBContext
						.newInstance(Servers.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.marshal(servers, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(Servers.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			servers = (Servers) jaxbUnmarshaller.unmarshal(file);
			if (servers.getServerList() == null) {
				servers.setServerList(new ArrayList<Server>());
			}
			
			logger.info("DAO:loadServers(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:loadServers(): JAXBException: ", e);
		}

		return servers;

	}

	public void saveServers() {

		try {

			File file = new File(DToolsContext.HOME_DIR + XML_SERVERS);
			JAXBContext jaxbContext = JAXBContext.newInstance(Servers.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(servers, file);
			
			logger.info("DAO:saveServers(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveServers(): JAXBException: ", e);
		}

	}

	public void addServer(Server server) {

		servers.addServer(server);
		saveServers();

	}

	public void deleteServer(Server server) {

		servers.deleteServer(server);
		saveServers();

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
	
	

	/* COMMANDS */

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

	/* SSH RESPONSE */

	public void saveSshResponse(String filename, SshResponse sshResponse) {

		try {

			File file = new File(DToolsContext.HOME_DIR + "/temp/" + filename
					+ ".xml");
			JAXBContext jaxbContext = JAXBContext
					.newInstance(SshResponse.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(sshResponse, file);
			
			logger.info("DAO:saveSshResponse(): " + file.getAbsolutePath());

		} catch (JAXBException e) {
			logger.error("DAO:saveSshResponse(): JAXBException: ", e);
		}

		try {
			File txtFile = new File(DToolsContext.HOME_DIR
					+ "/temp/" + filename + ".txt");
			FileWriter fw = new FileWriter(txtFile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sshResponse.getResponse());
			bw.close();
			logger.info("DAO:saveSshResponse(): " + txtFile.getAbsolutePath());
		} catch (IOException e) {
			logger.error("DAO:saveSshResponse(): IOException: ", e);
		}

	}

	public SshResponse loadSshResponse(String filename) {

		String resp = "";

		try {
			File txtFile = new File(DToolsContext.HOME_DIR
					+ "/temp/" + filename + ".txt");
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

			File file = new File(DToolsContext.HOME_DIR + "/temp/" + filename + ".xml");
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
		
		File dir = new File(DToolsContext.HOME_DIR + "/temp");
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

}

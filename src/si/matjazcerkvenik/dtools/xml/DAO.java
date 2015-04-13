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

public class DAO {

	private static DAO instance;

	private Servers servers;
	private Clients clients;
	private Commands commands;
	private Notes notes;

	private DAO() {
		// singleton
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

			File file = new File(DToolsContext.HOME_DIR + "/data/servers.xml");
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

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return servers;

	}

	public void saveServers() {

		try {

			File file = new File(DToolsContext.HOME_DIR + "/data/servers.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Servers.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(servers, file);

		} catch (JAXBException e) {
			e.printStackTrace();
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

	/* CLIENTS */

	public Clients loadClients() {

		if (clients != null) {
			return clients;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + "/data/clients.xml");
			if (!file.exists()) {
				clients = new Clients();
				JAXBContext jaxbContext = JAXBContext
						.newInstance(Clients.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
				jaxbMarshaller.marshal(clients, file);
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(Clients.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			clients = (Clients) jaxbUnmarshaller.unmarshal(file);
			if (clients.getSshClientList() == null) {
				clients.setSshClientList(new ArrayList<SshClient>());
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return clients;

	}

	public void saveClients() {

		try {

			File file = new File(DToolsContext.HOME_DIR + "/data/clients.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Clients.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(clients, file);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public void addSshClient(SshClient client) {

		clients.addSshClientAction(client);
		saveClients();

	}

	public void deleteSshClient(SshClient client) {

		clients.deleteSshClient(client);
		saveClients();

	}

	/* COMMANDS */

	public Commands loadCommands() {

		if (commands != null) {
			return commands;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + "/data/commands.xml");
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

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return commands;

	}

	public void saveCommands() {

		try {

			File file = new File(DToolsContext.HOME_DIR + "/data/commands.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Commands.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(commands, file);

		} catch (JAXBException e) {
			e.printStackTrace();
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

	/* NOTES */

	public Notes loadNotes() {

		if (notes != null) {
			return notes;
		}

		try {

			File file = new File(DToolsContext.HOME_DIR + "/data/notes.xml");
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

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return notes;

	}

	public void saveNotes() {

		try {

			File file = new File(DToolsContext.HOME_DIR + "/data/notes.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Notes.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(notes, file);

		} catch (JAXBException e) {
			e.printStackTrace();
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

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		try {
			FileWriter fw = new FileWriter(DToolsContext.HOME_DIR + "/temp/"
					+ filename + ".txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sshResponse.getResponse());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public SshResponse loadSshResponse(String filename) {

		String resp = "";

		try {
			FileReader fr = new FileReader(DToolsContext.HOME_DIR
					+ "/temp/" + filename + ".txt");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				resp += line + "\n";
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SshResponse sshResponse = new SshResponse();
		
		try {

			File file = new File(DToolsContext.HOME_DIR + "/temp/" + filename + ".xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(SshResponse.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			sshResponse = (SshResponse) jaxbUnmarshaller.unmarshal(file);

		} catch (JAXBException e) {
			e.printStackTrace();
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

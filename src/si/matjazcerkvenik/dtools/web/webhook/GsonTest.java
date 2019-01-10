package si.matjazcerkvenik.dtools.web.webhook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTest {
	
	public static String json = "";
	
	List<World> worlds;
	AmAlert alert;

	public static void main(String[] args) {
		
		// https://stackoverflow.com/questions/18421674/using-gson-to-parse-a-json-array
		// https://github.com/google/gson/blob/master/UserGuide.md
		// https://stackoverflow.com/questions/8371274/how-to-parse-json-array-with-gson/8371455
		
		json = readFile("test1.json");
		System.out.println(json);
		
		GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Collection.class, new CollectionDeserializer());
        Gson gson = builder.create();
        GsonTest decoded = gson.fromJson(json, GsonTest.class);
        System.out.println(decoded.worlds.toString());
		
        
        json = readFile("test2.json");
		System.out.println(json);
		
		GsonBuilder builder2 = new GsonBuilder();
        builder2.registerTypeAdapter(Collection.class, new CollectionDeserializer());
        Gson gson2 = builder2.create();
        GsonTest decoded2 = gson2.fromJson(json, GsonTest.class);
        System.out.println(decoded2.alert.toString());
		
//		Gson gson = new Gson();
//		AmAlert a = gson.fromJson(json, AmAlert.class);
//		System.out.println(a.toString());
		
		json = readFile("test3.json");
		System.out.println(json);
		
	}

	public static String readFile(String filename) {
		String s = "";
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				s += line;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	
}

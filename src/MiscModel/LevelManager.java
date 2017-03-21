package MiscModel;
import java.util.HashMap;
import java.util.Map;

public class LevelManager {
	static Map<String,Level> levelMap = new HashMap<String,Level>();
	
	public LevelManager(){
		
	}
	
	//load all maps into map list
	public void initializeLevels(){
		Level testLevel1 = new Level("Test1","test1","background2.jpg", 200, 100);
		levelMap.put("Test1", testLevel1);
		Level testLevel2 = new Level("Test2","test2","background3.jpg", 700, 400);
		levelMap.put("Test2", testLevel2);
	}
	
	//find map from map list with specified map name
	public static Level getLevel(String mapName){
		return levelMap.get(mapName);
	}
	
}

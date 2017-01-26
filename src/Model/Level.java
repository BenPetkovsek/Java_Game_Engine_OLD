package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Level {

	String fileName;
	String mapName;
	String background;
	ArrayList<GameObject> levelObjects = new ArrayList<GameObject>();
	
	public Level(String initMapName, String initFileName, String initBackgroundFile){
		this.fileName = initFileName;
		this.mapName = initMapName;
		this.background = initBackgroundFile;
		loadGameObjects();
		
	}
	
	public void loadGameObjects(){
		System.out.println("Generating game object list of " + mapName + " from file named " + fileName);
		String readFile = "Levels/"+ fileName;
		String line = null;
		
		try{
			FileReader fileReader = new FileReader(readFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null){
				System.out.println(line);
				
				String[] mapObjects = line.split("\\|");
				
				for(int i = 0; i < mapObjects.length; i++){
					System.out.println("Load Game Object: " + mapObjects[i]);
					
					String[] objectInfo = mapObjects[i].split(",");
					
					
					if(objectInfo[0].equals("rock")){
						levelObjects.add(new Rock(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2])));
					}
					else if(objectInfo[0].equals("enemy")){
						levelObjects.add(new Enemy(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2])));
					}
					else if(objectInfo[0].equals("loadTrigger")){
						
						levelObjects.add(new LoadTrigger(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2]), objectInfo[3]));
					}
				}
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex){
			ex.printStackTrace();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	

	
	
	
	
	
}

package MiscModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import EnemyModel.Enemy;
import GameObjectModel.Animatable;
import GameObjectModel.Collidable;
import GameObjectModel.LoadTrigger;
import GameObjectModel.StaticType;
import GameObjectModel.TerrainObject;
import View.GameRender;

public class Level {
	int spawnX;
	int spawnY;
	boolean smallMapX;
	boolean smallMapY;
	String fileName;
	String mapName;
	String background;
	int windowWidth = MainLoop.getWindowWidth();
	int windowHeight = MainLoop.getWindowHeight();
	int bgHeight;
	int bgWidth;
	ArrayList<Animatable> levelObjects = new ArrayList<Animatable>();
	ArrayList<Collidable> collidableObjects = new ArrayList<Collidable>();
	
	public Level(String initMapName, String initFileName, String initBackgroundFile, int x, int y){
		this.fileName = initFileName;
		this.mapName = initMapName;
		this.background = initBackgroundFile;
		this.spawnX = x;
		this.spawnY = y;
		this.bgHeight = GameRender.getHeightOf(background);
		this.bgWidth = GameRender.getWidthOf(background);
		if (bgWidth - windowWidth <=0){ this.smallMapX = true;}
		else{ this.smallMapX = false;}
		if (bgHeight - windowHeight <=0){ this.smallMapY = true;}
		else{ this.smallMapY = false;}
		loadGameObjects();
		
	}
	
	public void loadGameObjects(){
		//System.out.println("Generating game object list of " + mapName + " from file named " + fileName);
		String readFile = "Levels/"+ fileName;
		String line = null;
		
		try{
			FileReader fileReader = new FileReader(readFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null){
				//System.out.println(line);
				
				String[] mapObjects = line.split("\\|");
				
				for(int i = 0; i < mapObjects.length; i++){
					//System.out.println("Load Game Object: " + mapObjects[i]);
					
					String[] objectInfo = mapObjects[i].split(",");
					
					
					if(objectInfo[0].equals("rock")){
						levelObjects.add(new TerrainObject(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2]),StaticType.ROCK));
						collidableObjects.add(new TerrainObject(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2]), StaticType.ROCK));
					}
					else if(objectInfo[0].equals("enemy")){
						levelObjects.add(new Enemy(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2])));
						collidableObjects.add(new Enemy(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2])));
					}
					else if(objectInfo[0].equals("loadTrigger")){
						levelObjects.add(new LoadTrigger(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2]), objectInfo[3]));
						collidableObjects.add(new LoadTrigger(Integer.parseInt(objectInfo[1]), Integer.parseInt(objectInfo[2]), objectInfo[3]));
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
	
	public int getSpawnX(){
		return spawnX;
	}
	public int getSpawnY(){
		return spawnY;
	}
	public boolean getSmallMapX(){
		return smallMapX;
	}
	public boolean getSmallMapY(){
		return smallMapY;
	}
	public void mapDebug(){
		System.out.println("smallMapX - " + smallMapX);
		System.out.println("smallMapY - " + smallMapY);
		System.out.println("windowWidth - " + windowWidth);
		System.out.println("windowHeight - " + windowHeight);
		System.out.println("bgWidth - " + bgWidth);
		System.out.println("bgHeight - " + bgHeight);
	}
	

	
	
	
	
	
}
package Terrain;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileHandler {
    private static final String saveFilesDirPath = "./saveFiles/";

    static HashMap<Integer, Chunk> getChunksFromSeed(double seed) {
        return FileHandler.decodeJson(FileHandler.readFromFile(FileHandler.fileNameFromSeed(seed)));
    }

    static void saveChunksWithSeed(double seed, HashMap<Integer, Chunk> chunks) {
        FileHandler.writeToFile(FileHandler.fileNameFromSeed(seed), FileHandler.encodeJson(chunks));
    }

    static void writeToFile(String fileName, JSONObject obj) {
        try {
			FileWriter file = new FileWriter(FileHandler.filePathFromSeed(fileName));
			file.write(obj.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    static JSONObject readFromFile(String fileName) {
        JSONParser jsonParser = new JSONParser();
        
        try { 
            FileReader reader = new FileReader(FileHandler.filePathFromSeed(fileName));
            return (JSONObject)jsonParser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject encodeJson(HashMap<Integer, Chunk> chunks) {
        JSONObject chunkObj = new JSONObject();

        for (Integer key : chunks.keySet()) {
            JSONArray tiles = new JSONArray();
            Chunk chunk = chunks.get(key);
            
            for (byte y = 0; y < Chunk.CHK_HGT; y++) {
                JSONArray tilesX = new JSONArray();
                for (byte x = 0; x < Chunk.CHUNK_WIDTH; x++) {
                    tilesX.add(chunk.getTile(x, y));
                }
                tiles.add(tilesX);
            }
            chunkObj.put(chunk.offset, tiles);
        }
        return chunkObj;
    }

    static HashMap<Integer, Chunk> decodeJson(JSONObject obj) {
        HashMap<Integer, Chunk> chunks = new HashMap<Integer, Chunk>();
        for (Object key : obj.keySet()) {
            Integer offset = Integer.valueOf((String)key);
            chunks.put(offset, new Chunk(offset, (JSONArray)obj.get(key)));
        }
        return chunks;
    }

    static boolean fileExists(double seed) {
        File file = new File(FileHandler.filePathFromSeed(seed));
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        return true;
    }

    static String fileNameFromSeed(double seed) {
        return Double.toString(seed).substring(2);
    }

    static String filePathFromSeed(double seed) {
        return FileHandler.filePathFromSeed(FileHandler.fileNameFromSeed(seed));
    }
    static String filePathFromSeed(String fileName) {
        return saveFilesDirPath + fileName + ".json";
    }

    public static String[] existingFileNames() {
        File saveFilesDir = new File(saveFilesDirPath);
        File[] seedFiles = saveFilesDir.listFiles();
        String[] fileNames = new String[seedFiles.length];
        for (int i = 0; i < seedFiles.length; i++) {
            fileNames[i] = seedFiles[i].getName();
        }
        return fileNames;
    }

    public static double fileNameToSeed(String fileName) {
        return Double.parseDouble("0." + fileName);
    }

    public static BufferedImage getBufferedImage(String filePath) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            System.err.println(e);
            img = new BufferedImage(0, 0, 0);
        }
        return img;
    }
}
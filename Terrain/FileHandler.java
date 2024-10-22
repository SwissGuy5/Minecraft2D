package Terrain;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileHandler {
    static Chunk[] getChunksFromSeed(double seed) {
        return FileHandler.decodeJson(FileHandler.readFromFile(FileHandler.FileNameFromSeed(seed)));
    }

    static void saveChunksWithSeed(double seed, Chunk[] chunks) {
        FileHandler.writeToFile(FileHandler.FileNameFromSeed(seed), FileHandler.encodeJson(chunks));
    }

    static void writeToFile(String fileName, JSONObject obj) {
        try {
			FileWriter file = new FileWriter("./SaveFiles/" + fileName + ".json");
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
            FileReader reader = new FileReader(FileHandler.FilePathFromSeed(fileName));
            return (JSONObject)jsonParser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    static JSONObject encodeJson(Chunk[] chunks) {
        JSONObject obj = new JSONObject();
        JSONArray chunksArr = new JSONArray();
        
        for (int i = 0; i < 10; i++) {
            JSONObject chunkObj = new JSONObject();
            chunkObj.put("offset", chunks[i].offset);

            JSONArray tiles = new JSONArray();
            for (byte y = 0; y < Chunk.CHUNK_HEIGHT; y++) {
                JSONArray tilesX = new JSONArray();
                for (byte x = 0; x < Chunk.CHUNK_WIDTH; x++) {
                    tilesX.add(chunks[i].getTile(x, y));
                }
                tiles.add(tilesX);
            }
            chunkObj.put("tiles", tiles);
            chunksArr.add(chunkObj);
        }
        obj.put("chunks", chunksArr);
        return obj;
    }

    static Chunk[] decodeJson(JSONObject obj) {
        Chunk[] chunks = new Chunk[100];
        JSONArray chunkArray = (JSONArray)obj.get("chunks");
        for (int i = 0; i < chunkArray.size(); i++) {
            JSONObject chunkObj = (JSONObject)chunkArray.get(i);
            int offset = (int)(long)chunkObj.get("offset");
            chunks[offset] = new Chunk(chunkObj);
        }
        return chunks;
    }

    static boolean fileExists(double seed) {
        File file = new File(FileHandler.FilePathFromSeed(seed));
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        return true;
    }

    static String FileNameFromSeed(double seed) {
        return Double.toString(seed).substring(2);
    }

    static String FilePathFromSeed(double seed) {
        return FileHandler.FilePathFromSeed(FileHandler.FileNameFromSeed(seed));
    }
    static String FilePathFromSeed(String fileName) {
        return "./SaveFiles/" + fileName + ".json";
    }
}
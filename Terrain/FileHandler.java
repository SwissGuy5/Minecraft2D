package Terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The file handler object.
 */
public class FileHandler {
    private static String saveFilesDirPath = "./saveFiles/";

    static HashMap<Integer, Chunk> getChunksFromSeed(double seed) {
        return FileHandler.decodeJson(FileHandler.readFromFile(FileHandler.fileNameFromSeed(seed)));
    }

    static void saveChunksWithSeed(double seed, HashMap<Integer, Chunk> chunks) {
        FileHandler.writeToFile(FileHandler.fileNameFromSeed(seed), FileHandler.encodeJson(chunks));
    }

    /**
     * Writes a JSON object to a file.
     * @param fileName Name of file to write to.
     * @param obj JSON object to write.
     */
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

    /**
     * Reads a JSON object from a file.
     * @param fileName Name of file to read from.
     * @return JSON object read from file.
     */
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

    /**
     * Encodes a hashmap of chunks into a JSON object.
     * @param chunks The hashmap of chunks to encode.
     * @return The JSON object encoded from the hashmap.
     */
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

    /**
     * Decodes a JSON object into a hashmap of chunks.
     * @param obj The JSON object to decode.
     * @return The hashmap of chunks decoded from the JSON object.
     */
    static HashMap<Integer, Chunk> decodeJson(JSONObject obj) {
        HashMap<Integer, Chunk> chunks = new HashMap<Integer, Chunk>();
        for (Object key : obj.keySet()) {
            Integer offset = Integer.valueOf((String) key);
            chunks.put(offset, new Chunk(offset, (JSONArray) obj.get(key)));
        }
        return chunks;
    }

    /**
     * Checks if a file exists.
     * @param seed The seed of the file to check.
     * @return True if the file exists, false otherwise.
     */
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

    /**
     * Returns an array of existing file names.
     * @return An array of existing file names.
     */
    public static String[] existingFileNames() {
        File saveFilesDir = new File(saveFilesDirPath);
        File[] seedFiles = saveFilesDir.listFiles();
        String[] fileNames = new String[seedFiles.length];
        for (int i = 0; i < seedFiles.length; i++) {
            fileNames[i] = seedFiles[i].getName();
        }
        return fileNames;
    }

    /**
     * Converts a file name to a seed.
     * @param fileName The file name to convert.
     * @return The seed converted from the file name.
     */
    public static double fileNameToSeed(String fileName) {
        return Double.parseDouble("0." + fileName);
    }

    /**
     * Returns a buffered image from a file path.
     * @param filePath The file path to get the image from.
     * @return The buffered image from the file path.
     */
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
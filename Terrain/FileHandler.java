package Terrain;
import org.json.simple.*;

public class FileHandler {
    static void EncodeJson(Chunk[] chunks) {
        // System.out.println(chunks.length);
        JSONObject obj = new JSONObject();
        for (int i = 0; i < 10; i++) {
            // System.out.println(chunks[i]);
            obj.put(chunks[i].offset, chunks[i].getTiles());
        }
        System.out.println(obj);
    }

    static void Test() {
        // JSONObject obj = new JSONObject();
        // obj.put("name", "foo");
        // System.out.println(obj);
    }

    static Chunk[] DecodeFile() {

    }
}
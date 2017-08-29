import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ReelSets {


//    private boolean STAGE = false;
    private String urlStage = "https://gs2.stg.m27613.com/casino/game2";
    private String urlProd = "https://gs3.m27613.com/casino/game2";

    private JSONObject getGameInfo(String game) throws IOException {
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.post(Constants.STAGE ? urlStage : urlProd)
                    .header("Content-Type", "application/json")
                    .body("{\n" +
                            "  \"deviceId\": \"web\",\n" +
                            "  \"gameId\": \"" + game + "\",\n" +
                            "  \"name\": \"test\",\n" +
                            "  \"request\": \"init\",\n" +
                            "  \"requestId\": 0,\n" +
                            "  \"startGameToken\": {\n" +
                            "    \"playerCode\": \"sergeusd\",\n" +
                            "    \"gameCode\": \"" + game + "\",\n" +
                            "    \"brandId\": 1,\n" +
                            "    \"currency\": \"KRW\"\n" +
                            "  }\n" +
                            "}")
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return (JSONObject) jsonResponse.getBody().getObject().get("result");
    }

    JSONObject getReels(String game) throws IOException {
        JSONObject slot = (JSONObject) getGameInfo(game).get("slot");
        return (JSONObject) slot.get("sets");
    }

    ArrayList<ArrayList<Integer>> getReelsFromFile(String game, String filename){
        String path = Constants.resources + "/reelSets/";
        String file = path + "/" + game + "/" + filename;
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<Integer> reel0 = new ArrayList<>();
        ArrayList<Integer> reel1 = new ArrayList<>();
        ArrayList<Integer> reel2 = new ArrayList<>();
        ArrayList<Integer> reel3 = new ArrayList<>();
        ArrayList<Integer> reel4 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> reels = new ArrayList<>();
        reels.add(reel0);
        reels.add(reel1);
        reels.add(reel2);
        reels.add(reel3);
        reels.add(reel4);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(cvsSplitBy);
                for (int i = 0; i < row.length; i++){
                    if(!Objects.equals(row[i], "") && !Objects.equals(row[i], " ")){
                        reels.get(i).add(Integer.valueOf(row[i].replaceAll("[^0-9]+", "")));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reels;
    }
}

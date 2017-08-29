import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestReelSets {

    @DataProvider(name = "allGames")
    public Object[][] allGames() {
        return new Object[][]{{"sw_888t"}, {"sw_db"}, {"sw_dhcf"}, {"sw_dj"}, {"sw_rs"}, {"sw_tc"}, {"sw_ycs"}};
    }


    @Test(dataProvider = "allGames")
//    @Parameters("gameName")
    public void testReels(String gameName) throws IOException {
        System.out.println("\nChecking game: " + gameName);
        File pathForAllSets = new File(Constants.resources + "/reelSets/" + gameName);
        File[] sets = pathForAllSets.listFiles();
        List<File> lst = Arrays.asList(sets);

        ReelSets reelSets = new ReelSets();
        for (File file : lst){
            String setName = file.getName();
            System.out.println("Checking reels set: " + setName);
            ArrayList<ArrayList<Integer>> reelsFromFile = reelSets.getReelsFromFile(gameName, setName);
            JSONObject reelsFromServer = (JSONObject) reelSets.getReels(gameName).get(setName.replace(".csv", ""));
            JSONArray reelsFromServerJSONArray = reelsFromServer.getJSONArray("reels");

            for (int i = 0; i < reelsFromServerJSONArray.length(); i++) {
                JSONArray reel = reelsFromServerJSONArray.getJSONArray(i);
                ArrayList<Integer> reelFromFile = reelsFromFile.get(i);
                ArrayList<Integer> reelFromServer = new ArrayList<>();
                for (int j = 0; j < reel.length(); j++){
                    reelFromServer.add((Integer) reel.get(j));
                }
                Assert.assertEquals(reelFromServer, reelFromFile);
                reelFromServer.clear();
            }
        }
    }
}

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.text.MessageFormat;


class Methods {

    String getAdminToken() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("http://104.199.171.21/v1/login")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("secretKey", "aaa11200-19f1-48c1-a78c-3a3d56095f38")
                .field("username", "SUPERADMIN")
                .field("password", "SUPERadmin777")
                .asJson();
        return response.getBody().getObject().get("accessToken").toString();
    }

    void createPlayer(String custId, String currency, String accessToken) throws UnirestException {

        Unirest.post("http://104.199.163.39/v1/merchant/swftest2_1/customer")
                .header("x-access-token", accessToken)
                .header("Content-Type", "application/json")
                .body("{\"cust_id\": \"" + custId + "\"," +
                        "\"cust_login\": \"" + custId + "\"," +
                        "\"currency_code\": \"" + currency + "\"," +
                        "\"language\": \"en\"," +
                        "\"country\": \"US\"," +
                        "\"first_name\": \"\"," +
                        "\"last_name\": \"\"," +
                        "\"middle_name\": \"\"," +
                        "\"email\": \"\"," +
                        "\"phone\": \"\"," +
                        "\"street_address\": \"\"," +
                        "\"city\": \"\"," +
                        "\"postcode\": \"\"," +
                        "\"state\": \"\"," +
                        "\"secret_question\": \"\"," +
                        "\"secret_answer\": \"\"," +
                        "\"salutation\": \"\"," +
                        "\"sex\": \"\"," +
                        "\"referral_info\": \"\"," +
                        "\"rakeback\": false," +
                        "\"date_of_birth\": \"12/12/1990\"" +
                        "}").asJson();

    }

    void addBalance(String custId, String currency, String balance, String accessToken) throws UnirestException {
        Unirest.post(MessageFormat.format(
                "http://104.199.163.39/v1/merchant/swftest2_1/customer/{0}/balance/{1}", custId, balance))
                .header("x-access-token", accessToken)
                .header("Content-Type", "application/json")
                .body("{\"cust_id\": \"" + custId + "\"," +
                "\"cust_login\": \"" + custId + "\"," +
                "\"currency_code\": \"" + currency + "\"," +
                "\"language\": \"en\"," +
                "\"country\": \"US\"," +
                "\"first_name\": \"\"," +
                "\"last_name\": \"\"," +
                "\"middle_name\": \"\"," +
                "\"email\": \"\"," +
                "\"phone\": \"\"," +
                "\"street_address\": \"\"," +
                "\"city\": \"\"," +
                "\"postcode\": \"\"," +
                "\"state\": \"\"," +
                "\"secret_question\": \"\"," +
                "\"secret_answer\": \"\"," +
                "\"salutation\": \"\"," +
                "\"sex\": \"\"," +
                "\"referral_info\": \"\"," +
                "\"rakeback\": false," +
                "\"date_of_birth\": \"12/12/1990\"" +
                "}").asJson();
    }

    String getTicket(String custId) throws UnirestException {
        HttpResponse<String> response = Unirest.get(MessageFormat.format(
                "http://104.199.163.39/v1/merchant/swftest2_1/customer/{0}/ticket", custId))
                .header("Content-Type", "application/json")
                .header("X-ACCESS-TOKEN", "AAAAA")
                .asString();
        return response.getBody();
    }

    String getGameToken(String gameCode, String accessToken, String ticket) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("http://104.199.171.21/v1/merchants/game/url")
                .header("x-access-token", accessToken)
                .field("merchantType", "ipm")
                .field("merchantCode", "swftest2_1")
                .field("gameCode", gameCode)
                .field("ticket", ticket)
                .asJson();
        return response.getBody().getObject().get("url").toString();
    }
}

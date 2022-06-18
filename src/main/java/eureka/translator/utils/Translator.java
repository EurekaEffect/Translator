package eureka.translator.utils;

import com.alibaba.fastjson.JSONArray;
import com.darkprograms.speech.synthesiser.SynthesiserV2;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translator {
    private static final String PATH = "https://translate.googleapis.com/translate_a/single";
    private static final String CLIENT = "gtx";

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final Map<String, String> LANGUAGE_MAP = new HashMap();
    public final List<String> language = List.of("English", "Polish", "Spanish", "Ukrainian", "Russian");

    private final SynthesiserV2 synthesizer = new SynthesiserV2("AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw");

    public void init() {
        LANGUAGE_MAP.put("en", "English");
        LANGUAGE_MAP.put("pl", "Polish");
        LANGUAGE_MAP.put("es", "Spanish");
        LANGUAGE_MAP.put("uk", "Ukrainian");
        LANGUAGE_MAP.put("ru", "Russian");
    }

    public boolean isSupport(String language) {
        return null != LANGUAGE_MAP.get(language);
    }

    public String translateText(String text, String sourceLang, String targetLang) throws Exception {
        StringBuilder retStr = new StringBuilder();
        if (!(isSupport(sourceLang) || isSupport(targetLang))) {
            throw new Exception("unsupported language type");
        }

        List<NameValuePair> nvps = new ArrayList();
        nvps.add(new BasicNameValuePair("client", CLIENT));
        nvps.add(new BasicNameValuePair("sl", sourceLang));
        nvps.add(new BasicNameValuePair("tl", targetLang));
        nvps.add(new BasicNameValuePair("dt", "t"));
        nvps.add(new BasicNameValuePair("q", text));

        String resp = postHttp(nvps);
        if (null == resp) {
            throw new Exception("network exception");
        }

        JSONArray jsonObject = JSONArray.parseArray(resp);
        for (Object o : jsonObject.getJSONArray(0)) {
            JSONArray a = (JSONArray) o;
            retStr.append(a.getString(0));
        }

        return retStr.toString();
    }

    private String postHttp(List<NameValuePair> nvps) {
        String responseStr = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Translator.PATH);
        httpPost.setHeader("User-Agent", USER_AGENT);
        CloseableHttpResponse response2 = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
            response2 = httpclient.execute(httpPost);
            HttpEntity entity2 = response2.getEntity();
            responseStr = EntityUtils.toString(entity2);
            EntityUtils.consume(entity2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != response2) {
                try {
                    response2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpclient) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseStr;
    }

    public void speak(String text) {
        Thread thread = new Thread(() -> {
            try {
                synthesizer.setSpeed(0.75);

                AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(text));
                player.play();
            } catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(false);
        thread.start();
    }
}

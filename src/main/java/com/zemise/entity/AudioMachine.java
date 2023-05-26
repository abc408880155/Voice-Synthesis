package com.zemise.entity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.zemise.exception.AudException;
import com.zemise.exception.Error;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import static com.zemise.entity.Constant.DEFAULT_HOST_URL;


/**
 * @Author zemise_
 * @Date 2023/5/24
 * @Description
 */
@Slf4j
public class AudioMachine {
    private final AudioOption options;
    private String message;

    private boolean wsCloseFlag = false;

    public AudioMachine(AudioOption options) {
        this.options = options;
    }

    public void generateAudio(String message, String outPath) {
        String authUrl = getAuthUrl(options.getAppKey(), options.getApiSecret())
                .replace("https://", "wss://");

        websocketWork(message, authUrl, outPath);
    }

    // Websocket方法
    private void websocketWork(String message, String uri, String outPath) {
        this.message = message;

        try {
            OutputStream outputStream = new FileOutputStream(outPath);
            URI wscUri = new URI(uri);

            WebSocketClient webSocketClient = new WebSocketClient(wscUri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                    log.info("wss建立连接成功！");
                    send(buildRequestJson());
                }

                @Override
                public void onMessage(String text) {
                    JsonParse myJsonParse = options.getGson().fromJson(text, JsonParse.class);
                    if (myJsonParse.code != 0) {
                        log.error("发生错误，错误码为：{}，可于此链接查询错误码https://www.xfyun.cn/doc/tts/online_tts/API.html#错误码", myJsonParse.code);
                        log.error("本次请求的sid为：" + myJsonParse.sid);
                    }
                    if (myJsonParse.data != null) {
                        try {
                            byte[] textBase64Decode = Base64.getDecoder().decode(myJsonParse.data.audio);
                            outputStream.write(textBase64Decode);
                            outputStream.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (myJsonParse.data.status == 2) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            log.info("本次请求的sid为：{}", myJsonParse.sid);
                            log.info("语音合成成功，保存路径为：{}", outPath);

                            // 可以关闭连接，释放资源
                            wsCloseFlag = true;
                            close();
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("wws链接已关闭，本次请求完成");
                }

                @Override
                public void onError(Exception e) {
                    log.error("发生错误：{}", e.getMessage());
                }
            };
            // 建立连接
            webSocketClient.connect();
            while (!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    // 鉴权方法
    private static String getAuthUrl(String apiKey, String apiSecret) {
        try {
            URL url = new URL(DEFAULT_HOST_URL);
            // 时间
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());
            // 拼接
            String preStr = "host: " + url.getHost() + "\n" +
                    "date: " + date + "\n" +
                    "GET " + url.getPath() + " HTTP/1.1";
            // SHA256加密
            Mac mac = Mac.getInstance("hmacsha256");

            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
            mac.init(spec);
            byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
            // Base64加密
            String sha = Base64.getEncoder().encodeToString(hexDigits);
            // 拼接
            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
            // 拼接地址
            HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                    addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                    addQueryParameter("date", date).//
                    addQueryParameter("host", url.getHost()).//
                    build();

            return httpUrl.toString();
        } catch (MalformedURLException e) {
            log.error("GetAuthUrl failed: {}, please try again", e.getMessage());
            throw new AudException(Error.INCORRECT_AUTHORIZATION.getCode(), e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("Encryption failed: {}, please try again", e.getMessage());
            throw new AudException(Error.INCORRECT_SHA256_ENCRYPTION.getCode(), e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("Incorrect appid appKey or apiSecret: {}, please try again", e.getMessage());
            throw new AudException(Error.INCORRECT_API_KEY_PROVIDED.getCode(), e.getMessage());
        }

    }


    //返回的json结果拆解
    class JsonParse {
        int code;
        String sid;
        Data data;
    }

    class Data {
        int status;
        String audio;
    }


    // 构建请求参数json串
    private String buildRequestJson() {
        return "{\n" +
                "  \"common\": {\n" +
                "    \"app_id\": \"" + options.getAppid() + "\"\n" +
                "  },\n" +
                "  \"business\": {\n" +
                "    \"aue\": \"lame\",\n" +
                "    \"tte\": \"" + options.getTTE() + "\",\n" +
                "    \"ent\": \"intp65\",\n" +
                "    \"vcn\": \"" + options.getSpeaker().toString() + "\",\n" +
                "    \"pitch\": " + options.getPitch() + ",\n" +
                "    \"speed\": " + options.getSpeed() + "\n" +
                "  },\n" +
                "  \"data\": {\n" +
                "    \"status\": 2,\n" +
                "    \"text\": \"" + Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8)) + "\"\n" +
                "  }\n" +
                "}";
    }
}

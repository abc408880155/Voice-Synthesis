import com.zemise.Voice;

/**
 * @Author zemise_
 * @Date 2023/5/24
 * @Description
 */

public class testAu {

    public static void main(String[] args) throws Exception {
        Voice voice = new Voice(
                "appid",
                "appKey",
                "apiSecret");


        voice.generate("测试", "/Users/output.mp3");
    }
}

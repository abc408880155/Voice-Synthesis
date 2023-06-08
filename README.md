# Voice-Synthesis Java

A Java client for the [XunFei API](https://www.xfyun.cn/doc/tts/online_tts/API.html).



## Change Log

### 1.0.0

- Initial release
- Use official API

## Download

Download the latest JAR or grab via [Maven](https://central.sonatype.dev/artifact/com.lilittlecat/chatgpt/1.0.0):

```
<dependency>
  <groupId>io.github.zemise</groupId>
  <artifactId>Voice-Synthesis</artifactId>
  <version>1.0.0</version>
</dependency>
```



or Gradle:

```
implementation 'io.github.zemise:Voice-Synthesis:1.0.0'
```



## Usage

```
import io.github.zemise.Voice;
public class Main {
   public static void main(String[] args) {
   	 public static void main(String[] args) {
        Voice voice = new Voice("aapid", "appKey", "apiSecret");
        voice.generate("合成语音测试消息","/path/output.mp3");
    }
}
```



More examples can be found in the test.

## Happy coding

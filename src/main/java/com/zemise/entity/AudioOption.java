package com.zemise.entity;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * @Author zemise_
 * @Date 2023/5/24
 * @Description 基本调节参数
 */
@Builder
@Getter
public class AudioOption {
    @NonNull
    private String appid;
    @NonNull
    private String appKey;
    @NonNull
    private String apiSecret;

    // 合成文本编码格式
    @Builder.Default
    private String TTE = "UTF8";// 小语种必须使用UNICODE编码作为值
    // 音频编码格式
    @Builder.Default
    private Format format = Format.MP3;
    // 发音人参数。到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值，若试用未添加的发音人会报错11200
    @Builder.Default
    private Speaker speaker = Speaker.XIAOYAN;
    @Builder.Default
    // 语速
    private int speed = 50;
    @Builder.Default
    // 音高
    private int pitch = 50;
    @Builder.Default
    private Gson gson = new Gson();
}

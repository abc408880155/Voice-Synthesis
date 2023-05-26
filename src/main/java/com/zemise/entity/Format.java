package com.zemise.entity;

/**
 * @Author Zemise_
 * @Date 2023/5/24
 * @Description 语音格式
 */
public enum Format {
    //raw：未压缩的pcm
    MP3("lame"),
    PCM("raw"),
    //标准开源speex（for speex_wideband，即16k）数字代表指定压缩等级（默认等级为8）
    SPEEX_ORG_WB("speex-org-wb"),
    //标准开源speex（for speex_narrowband，即8k）数字代表指定压缩等级（默认等级为8）
    SPEEX_ORG_NB("speex-org-nb"),
    //压缩格式，压缩等级1~10，默认为7（8k讯飞定制speex）
    SPEEX("speex"),
    //压缩格式，压缩等级1~10，默认为7（16k讯飞定制speex）
    SPEEX_WB("speex-wb");

    private final String name;

    Format(String name) {
        this.name = name;
    }
}

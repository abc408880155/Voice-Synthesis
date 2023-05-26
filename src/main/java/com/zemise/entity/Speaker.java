package com.zemise.entity;

/**
 * @Author Zemise_
 * @Date 2023/5/24
 * @Description
 */
public enum Speaker {
    XIAOYAN("xiaoyan"),
    AISJIUXU("aisjiuxu"),
    AISXPING("aisxping"),
    AISJINGER("aisjinger"),
    AISBABYXU("aisbabyxu"),
    LINGXIAOLU("x4_lingxiaolu_em_v2"),
    LAURA("x2_engam_laura"),
    X2_XIAOQIAN("x2_xiaoqian"),
    ZHONGCUN("x2_JaJp_ZhongCun"),
    X_XIAOLING("x_xiaoling");


    private final String name;

    Speaker(String name) {
        this.name = name;
    }
}

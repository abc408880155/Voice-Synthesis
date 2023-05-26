package com.zemise;


import com.zemise.entity.AudioMachine;
import com.zemise.entity.AudioOption;
import com.zemise.exception.AudException;
import com.zemise.exception.Error;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * @Author zemise_
 * @Date 2023/5/26
 * @Description
 */


@Slf4j
public class Voice {
    private final String appid;
    private final String appKey;
    private final String apiSecret;
    private final AudioOption option;

    public Voice(String appid, String appKey, String apiSecret) {
        this.appid = appid;
        this.appKey = appKey;
        this.apiSecret = apiSecret;
        this.option = AudioOption.builder()
                .appid(appid)
                .appKey(appKey)
                .apiSecret(apiSecret)
                .build();
    }

    public void generate(String message, String outPath) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        generate(message, outPath, option);
    }

    public void generate(String message, String outPath, AudioOption option) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        AudioMachine audioMachine = new AudioMachine(option);
        //try {
            audioMachine.generateAudio(message, outPath);
        //} catch (InvalidKeyException e) {
        //    log.error("Incorrect appid appKey or apiSecret: {}, please try again", e.getMessage());
        //    throw new AudException(Error.INCORRECT_API_KEY_PROVIDED.getCode(), e.getMessage());
        //}

    }

}

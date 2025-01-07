package com.soflyit.gateway.service;

import com.soflyit.common.core.exception.CaptchaException;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.io.IOException;

/**
 * 验证码处理
 *
 * @author soflyit
 */
public interface ValidateCodeService {

    AjaxResult createCaptcha() throws IOException, CaptchaException;


    void checkCaptcha(String key, String value) throws CaptchaException;
}

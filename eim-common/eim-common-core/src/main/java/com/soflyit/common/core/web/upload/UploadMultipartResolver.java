package com.soflyit.common.core.web.upload;

import com.soflyit.common.core.constant.TokenConstants;
import com.soflyit.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件上传解析<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-24 11:02
 */
@Slf4j
public class UploadMultipartResolver extends CommonsMultipartResolver {


    private UploadProgressListener uploadProgressListener;

    @Override
    @SuppressWarnings("unchecked")
    public MultipartParsingResult parseRequest(HttpServletRequest request)
            throws MultipartException {

        if (uploadProgressListener == null) {
            return super.parseRequest(request);
        }
        String token = getToken(request);

        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        ProgressData data = new ProgressData();
        data.setToken(token);
        uploadProgressListener.setProgressData(data);
        fileUpload.setProgressListener(uploadProgressListener);
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            String fileUploadKey = request.getParameter("fileUploadKey");
            data.setFileUploadKey(fileUploadKey);
            data.setTotalItems(fileItems.size());
            return parseFileItems(fileItems, encoding);
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

    private String getToken(HttpServletRequest request) {

        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        log.debug("获取token===》{}=={}", TokenConstants.AUTHENTICATION, token);
        String result = replaceTokenPrefix(token);
        if (StringUtils.isEmpty(result)) {
            result = getTokenFromCookie(request);
            log.debug("获取 cookie token===》{}=={}", TokenConstants.AUTHENTICATION, result);
        }
        return result;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TokenConstants.TOKEN_COOKIE_KEY.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    public static String replaceTokenPrefix(String token) {

        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired(required = false)
    public void setUploadProgressListener(UploadProgressListener uploadProgressListener) {
        this.uploadProgressListener = uploadProgressListener;
    }
}

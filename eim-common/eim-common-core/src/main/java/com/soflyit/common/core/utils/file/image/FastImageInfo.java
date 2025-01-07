package com.soflyit.common.core.utils.file.image;

import lombok.Data;

/**
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-15 16:35
 */
@Data
public class FastImageInfo {

    private int height;
    private int width;
    private String mimeType;

    @Override
    public String toString() {
        return "MIME Type : " + mimeType + "\t Width : " + width
                + "\t Height : " + height;
    }
}

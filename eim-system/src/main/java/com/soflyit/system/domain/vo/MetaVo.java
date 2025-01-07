package com.soflyit.system.domain.vo;

import com.soflyit.common.core.utils.StringUtils;

/**
 * 路由显示信息
 *
 * @author soflyit
 */
public class MetaVo {

    private String title;


    private String icon;


    private boolean noCache;

    private Long menuId;

    private String isOwner;


    private String prefixPath;

    private Long belongAppId;


    private String link;

    public MetaVo() {
    }

    public MetaVo(String title, String icon, Long menuId) {
        this.title = title;
        this.icon = icon;
        this.menuId = menuId;
    }

    public MetaVo(String title, String icon, boolean noCache, Long menuId) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.menuId = menuId;
    }

    public MetaVo(String title, String icon, String link, Long menuId) {
        this.title = title;
        this.icon = icon;
        this.link = link;
        this.menuId = menuId;
    }

    public MetaVo(String title, String icon, boolean noCache, String link, Long menuId) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        if (StringUtils.ishttp(link)) {
            this.link = link;
        }
        this.menuId = menuId;
    }

    public boolean isNoCache() {
        return noCache;
    }

    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }


    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getPrefixPath() {
        return prefixPath;
    }

    public void setPrefixPath(String prefixPath) {
        this.prefixPath = prefixPath;
    }

    public Long getBelongAppId() {
        return belongAppId;
    }

    public void setBelongAppId(Long belongAppId) {
        this.belongAppId = belongAppId;
    }
}

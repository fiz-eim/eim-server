package com.soflyit.system.service.impl;

import com.soflyit.common.core.constant.Constants;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysMenu;
import com.soflyit.system.api.domain.SysRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import com.soflyit.system.domain.SysRoleMenu;
import com.soflyit.system.domain.vo.MetaVo;
import com.soflyit.system.domain.vo.RouterVo;
import com.soflyit.system.domain.vo.SysMenuVo;
import com.soflyit.system.domain.vo.TreeSelect;
import com.soflyit.system.mapper.SysMenuMapper;
import com.soflyit.system.mapper.SysRoleMapper;
import com.soflyit.system.mapper.SysRoleMenuMapper;
import com.soflyit.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.soflyit.common.core.constant.UserConstants.LAYOUT;

/**
 * 菜单 业务层处理
 *
 * @author soflyit
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;


    @Override
    public List<SysMenuVo> selectMenuList(Long userId) {
        return null;
    }


    @Override
    public List<SysMenuVo> selectMenuList(SysMenu menu, SysUser user) {
        List<SysMenuVo> menuList = null;
        Long userId = user.getUserId();

        if (SysUser.isAdmin(userId)) {
            menuList = menuMapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menu.getParams().put("deptId", user.getDeptId());
            menuList = menuMapper.selectMenuListByUser(menu);
        }
        return menuList;
    }


    @Override
    public List<SysMenuVo> selectMenuList(SysMenu menu) {
        return menuMapper.selectMenuList(menu);
    }


    @Override
    public Set<String> selectMenuPermsByUserId(Long appId, Long userId, Long deptId) {
        SysMenu condition = new SysMenu();
        condition.getParams().put("userId", userId);
        condition.getParams().put("deptId", deptId);
        condition.setAppId(appId);
        List<String> perms = menuMapper.selectMenuPermsByUserId(condition);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }


    @Override
    public List<SysMenu> selectMenuTreeByUser(Long appId, SysUser user) {
        Long userId = user.getUserId();
        List<SysMenu> menus = null;
        SysMenu condition = new SysMenu();
        if (appId == null) {

            condition.setAppId(-99L);
        } else {
            condition.setAppId(appId);
        }
        if (SecurityUtils.isAdmin(userId)) {
            menus = BeanUtils.convertList(menuMapper.selectMenuTreeAll(condition), SysMenu.class);
        } else {
            condition.getParams().put("userId", userId);
            condition.getParams().put("activeDeptId", user.getDeptId());
            menus = BeanUtils.convertList(menuMapper.selectMenuTreeByUser(condition), SysMenu.class);
        }
        return getChildPerms(menus, 0);
    }


    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }


    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath(), menu.getMenuId()));
            router.setRemark(menu.getRemark());
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && !cMenus.isEmpty() && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setHidden("1".equals(menu.getVisible()));
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath(), menu.getMenuId()));
                children.setQuery(menu.getQuery());
                children.setRemark(menu.getRemark());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getMenuId()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath(), menu.getMenuId()));
                children.setRemark(menu.getRemark());
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }


    @Override
    public List<RouterVo> buildMenusV3(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPathV3(menu));
            router.setComponent(getComponentV3(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath(), menu.getMenuId()));
            router.setRemark(menu.getRemark());
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && !cMenus.isEmpty() && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenusV3(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setHidden("1".equals(menu.getVisible()));
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath(), menu.getMenuId()));
                children.setQuery(menu.getQuery());
                children.setRemark(menu.getRemark());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getMenuId()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath(), menu.getMenuId()));
                children.setRemark(menu.getRemark());
                childrenList.add(children);
                router.setChildren(childrenList);
            }

            if (("W").equals(menu.getMenuType()) && ("1").equals(menu.getIsOwner())) {
                router.getMeta().setIsOwner(menu.getIsOwner());
                router.getMeta().setPrefixPath(menu.getPrefixPath());
                router.getMeta().setBelongAppId(menu.getBelongAppId());
            }
            routers.add(router);
        }

        return routers;
    }


    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenu dept : menus) {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = iterator.next();

            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }


    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }


    @Override
    public SysMenuVo selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }


    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }


    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }


    @Override
    public int insertMenu(SysMenu menu) {
        return menuMapper.insertMenu(menu);
    }


    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }


    @Override
    public int deleteMenuById(Long menuId) {
        return menuMapper.deleteMenuById(menuId);
    }


    @Override
    public String checkMenuNameUnique(SysMenu menu) {
        long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId(), menu.getAppId());
        if (StringUtils.isNotNull(info) && info.getMenuId() != menuId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    public String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());

        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }


    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        routerPath = paramsReplace(routerPath);
        menu.setPath(routerPath);


        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
            try {
                routerPath = URLEncoder.encode(routerPath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }

        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }


    public String getRouterPathV3(SysMenu menu) {
        String routerPath = menu.getPath();
        routerPath = paramsReplace(routerPath);
        menu.setPath(routerPath);


        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
            try {
                routerPath = URLEncoder.encode(routerPath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "";
        }

        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }


    public String getComponent(SysMenu menu) {
        String component = LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }


    public String getComponentV3(SysMenu menu) {
        String component = null;
        if (UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
            component = "";
        } else if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }


    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }


    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }


    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }


    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = iterator.next();

            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }


    private void recursionFn(List<SysMenu> list, SysMenu t) {

        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }


    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }


    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return !getChildList(list, t).isEmpty();
    }


    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS},
                new String[]{"", ""});
    }


    private String paramsReplace(String path) {
        if (StringUtils.ishttp(path)) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            path = path.replace("${userId}", loginUser.getUserid().toString());
            path = path.replace("${userName}", loginUser.getUsername());
            if (loginUser.getSysUser().getDept() != null) {
                path = path.replace("${deptId}", loginUser.getSysUser().getDept().getDeptId().toString());
            }
        }
        return path;
    }


    @Override
    public int updateMenuStatus(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }


    @Override
    public int deleteAuthRole(SysRoleMenu sysRoleMenu) {
        return roleMenuMapper.deleteAuthRole(sysRoleMenu);
    }


    @Override
    public int deleteAuthRoles(Long menuId, Long[] roleIds) {
        return roleMenuMapper.deleteAuthRoles(menuId, roleIds);
    }


    @Override
    public List<SysRole> unallocatedPostList(SysRole sysRole) {
        return roleMapper.unallocatedPostList(sysRole);
    }


    @Override
    public int insertAuthRoles(Long[] menuIds, Long[] roleIds) {
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            for (Long menuId : menuIds) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(menuId);
                list.add(sysRoleMenu);
            }
        }
        roleMenuMapper.deleteRoleMunus(list);
        return roleMenuMapper.insertAuthRoles(list);
    }
}

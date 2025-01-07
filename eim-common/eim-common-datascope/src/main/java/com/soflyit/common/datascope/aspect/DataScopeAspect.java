package com.soflyit.common.datascope.aspect;

import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.web.domain.BaseEntity;
import com.soflyit.common.datascope.annotation.DataScope;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysDept;
import com.soflyit.system.api.domain.SysRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据过滤处理
 *
 * @author soflyit
 */
@Aspect
@Component
public class DataScopeAspect {

    public static final String DATA_SCOPE_ALL = "1";


    public static final String DATA_SCOPE_CUSTOM = "2";


    public static final String DATA_SCOPE_DEPT = "3";


    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";


    public static final String DATA_SCOPE_SELF = "5";


    public static final String DATA_SCOPE = "dataScope";

    @Autowired
    private DataSource dataSource;


    private String systemSchema;

    @Value("${system.schema:}")
    public void setSystemSchema(String systemSchema) {
        if (StringUtils.isNotEmpty(systemSchema)) {
            systemSchema = systemSchema + ".";
        }
        this.systemSchema = systemSchema;
    }

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    private String getDatabaseProductName(DataSource dataSource) throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();
            return metaData.getDatabaseProductName();
        }
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) throws SQLException {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            SysUser currentUser = loginUser.getSysUser();

            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin()) {
                dataScopeFilter(
                        joinPoint,
                        currentUser,
                        controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias(),
                        controllerDataScope.deptFieldName(),
                        controllerDataScope.userFieldName());
            }
        }
    }


    public void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias, String deptFieldName, String userFieldName) throws SQLException {
        String dbType = getDatabaseProductName(dataSource);
        StringBuilder sqlString = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        for (SysRole role : user.getRoles()) {
            String dataScope = role.getDataScope();

            if (!DATA_SCOPE_CUSTOM.equals(dataScope) && conditions.contains(dataScope)) {
                continue;
            }
            if (DATA_SCOPE_ALL.equals(dataScope)) {
                sqlString = new StringBuilder();
                break;
            } else if (DATA_SCOPE_CUSTOM.equals(dataScope)) {
                sqlString.append(
                        StringUtils.format(
                                " OR {}.{} IN ( SELECT dept_id FROM {}sys_role_dept WHERE role_id = {} ) ",
                                deptAlias,
                                deptFieldName,
                                systemSchema,
                                role.getRoleId()
                        )
                );
            } else if (DATA_SCOPE_DEPT.equals(dataScope)) {
                List<SysDept> sysDeptList = user.getDepts();
                if (CollectionUtils.isEmpty(sysDeptList)) {
                    sqlString.append(StringUtils.format(" OR {}.{} = {} ", deptAlias, deptFieldName, user.getDeptId()));
                } else {
                    sqlString.append(
                            StringUtils.format(
                                    " OR {}.{} IN ( {} ) ",
                                    deptAlias,
                                    deptFieldName,
                                    getSubSqlFromDeptList(sysDeptList, 0, dbType).toString()
                            )
                    );
                }
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                List<SysDept> sysDeptList = user.getDepts();
                if (CollectionUtils.isEmpty(sysDeptList)) {
                    String template = " OR {}.{} IN ( SELECT dept_id FROM {}sys_dept WHERE dept_id = {} OR find_in_set( {} , ancestors ) )";
                    if ("Microsoft SQL Server".equals(dbType)) {
                        template = "OR {}.{} IN ( SELECT dept_id FROM {}sys_dept WHERE dept_id = {} OR charindex(concat(',', {}, ','), concat(',', ancestors, ',')) > 0 )";
                    }
                    sqlString.append(
                            StringUtils.format(
                                    template,
                                    deptAlias,
                                    deptFieldName,
                                    systemSchema,
                                    user.getDeptId(),
                                    user.getDeptId()
                            )
                    );
                } else {
                    sqlString.append(
                            StringUtils.format(
                                    " OR {}.{} IN ( SELECT DISTINCT dept_id FROM {}sys_dept WHERE dept_id IN ( {} ) {} )",
                                    deptAlias,
                                    deptFieldName,
                                    systemSchema,
                                    getSubSqlFromDeptList(sysDeptList, 0, dbType).toString(),
                                    getSubSqlFromDeptList(sysDeptList, 1, dbType).toString()
                            )
                    );
                }
            } else if (DATA_SCOPE_SELF.equals(dataScope)) {
                if (StringUtils.isNotBlank(userAlias)) {
                    sqlString.append(StringUtils.format(" OR {}.{} = {} ", userAlias, userFieldName, user.getUserId()));
                } else {

                    sqlString.append(" OR 1=0 ");
                }
            }
            conditions.add(dataScope);
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }
    }


    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }


    private static StringBuilder getSubSqlFromDeptList(List<SysDept> sysDeptList, int type, String dbType) {
        StringBuilder subSqlStr = new StringBuilder();
        switch (type) {
            case 0:

                for (int i = 0; i < sysDeptList.size(); i++) {
                    if ((i + 1) == sysDeptList.size()) {
                        subSqlStr.append(StringUtils.format(" {} ", sysDeptList.get(i).getDeptId()));
                    } else {
                        subSqlStr.append(StringUtils.format(" {},", sysDeptList.get(i).getDeptId()));
                    }
                }
                break;
            case 1:

                String template = " OR find_in_set ({}, ancestors)";
                if ("Microsoft SQL Server".equals(dbType)) {
                    template = " OR charindex(concat(',', {}, ','), concat(',', ancestors, ',')) > 0";
                }
                for (SysDept sysDeptItem : sysDeptList) {
                    subSqlStr.append(StringUtils.format(template, sysDeptItem.getDeptId()));
                }
                break;
        }
        return subSqlStr;
    }
}

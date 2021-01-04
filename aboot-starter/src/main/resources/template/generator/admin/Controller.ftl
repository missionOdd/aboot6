/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 * without the prior written consent of Whale Cloud Inc.
 *
 */
package ${package}.web;

import com.wteam.annotation.permission.PermissionGroup;
import com.wteam.domain.vo.R;
import ${package}.domain.${className};
import ${package}.service.${className}Service;
import ${package}.domain.criteria.${className}QueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
<#if hasDict>
import com.wteam.aboot.aop.Dict;
</#if>

/**
 * ${tableComment} 控制层.
 *
 * @author ${author}
 * @since ${date}
 */
@SuppressWarnings({"rawtypes"})
@Api(value = "${tableComment}Controller", tags = {"新增：${tableComment}操作"})
@RestController
@RequiredArgsConstructor
@RequestMapping("api/${changeClassName}")
@PermissionGroup(value = "${upperCaseClassName}", aliasPrefix = "${tableComment}")
public class ${className}Controller {

    private final ${className}Service ${changeClassName}Service;

    <#if hasDict>
    @Dict({<#if dicts??><#list dicts as dict>"${dict}"<#if dict?index != dicts?size-1 >,</#if> </#list></#if>})
    </#if>
    //@Log("查询${tableComment}分页")
    @ApiOperation(value = "查询${tableComment}分页")
    @GetMapping(value = "/page")
    @PreAuthorize("@R.check('${upperCaseClassName}:all', '${upperCaseClassName}:list')")
    public R get${className}s(${className}QueryCriteria criteria, Pageable pageable){
        return R.ok(${changeClassName}Service.queryAll(criteria, pageable));
    }

    <#if hasDict>
    @Dict({<#if dicts??><#list dicts as dict>"${dict}"<#if dict?index != dicts?size-1 >,</#if> </#list></#if>})
    </#if>
    //@Log("查询${tableComment}详情")
    @ApiOperation(value = "查询${tableComment}详情")
    @GetMapping(value = "/get/{${pkChangeColName}}")
    @PreAuthorize("@R.check('${upperCaseClassName}:all', '${upperCaseClassName}:list')")
    public R get(@PathVariable ${pkColumnType} ${pkChangeColName}){
        return R.ok(${changeClassName}Service.findDTOById(${pkChangeColName}));
    }

    //@Log("新增${tableComment}")
    @ApiOperation(value = "新增${tableComment}")
    @PostMapping(value = "/add")
    @PreAuthorize("@R.check('${upperCaseClassName}:all', '${upperCaseClassName}:add')")
    public R create(@Validated(${className}.Create.class) @RequestBody ${className} resources){
        return R.ok(${changeClassName}Service.create(resources));
    }

    //@Log("修改${tableComment}")
    @ApiOperation(value = "修改${tableComment}")
    @PostMapping(value = "/edit")
    @PreAuthorize("@R.check('${upperCaseClassName}:all', '${upperCaseClassName}:edit')")
    public R update(@Validated(${className}.Update.class) @RequestBody ${className} resources){
        ${changeClassName}Service.update(resources);
        return R.ok();
    }

    //@Log("删除${tableComment}")
    @ApiOperation(value = "删除${tableComment}")
    @PostMapping(value = "/del")
    @PreAuthorize("@R.check('${upperCaseClassName}:all', '${upperCaseClassName}:del')")
    public R delete(@RequestBody Set<${pkColumnType}> ids){
        ${changeClassName}Service.deleteAll(ids);
        return R.ok();
    }

    //@Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@R.check('${upperCaseClassName}:all', '${upperCaseClassName}:list')")
    public void download(HttpServletResponse response, ${className}QueryCriteria criteria) throws IOException {
        ${changeClassName}Service.download(${changeClassName}Service.queryAll(criteria), response);
    }

}
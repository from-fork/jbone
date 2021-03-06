package com.majunwei.jbone.sys.admin.controller;

import com.majunwei.jbone.common.ui.result.Result;
import com.majunwei.jbone.common.utils.ResultUtils;
import com.majunwei.jbone.sys.dao.domain.RbacRoleEntity;
import com.majunwei.jbone.sys.dao.domain.RbacUserEntity;
import com.majunwei.jbone.sys.service.RoleService;
import com.majunwei.jbone.sys.service.UserService;
import com.majunwei.jbone.sys.service.model.ListModel;
import com.majunwei.jbone.sys.service.model.role.CreateRoleModel;
import com.majunwei.jbone.sys.service.model.role.UpdateRoleModel;
import com.majunwei.jbone.sys.service.model.user.CreateUserModel;
import com.majunwei.jbone.sys.service.model.user.UpdateUserModel;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequiresRoles("admin")
    @RequestMapping("/index")
    public String index(){
        return "pages/role/index";
    }

    @RequiresRoles("admin")
    @RequestMapping("/list")
    @ResponseBody
    public Result list(ListModel listModel){
        PageRequest pageRequest = new PageRequest(listModel.getPageNumber()-1,listModel.getPageSize(), Sort.Direction.fromString(listModel.getSortOrder()),listModel.getSortName());
        //分页查找
        Page<RbacRoleEntity> page = roleService.findPage(listModel.getSearchText(),pageRequest);
        return ResultUtils.wrapSuccess(page.getTotalElements(),page.getContent());
    }


    @RequestMapping("/toCreate")
    public String toCreate(){
        return "pages/role/create";
    }

    @RequestMapping("/create")
    @ResponseBody
    public Result create(@Validated CreateRoleModel roleModel, BindingResult bindingResult){
        roleService.save(roleModel);
        return ResultUtils.wrapSuccess();
    }

    @RequestMapping("/toUpdate/{id}")
    public String toUpdate(@PathVariable("id")String id, ModelMap modelMap){
        RbacRoleEntity roleEntity = roleService.get(Integer.parseInt(id));
        modelMap.put("role",roleEntity);
        return "pages/role/update";
    }

    @RequestMapping("/update")
    @ResponseBody
    public Result update(@Validated UpdateRoleModel roleModel, BindingResult bindingResult){
        roleService.update(roleModel);
        return ResultUtils.wrapSuccess();
    }

    @RequestMapping("/delete/{ids}")
    @ResponseBody
    public Result delete(@PathVariable("ids")String ids){
        roleService.delete(ids);
        return ResultUtils.wrapSuccess();
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public Result get(@PathVariable("id")String id){
        RbacRoleEntity roleEntity = roleService.get(Integer.parseInt(id));
        return ResultUtils.wrapSuccess(roleEntity);
    }
}

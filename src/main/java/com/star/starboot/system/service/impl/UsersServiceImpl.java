package com.star.starboot.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.starboot.common.enums.ResultCode;
import com.star.starboot.common.utils.CommonUtils;
import com.star.starboot.common.utils.ShiroUtils;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.exception.BusinessException;
import com.star.starboot.system.dao.UsersMapper;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Department;
import com.star.starboot.system.entity.Users;
import com.star.starboot.system.service.DepartmentService;
import com.star.starboot.system.service.UsersReRolesService;
import com.star.starboot.system.service.UsersService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author xpy
 * @since 2020-06-30
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UsersReRolesService usersReRolesService;

    @Override
    public UsersDto getUserByUserCodeAndCompanyCode(String userCode, String companyCode) {
        return usersMapper.getUserByUserCodeAndCompanyCode(userCode, companyCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UsersDto usersDto) {
        // 生成秘钥和密文
        String[] encrpty = CommonUtils.encryptPassword(usersDto.getPassword());
        usersDto.setSalt(encrpty[0]);
        usersDto.setPassword(encrpty[1]);

        // 判断用户工号是否已经存在
//        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper();
//        wrapper.eq(Users::getUserCode,usersDto.getUserCode());
//        List list = usersMapper.selectList(wrapper);
//        if(!StringUtils.isEmpty(list) && list.size()>0){
//            throw  new BusinessException("用户名已经存在");
//        }

        UsersDto user = usersMapper.getUserByUserCodeAndCompanyCode(usersDto.getUserCode(), usersDto.getCompanyCode());
        if(!StringUtils.isEmpty(user)){
            throw  new BusinessException("用户名已经存在");
        }
        usersDto.setWorking(SystemConstant.WORKING);

        // 查询该部门代码的部门id 前端默认部门和组织时使用
        Department department = departmentService.getByCodeAndCompanyCode(usersDto.getDepartmentCode(),usersDto.getCompanyCode());
        usersDto.setDepartmentId(department.getDepartmentId());

        usersMapper.insert(usersDto);

        // 设置默认角色
//        UsersReRoles usersReRoles = new UsersReRoles();
//        usersReRoles.setUserId(usersDto.getUserId());
//        usersReRoles.setRoleId("e38e757154d8f746944b69f040065645");
//        usersReRolesService.save(usersReRoles);
    }

    @Override
    public IPage<UsersDto> queryPager(UsersDto usersDto, Integer current, Integer size) {
        return usersMapper.queryPage(new Page(current, size), usersDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String userId) {
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        Users user = new Users();
        user.setUserId(userId);
        user.setDeletedAt(new Date());
        user.setDeletedBy(userInfo.getUserId());
        usersMapper.updateById(user);
        // updateById 无法更新删除字段  UPDATE t_users SET DELETED_BY=?, DELETED_AT=? WHERE USER_ID=? AND DELETED_CODE='0'
        usersMapper.deleteById(userId);
    }

    @Override
    public List<UsersDto> queryList() {
        return usersMapper.queryList();
    }

    @Override
    public List<UsersDto> getByIds(List<String> userIds) {
        return usersMapper.getByIds(userIds);
    }

    @Override
    public UsersDto getUserByUserTelAndCompanyCode(String tel, String companyCode) {
        return usersMapper.getUserByUserTelAndCompanyCode(tel, companyCode);
    }

    @Override
    public void changePassword(UsersDto usersDto) {
        Users user = new Users();
        UsersDto userInfo = ShiroUtils.build().getUserInfo();
        // 修改
        String salt = UUID.randomUUID().toString();
        // 默认算法是SHA-512
        DefaultHashService hashService = new DefaultHashService();

        Users oldUser = usersMapper.selectById(userInfo.getUserId());
        // 旧密码
        HashRequest oldRequest = new HashRequest.Builder().setAlgorithmName("MD5")
                .setSource(ByteSource.Util.bytes(usersDto.getOldPassword())).setSalt(ByteSource.Util.bytes(oldUser.getSalt()))
                .setIterations(2).build();
        String oldPassword = hashService.computeHash(oldRequest).toHex();

        if(!oldUser.getPassword().equals(oldPassword)){
            throw new BusinessException(ResultCode.ERROR_PASSWORD_CHANGE.getMsg());
        }

        // 新密码
        HashRequest request = new HashRequest.Builder().setAlgorithmName("MD5")
                .setSource(ByteSource.Util.bytes(usersDto.getPassword())).setSalt(ByteSource.Util.bytes(salt))
                .setIterations(2).build();
        String password = hashService.computeHash(request).toHex();

        user.setUserId(userInfo.getUserId());
        user.setSalt(salt);
        user.setPassword(password);
        usersMapper.updateById(user);
    }
}

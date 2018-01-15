package com.mmec.centerService.userModule.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.mmec.centerService.userModule.entity.AuthEntity;
@Repository
public class UserAuthDao {
	@PersistenceContext  
    private EntityManager em;  
	//查询对接平台权限
	public List<AuthEntity> queryMMECAuthByRoleId(String roleId) throws Exception {
		String sqlStr = "select a from AuthEntity a, RoleAuthRelationEntity rr where rr.authId = a.id and rr.roleId = " + roleId;
        List<AuthEntity>  authList = em.createQuery(sqlStr).getResultList();
        return authList;
    }

	
	//查询对接用户权限
	public List<AuthEntity> queryMMECAuthByUserId(int userId) throws Exception {
		String sqlStr = "select a from AuthEntity a, RoleAuthRelationEntity rr,UserRoleRelationEntity ur where ur.roleId = rr.roleId and rr.authId = a.id and ur.userId = " + userId;
        List<AuthEntity>  authList = em.createQuery(sqlStr).getResultList();
        return authList;
    }

	//查询云签子账号权限
	public String queryYSChildAuthByUserId(int userId) throws Exception {
		String sqlStr = "select r.roleName from RoleEntity as r, UserRoleRelationEntity as ur where ur.roleId = r.id ur.userId = " + userId;
		String roleName = (String)em.createQuery(sqlStr).getSingleResult();
        return roleName;
    }
	
	//云签本地化;根据平台查询配置权限
	public List<AuthEntity> queryAuthByPlatform(int roleId){
		String sqlStr = "select a from AuthEntity a, RoleAuthRelationEntity rr where  rr.authId = a.id and rr.roleId = " + roleId;
        List<AuthEntity>  authList = em.createQuery(sqlStr).getResultList();
        return authList;
	}
}

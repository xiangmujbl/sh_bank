package com.mmec.centerService.userModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.UserAuthorityEntity;

public interface UserAuthorityDao  extends JpaRepository<UserAuthorityEntity,Integer> {
	
	//根据平台ID和被授权人的ID查询
	/**
	 * 
	 * @param platformId 平台Id
	 * @param authorId 被授权人的Id
	 * @param userId 授权人的Id
	 * @return
	 */
	@Query(value="SELECT u FROM UserAuthorityEntity u WHERE u.platformId = :platformId AND u.authorId = :authorId AND u.userId = :userId ")
	public UserAuthorityEntity queryUserAuthorityByAuthorId(@Param("platformId") int platformId,@Param("authorId") int authorId,@Param("userId")int userId);
	
	/**
	 * 一代多判断
	 * @param platformId
	 * @param authorId
	 * @param userId
	 * @return
	 */
	@Query(value="SELECT u FROM UserAuthorityEntity u WHERE u.platformId = :platformId AND u.authorId = :authorId ")
	public List<UserAuthorityEntity> queryUserAuthoritys(@Param("platformId") int platformId,@Param("authorId") int authorId);
}

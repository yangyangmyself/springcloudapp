package org.spring.oauth.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.oauth.server.dao.PermissionDao;
import org.spring.oauth.server.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 * @author oy
 * 
 */
@Service("scUserDetailsService")
public class SystemUserDetailsService implements UserDetailsService {

	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserDao userDao;

	@Autowired
	private PermissionDao permissionDao;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Map map = userDao.findByUserName(username);
		if (map != null) {
			List permissions = getAuthorities(map.get("yhdh").toString());
			return new User(map.get("yhdh").toString(), map.get("mm")
					.toString(), permissions);
		} else {
			throw new UsernameNotFoundException("admin: " + username
					+ " do not exist!");
		}
	}

	private List<GrantedAuthority> getAuthorities(String yhdh) {
		List permissions = permissionDao.findPermissionById(yhdh);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (Object permission : permissions) {
			Map map = (Map)permission;
			if (permission != null && map.get("role") != null) {
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
						map.get("role").toString());
				// 1：此处将权限信息添加到 GrantedAuthority
				// 对象中，在后面进行全权限验证时会使用GrantedAuthority对象
				grantedAuthorities.add(grantedAuthority);
			}
		}
		return grantedAuthorities;
	}
}

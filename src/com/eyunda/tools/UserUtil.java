package com.eyunda.tools;

import com.eyunda.third.domain.account.UserData;
import com.eyunda.third.domain.enumeric.UserRoleCode;

public class UserUtil {
	
	public static boolean isRole(UserData userData, UserRoleCode roleCode) {
		boolean flag = false;
		try {
			String rss = userData.getRoles();
			if (rss != null && !"".equals(rss)) {
				if (rss.indexOf(Integer.toString(roleCode.ordinal())) >= 0)
					flag = true;
			}
		} catch (Exception e) {
			return flag;
		}
		return flag;
	}
}

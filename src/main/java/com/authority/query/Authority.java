package com.authority.query;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.authority.constants.UserInfoConst;
import com.authority.service.AuthorityService;
import com.authority.utils.Util;


@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域问题
@RestController//json格式
public class Authority {
	
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping("/hello")
    public String hello() {
      return "Hello World!";
    }
	
	/**
	 * 查询test库下user表中所有用户
	 * @return queryDataList
	 */
	@RequestMapping(value="/queryDataList",method=RequestMethod.POST)
	public Object queryDataList(HttpServletRequest request) {
		List<Map<String, Object>> queryDataList = null;
		try {
			queryDataList = authorityService.queryDataList();
			HttpSession session = request.getSession();
			session.setAttribute(UserInfoConst.SESSION_USER_INFO, queryDataList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryDataList;
	}
	
	/**
	 * 查询common库中权限
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryAuthList",method=RequestMethod.POST)
	public Object queryAuthList(HttpServletRequest request) {
		List<Map<String, Object>> queryAuthList = null;
		try {
			String ids = "381";
			queryAuthList = authorityService.queryAuthList(ids);
			HttpSession session = request.getSession();
			session.setAttribute(UserInfoConst.SESSION_USER_INFO, queryAuthList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryAuthList;
	}
	
	/**
	 * 查询同单位下同处级和科级部门下的人员id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryPersonIdsList",method=RequestMethod.POST)
	public Object queryPersonIdsList(HttpServletRequest request) {
		List<Map<String, Object>> queryPersonIdsList = null;
		try {
			Integer inst_senior_id = 1;//单位id
			Integer inst_id = 2;//处级部门id
			Integer dep_id = 2;//科级部门id
			queryPersonIdsList = authorityService.queryPersonIdsList(inst_senior_id,inst_id,dep_id);
			HttpSession session = request.getSession();
			session.setAttribute(UserInfoConst.SESSION_USER_INFO, queryPersonIdsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return queryPersonIdsList;
	}
	
	/**
	 * 根据ids批量修改权限功能
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateAuthList",method=RequestMethod.POST)
	public Object updateAuthList(HttpServletRequest request) {
		int updateAuthList = 0;
		try {
			Object _ids = queryPersonIdsList(request);//查询同单位下同处级和科级部门下的人员id
			String ids = "";
			if(!Util.isEmpty(_ids)){
				ids = _ids.toString();
			}
			updateAuthList = authorityService.updateAuthList(ids);
			HttpSession session = request.getSession();
			session.setAttribute(UserInfoConst.SESSION_USER_INFO, updateAuthList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateAuthList;
	}
}

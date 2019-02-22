package com.authority.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.authority.utils.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class AuthorityService {

	@Autowired
	private JdbcTemplate primaryJdbcTemplate;//test库
	
	@Autowired
	private JdbcTemplate secondaryJdbcTemplate;//common库
	
	@Autowired
	private JdbcTemplate thirddaryJdbcTemplate;//zdr_data库
	
	/**
	 * 查询test库下面user表中的所有数据
	 * @return queryForList
	 */
	public List<Map<String, Object>> queryDataList() {
		String sql = "select * from user";
		List<Map<String, Object>> queryForList = primaryJdbcTemplate.queryForList(sql);
		System.out.println("查询结果为："+queryForList);
		return queryForList;
	}
	
	/**
	 * 查询出用户权限
	 * @param ids
	 * @return
	 */
	public List<Map<String, Object>> queryAuthList(String ids) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT p.frame FROM permission p LEFT JOIN user_info u ON u.id=p.user_id")
		.append(" LEFT JOIN department d ON d.id=u.dep_id")
		.append(" LEFT JOIN institution i ON i.id=d.inst_id")
		.append(" LEFT JOIN institution_senior t ON t.id=i.inst_senior_id")
		.append(" WHERE u.id IN("+ids+")");
		
		String sql = sb.toString();
		
		List<Map<String, Object>> queryForList = secondaryJdbcTemplate.queryForList(sql);
		System.out.println("查询结果为："+queryForList);
		return queryForList;
	}
	/**
	 * 查询同单位下同处级和科级部门下的人员id
	 * @param inst_senior_id
	 * @param inst_id
	 * @param dep_id
	 * @return
	 */
	public List<Map<String, Object>> queryPersonIdsList(Integer inst_senior_id, Integer inst_id, Integer dep_id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT u.id FROM user_info u LEFT JOIN department d ON d.id=u.dep_id")
		.append(" LEFT JOIN institution i ON i.id=d.inst_id")
		.append(" LEFT JOIN institution_senior s ON s.id=i.inst_senior_id")
		.append(" where 1=1")
		.append(" AND s.id=?")
		.append(" AND i.id=?")
		.append(" AND d.id=?");
		String sql = sb.toString();
		List<Map<String, Object>> queryForList = secondaryJdbcTemplate.queryForList(sql,inst_senior_id,inst_id,dep_id);
		
		System.out.println("查询结果为："+queryForList);
		return queryForList;
	}
	
	/**
	 * 根据ids批量修改权限
	 * @param ids
	 * @return
	 * @throws IOException 
	 */
	public int updateAuthList(String ids) throws IOException {
		StringBuffer sb = new StringBuffer();
		//读取properties文件方法一
		Properties prop =  PropertiesLoaderUtils.loadAllProperties("authority.properties");//从配置文件拿到json值
		String trim = prop.getProperty("auth.authority").trim();
		String frame = new String(trim.getBytes("ISO8859-1"), "UTF-8");
		System.out.println(frame);
		if(!Util.isEmpty(ids)){
			StringBuffer buffer = new StringBuffer();
			JSONArray fromObject = JSONArray.fromObject(ids);
			for (int i = 0; i < fromObject.size(); i++) {
				String objStr = fromObject.get(i).toString();
				JSONObject fromObject2 = JSONObject.fromObject(objStr);
				for (int j = 0; j < fromObject2.size(); j++) {
					String id = fromObject2.get("id").toString();
					buffer.append(id+",");
				}
			}
			String userId = buffer.deleteCharAt(buffer.length()-1).toString();
			System.out.println(userId);
			sb.append("update permission p SET p.frame=? WHERE p.user_id in("+userId+")");
			String sql = sb.toString();
			int update = secondaryJdbcTemplate.update(sql, frame);
			return update;
		}
		return 0;
	}
}

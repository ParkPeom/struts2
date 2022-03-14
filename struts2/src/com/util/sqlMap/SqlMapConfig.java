package com.util.sqlMap;

import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

// 파일의 이름은 내맘대로
// xml은 텍스트이기때문에 객체로 바꿔야만 -> Object 클래스로 (객체화)메모리상에 올려야지 쓸수있다.
public class SqlMapConfig {
	
	private static final SqlMapClient sqlMap; // sqlMap : xml에서 읽어온 객체를 읽을것이다.
											  // 상수여서 초기화 해줘야한다.
	static {	
		try {	
			String resource = "com/util/sqlMap/sqlMapConfig.xml"; // String에 문자가 들어간다.
			Reader reader = Resources.getResourceAsReader(resource);
			// 경로로 들어가서 xml를 읽어오게 한다
			// 아직 객체화가 되지 않았다
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			//SqlMapClientBuilder: 객체를 만들어서 sqlMap에 들어가게된다.
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("sqlMap Error : " + e);
		}
	}
	// getSqlMapInstance() 호출하면 sqlMap를 반환
	public static SqlMapClient getSqlMapInstance() {
		return sqlMap;
	}	
}

package com.util.sqlMap;

import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

// ������ �̸��� �������
// xml�� �ؽ�Ʈ�̱⶧���� ��ü�� �ٲ�߸� -> Object Ŭ������ (��üȭ)�޸𸮻� �÷����� �����ִ�.
public class SqlMapConfig {
	
	private static final SqlMapClient sqlMap; // sqlMap : xml���� �о�� ��ü�� �������̴�.
											  // ������� �ʱ�ȭ ������Ѵ�.
	static {	
		try {	
			String resource = "com/util/sqlMap/sqlMapConfig.xml"; // String�� ���ڰ� ����.
			Reader reader = Resources.getResourceAsReader(resource);
			// ��η� ���� xml�� �о���� �Ѵ�
			// ���� ��üȭ�� ���� �ʾҴ�
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			//SqlMapClientBuilder: ��ü�� ���� sqlMap�� ���Եȴ�.
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("sqlMap Error : " + e);
		}
	}
	// getSqlMapInstance() ȣ���ϸ� sqlMap�� ��ȯ
	public static SqlMapClient getSqlMapInstance() {
		return sqlMap;
	}	
}

package com.util.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.util.sqlMap.SqlMapConfig;


// �������̽��� ������ Ŭ����
public class CommonDAOImpl implements CommonDAO {
	
	private SqlMapClient sqlMap;
	
	// �о���� sqlMap�� �ʱ�ȭ ��Ų��.
	public CommonDAOImpl() {
		this.sqlMap = SqlMapConfig.getSqlMapInstance();
	}
	
	// ����Ϸ��� ��ü�� �����ؾ��Ѵ�.
	public static CommonDAO getInstance() {
		// �ڱ��ڽ��� ��ü�� ��ȯ���ش�.
		// ��ü�� �������ָ� sqlMap�� ����Ҽ��ִ�.
		return new CommonDAOImpl();
	}
	@Override
	public void insertData(String id, Object value) {
	
		try {
			// insert , update , delete �� �����Ҷ� �ڵ������� Ʈ������� ���۵ȴ�.
			sqlMap.startTransaction();
			
			sqlMap.insert(id, value); // ������ �����͸� insert ��Ų��.
			
			sqlMap.commitTransaction(); // Ŀ���� ��Ų��.
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			try {
				sqlMap.endTransaction();				
			} catch (Exception e2) {}
		}
	}

	@Override
	public int updateData(String id, Object value) {
		
		int result = 0;
		try {		
			sqlMap.startTransaction();	
			result = sqlMap.update(id, value); // ������ �����͸� insert ��Ų��.
			
			sqlMap.commitTransaction(); // Ŀ���� ��Ų��.
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			try {
				sqlMap.endTransaction();				
			} catch (Exception e2) {}
		}
		return result;
	}
	@Override
	public int updateData(String id, Map<String, Object> map) {
		
		int result = 0;
		
		try {	
			sqlMap.startTransaction(); // �ڵ����� Ʈ����� ������ 
			
			result = sqlMap.update(id, map); // ������ �����͸� insert ��Ų��.
			
			sqlMap.commitTransaction(); // Ŀ���� ��Ų��.
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally { // ������ ����
			try {
				sqlMap.endTransaction(); // Ʈ������� �ݵ�� ��������Ѵ�.				
			} catch (Exception e2) {}
		}
		return result;
	}

	@Override
	public int deleteData(String id, Object value) {
		
		int result = 0;
		
		try {
			
			sqlMap.startTransaction();
			
			result = sqlMap.delete(id, value); // ������ �����͸� insert ��Ų��.
			
			sqlMap.commitTransaction(); // Ŀ���� ��Ų��.
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			try {
				sqlMap.endTransaction();				
			} catch (Exception e2) {}
		}
		return result;
	}

	@Override
	public int deleteData(String id, Map<String, Object> map) {
		
		int result = 0;
		
		try {
			
			sqlMap.startTransaction();
			
			result = sqlMap.delete(id, map); // ������ �����͸� insert ��Ų��.
			
			sqlMap.commitTransaction(); // Ŀ���� ��Ų��.
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			try {
				sqlMap.endTransaction();				
			} catch (Exception e2) {}
		}
		return result;
	}

	@Override
	public int deleteAllData(String id) {
		
		int result = 0;
		
		try {
			
			sqlMap.startTransaction();
			
			result = sqlMap.delete(id); // ������ �����͸� ���� ��Ų��.
			
			sqlMap.commitTransaction(); // Ŀ���� ��Ų��.
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			try {
				sqlMap.endTransaction();				
			} catch (Exception e2) {}
		}
		return result;
	}

	@Override
	public Object getReadData(String id) {
		// �Է¼������� �Ҷ��� Ʈ����ǽ�����
		
		try {
			return sqlMap.queryForObject(id); // ������ id�� selecte�Ѱ�� Object
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null; // �����;����� null 
	}

	@Override
	public Object getReadData(String id, Object value) {
		
		try {
			return sqlMap.queryForObject(id, value); // ������ id�� selecte�Ѱ�� Object
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null; // �����;����� null 
	}

	@Override
	public Object getReadData(String id, Map<String, Object> map) {
		try {
			return sqlMap.queryForObject(id, map); // ������ id�� selecte�Ѱ�� Object
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null; // �����;����� null 
	}

	@Override
	public int getIntValue(String id) {
		
		try {
			return ((Integer)sqlMap.queryForObject(id)).intValue(); // int������ ��ȯ���ش�.
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return 0; // �ȵǸ� 0
	}

	@Override
	public int getIntValue(String id, Object value) {
		
		try {
			return ((Integer)sqlMap.queryForObject(id, value)).intValue(); // int������ ��ȯ���ش�.
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return 0;
	}

	@Override
	public int getIntValue(String id, Map<String, Object> map) {
		
		try {
			return ((Integer)sqlMap.queryForObject(id,map)).intValue(); // int������ ��ȯ���ش�.
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return 0;
	}

	@SuppressWarnings("unchecked") // ���â������������
	@Override
	public List<Object> getListData(String id) {
		
		try {
			return (List<Object>)sqlMap.queryForList(id);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked") // ���â������������
	@Override
	public List<Object> getListData(String id, Object value) {
		
		try {
			return (List<Object>)sqlMap.queryForList(id,value);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked") // ���â������������
	@Override
	public List<Object> getListData(String id, Map<String, Object> map) {
		
		try {
			return (List<Object>)sqlMap.queryForList(id,map);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
}

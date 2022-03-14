package com.util.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.util.sqlMap.SqlMapConfig;


// 인터페이스를 구현한 클래스
public class CommonDAOImpl implements CommonDAO {
	
	private SqlMapClient sqlMap;
	
	// 읽어오고 sqlMap를 초기화 시킨다.
	public CommonDAOImpl() {
		this.sqlMap = SqlMapConfig.getSqlMapInstance();
	}
	
	// 사용하려면 객체를 생성해야한다.
	public static CommonDAO getInstance() {
		// 자기자신의 객체를 반환해준다.
		// 객체를 생성해주면 sqlMap를 사용할수있다.
		return new CommonDAOImpl();
	}
	@Override
	public void insertData(String id, Object value) {
	
		try {
			// insert , update , delete 는 시작할때 자동적으로 트랜잭션이 시작된다.
			sqlMap.startTransaction();
			
			sqlMap.insert(id, value); // 실제로 데이터를 insert 시킨다.
			
			sqlMap.commitTransaction(); // 커밋을 시킨다.
			
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
			result = sqlMap.update(id, value); // 실제로 데이터를 insert 시킨다.
			
			sqlMap.commitTransaction(); // 커밋을 시킨다.
			
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
			sqlMap.startTransaction(); // 자동으로 트랜잭션 시작함 
			
			result = sqlMap.update(id, map); // 실제로 데이터를 insert 시킨다.
			
			sqlMap.commitTransaction(); // 커밋을 시킨다.
			
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally { // 무조건 실행
			try {
				sqlMap.endTransaction(); // 트랜잭션을 반드시 끝내줘야한다.				
			} catch (Exception e2) {}
		}
		return result;
	}

	@Override
	public int deleteData(String id, Object value) {
		
		int result = 0;
		
		try {
			
			sqlMap.startTransaction();
			
			result = sqlMap.delete(id, value); // 실제로 데이터를 insert 시킨다.
			
			sqlMap.commitTransaction(); // 커밋을 시킨다.
			
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
			
			result = sqlMap.delete(id, map); // 실제로 데이터를 insert 시킨다.
			
			sqlMap.commitTransaction(); // 커밋을 시킨다.
			
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
			
			result = sqlMap.delete(id); // 실제로 데이터를 삭제 시킨다.
			
			sqlMap.commitTransaction(); // 커밋을 시킨다.
			
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
		// 입력수정삭제 할때만 트랜잭션실행함
		
		try {
			return sqlMap.queryForObject(id); // 있을때 id를 selecte한결과 Object
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null; // 데이터없을때 null 
	}

	@Override
	public Object getReadData(String id, Object value) {
		
		try {
			return sqlMap.queryForObject(id, value); // 있을때 id를 selecte한결과 Object
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null; // 데이터없을때 null 
	}

	@Override
	public Object getReadData(String id, Map<String, Object> map) {
		try {
			return sqlMap.queryForObject(id, map); // 있을때 id를 selecte한결과 Object
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null; // 데이터없을때 null 
	}

	@Override
	public int getIntValue(String id) {
		
		try {
			return ((Integer)sqlMap.queryForObject(id)).intValue(); // int값으로 반환해준다.
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return 0; // 안되면 0
	}

	@Override
	public int getIntValue(String id, Object value) {
		
		try {
			return ((Integer)sqlMap.queryForObject(id, value)).intValue(); // int값으로 반환해준다.
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return 0;
	}

	@Override
	public int getIntValue(String id, Map<String, Object> map) {
		
		try {
			return ((Integer)sqlMap.queryForObject(id,map)).intValue(); // int값으로 반환해준다.
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return 0;
	}

	@SuppressWarnings("unchecked") // 경고창보여주지말라
	@Override
	public List<Object> getListData(String id) {
		
		try {
			return (List<Object>)sqlMap.queryForList(id);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked") // 경고창보여주지말라
	@Override
	public List<Object> getListData(String id, Object value) {
		
		try {
			return (List<Object>)sqlMap.queryForList(id,value);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked") // 경고창보여주지말라
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

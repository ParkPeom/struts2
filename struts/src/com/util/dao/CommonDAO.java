package com.util.dao;

import java.util.List;
import java.util.Map;

public interface CommonDAO {
	
	//데이터 추가
	public void insertData(String id, Object value); // 이름마음대로지만 inser는 반드시 만들어줘야함
	
	//데이터 수정
	public int updateData(String id,Object value);
	public int updateData(String id,Map<String, Object> map);
	
	//데이터 삭제하는 경우의 수 3개 
	public int deleteData(String id,Object value); // 이것만씀
	public int deleteData(String id,Map<String, Object> map); // 이것만씀 
	public int deleteAllData(String id);
	
	//레코드 가져오기
	public Object getReadData(String id); // 하나의 데이터가져오기(자주쓴다)
	public Object getReadData(String id,Object value);
	public Object getReadData(String id,Map<String, Object> map);
	
	//하나의 데이터를 가져오는 경우의 수
	public int getIntValue(String id); // 하나의 데이터가져오기(자주쓴다)
	public int getIntValue(String id,Object value);
	public int getIntValue(String id,Map<String, Object> map);
	
	//많은데이터를 가져오기

	public List<Object> getListData(String id); // 하나의 데이터가져오기(자주쓴다)
	public List<Object> getListData(String id,Object value);
	public List<Object> getListData(String id,Map<String, Object> map);
	
}

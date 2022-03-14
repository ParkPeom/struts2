package com.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {

	private Connection conn;
	
	public BoardDAO(Connection conn) {
		this.conn = conn; // 객체 생성 동시에 초기화 = 의존성 주입 
		
	}
	// insert 할때 num의 최대값을 구하는 메소드가 필요하다
	// 다음번 번호의 num을 구할수있다.
	// insert 할때 num 을 가져오기 위해서 만듬 
	public int getMaxNum() {
		
		int maxNum = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			// board에서 num을 select하는데 값이있으면 최대값 , null 이면 0으로 
			sql = "select nvl(max(num),0) from board"; 
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			// 데이터가 max값 하나이므로 단일값 
			if(rs.next()) {
				// 값이 있으면 maxNum에 받아내라
				// 컬럼명이 파생컬럼 nvl(max(num),0) 으로 나오는데..
				// 이런경우에는 컬럼 단 한개 나왔으므로 1이다. 
				// 숫자 1 적어주면 된다.
				maxNum = rs.getInt(1);
			}
			rs.close();
			pstmt.close();		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return maxNum;
	}
	
	// 입력
	public int insertData(BoardForm dto) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			// 여기다가 insert 적어주면 된다.
			// hitcount 기본값은 0
			// sysdate는 직접넣어주면 됨
			sql = "insert into board (num,name,pwd,email,subject,";
			sql+= "content,ipAddr,hitcount,created) ";
			sql+= " values (?,?,?,?,?,?,?,0,sysdate)";
			// num 하고 ipAddr를 만들어주면 됨
			
			pstmt = conn.prepareStatement(sql);
			// 9개중에 ?표 7개에 넣어주면 됨
			
			// 여기 넘어온 DTO는  NUM이 있는 DTO
			pstmt.setInt(1,dto.getNum());
			pstmt.setString(2,  dto.getName());
			pstmt.setString(3,  dto.getPwd());
			pstmt.setString(4,  dto.getEmail());
			pstmt.setString(5,  dto.getSubject());
			pstmt.setString(6,  dto.getContent());
			pstmt.setString(7 , dto.getIpAddr());
			
			// 회사에서 20년동안 쓰다가 마이바티스 , 아이바티스가 나옴
			
			result = pstmt.executeUpdate();
			pstmt.close();
		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	// 검색한 데이터 갯수
	public int getDataCount(String searchKey,String searchValue) {
		int totalCount = 0;	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		
		try {
			searchValue = "%" + searchValue + "%";
			// null 값은 0으로 해수 개수를 새준다
			sql = "select nvl(count(*),0)from board ";
			sql += "where "+ searchKey + " like ?";
			// searchKey  : 안에있는 데이터 
			//  searchValue = 우리가 서치하는 단어        
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, searchValue); // null 이들어오면 모든데이터가 보임
			
			// searchValue를 검색을 해서 key값을 얻는다.
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				// searchKey컬럼 하나에 대한
				totalCount = rs.getInt(1); 
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return totalCount;	
	}
	
	// 전체데이터 
	// Board dto 가 들어가있는 list
	public List<BoardForm> getLists(int start,int end ,
			String searchKey,String searchValue) {
		
		List<BoardForm> lists = new ArrayList<BoardForm>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;	
		
		try {
			searchValue = "%" + searchValue + "%";
			
			/*
			select * from (
				select rownum rnum,data.* from (
				select num,name,subject from board order by num desc) data)
				where rownum >= 2 and rownum <= 3; 
				
			*/ // data.* : 해당데이터 모든것을 가져와라
			sql = "select * from (";
			sql+= "select rownum rnum, data.* from (";
			sql+= "select num,name,subject,hitCount,";
			sql+= "to_char(created,'YYYY-MM-DD') created ";
			sql+= "from board where " + searchKey + " like ? ";
			sql+= "order by num desc) data) ";
			sql+= "where rnum>=? and rnum<=?"; // 안에있는 파생컬럼을 밖에서 조건절에 사용
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,searchValue);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				BoardForm dto = new BoardForm();
				// dto 에 5개의 데이터가 들어가고
				// 그걸 list 에 add로 넣음
				dto.setNum(rs.getInt("num"));
				dto.setName(rs.getString("name"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));	
				lists.add(dto);
			}
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return lists;
	}
	
	
	// num으로 조회한 한개의 데이터 ( article , update 할때 쓰일것임 ) 
		// Board dto 가 들어가있는 list
		
	public BoardForm getReadData(int num) { 
			
			BoardForm dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;	
			try {
//				한사람의 정보를 가져옴
				sql = "select num,name,pwd,email,subject,content,";
				sql +="ipAddr,hitCount,created ";
				sql +="from board where num=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				
				rs = pstmt.executeQuery();
				
				// 데이터가 하나이므로 if문
				if(rs.next()) {
					
					dto = new BoardForm();
					// dto 에 5개의 데이터가 들어가고
					// 그걸 list 에 add로 넣음
					dto.setNum(rs.getInt("num"));
					dto.setName(rs.getString("name"));
					dto.setPwd(rs.getString("pwd"));
					dto.setEmail(rs.getString("email"));
					dto.setSubject(rs.getString("subject"));
					dto.setContent(rs.getString("content"));
					dto.setIpAddr(rs.getString("ipAddr"));
					dto.setHitCount(rs.getInt("hitCount"));
					dto.setCreated(rs.getString("created"));	
				}
				rs.close();
				pstmt.close();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return dto;
		}
		
		// 조회수 증가(update 업데이트)
	
		// num이라는 값의 조회수를 증가시킬것이다.
		public int updateHitCount(int num) {
			
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				// num에 해당하는 데이터에 +1 하게됨
				sql = "update board set hitCount=hitCount+1 where num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				result = pstmt.executeUpdate(); // 변경되면 1 안되면 0 
				pstmt.close();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
		}
		
		//수정
		public int updateData(BoardForm dto) {
			int result = 0;
			
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "update board set name=?,pwd=?,email=?,subject=?,";
				sql += "content=? where num=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, dto.getName());
				pstmt.setString(2, dto.getPwd());
				pstmt.setString(3, dto.getEmail());
				pstmt.setString(4, dto.getSubject());
				pstmt.setString(5, dto.getContent());
				pstmt.setInt(6, dto.getNum());
				
				result = pstmt.executeUpdate();
				pstmt.close();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
		}
		
		// 삭제
		
		public int deleteData(int num) {
			
			int result = 0;
			
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				
				sql ="delete board where num=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				
				result = pstmt.executeUpdate();
				
				pstmt.close();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
		}
	}

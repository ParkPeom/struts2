package com.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {

	private Connection conn;
	
	public BoardDAO(Connection conn) {
		this.conn = conn; // ��ü ���� ���ÿ� �ʱ�ȭ = ������ ���� 
		
	}
	// insert �Ҷ� num�� �ִ밪�� ���ϴ� �޼ҵ尡 �ʿ��ϴ�
	// ������ ��ȣ�� num�� ���Ҽ��ִ�.
	// insert �Ҷ� num �� �������� ���ؼ� ���� 
	public int getMaxNum() {
		
		int maxNum = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			// board���� num�� select�ϴµ� ���������� �ִ밪 , null �̸� 0���� 
			sql = "select nvl(max(num),0) from board"; 
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			// �����Ͱ� max�� �ϳ��̹Ƿ� ���ϰ� 
			if(rs.next()) {
				// ���� ������ maxNum�� �޾Ƴ���
				// �÷����� �Ļ��÷� nvl(max(num),0) ���� �����µ�..
				// �̷���쿡�� �÷� �� �Ѱ� �������Ƿ� 1�̴�. 
				// ���� 1 �����ָ� �ȴ�.
				maxNum = rs.getInt(1);
			}
			rs.close();
			pstmt.close();		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return maxNum;
	}
	
	// �Է�
	public int insertData(BoardForm dto) {
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			// ����ٰ� insert �����ָ� �ȴ�.
			// hitcount �⺻���� 0
			// sysdate�� �����־��ָ� ��
			sql = "insert into board (num,name,pwd,email,subject,";
			sql+= "content,ipAddr,hitcount,created) ";
			sql+= " values (?,?,?,?,?,?,?,0,sysdate)";
			// num �ϰ� ipAddr�� ������ָ� ��
			
			pstmt = conn.prepareStatement(sql);
			// 9���߿� ?ǥ 7���� �־��ָ� ��
			
			// ���� �Ѿ�� DTO��  NUM�� �ִ� DTO
			pstmt.setInt(1,dto.getNum());
			pstmt.setString(2,  dto.getName());
			pstmt.setString(3,  dto.getPwd());
			pstmt.setString(4,  dto.getEmail());
			pstmt.setString(5,  dto.getSubject());
			pstmt.setString(6,  dto.getContent());
			pstmt.setString(7 , dto.getIpAddr());
			
			// ȸ�翡�� 20�⵿�� ���ٰ� ���̹�Ƽ�� , ���̹�Ƽ���� ����
			
			result = pstmt.executeUpdate();
			pstmt.close();
		
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	
	// �˻��� ������ ����
	public int getDataCount(String searchKey,String searchValue) {
		int totalCount = 0;	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		
		try {
			searchValue = "%" + searchValue + "%";
			// null ���� 0���� �ؼ� ������ ���ش�
			sql = "select nvl(count(*),0)from board ";
			sql += "where "+ searchKey + " like ?";
			// searchKey  : �ȿ��ִ� ������ 
			//  searchValue = �츮�� ��ġ�ϴ� �ܾ�        
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, searchValue); // null �̵����� ��絥���Ͱ� ����
			
			// searchValue�� �˻��� �ؼ� key���� ��´�.
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				// searchKey�÷� �ϳ��� ����
				totalCount = rs.getInt(1); 
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return totalCount;	
	}
	
	// ��ü������ 
	// Board dto �� ���ִ� list
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
				
			*/ // data.* : �ش絥���� ������ �����Ͷ�
			sql = "select * from (";
			sql+= "select rownum rnum, data.* from (";
			sql+= "select num,name,subject,hitCount,";
			sql+= "to_char(created,'YYYY-MM-DD') created ";
			sql+= "from board where " + searchKey + " like ? ";
			sql+= "order by num desc) data) ";
			sql+= "where rnum>=? and rnum<=?"; // �ȿ��ִ� �Ļ��÷��� �ۿ��� �������� ���
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,searchValue);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				BoardForm dto = new BoardForm();
				// dto �� 5���� �����Ͱ� ����
				// �װ� list �� add�� ����
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
	
	
	// num���� ��ȸ�� �Ѱ��� ������ ( article , update �Ҷ� ���ϰ��� ) 
		// Board dto �� ���ִ� list
		
	public BoardForm getReadData(int num) { 
			
			BoardForm dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;	
			try {
//				�ѻ���� ������ ������
				sql = "select num,name,pwd,email,subject,content,";
				sql +="ipAddr,hitCount,created ";
				sql +="from board where num=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, num);
				
				rs = pstmt.executeQuery();
				
				// �����Ͱ� �ϳ��̹Ƿ� if��
				if(rs.next()) {
					
					dto = new BoardForm();
					// dto �� 5���� �����Ͱ� ����
					// �װ� list �� add�� ����
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
		
		// ��ȸ�� ����(update ������Ʈ)
	
		// num�̶�� ���� ��ȸ���� ������ų���̴�.
		public int updateHitCount(int num) {
			
			int result = 0;
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				// num�� �ش��ϴ� �����Ϳ� +1 �ϰԵ�
				sql = "update board set hitCount=hitCount+1 where num=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				result = pstmt.executeUpdate(); // ����Ǹ� 1 �ȵǸ� 0 
				pstmt.close();
				
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return result;
		}
		
		//����
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
		
		// ����
		
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

package com.goree.api.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.goree.api.Application;
import com.goree.api.domain.Group;
import com.goree.api.domain.Member;
import com.goree.api.service.MemberService;
import com.goree.api.util.DBUnitOperator;
import com.goree.api.util.IDataSetFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes={Application.class})
public class GroupTest {
    @Autowired
    private GroupController groupController;
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private DataSource dataSource;
    
    
    @Before
    public void setUp() {
    	DBUnitOperator.setDataSource(dataSource);
    	IDataSet dataset = IDataSetFactory.fromXml("src/test/resources/testdataset/findGroupsJoined_setup.xml");
    	DBUnitOperator.cleanInsert(dataset);
    }
    
    
    @Test
    public void findGroupsJoined() throws Exception {
        
        Member member = new Member();
        member.setId(1);
        List<Group> joinedGroups = groupController.findGroupsJoined(member);
        Assert.assertTrue(joinedGroups.size() == 1);
        Assert.assertTrue(joinedGroups.get(0).getId() == 1 );
    }
    
    @Test
    public void createGroup() {
        List<Member> memberList = memberService.findMemberAll();
        Member leader = null;
        if(memberList != null && !memberList.isEmpty() ){
            leader = memberList.get(0);
        } else {
            
            Assert.fail("memberList is null or empty");
        }
        
        Group expected = new Group();
        expected.setName("It is Group"+new Date().getTime());
        expected.setDescription("It is description");
        expected.setLeader(leader);
        
        Group resultGroup = groupController.makingGroup(expected);
        
        Assert.assertEquals(expected.getName(), resultGroup.getName());
    }
    
    @Test
    public void findGroupById(){
        Group expected = groupController.findGroupAll().get(0);
        Group result = groupController.findGroupById(expected.getId());
        Assert.assertEquals(expected.getId(), result.getId());
        
    }
    
    @Test
    public void findGroupByName(){
        List<Group> groups = groupController.findGroupAll();
        Group expected = groups.get(0);
        Group result = groupController.findGroupByName(expected.getName());
        
        Assert.assertEquals(expected.getName(), result.getName()); 
        
    }
    
    @Test
    public void findGroupAll() {
        List<Group> groups = groupController.findGroupAll();
        Assert.assertNotNull(groups);
        Assert.assertFalse(groups.isEmpty());
    }
    
}

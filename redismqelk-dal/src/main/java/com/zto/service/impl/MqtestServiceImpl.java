package com.zto.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zto.entity.mqtest;
import com.zto.entity.mqtestExample;
import com.zto.mapper.mqtestMapper;
import com.zto.service.MqtestService;

@Service("mqtestService")
public class MqtestServiceImpl implements MqtestService {

	@Autowired
	mqtestMapper mqtestMapper;

	@Override
	public int countByExample(mqtestExample example) {
		// TODO Auto-generated method stub
		return mqtestMapper.countByExample(example);
	}

	@Override
	public int deleteByExample(mqtestExample example) {
		// TODO Auto-generated method stub
		return mqtestMapper.deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return mqtestMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(mqtest record) {
		// TODO Auto-generated method stub
		return mqtestMapper.insert(record);
	}

	@Override
	public int insertSelective(mqtest record) {
		// TODO Auto-generated method stub
		return mqtestMapper.insertSelective(record);
	}

	@Override
	public List<mqtest> selectByExample(mqtestExample example) {
		// TODO Auto-generated method stub
		return mqtestMapper.selectByExample(example);
	}

	@Override
	public mqtest selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return mqtestMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByExampleSelective(mqtest record, mqtestExample example) {
		// TODO Auto-generated method stub
		return mqtestMapper.updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExample(mqtest record, mqtestExample example) {
		// TODO Auto-generated method stub
		return mqtestMapper.updateByExample(record, example);
	}

	@Override
	public int updateByPrimaryKeySelective(mqtest record) {
		// TODO Auto-generated method stub
		return mqtestMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(mqtest record) {
		// TODO Auto-generated method stub
		return mqtestMapper.updateByPrimaryKey(record);
	}

}

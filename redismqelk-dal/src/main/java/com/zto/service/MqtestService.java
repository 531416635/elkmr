package com.zto.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zto.entity.mqtest;
import com.zto.entity.mqtestExample;

public interface MqtestService {
	int countByExample(mqtestExample example);

	int deleteByExample(mqtestExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(mqtest record);

	int insertSelective(mqtest record);

	List<mqtest> selectByExample(mqtestExample example);

	mqtest selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") mqtest record,
			@Param("example") mqtestExample example);

	int updateByExample(@Param("record") mqtest record,
			@Param("example") mqtestExample example);

	int updateByPrimaryKeySelective(mqtest record);

	int updateByPrimaryKey(mqtest record);
}

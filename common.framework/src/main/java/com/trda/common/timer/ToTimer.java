package com.trda.common.timer;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.trda.permission.service.RoleService;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月5日 上午10:50:21
* 定时任务恢复数据
*/
@Component
public class ToTimer {
	
	@Resource
	RoleService roleService;
	@Scheduled(cron="0/20 * * * * ?")
	public void run(){
		/**
		 * 调用存储过程，重新创建表，插入初始化数据。
		 */
		roleService.initData();
		System.out.println(new Date().getTime());
	}
	
}

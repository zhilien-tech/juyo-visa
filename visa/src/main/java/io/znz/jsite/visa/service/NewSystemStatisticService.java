/**
 * NewSystemStatisticService.java
 * io.znz.jsite.visa.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpDjsEntity;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

/**
 * <p>
 *
 * @author   彭辉
 * @Date	 2017年8月22日 	 
 */
@Service
@Transactional(readOnly = true)
public class NewSystemStatisticService extends NutzBaseService {

	//下拉列表
	public Object compSelectfind(int compType) {
		List<NewComeBabyJpEntity> sqsCompList = Lists.newArrayList();
		List<NewComeBabyJpDjsEntity> disCompList = Lists.newArrayList();

		if (compType == 1) {
			sqsCompList = dbDao.query(NewComeBabyJpEntity.class, null, null);
			NewComeBabyJpEntity entity = new NewComeBabyJpEntity();
			entity.setId(-1L);
			entity.setComFullName("送签社");
			sqsCompList.add(0, entity);
			return sqsCompList;

		}
		if (compType == 2) {
			disCompList = dbDao.query(NewComeBabyJpDjsEntity.class, null, null);
			NewComeBabyJpDjsEntity DjsEntity = new NewComeBabyJpDjsEntity();
			DjsEntity.setId(-1L);
			DjsEntity.setLandcomFullName("地接社");
			disCompList.add(0, DjsEntity);
			return disCompList;
		}
		return new ArrayList();
	}

}

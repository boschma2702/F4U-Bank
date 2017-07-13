package com.bank.repository.systeminfo;

import com.bank.bean.systeminfo.SystemInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemInfoRepository extends CrudRepository<SystemInfo, Integer> {


}

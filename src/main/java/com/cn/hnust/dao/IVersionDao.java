package com.cn.hnust.dao;

import com.cn.hnust.pojo.Version;

public interface IVersionDao {
   public Version queryLastVersion();
   public String dataAccess(String version);
}
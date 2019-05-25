package com.onemena.recordpreloader.entity;

import com.onemena.recordpreloader.util.RelationPreloader;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public interface IdEntity<K extends Serializable> {
    K getId();
    ConcurrentHashMap<RelationPreloader, Object> getPreloadResult();
}

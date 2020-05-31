package com.onemena.recordpreloader.util;

import com.onemena.recordpreloader.entity.IdEntity;
import com.onemena.recordpreloader.util.RelationPreloader.BelongsToPreloader;
import com.onemena.recordpreloader.util.RelationPreloader.HasManyPreloader;
import com.onemena.recordpreloader.util.RelationPreloader.HasOnePreloader;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * The type Record list preloader.
 *
 * @param <T> the type parameter
 * @param <H> the type parameter
 */
public class RecordListPreloader<T extends IdEntity<H>, H extends Serializable> {

    private List<T> records;

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    /**
     * Instantiates a new Record list preloader.
     *
     * @param records the records
     */
    public RecordListPreloader(List<T> records) {
        this.records = records;
    }

    /**
     * Has many record list preloader.
     *
     * @param <K>                      the type parameter
     * @param preloader                the preloader
     * @param relationGetGroupNameFunc the relation get group name func
     * @param getTargetRecordsFunc     the get target records func
     * @return the record list preloader
     */
    private <K, P> RecordListPreloader<T, H> hasMany(HasManyPreloader preloader, Function<K, H> relationGetGroupNameFunc, BiFunction<List<H>, P, List<K>> getTargetRecordsFunc, P params) {
        List<H> recordIds = this.records.stream().map(IdEntity::getPreloadPrimaryId).collect(toList());
        recordIds = recordIds.stream().filter(i->(i!=null)&&!i.equals(0L)).collect(toList());
        List<K> targetRecords = getTargetRecordsFunc.apply(recordIds, params);
        Map<H, List<K>> groupedTR = targetRecords.stream().collect(groupingBy(relationGetGroupNameFunc));
        for (T record : records) {
            List<K> list = groupedTR.get(record.getPreloadPrimaryId());
            Object target = (list == null) ?  RelationPreloader.NIL : list;
            record.getPreloadResult().put(preloader, target );
        }
        return this;
    }

    /**
     * Has one record list preloader.
     *
     * @param <K>                      the type parameter
     * @param preloader                the preloader
     * @param relationGetGroupNameFunc the relation get group name func
     * @param getTargetRecordsFunc     the get target records func
     * @return the record list preloader
     */
    private <K,P> RecordListPreloader<T, H> hasOneWithFunc(HasOnePreloader<T,H,K,P> preloader, Function<K, H> relationGetGroupNameFunc, Function<List<H>, List<K>> getTargetRecordsFunc) {
        List<H> recordIds = this.records.stream().map(IdEntity::getPreloadPrimaryId).collect(toList());
        recordIds = recordIds.stream().filter(i->(i!=null)&&!i.equals(0L)).collect(toList());
        List<K> targetRecords = getTargetRecordsFunc.apply(recordIds);
        Map<H, List<K>> groupedTR = targetRecords.stream().collect(groupingBy(relationGetGroupNameFunc));
        for (T record : this.records) {
            List<K> list = groupedTR.get(record.getPreloadPrimaryId());
            Object target;
            if (null == list) {
                target = RelationPreloader.NIL;
            } else {
                target = list.get(0);
                if (null == target) {
                    target = RelationPreloader.NIL;
                }
            }
            record.getPreloadResult().put(preloader, target);
        }
        return this;
    }

    static String MISS_GET_TARGET_FUNC = "miss getTargetRecordsFunc";

    private <K,P> RecordListPreloader<T, H> hasOneWithParam(HasOnePreloader<T,H,K,P> preloader, Function<K, H> relationGetGroupNameFunc, P param) {

        List<H> recordIds = this.records.stream().map(IdEntity::getPreloadPrimaryId).collect(toList());
        recordIds = recordIds.stream().filter(i->(i!=null)&&!i.equals(0L)).collect(toList());
        List<K> targetRecords;
        if (preloader.getTargetRecordsFunc() != null) {
            targetRecords = preloader.getTargetRecordsFunc().apply(recordIds, param);
        } else {
            throw new RuntimeException(MISS_GET_TARGET_FUNC);
        }

        Map<H, List<K>> groupedTR = targetRecords.stream().collect(groupingBy(relationGetGroupNameFunc));
        for (T record : this.records) {
            List<K> list = groupedTR.get(record.getPreloadPrimaryId());
            Object target;
            if (null == list) {
                target = RelationPreloader.NIL;
            } else {
                target = list.get(0);
                if (null == target) {
                    target = RelationPreloader.NIL;
                }
            }
            record.getPreloadResult().put(preloader, target);
        }
        return this;
    }



    /**
     * Belongs to record list preloader.
     *
     * @param <K>                  the type parameter
     * @param preloader            the preloader
     * @param getPrimaryIdFunc     the get primary id func
     * @param getTargetRecordsFunc the get target records func
     * @return the record list preloader
     */

    private <K extends IdEntity<F>, F extends Serializable, P> RecordListPreloader<T, H> belongsToWithFunc(BelongsToPreloader<T, F, K, P> preloader, Function<T, F> getPrimaryIdFunc, Function<List<F>, List<K>> getTargetRecordsFunc) {
        List<F> recordIds = this.records.stream().map(getPrimaryIdFunc).collect(toList());
        recordIds = recordIds.stream().filter(i->(i!=null)&&!i.equals(0L)).collect(toList());
        List<K> targetRecords = getTargetRecordsFunc.apply(recordIds);
        Map<F, List<K>> groupedTR = targetRecords.stream().collect(groupingBy(IdEntity::getPreloadPrimaryId));
        for (T record : this.records) {
            List<K> list = groupedTR.get(getPrimaryIdFunc.apply(record));
            Object target;
            if (null == list) {
                target = RelationPreloader.NIL;
            } else {
                target = list.get(0);
                if (null == target) {
                    target = RelationPreloader.NIL;
                }
            }
            record.getPreloadResult().put(preloader, target);
        }
        return this;
    }


    private <K extends IdEntity<F>, F extends Serializable, P> RecordListPreloader<T, H> belongsToWithParam(BelongsToPreloader<T, F, K, P> preloader, Function<T, F> getPrimaryIdFunc, P param) {
        List<F> recordIds = this.records.stream().map(getPrimaryIdFunc).collect(toList());
        recordIds = recordIds.stream().filter(i->(i!=null)&&!i.equals(0L)).collect(toList());
        List<K> targetRecords;
        if (preloader.getTargetRecordsFunc() != null) {
            targetRecords = preloader.getTargetRecordsFunc().apply(recordIds, param);
        } else {
            throw new RuntimeException(MISS_GET_TARGET_FUNC);
        }
        Map<F, List<K>> groupedTR = targetRecords.stream().collect(groupingBy(IdEntity::getPreloadPrimaryId));
        for (T record : this.records) {
            List<K> list = groupedTR.get(getPrimaryIdFunc.apply(record));
            Object target;
            if (null == list) {
                target = RelationPreloader.NIL;
            } else {
                target = list.get(0);
                if (null == target) {
                    target = RelationPreloader.NIL;
                }
            }
            record.getPreloadResult().put(preloader, target);
        }
        return this;
    }

    /**
     * Preload record list preloader.
     *
     * @param <K>                  the type parameter
     * @param preloader            the preloader
     * @param getTargetRecordsFunc the get target records func
     * @return the record list preloader
     */
    public <K, P> RecordListPreloader<T, H> preloadWithFunc(HasOnePreloader<T,H,K,P> preloader, Function<List<H>, List<K>> getTargetRecordsFunc) {
        this.hasOneWithFunc(preloader, preloader.getRelationGetGroupNameFunc(), getTargetRecordsFunc);
        return this;
    }

    public <K, P> RecordListPreloader<T, H> preload(HasOnePreloader<T,H,K,P> preloader, P params) {
        this.hasOneWithParam(preloader, preloader.getRelationGetGroupNameFunc(), params);
        return this;
    }

    public <K, P> RecordListPreloader<T, H> preload(HasOnePreloader<T,H,K,P> preloader) {
        this.hasOneWithParam(preloader, preloader.getRelationGetGroupNameFunc(), null);
        return this;
    }

    public <K extends IdEntity<F>, F extends Serializable, P> List<K> preloadWithFuncAndGet(HasOnePreloader<T,H,K,P> preloader, Function<List<H>, List<K>> getTargetRecordsFunc) {
        this.preloadWithFunc(preloader, getTargetRecordsFunc);
        return this.records.stream().map(preloader::getLoaded).collect(toList());
    }

    public <K extends IdEntity<F>, F extends Serializable, P> List<K> preloadAndGet(HasOnePreloader<T,H,K,P> preloader, P params) {
        this.preload(preloader, params);
        return this.records.stream().map(preloader::getLoaded).collect(toList());
    }

    public <K extends IdEntity<F>, F extends Serializable, P> List<K> preloadAndGet(HasOnePreloader<T,H,K,P> preloader) {
        this.preload(preloader);
        return this.records.stream().map(preloader::getLoaded).collect(toList());
    }

    /**
     * Preload record list preloader.
     *
     * @param <K>                  the type parameter
     * @param preloader            the preloader
     * @param getTargetRecordsFunc the get target records func
     * @return the record list preloader
     */
    public <K extends IdEntity<F>, F extends Serializable, P> RecordListPreloader<T, H> preloadWithFunc(BelongsToPreloader<T, F, K, P> preloader, Function<List<F>, List<K>> getTargetRecordsFunc) {
        this.belongsToWithFunc(preloader, preloader.getGetPrimaryIdFunc(), getTargetRecordsFunc);
        return this;
    }

    public <K extends IdEntity<F>, F extends Serializable, P> RecordListPreloader<T, H> preload(BelongsToPreloader<T, F, K, P> preloader, P params) {
        this.belongsToWithParam(preloader, preloader.getGetPrimaryIdFunc(), params);
        return this;
    }

    public <K extends IdEntity<F>, F extends Serializable, P> RecordListPreloader<T, H> preload(BelongsToPreloader<T, F, K, P> preloader) {
        return this.belongsToWithParam(preloader, preloader.getGetPrimaryIdFunc(), null);
    }

    public <K extends IdEntity<F>, F extends Serializable, P> List<K> preloadAndGet(BelongsToPreloader<T, F, K, P> preloader, Function<List<F>, List<K>> getTargetRecordsFunc) {
        this.preloadWithFunc(preloader, getTargetRecordsFunc);
        return this.records.stream().map(preloader::getLoaded).collect(toList());
    }

    public <K extends IdEntity<F>, F extends Serializable, P> List<K> preloadAndGet(BelongsToPreloader<T, F, K, P> preloader, P params) {
        this.preload(preloader, params);
        return this.records.stream().map(preloader::getLoaded).collect(toList());
    }


    public <K extends IdEntity<F>, F extends Serializable, P> List<K> preloadAndGet(BelongsToPreloader<T, F, K, P> preloader) {
        this.preload(preloader);
        return this.records.stream().map(preloader::getLoaded).collect(toList());
    }

    @SuppressWarnings("unchecked")
    public RecordListPreloader<T, H> preload(HasManyPreloader preloader) {
        this.hasMany(preloader, preloader.getRelationGetGroupNameFunc(), preloader.getTargetRecordsFunc(), null);
        return this;
    }

    /**
     * 关联对象已经加载过，不需要预加载时，可直接设置为关联对象.
     *
     * @param <K>       the type parameter
     * @param preloader the preloader
     * @param target    the target
     * @return the record list preloader
     */
    public <K> RecordListPreloader<T, H> fakePreload(RelationPreloader preloader, K target) {
        Object obj = target;
        if (null == obj) {
            obj = RelationPreloader.NIL;
        }
        for (T record : this.records) {
            record.getPreloadResult().put(preloader, obj);
        }
        return this;
    }

    public <K> RecordListPreloader<T, H> fakePreload(RelationPreloader preloader) {
        return this.fakePreload(preloader, null);
    }
}

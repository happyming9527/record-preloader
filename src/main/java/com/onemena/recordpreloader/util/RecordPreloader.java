package com.onemena.recordpreloader.util;

import com.onemena.recordpreloader.entity.IdEntity;
import com.onemena.recordpreloader.util.RelationPreloader.HasManyPreloader;
import com.onemena.recordpreloader.util.RelationPreloader.HasOnePreloader;
import com.onemena.recordpreloader.util.RelationPreloader.BelongsToPreloader;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * The type Record preloader.
 *
 * @param <T> the type parameter
 * @param <F> the type parameter
 */
public class RecordPreloader<T extends IdEntity<F>, F extends Serializable> {

    private T record;

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }

    /**
     * Instantiates a new Record preloader.
     *
     * @param record the record
     */
    public RecordPreloader(T record) {
        this.record = record;
    }

    /**
     * Has many record preloader.
     *
     * @param <K>                 the type parameter
     * @param preloader           the preloader
     * @param getTargetRecordFunc the get target record func
     * @return the record preloader
     */
    public <K> RecordPreloader<T, F> hasMany(HasManyPreloader preloader, Function<F, List<K>> getTargetRecordFunc) {
        F id = this.record.getId();
        if (id.equals(0L)) {
            return this;
        }
        List<K> targets = getTargetRecordFunc.apply(id);
        this.record.getPreloadResult().put(preloader, targets==null ? RelationPreloader.NIL : targets);
        return this;
    }

    /**
     * Has one record preloader.
     *
     * @param <K>                 the type parameter
     * @param preloader           the preloader
     * @param getTargetRecordFunc the get target record func
     * @return the record preloader
     */
    public <K> RecordPreloader<T, F> hasOne(HasOnePreloader preloader, Function<F, K> getTargetRecordFunc) {
        F id = this.record.getId();
        if (id.equals(0L)) {
            return this;
        }
        K target = getTargetRecordFunc.apply(id);
        try {
            this.record.getPreloadResult().put(preloader, target==null ? RelationPreloader.NIL : target);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return this;
    }


    /**
     * Belongs to record preloader.
     *
     * @param <K>                 the type parameter
     * @param preloader           the preloader
     * @param getPrimaryIdFunc    the get primary id func
     * @param getTargetRecordFunc the get target record func
     * @return the record preloader
     */
    public <K extends IdEntity<F>> RecordPreloader<T, F> belongsTo(BelongsToPreloader preloader, Function<T, F> getPrimaryIdFunc, Function<F, K> getTargetRecordFunc) {
        F id = getPrimaryIdFunc.apply(this.record);
        if (id.equals(0L)) {
            return this;
        }
        K target = getTargetRecordFunc.apply(id);
        this.record.getPreloadResult().put(preloader, target==null ? RelationPreloader.NIL : target);
        return this;
    }

    /**
     * Preload record preloader.
     *
     * @param <K>                 the type parameter
     * @param preloader           the preloader
     * @param getTargetRecordFunc the get target record func
     * @return the record preloader
     */
    public <K extends IdEntity<F>> RecordPreloader<T, F> preload(HasOnePreloader preloader, Function<F, K> getTargetRecordFunc) {
        this.hasOne(preloader, getTargetRecordFunc);
        return this;
    }

    /**
     * Preload record preloader.
     *
     * @param <K>                 the type parameter
     * @param preloader           the preloader
     * @param getTargetRecordFunc the get target record func
     * @return the record preloader
     */
    @SuppressWarnings("unchecked")
    public <K extends IdEntity<F>> RecordPreloader<T, F> preload(BelongsToPreloader preloader, Function<F, K> getTargetRecordFunc) {
        this.belongsTo(preloader, preloader.getGetPrimaryIdFunc(), getTargetRecordFunc);
        return this;
    }

    /**
     * Preload record preloader.
     *
     * @param <K>                 the type parameter
     * @param preloader           the preloader
     * @param getTargetRecordFunc the get target record func
     * @return the record preloader
     */
    public <K extends IdEntity<F>> RecordPreloader<T, F> preload(HasManyPreloader preloader, Function<F, List<K>> getTargetRecordFunc) {
        this.hasMany(preloader, getTargetRecordFunc);
        return this;
    }

    /**
     * 关联对象已经加载过，不需要预加载时，可直接设置为关联对象.
     *
     * @param <K>       the type parameter
     * @param preloader the preloader
     * @param target    the target
     * @return the record preloader
     */
    public <K> RecordPreloader<T, F> fakePreload(RelationPreloader preloader, K target) {
        Object obj = target;
        if (null == obj) {
            obj = RelationPreloader.NIL;
        }
        this.record.getPreloadResult().put(preloader, obj);
        return this;
    }

}

package com.onemena.recordpreloader.util;

import com.onemena.recordpreloader.entity.IdEntity;
import com.onemena.recordpreloader.util.RelationPreloader.HasManyPreloader;
import com.onemena.recordpreloader.util.RelationPreloader.HasOnePreloader;
import com.onemena.recordpreloader.util.RelationPreloader.BelongsToPreloader;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class RecordPreloader<T extends IdEntity<F>, F extends Serializable> {

    private T record;

    public RecordPreloader(T record) {
        this.record = record;
    }

    public <K> RecordPreloader<T, F> hasMany(HasManyPreloader preloader, Function<F, List<K>> getTargetRecordFunc) {
        F id = this.record.getId();
        if (id.equals(0L)) {
            return this;
        }
        List<K> targets = getTargetRecordFunc.apply(id);
        this.record.getPreloadResult().put(preloader, targets==null ? RelationPreloader.NIL : targets);
        return this;
    }

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


    public <K extends IdEntity<F>> RecordPreloader<T, F> belongsTo(BelongsToPreloader preloader, Function<T, F> getPrimaryIdFunc, Function<F, K> getTargetRecordFunc) {
        F id = getPrimaryIdFunc.apply(this.record);
        if (id.equals(0L)) {
            return this;
        }
        K target = getTargetRecordFunc.apply(id);
        this.record.getPreloadResult().put(preloader, target==null ? RelationPreloader.NIL : target);
        return this;
    }

    public <K extends IdEntity<F>> RecordPreloader<T, F> preload(HasOnePreloader preloader, Function<F, K> getTargetRecordFunc) {
        this.hasOne(preloader, getTargetRecordFunc);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <K extends IdEntity<F>> RecordPreloader<T, F> preload(BelongsToPreloader preloader, Function<F, K> getTargetRecordFunc) {
        this.belongsTo(preloader, preloader.getGetPrimaryIdFunc(), getTargetRecordFunc);
        return this;
    }

    public <K extends IdEntity<F>> RecordPreloader<T, F> preload(HasManyPreloader preloader, Function<F, List<K>> getTargetRecordFunc) {
        this.hasMany(preloader, getTargetRecordFunc);
        return this;
    }

    public RecordPreloader<T, F> fakePreload(RelationPreloader preloader, T target) {
        Object obj = target;
        if (null == obj) {
            obj = RelationPreloader.NIL;
        }
        this.record.getPreloadResult().put(preloader, obj);
        return this;
    }

}

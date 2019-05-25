package com.onemena.recordpreloader.util;

import com.onemena.recordpreloader.entity.IdEntity;
import com.onemena.recordpreloader.util.RelationPreloader.HasManyPreloader;
import com.onemena.recordpreloader.util.RelationPreloader.HasOnePreloader;
import com.onemena.recordpreloader.util.RelationPreloader.BelongsToPreloader;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Setter
@Getter
public class RecordListPreloader<T extends IdEntity<F>, F extends Serializable> {

    private List<T> records;

    public RecordListPreloader(List<T> records) {
        this.records = records;
    }

    public <K> RecordListPreloader<T, F> hasMany(HasManyPreloader preloader, Function<K, F> relationGetGroupNameFunc, Function<List<F>, List<K>> getTargetRecordsFunc) {
        List<F> recordIds = this.records.stream().map(IdEntity::getId).collect(toList());
        recordIds = recordIds.stream().filter(i->(i!=null)&&!i.equals(0L)).collect(toList());
        List<K> targetRecords = getTargetRecordsFunc.apply(recordIds);
        Map<F, List<K>> groupedTR = targetRecords.stream().collect(groupingBy(target -> relationGetGroupNameFunc.apply(target)));
        for (T record : records) {
            List<K> list = groupedTR.get(record.getId());
            Object target = (list == null) ?  RelationPreloader.NIL : list;
            record.getPreloadResult().put(preloader, target );
        }
        return this;
    }

    public <K> RecordListPreloader<T, F> hasOne(HasOnePreloader preloader, Function<K, F> relationGetGroupNameFunc, Function<List<F>, List<K>> getTargetRecordsFunc) {
        List<F> recordIds = this.records.stream().map(IdEntity::getId).collect(toList());
        recordIds = recordIds.stream().filter(i->(i!=null)&&!i.equals(0L)).collect(toList());
        List<K> targetRecords = getTargetRecordsFunc.apply(recordIds);
        Map<F, List<K>> groupedTR = targetRecords.stream().collect(groupingBy(target -> relationGetGroupNameFunc.apply(target)));
        for (T record : this.records) {
            List<K> list = groupedTR.get(record.getId());
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

    public <K extends IdEntity<F>> RecordListPreloader<T, F> belongsTo(BelongsToPreloader preloader, Function<T, F> getPrimaryIdFunc, Function<List<F>, List<K>> getTargetRecordsFunc) {
        List<F> recordIds = this.records.stream().map(getPrimaryIdFunc).collect(toList());
        recordIds = recordIds.stream().filter(i->(i!=null)&&!i.equals(0L)).collect(toList());
        List<K> targetRecords = getTargetRecordsFunc.apply(recordIds);
        Map<F, List<K>> groupedTR = targetRecords.stream().collect(groupingBy(target->target.getId()));
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

    @SuppressWarnings("unchecked")
    public <K extends IdEntity<F>> RecordListPreloader<T, F> preload(HasOnePreloader preloader, Function<List<F>, List<K>> getTargetRecordsFunc) {
        this.hasOne(preloader, preloader.getRelationGetGroupNameFunc(), getTargetRecordsFunc);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <K extends IdEntity<F>> RecordListPreloader<T, F> preload(BelongsToPreloader preloader, Function<List<F>, List<K>> getTargetRecordsFunc) {
        this.belongsTo(preloader, preloader.getGetPrimaryIdFunc(), getTargetRecordsFunc);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <K extends IdEntity<F>> RecordListPreloader<T, F> preload(HasManyPreloader preloader, Function<List<F>, List<K>> getTargetRecordsFunc) {
        this.hasMany(preloader, preloader.getRelationGetGroupNameFunc(), getTargetRecordsFunc);
        return this;
    }

    public RecordListPreloader<T, F> fakePreload(RelationPreloader preloader, T target) {
        Object obj = target;
        if (null == obj) {
            obj = RelationPreloader.NIL;
        }
        for (T record : this.records) {
            record.getPreloadResult().put(preloader, obj);
        }
        return this;
    }
}

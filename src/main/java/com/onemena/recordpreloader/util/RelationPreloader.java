package com.onemena.recordpreloader.util;

import com.onemena.recordpreloader.entity.IdEntity;
import com.onemena.recordpreloader.exception.RecordNotPreloadException;
import lombok.Getter;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public class RelationPreloader {
    static final String NIL = ":nil";

    @Getter
    @SuppressWarnings("unchecked")
    public static class HasOnePreloader<T extends IdEntity<F>, K, F extends Serializable> extends RelationPreloader {
        private Function<K, F> relationGetGroupNameFunc;

        public HasOnePreloader(Function<K, F> relationGetGroupNameFunc) {
            this.relationGetGroupNameFunc = relationGetGroupNameFunc;
        }

        @Nullable
        public K get(T t) {
            Object obj = t.getPreloadResult().get(this);
            if (RelationPreloader.NIL == obj) {
                return null;
            } else if (null == obj) {
                throw new RecordNotPreloadException();
            }
            return (K) obj;
        }
    }

    @Getter
    @SuppressWarnings("unchecked")
    public static class BelongsToPreloader<T extends IdEntity<F>, K, F extends Serializable> extends RelationPreloader {
        private Function<T, F> getPrimaryIdFunc;

        public BelongsToPreloader(Function<T, F> getPrimaryIdFunc) {
            this.getPrimaryIdFunc = getPrimaryIdFunc;
        }

        @Nullable
        public K get(T t) {
            Object obj = t.getPreloadResult().get(this);
            if (RelationPreloader.NIL == obj) {
                return null;
            } else if (null == obj) {
                throw new RecordNotPreloadException();
            }
            return (K) obj;
        }
    }

    @Getter
    @SuppressWarnings("unchecked")
    public static class HasManyPreloader<T extends IdEntity<F>, K, F extends Serializable> extends RelationPreloader {
        private Function<K, F> relationGetGroupNameFunc;

        public HasManyPreloader(Function<K, F> relationGetGroupNameFunc) {
            this.relationGetGroupNameFunc = relationGetGroupNameFunc;
        }

        @Nullable
        public List<K> get(T t) {
            Object obj = t.getPreloadResult().get(this);
            if (RelationPreloader.NIL == obj) {
                return null;
            } else if (null == obj) {
                throw new RecordNotPreloadException();
            }
            return (List<K>) obj;
        }
    }
}

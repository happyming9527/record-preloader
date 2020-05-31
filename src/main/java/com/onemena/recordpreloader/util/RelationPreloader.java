package com.onemena.recordpreloader.util;

import com.onemena.recordpreloader.entity.IdEntity;
import com.onemena.recordpreloader.exception.RecordNotPreloadException;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * The type Relation preloader.
 */
public class RelationPreloader {
    /**
     * The Nil.
     */
    static final String NIL = ":nil";

    /**
     * 一般target对象中有current类的主键.
     *
     * @param <T> current类的类型。
     * @param <K> target对象的类型
     * @param <H> preload结果用来分组的key，为current对象的主键
     */
    @SuppressWarnings("unchecked")
    public static class HasOnePreloader<T extends IdEntity<H>, H extends Serializable, K, P> extends RelationPreloader {
        public Function<K, H> getRelationGetGroupNameFunc() {
            return relationGetGroupNameFunc;
        }

        public BiFunction<List<H>, P, List<K>> getTargetRecordsFunc() {
            return targetRecordsFunc;
        }

        private Function<K, H> relationGetGroupNameFunc;

        private BiFunction<List<H>, P, List<K>> targetRecordsFunc;

        /**
         * Instantiates a new Has one preloader.
         *
         * @param relationGetGroupNameFunc 用来将获取到的记录，分组， 组名为F（F一般设置为id）。
         */
        public HasOnePreloader(Function<K, H> relationGetGroupNameFunc) {
            this.relationGetGroupNameFunc = relationGetGroupNameFunc;
        }

        public HasOnePreloader(Function<K, H> relationGetGroupNameFunc, BiFunction<List<H>, P, List<K>> targetRecordsFunc) {
            this.relationGetGroupNameFunc = relationGetGroupNameFunc;
            this.targetRecordsFunc = targetRecordsFunc;
        }

        /**
         * 获取预加载结果，如果没有预加载过，会返回null.
         *
         * @param t the t
         * @return the k
         */
        @Nullable
        public K get(T t) {
            Object obj = t.getPreloadResult().get(this);
            if (RelationPreloader.NIL == obj) {
                return null;
            }
            return (K) obj;
        }

        /**
         * 获取预加载结果，如果没有预加载过，会直接抛出错误.
         *
         * @param t the t
         * @return the loaded
         */
        @Nullable
        public K getLoaded(T t) {
            Object obj = t.getPreloadResult().get(this);
            if (RelationPreloader.NIL == obj) {
                return null;
            } else if (null == obj) {
                throw new RecordNotPreloadException();
            }
            return (K) obj;
        }
    }

    /**
     * The type Belongs to preloader.
     *
     * @param <T> the type parameter
     * @param <K> the type parameter
     * @param <F> the type parameter
     */
    @SuppressWarnings("unchecked")
    public static class BelongsToPreloader<T extends IdEntity, F extends Serializable, K, P> extends RelationPreloader {

        public Function<T, F> getGetPrimaryIdFunc() {
            return getPrimaryIdFunc;
        }

        public BiFunction<List<F>, P, List<K>> getTargetRecordsFunc() {
            return targetRecordsFunc;
        }

        private Function<T, F> getPrimaryIdFunc;

        private BiFunction<List<F>, P, List<K>> targetRecordsFunc;

        /**
         * Instantiates a new Belongs to preloader.
         *
         * @param getPrimaryIdFunc the get primary id func
         */
        public BelongsToPreloader(Function<T, F> getPrimaryIdFunc) {
            this.getPrimaryIdFunc = getPrimaryIdFunc;
        }

        public BelongsToPreloader(Function<T, F> getPrimaryIdFunc, BiFunction<List<F>, P, List<K>> targetRecordsFunc) {
            this.getPrimaryIdFunc = getPrimaryIdFunc;
            this.targetRecordsFunc = targetRecordsFunc;
        }


        /**
         * 获取预加载结果，如果没有预加载过，会返回null.
         *
         * @param t the t
         * @return the k
         */
        @Nullable
        public K get(T t) {
            Object obj = t.getPreloadResult().get(this);
            if (RelationPreloader.NIL == obj) {
                return null;
            }
            return (K) obj;
        }

        /**
         * 获取预加载结果，如果没有预加载过，会直接抛出错误.
         *
         * @param t the t
         * @return the loaded
         */
        @Nullable
        public K getLoaded(T t) {
            Object obj = t.getPreloadResult().get(this);
            if (RelationPreloader.NIL == obj) {
                return null;
            } else if (null == obj) {
                throw new RecordNotPreloadException();
            }
            return (K) obj;
        }
    }

    @SuppressWarnings("unchecked")
    public static class HasManyPreloader<T extends IdEntity<H>, H extends Serializable, K, P> extends RelationPreloader {
        public Function<K, H> getRelationGetGroupNameFunc() {
            return relationGetGroupNameFunc;
        }

        public BiFunction<List<H>, P, List<K>> getTargetRecordsFunc() {
            return targetRecordsFunc;
        }

        private Function<K, H> relationGetGroupNameFunc;

        private BiFunction<List<H>, P, List<K>> targetRecordsFunc;

        /**
         * Instantiates a new Has many preloader.
         *
         * @param relationGetGroupNameFunc the relation get group name func
         */
        public HasManyPreloader(Function<K, H> relationGetGroupNameFunc, BiFunction<List<H>, P, List<K>> targetRecordsFunc) {
            this.relationGetGroupNameFunc = relationGetGroupNameFunc;
            this.targetRecordsFunc = targetRecordsFunc;
        }

        /**
         * 获取预加载结果，如果没有预加载过，会返回null.
         *
         * @param t the t
         * @return the list
         */
        @Nullable
        public List<K> get(T t) {
            Object obj = t.getPreloadResult().get(this);
            if (RelationPreloader.NIL == obj) {
                return null;
            }
            return (List<K>) obj;
        }

        /**
         * 获取预加载结果，如果没有预加载过，会直接抛出错误.
         *
         * @param t the t
         * @return the loaded
         */
        @Nullable
        public List<K> getLoaded(T t) {
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

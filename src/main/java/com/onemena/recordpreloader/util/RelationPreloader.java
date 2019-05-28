package com.onemena.recordpreloader.util;

import com.onemena.recordpreloader.entity.IdEntity;
import com.onemena.recordpreloader.exception.RecordNotPreloadException;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
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
     * The type Has one preloader.
     *
     * @param <T> the type parameter
     * @param <K> the type parameter
     * @param <F> the type parameter
     */
    @SuppressWarnings("unchecked")
    public static class HasOnePreloader<T extends IdEntity<F>, K, F extends Serializable> extends RelationPreloader {
        private Function<K, F> relationGetGroupNameFunc;

        public Function<K, F> getRelationGetGroupNameFunc() {
            return relationGetGroupNameFunc;
        }

        /**
         * Instantiates a new Has one preloader.
         *
         * @param relationGetGroupNameFunc the relation get group name func
         */
        public HasOnePreloader(Function<K, F> relationGetGroupNameFunc) {
            this.relationGetGroupNameFunc = relationGetGroupNameFunc;
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
    public static class BelongsToPreloader<T extends IdEntity<F>, K, F extends Serializable> extends RelationPreloader {
        private Function<T, F> getPrimaryIdFunc;

        public Function<T, F> getGetPrimaryIdFunc() {
            return getPrimaryIdFunc;
        }

        /**
         * Instantiates a new Belongs to preloader.
         *
         * @param getPrimaryIdFunc the get primary id func
         */
        public BelongsToPreloader(Function<T, F> getPrimaryIdFunc) {
            this.getPrimaryIdFunc = getPrimaryIdFunc;
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
     * The type Has many preloader.
     *
     * @param <T> the type parameter
     * @param <K> the type parameter
     * @param <F> the type parameter
     */
    @SuppressWarnings("unchecked")
    public static class HasManyPreloader<T extends IdEntity<F>, K, F extends Serializable> extends RelationPreloader {
        private Function<K, F> relationGetGroupNameFunc;

        public Function<K, F> getRelationGetGroupNameFunc() {
            return relationGetGroupNameFunc;
        }

        /**
         * Instantiates a new Has many preloader.
         *
         * @param relationGetGroupNameFunc the relation get group name func
         */
        public HasManyPreloader(Function<K, F> relationGetGroupNameFunc) {
            this.relationGetGroupNameFunc = relationGetGroupNameFunc;
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

package cuit.pymjl.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 21:00
 **/
public class ProtostuffUtils {
    /**
     * 避免每次序列化都重新申请Buffer空间
     * 这个字段表示，申请一个内存空间用户缓存，LinkedBuffer.DEFAULT_BUFFER_SIZE表示申请了默认大小的空间512个字节，
     * 我们也可以使用MIN_BUFFER_SIZE，表示256个字节。
     */
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    /**
     * 缓存Schema
     * 这个字段表示缓存的Schema。那这个Schema是什么呢？就是一个组织结构，就好比是数据库中的表、视图等等这样的组织机构，
     * 在这里表示的就是序列化对象的结构。
     */
    private static final Map<Class<?>, Schema<?>> SCHEMA_CACHE = new ConcurrentHashMap<>();

    /**
     * 序列化方法，把指定对象序列化成字节数组
     *
     * @param obj 对象
     * @return byte[]
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        byte[] data;
        try {
            data = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }
        return data;
    }

    /**
     * 反序列化方法，将字节数组反序列化成指定Class类型
     *
     * @param data  字节数组
     * @param clazz 字节码
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) SCHEMA_CACHE.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            if (schema == null) {
                SCHEMA_CACHE.put(clazz, schema);
            }
        }
        return schema;
    }
}

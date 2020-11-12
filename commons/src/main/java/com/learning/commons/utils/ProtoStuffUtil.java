package com.learning.commons.utils;

import com.learning.commons.exception.RuntimeServerException;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * ProtoStuffUtil 工具类，做对象序列化使用
 *
 * @author Wang Xu
 * @date 2020/10/20
 */
public class ProtoStuffUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoStuffUtil.class);
    private static final int LINKED_BUFFER_SIZE = 1024 * 1024;

    /**
     * 序列化对象
     * @param obj 待序列化的对象
     * @return 序列化后的byte[]
     */
    public static <T> byte[] serialize(T obj) {
        if (Objects.isNull(obj)) {
            throw new RuntimeServerException("serialize obj not is null!");
        }
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LINKED_BUFFER_SIZE);
        byte[] protoStuff = null;
        try {
            protoStuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            LOGGER.error("序列化[{}]对象[{}]发生异常! 异常信息为：{}", obj.getClass(), obj, e.toString(), e);
            throw new RuntimeServerException("序列化(" + obj.getClass() + ")对象(" + obj + ")发生异常!");
        } finally {
            buffer.clear();
        }
        return protoStuff;
    }

    /**
     * 反序列化对象
     * @param arrayOfByte 待反序列化的Byte数组
     * @param targetClass 反序列化后要得到的对象class
     * @return 反序列化后的实例对象
     */
    public static <T> T deserialize(byte[] arrayOfByte, Class<T> targetClass) {
        if (Objects.isNull(arrayOfByte) || arrayOfByte.length == 0) {
            throw new RuntimeServerException("deserialize error, and Byte Array is null!");
        }
        T instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ie) {
            LOGGER.error("deserialize create obj error about targetClass!, msg:{}", ie.toString(), ie);
            throw new RuntimeServerException("deserialize create obj error about targetClass!", ie);
        }
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        ProtostuffIOUtil.mergeFrom(arrayOfByte, instance, schema);
        return instance;
    }

    /**
     * 序列化列表
     * @param objList 待序列化的对象集合
     * @return 序列化后的byte[]
     */
    public static <T> byte[] serializeList(List<T> objList) {
        if (Objects.isNull(objList) || objList.isEmpty()) {
            throw new RuntimeServerException("serialize obj_list not is null!");
        }
        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) RuntimeSchema.getSchema(objList.get(0).getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LINKED_BUFFER_SIZE);
        byte[] protoStuff = null;
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protoStuff = bos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("deserialize obj_list error, msg:{}", e.toString(), e);
            throw new RuntimeServerException("序列化对象列表(" + objList + ")发生异常!", e);
        } finally {
            buffer.clear();
        }
        return protoStuff;
    }

    /**
     * 反序列化列表
     * @param arrayOfByte 待反序列化的Byte数组
     * @param targetClass 反序列化后要得到的对象class
     * @return 反序列化后的实例对象集合
     */
    public static <T> List<T> deserializeList(byte[] arrayOfByte, Class<T> targetClass) {
        if (Objects.isNull(arrayOfByte) || arrayOfByte.length == 0) {
            throw new RuntimeServerException("deserialize error, and Byte Array is null!");
        }
        Schema<T> schema = RuntimeSchema.getSchema(targetClass);
        List<T> collection = null;
        try {
            collection = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(arrayOfByte), schema);
        } catch (IOException e) {
            LOGGER.error("deserialize obj_list error!, msg:{}", e.toString(), e);
            throw new RuntimeServerException("反序列化对象列表发生异常!", e);
        }
        return collection;
    }
}
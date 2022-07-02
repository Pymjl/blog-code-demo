package cuit.pymjl.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/2 12:39
 **/
public class HessianUtils {

    /**
     * 序列化
     *
     * @param obj obj
     * @return {@code byte[]}
     */
    public static byte[] serialize(Object obj) {
        Hessian2Output ho = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ho = new Hessian2Output(baos);
            ho.writeObject(obj);
            ho.flush();
            return baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("serialize failed");
        } finally {
            if (null != ho) {
                try {
                    ho.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 字节
     * @param clazz clazz
     * @return {@code T}
     */
    public static  <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Hessian2Input hi = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            hi = new Hessian2Input(bais);
            Object o = hi.readObject();
            return clazz.cast(o);
        } catch (Exception ex) {
            throw new RuntimeException("deserialize failed");
        } finally {
            if (null != hi) {
                try {
                    hi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bais) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

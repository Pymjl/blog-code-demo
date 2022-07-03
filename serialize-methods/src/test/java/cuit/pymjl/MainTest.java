package cuit.pymjl;

import cuit.pymjl.entity.Student;
import cuit.pymjl.utils.HessianUtils;
import cuit.pymjl.utils.KryoUtils;
import cuit.pymjl.utils.ProtostuffUtils;
import org.junit.jupiter.api.Test;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/2 12:40
 **/
public class MainTest {
    @Test
    void testLength() {
        Student student = new Student("pymjl", "123456", 18, "北京", "123456789");
        int kryoLength = KryoUtils.writeObjectToByteArray(student).length;
        int hessianLength = HessianUtils.serialize(student).length;
        int protostuffLength = ProtostuffUtils.serialize(student).length;
        System.out.println("kryoLength: " + kryoLength);
        System.out.println("hessianLength: " + hessianLength);
        System.out.println("protostuffLength: " + protostuffLength);
    }

    @Test
    void test() {
    }
}

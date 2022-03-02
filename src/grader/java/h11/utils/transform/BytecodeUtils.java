package h11.utils.transform;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class BytecodeUtils {

    private BytecodeUtils() {}

    static int store(MethodVisitor visitor, List<Type> types, int start) {
        types = new ArrayList<>(types);
        Collections.reverse(types);
        for (Type type : types) {
            if (type == Type.BOOLEAN_TYPE
                || type == Type.BYTE_TYPE
                || type == Type.CHAR_TYPE
                || type == Type.SHORT_TYPE
                || type == Type.INT_TYPE
            ) {
                visitor.visitVarInsn(Opcodes.ISTORE, start++);
            } else if (type == Type.LONG_TYPE) {
                visitor.visitVarInsn(Opcodes.LSTORE, start++);
                //start++;
            } else if (type == Type.FLOAT_TYPE) {
                visitor.visitVarInsn(Opcodes.FSTORE, start++);
            } else if (type == Type.DOUBLE_TYPE) {
                visitor.visitVarInsn(Opcodes.DSTORE, start++);
                //start++;
            } else {
                visitor.visitVarInsn(Opcodes.ASTORE, start++);
            }
        }
        return start - 1;
    }

    static int load(MethodVisitor visitor, List<Type> types, int start) {
        for (Type type : types) {
            if (type == Type.BOOLEAN_TYPE
                || type == Type.BYTE_TYPE
                || type == Type.CHAR_TYPE
                || type == Type.SHORT_TYPE
                || type == Type.INT_TYPE
            ) {
                visitor.visitVarInsn(Opcodes.ILOAD, start--);
            } else if (type == Type.LONG_TYPE) {
                visitor.visitVarInsn(Opcodes.LLOAD, start--);
                //start--;
            } else if (type == Type.FLOAT_TYPE) {
                visitor.visitVarInsn(Opcodes.FLOAD, start--);
            } else if (type == Type.DOUBLE_TYPE) {
                visitor.visitVarInsn(Opcodes.DLOAD, start--);
                //start--;
            } else {
                visitor.visitVarInsn(Opcodes.ALOAD, start--);
            }

        }
        return start + 1;
    }
}

package h11.utils.transform;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

class StreamMergerVisitor extends MethodVisitor {

    StreamMergerVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (opcode == Opcodes.INVOKESTATIC
            && ((owner.equals("java/util/stream/Stream") && name.equals("of"))
                || (owner.equals("java/util/stream/Stream") && name.equals("generate"))
                || (owner.equals("java/util/stream/IntStream") && name.equals("range"))
        )) {
            super.visitFieldInsn(Opcodes.GETSTATIC,
                "h11/merge/Tutor_StreamMergerMetaTests",
                "INVOKED_REQUIRED_METHODS",
                "Ljava/util/Map;");
            super.visitLdcInsn(name);
            super.visitInsn(Opcodes.ICONST_1);
            super.visitMethodInsn(Opcodes.INVOKESTATIC,
                "java/lang/Boolean",
                "valueOf",
                "(Z)Ljava/lang/Boolean;",
                false);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE,
                "java/util/Map",
                "put",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                true);
            super.visitInsn(Opcodes.POP);
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}

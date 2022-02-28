package h11.utils.transform;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class UnicodeVisitors extends MethodVisitor {

    private final String className;

    UnicodeVisitors(String className, MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
        this.className = className;
    }

    @Override
    public void visitInsn(int opcode) {
        if (className.matches("^h11/unicode/CharFromUnicode(CasesExchanged)?$") && opcode == Opcodes.I2C) {
            super.visitInsn(Opcodes.ICONST_1);
            super.visitFieldInsn(Opcodes.PUTSTATIC,
                className + "Tests",
                "ILLEGAL_INSTRUCTION_USED",
                "Z");
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (className.equals("h11/unicode/UnicodeTests")
            && opcode == Opcodes.INVOKEVIRTUAL
            && owner.matches("^h11/unicode/CharFromUnicode(CasesExchanged)?$")
            && name.equals("apply")
            && descriptor.equals("(Ljava/lang/Integer;)Ljava/lang/Character;")
        ) {
            super.visitInsn(Opcodes.DUP);
            super.visitFieldInsn(Opcodes.GETSTATIC,
                "h11/unicode/UnicodeMetaTests",
                "CONSTRUCTOR_INVOCATION_ARGS",
                "Ljava/util/Map;");
            super.visitLdcInsn(owner);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE,
                "java/util/Map",
                "get",
                "(Ljava/lang/Object;)Ljava/lang/Object;",
                true);
            super.visitInsn(Opcodes.SWAP);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE,
                "java/util/List",
                "add",
                "(Ljava/lang/Object;)Z",
                true);
            super.visitInsn(Opcodes.POP);
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}

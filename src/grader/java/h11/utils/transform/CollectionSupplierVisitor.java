package h11.utils.transform;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class CollectionSupplierVisitor extends MethodVisitor {

    CollectionSupplierVisitor(MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (
            !owner.equals("java/lang/Object")
                && !(owner.equals("java/util/Collection") && name.equals("iterator"))
                && !owner.equals("java/util/Iterator")
                && !owner.equals("h11/supplier/CollectionSupplier")
                && !owner.equals("org/sourcegrade/jagr/core/executor/TimeoutHandler")
        ) {
            super.visitInsn(Opcodes.ICONST_1);
            super.visitFieldInsn(Opcodes.PUTSTATIC,
                "h11/supplier/CollectionSupplierTests",
                "ILLEGAL_INSTRUCTION_USED",
                "Z");
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}

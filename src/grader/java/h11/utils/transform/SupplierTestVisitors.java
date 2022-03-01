package h11.utils.transform;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;

class SupplierTestVisitors {

    static class ArraySupplierMethodVisitor extends MethodVisitor {

        private int maxVar = 0;

        ArraySupplierMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitIincInsn(int var, int increment) {
            maxVar = Math.max(maxVar, var + 1);
            super.visitIincInsn(var, increment);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            maxVar = Math.max(maxVar, var + 1);
            super.visitVarInsn(opcode, var);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (
                opcode == Opcodes.INVOKEVIRTUAL
                    && owner.equals("h11/supplier/SupplierTests")
                    && name.equals("buildIntegerArray")
                    && descriptor.equals("(III)[Ljava/lang/Integer;")
            ) {
                List<Type> types = List.of(Type.getArgumentTypes(descriptor));
                int n = BytecodeUtils.store(this, types, maxVar);
                BytecodeUtils.load(this, types, n);
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "h11/supplier/Tutor_SupplierMetaTests",
                    "interceptBuildIntegerArray",
                    "(III)V",
                    false);
                maxVar = BytecodeUtils.load(this, types, n);
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    static class CollectionSupplierMethodVisitor extends MethodVisitor {

        private int maxVar = 0;

        CollectionSupplierMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitIincInsn(int var, int increment) {
            maxVar = Math.max(maxVar, var + 1);
            super.visitIincInsn(var, increment);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            maxVar = Math.max(maxVar, var + 1);
            super.visitVarInsn(opcode, var);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (
                opcode == Opcodes.INVOKEVIRTUAL
                    && owner.equals("h11/supplier/SupplierTests")
                    && name.equals("buildIntegerList")
                    && descriptor.equals("(III)Ljava/util/List;")
            ) {
                List<Type> types = List.of(Type.getArgumentTypes(descriptor));
                int n = BytecodeUtils.store(this, types, maxVar);
                BytecodeUtils.load(this, types, n);
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "h11/supplier/Tutor_SupplierMetaTests",
                    "interceptBuildIntegerList",
                    "(III)V",
                    false);
                maxVar = BytecodeUtils.load(this, types, n);
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    static class CyclicRangeSupplierMethodVisitor extends MethodVisitor {

        private int maxVar = 0;

        CyclicRangeSupplierMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitIincInsn(int var, int increment) {
            maxVar = Math.max(maxVar, var + 1);
            super.visitIincInsn(var, increment);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            maxVar = Math.max(maxVar, var + 1);
            super.visitVarInsn(opcode, var);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (
                opcode == Opcodes.INVOKEVIRTUAL
                    && owner.equals("h11/supplier/CyclicRangeSupplier")
                    && name.equals("get")
                    && descriptor.equals("()Ljava/lang/Integer;")
            ) {
                super.visitFieldInsn(Opcodes.GETSTATIC,
                    "h11/supplier/Tutor_SupplierMetaTests",
                    "CYCLIC_RANGE_SUPPLIER_GET_CALLS",
                    "I");
                super.visitInsn(Opcodes.ICONST_1);
                super.visitInsn(Opcodes.IADD);
                super.visitFieldInsn(Opcodes.PUTSTATIC,
                    "h11/supplier/Tutor_SupplierMetaTests",
                    "CYCLIC_RANGE_SUPPLIER_GET_CALLS",
                    "I");
            } else if (
                opcode == Opcodes.INVOKESPECIAL
                    && owner.equals("h11/supplier/CyclicRangeSupplier")
                    && name.equals("<init>")
                    && descriptor.equals("(II)V")
            ) {
                List<Type> types = List.of(Type.getArgumentTypes(descriptor));
                super.visitInsn(Opcodes.ICONST_0);
                super.visitFieldInsn(Opcodes.PUTSTATIC,
                    "h11/supplier/Tutor_SupplierMetaTests",
                    "CYCLIC_RANGE_SUPPLIER_GET_CALLS",
                    "I");
                int n = BytecodeUtils.store(this, types, maxVar);
                BytecodeUtils.load(this, types, n);
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "h11/supplier/Tutor_SupplierMetaTests",
                    "interceptCyclicRangeSupplierConstructor",
                    "(II)V",
                    false);
                maxVar = BytecodeUtils.load(this, types, n);
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}

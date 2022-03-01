package h11.utils.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

/**
 * Applies bytecode transformations for some tests.
 */
public class BytecodeTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        reader.accept(new MethodTransformer(writer), 0);
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
    }

    private static class MethodTransformer extends ClassVisitor {

        private String currentClass = "";

        private MethodTransformer(ClassWriter classWriter) {
            super(Opcodes.ASM9, classWriter);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            currentClass = name;
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (currentClass.equals("h11/supplier/SupplierTests")) {
                if (name.equals("testArraySupplier") && descriptor.equals("()V")) {
                    return new SupplierTestVisitors.ArraySupplierMethodVisitor(
                        super.visitMethod(access, name, descriptor, signature, exceptions));
                } else if (name.equals("testCollectionSupplier") && descriptor.equals("()V")) {
                    return new SupplierTestVisitors.CollectionSupplierMethodVisitor(
                        super.visitMethod(access, name, descriptor, signature, exceptions));
                } else if (name.equals("testCyclicRangeSupplier") && descriptor.equals("()V")) {
                    return new SupplierTestVisitors.CyclicRangeSupplierMethodVisitor(
                        super.visitMethod(access, name, descriptor, signature, exceptions));
                }
            } else if (currentClass.equals("h11/supplier/CollectionSupplier")) {
                return new CollectionSupplierVisitor(
                    super.visitMethod(access, name, descriptor, signature, exceptions));
            } else if (currentClass.matches("^h11/unicode/\\w+$")) {
                return new UnicodeVisitors(currentClass,
                    super.visitMethod(access, name, descriptor, signature, exceptions));
            } else if (currentClass.equals("h11/merge/StreamMergerTest") && name.equals("testMerge")) {
                return new StreamMergerVisitor(
                    super.visitMethod(access, name, descriptor, signature, exceptions));
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
}

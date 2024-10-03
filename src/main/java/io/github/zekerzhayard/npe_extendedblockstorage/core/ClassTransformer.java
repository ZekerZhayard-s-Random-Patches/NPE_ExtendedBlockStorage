package io.github.zekerzhayard.npe_extendedblockstorage.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if ("net.minecraft.world.chunk.storage.ExtendedBlockStorage".equals(transformedName)) {
            ClassNode cn = new ClassNode();
            new ClassReader(basicClass).accept(cn, ClassReader.EXPAND_FRAMES);
            for (MethodNode mn : cn.methods) {
                if (RemapUtils.checkMethodName(cn.name, mn.name, mn.desc, "func_76670_c") && RemapUtils.checkMethodDesc(mn.desc, "(III)I")) {
                    LabelNode ln0 = new LabelNode(), ln1 = new LabelNode();
                    for (AbstractInsnNode ain : mn.instructions.toArray()) {
                        if (ain.getOpcode() == Opcodes.GETFIELD) {
                            FieldInsnNode fin = (FieldInsnNode) ain;
                            if (RemapUtils.checkClassName(fin.owner, "net/minecraft/world/chunk/storage/ExtendedBlockStorage") && RemapUtils.checkFieldName(fin.owner, fin.name, fin.desc, "field_76685_h") && RemapUtils.checkFieldDesc(fin.desc, "Lnet/minecraft/world/chunk/NibbleArray;")) {
                                InsnList il = new InsnList();
                                il.add(new InsnNode(Opcodes.DUP));
                                il.add(new JumpInsnNode(Opcodes.IFNULL, ln0));
                                mn.instructions.insert(ain, il);
                            }
                        } else if (ain.getOpcode() == Opcodes.IRETURN) {
                            mn.instructions.insertBefore(ain, new JumpInsnNode(Opcodes.GOTO, ln1));
                            mn.instructions.insertBefore(ain, ln0);
                            mn.instructions.insertBefore(ain, new FrameNode(Opcodes.F_SAME1, 0, null, 0, new Object[] { "net/minecraft/world/chunk/NibbleArray" }));
                            mn.instructions.insertBefore(ain, new InsnNode(Opcodes.POP));
                            mn.instructions.insertBefore(ain, new InsnNode(Opcodes.ICONST_0));
                            mn.instructions.insertBefore(ain, ln1);
                            mn.instructions.insertBefore(ain, new FrameNode(Opcodes.F_SAME1, 0, null, 0, new Object[] { Opcodes.INTEGER }));
                        }
                    }
                }
            }
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cn.accept(cw);
            basicClass = cw.toByteArray();
        }
        return basicClass;
    }
}

/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.java.compile

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.ClassReader

class ClassFile {
    final File file
    boolean hasSourceFile
    boolean hasLineNumbers
    boolean hasLocalVars

    ClassFile(File file) {
        this.file = file
        def methodVisitor = new MethodVisitor(Opcodes.ASM4) {
            @Override
            void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                hasLocalVars = true
            }

            @Override
            void visitLineNumber(int line, Label start) {
                hasLineNumbers = true
            }
        }
        def visitor = new ClassVisitor(Opcodes.ASM4) {
            @Override
            MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                return methodVisitor
            }

            @Override
            void visitSource(String source, String debug) {
                hasSourceFile = true
            }
        }
        new ClassReader(file.bytes).accept(visitor, 0)
    }

    boolean getDebugIncludesSourceFile() {
        return hasSourceFile
    }

    boolean getDebugIncludesLineNumbers() {
        return hasLineNumbers
    }

    boolean getDebugIncludesLocalVariables() {
        return hasLocalVars
    }
}

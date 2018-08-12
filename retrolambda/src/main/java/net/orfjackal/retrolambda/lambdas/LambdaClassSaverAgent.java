// Copyright © 2013-2014 Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://www.apache.org/licenses/LICENSE-2.0

package net.orfjackal.retrolambda.lambdas;

import org.objectweb.asm.ClassReader;
import net.orfjackal.retrolambda.ClassReader2;

import java.lang.instrument.*;
import java.security.ProtectionDomain;

public class LambdaClassSaverAgent implements ClassFileTransformer {

    private LambdaClassSaver lambdaClassSaver;

    public void setLambdaClassSaver(LambdaClassSaver lambdaClassSaver) {
        this.lambdaClassSaver = lambdaClassSaver;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className == null) {
            // Since JDK 8 build b121 or so, lambda classes have a null class name,
            // but we can read it from the bytecode where the name still exists.
            className = new ClassReader2(classfileBuffer).getClassName();
        }
        if (lambdaClassSaver != null) {
            lambdaClassSaver.saveIfLambda(className, classfileBuffer);
        }
        return null;
    }
}

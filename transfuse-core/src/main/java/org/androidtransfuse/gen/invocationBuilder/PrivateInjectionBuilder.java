/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.InjectionUtil;

import javax.inject.Inject;
import java.util.List;

/**
 * Injection Builder for building privately scoped elements.
 *
 * @author John Ericksen
 */
public class PrivateInjectionBuilder implements ModifierInjectionBuilder {

    private final ClassGenerationUtil generationUtil;

    @Inject
    public PrivateInjectionBuilder(ClassGenerationUtil generationUtil) {
        this.generationUtil = generationUtil;
    }

    @Override
    public JExpression buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint, Iterable<JExpression> parameters, ASTType type) {

        //InjectionUtil.setConstructor(Class<T> targetClass, Class[] argClasses,Object[] args)
        JInvocation constructorInvocation = generationUtil.ref(InjectionUtil.class).staticInvoke(InjectionUtil.CALL_CONSTRUCTOR_METHOD)
                .arg(generationUtil.ref(type).dotclass());

        //add classes
        JArray classArray = JExpr.newArray(generationUtil.ref(Class.class));
        for (InjectionNode injectionNode : constructorInjectionPoint.getInjectionNodes()) {
            classArray.add(generationUtil.ref(injectionNode.getSignature().getType()).dotclass());
        }
        constructorInvocation.arg(classArray);

        //add args
        constructorInvocation.arg(buildArgsArray(parameters));

        return constructorInvocation;
    }

    @Override
    public JInvocation buildMethodCall(ASTType returnType, String methodName, Iterable<JExpression> parameters, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression) {

        JClass targetType = generationUtil.ref(targetExpressionType.getName());
        //InjectionUtil.getInstance().setMethod(Class targetClass, Object target, String method, Class[] argClasses,Object[] args)
        JInvocation methodInvocation = generationUtil.ref(InjectionUtil.class).staticInvoke(InjectionUtil.CALL_METHOD_METHOD)
                .arg(generationUtil.ref(returnType.getName()).dotclass())
                .arg(targetType.dotclass())
                .arg(targetExpression)
                .arg(methodName);

        //add classes
        JArray classArray = JExpr.newArray(generationUtil.ref(Class.class));
        for (ASTType injectionNode : injectionNodeType) {
            classArray.add(generationUtil.ref(injectionNode).dotclass());
        }
        methodInvocation.arg(classArray);

        //add args
        methodInvocation.arg(buildArgsArray(parameters));

        return methodInvocation;
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name) {
        return generationUtil.ref(InjectionUtil.class).staticInvoke(InjectionUtil.GET_FIELD_METHOD)
                .arg(generationUtil.ref(returnType).dotclass())
                .arg(generationUtil.ref(variableType).dotclass())
                .arg(variable)
                .arg(name);
    }

    @Override
    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {
        JClass variableType = generationUtil.ref(fieldInjectionPoint.getContainingType());

        return generationUtil.ref(InjectionUtil.class).staticInvoke(InjectionUtil.SET_FIELD_METHOD)
                .arg(variableType.dotclass())
                .arg(variable)
                .arg(fieldInjectionPoint.getName())
                .arg(expression.getExpression());
    }

    private JExpression buildArgsArray(Iterable<JExpression> parameters) {
        JArray argArray = JExpr.newArray(generationUtil.ref(Object.class));
        for (JExpression parameter : parameters) {
            argArray.add(parameter);
        }
        return argArray;
    }
}

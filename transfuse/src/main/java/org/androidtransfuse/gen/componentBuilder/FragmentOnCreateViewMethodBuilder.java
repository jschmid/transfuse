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
package org.androidtransfuse.gen.componentBuilder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.config.Nullable;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.MethodDescriptorBuilder;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FragmentOnCreateViewMethodBuilder implements MethodBuilder {

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;
    private final ASTMethod onCreateViewMethod;
    private final UniqueVariableNamer namer;
    private final ASTClassFactory astClassFactory;
    private final Integer layout;
    private final RResourceReferenceBuilder rResourceReferenceBuilder;

    @Inject
    public FragmentOnCreateViewMethodBuilder(/*@Assisted*/ @Nullable Integer layout,
                                             /*@Assisted*/ ASTMethod onCreateViewMethod,
                                             JCodeModel codeModel,
                                             ClassGenerationUtil generationUtil,
                                             UniqueVariableNamer namer,
                                             ASTClassFactory astClassFactory,
                                             RResourceReferenceBuilder rResourceReferenceBuilder) {
        this.codeModel = codeModel;
        this.onCreateViewMethod = onCreateViewMethod;
        this.generationUtil = generationUtil;
        this.namer = namer;
        this.astClassFactory = astClassFactory;
        this.layout = layout;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
    }

    @Override
    public MethodDescriptor buildMethod(JDefinedClass definedClass) {
        JMethod onCreateMethod = definedClass.method(JMod.PUBLIC, codeModel.ref(View.class), "onCreateView");
        onCreateMethod.annotate(Override.class);
        MethodDescriptorBuilder onCreateMethodDescriptorBuilder = new MethodDescriptorBuilder(onCreateMethod, onCreateViewMethod);

        for (ASTParameter methodArgument : onCreateViewMethod.getParameters()) {
            JVar param = onCreateMethod.param(generationUtil.ref(methodArgument.getASTType()), namer.generateName(methodArgument.getASTType()));
            onCreateMethodDescriptorBuilder.putParameter(methodArgument, new TypedExpression(methodArgument.getASTType(), param));
        }

        //layoutInflater_0 .inflate(layout.details, viewGroup_0, false);
        JBlock body = onCreateMethod.body();

        JVar viewDeclaration = body.decl(codeModel.ref(View.class), namer.generateName(View.class));

        ASTType viewType = astClassFactory.getType(View.class);
        onCreateMethodDescriptorBuilder.putType(viewType, new TypedExpression(viewType, viewDeclaration));

        MethodDescriptor onCreateMethodDescriptor = onCreateMethodDescriptorBuilder.build();

        if (layout == null) {
            JInvocation onCreateView = JExpr._super().invoke("onCreateView");

            for (ASTParameter astParameter : onCreateViewMethod.getParameters()) {
                onCreateView.arg(onCreateMethodDescriptor.getExpression(astParameter.getASTType()).getExpression());
            }

            body.assign(viewDeclaration, onCreateView);
        } else {
            ASTType viewGroupType = astClassFactory.getType(ViewGroup.class);
            ASTType layoutInflaterType = astClassFactory.getType(LayoutInflater.class);
            body.assign(viewDeclaration, onCreateMethodDescriptor.getExpression(layoutInflaterType).getExpression()
                    .invoke("inflate")
                    .arg(rResourceReferenceBuilder.buildReference(layout))
                    .arg(onCreateMethodDescriptor.getExpression(viewGroupType).getExpression())
                    .arg(JExpr.lit(false)));
        }

        return onCreateMethodDescriptor;
    }

    public void closeMethod(MethodDescriptor descriptor) {
        JMethod method = descriptor.getMethod();

        ASTType viewType = astClassFactory.getType(View.class);
        method.body()._return(descriptor.getExpression(viewType).getExpression());
    }
}

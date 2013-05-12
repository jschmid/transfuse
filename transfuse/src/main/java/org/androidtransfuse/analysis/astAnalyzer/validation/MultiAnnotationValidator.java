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
package org.androidtransfuse.analysis.astAnalyzer.validation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.adapter.ASTType;

import java.util.Set;

/**
 * @author John Ericksen
 */
public class MultiAnnotationValidator implements AnnotationValidator {

    private final ImmutableMap<ASTType, Set<AnnotationValidator>> validatorMap;

    public MultiAnnotationValidator(ImmutableMap<ASTType, Set<AnnotationValidator>> validatorMap) {
        this.validatorMap = validatorMap;
    }

    @Override
    public void validate(ASTAnnotation annotation, ASTBase astBase, ImmutableSet<ASTAnnotation> applicableAnnotations) {
        if(validatorMap.containsKey(annotation.getASTType())){
            Set<AnnotationValidator> validators = validatorMap.get(annotation.getASTType());

            for (AnnotationValidator validator : validators) {
                validator.validate(annotation, astBase, applicableAnnotations);
            }
        }
    }
}

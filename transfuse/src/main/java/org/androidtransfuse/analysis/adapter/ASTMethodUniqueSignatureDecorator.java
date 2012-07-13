package org.androidtransfuse.analysis.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * Class to decorate an ASTMethod with an equals and hashcode based on the method signature.  This helps enforce
 * uniqueness of a method on a class throughout the inheritance hierarchy.
 *
 */
public class ASTMethodUniqueSignatureDecorator implements ASTMethod{
    private ASTMethod method;
    private MethodSignature methodSignature;

    public ASTMethodUniqueSignatureDecorator(ASTMethod method) {
        this.method = method;
        this.methodSignature = new MethodSignature(method);
    }

    @Override
    public List<ASTParameter> getParameters() {
        return method.getParameters();
    }

    @Override
    public ASTType getReturnType() {
        return method.getReturnType();
    }

    @Override
    public ASTAccessModifier getAccessModifier() {
        return method.getAccessModifier();
    }

    @Override
    public List<ASTType> getThrowsTypes() {
        return method.getThrowsTypes();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return method.isAnnotated(annotation);
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return method.getAnnotations();
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return method.getAnnotation(annotation);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return method.getASTAnnotation(annotation);
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTMethodUniqueSignatureDecorator)) {
            return false;
        }

        ASTMethodUniqueSignatureDecorator that = (ASTMethodUniqueSignatureDecorator) o;

        return new EqualsBuilder().append(methodSignature, that.methodSignature).isEquals();
    }

    @Override
    public int hashCode() {
        return methodSignature.hashCode();
    }
}
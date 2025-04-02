package com.amtgard.buildertraitscompletions.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PsiPatternMatchersUtil {

    public static Boolish<MethodReferenceImpl> identifierMatchesBuilderPattern(@NotNull PsiElement identifier, String rootPredicate) {
        if (identifier.getParent().getFirstChild() instanceof MethodReferenceImpl) {
            List<String> identifiers = findChildPsiElementIdentifiers(identifier.getParent().getFirstChild());
            if (identifiers.contains(rootPredicate)) {
                return Boolish.maybe((MethodReferenceImpl) identifier.getParent().getFirstChild());
            }
        }
        return Boolish.falsey();
    }

    /**
     * Starting at a MethodReferenceImpl node, descends vertically through MethodReferenceImpl
     * find the most leaf-ward node. Then compares the first child of this leafward node to the
     * target class. If they target class matches, returns that node.
     *
     * @param methodReference
     * @param targetClass
     * @return
     */
    public static Optional methodRefHasClassRefFirstChild(@NotNull MethodReferenceImpl methodReference,
                                                          @NotNull Class targetClass) {
        MethodReferenceImpl methodRef = methodReference;
        while (methodRef.getFirstChild() instanceof MethodReferenceImpl) {
            methodRef = (MethodReferenceImpl) methodRef.getFirstChild();
        }
        PsiElement desc = (PsiElement) findDescendantOfClass(methodRef, targetClass).get();
        if (Optional.ofNullable(desc).isPresent() && targetClass.isInstance(desc)) {
            return Optional.of(desc);
        }
        return Optional.empty();
    }

    public static Optional findDescendantOfClass(PsiElement element, Class targetClass) {
        PsiElement ref = element;
        while (ref != null && !targetClass.isInstance(ref)) {
            ref = ref.getFirstChild();
        }
        return Optional.ofNullable(ref);
    }

    /**
     * Ascends the tree through each ancestor which is a MethodReferenceImpl and
     * returns the penultimate MethodRefImpl node
     *
     * @param element
     * @return
     */
    public static PsiElement findProgenitorMethodRefImpl(@NotNull PsiElement element) {
        PsiElement ref = element.getParent();
        while (ref.getParent() instanceof MethodReferenceImpl) {
            ref = ref.getParent();
        }
        return ref;
    }

    /**
     * Given an element, finds and returns a child node that is a MethodRefImpl
     *
     * @param element
     * @return
     */
    public static MethodReferenceImpl getMethodReferenceChild(PsiElement element) {
        for (PsiElement psiElement : element.getChildren()) {
            if (psiElement instanceof MethodReferenceImpl) {
                return (MethodReferenceImpl) psiElement;
            }
        }
        return null;
    }

    /**
     * Descends through tree from an element through each MethodRefImpl node. At each node,
     * collects all PsiElements that are PhpTokenTypes.IDENTIFIER and returns a list of those
     * string values
     *
     * @param element
     * @return
     */
    public static List<String> findChildPsiElementIdentifiers(@NotNull PsiElement element) {
        List<String> psiElementIdentifiers = new ArrayList<>();
        PsiElement ref = element instanceof MethodReferenceImpl ? element : getMethodReferenceChild(element);
        while (Optional.ofNullable(ref).isPresent()) {
            ASTNode node = ref.getNode().findChildByType(PhpTokenTypes.IDENTIFIER);
            psiElementIdentifiers.add(node.getText());
            ref = getMethodReferenceChild(ref);
        }
        return psiElementIdentifiers;
    }

}

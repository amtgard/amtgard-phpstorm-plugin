package com.amtgard.buildertraitscompletions.util;

import com.google.common.collect.ImmutableList;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.impl.ClassReferenceImpl;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PsiPatternMatchersUtilTest {

    @Mock
    PsiElement psiElement = Mockito.mock(PsiElement.class);

    @Mock
    MethodReferenceImpl methodReference = Mockito.mock(MethodReferenceImpl.class);

    @Mock
    MethodReferenceImpl methodReferenceParent = Mockito.mock(MethodReferenceImpl.class);

    @Mock
    MethodReferenceImpl methodReferenceGrandParent = Mockito.mock(MethodReferenceImpl.class);

    @Mock
    ClassReferenceImpl classReference = Mockito.mock(ClassReferenceImpl.class);

    @Mock
    ASTNode node = Mockito.mock(ASTNode.class);

    @Test
    public void identifierMatchesBuilderPattern_Matches() {
        when(psiElement.getParent()).thenReturn(psiElement);
        when(psiElement.getFirstChild()).thenReturn(methodReference);
        when(methodReference.getChildren())
            .thenReturn(new PsiElement[]{methodReference})
            .thenReturn(new PsiElement[]{psiElement});
        when(methodReference.getNode()).thenReturn(node);
        when(node.findChildByType(PhpTokenTypes.IDENTIFIER)).thenReturn(node);
        when(node.getText()).thenReturn("builder").thenReturn("toBuilder");
        assertTrue(PsiPatternMatchersUtil.identifierMatchesBuilderPattern(psiElement, "builder").truthy());
        assertFalse(PsiPatternMatchersUtil.identifierMatchesBuilderPattern(psiElement, "penguin").truthy());
    }

    @Test
    public void methodRefHasClassRefFirstChild_Matches() {
        when(methodReferenceGrandParent.getFirstChild()).thenReturn(methodReferenceParent);
        when(methodReferenceParent.getFirstChild()).thenReturn(methodReference);
        when(methodReference.getFirstChild()).thenReturn(classReference);
        assertTrue(PsiPatternMatchersUtil.methodRefHasClassRefFirstChild(methodReferenceGrandParent, ClassReferenceImpl.class).isPresent());

        when(methodReference.getFirstChild()).thenReturn(psiElement);
        assertTrue(PsiPatternMatchersUtil.methodRefHasClassRefFirstChild(methodReferenceGrandParent, ClassReferenceImpl.class).isEmpty());
    }

    @Test
    public void findChildPsiElementIdentifiers_Matches() {
        when(methodReference.getChildren())
            .thenReturn(new PsiElement[]{methodReference})
            .thenReturn(new PsiElement[]{methodReference})
            .thenReturn(new PsiElement[]{psiElement});
        when(methodReference.getNode()).thenReturn(node);
        when(node.findChildByType(PhpTokenTypes.IDENTIFIER)).thenReturn(node);
        when(node.getText()).thenReturn("test1").thenReturn("test2").thenReturn("test3").thenReturn("test4");
        assertEquals(ImmutableList.of("test1", "test2", "test3"), PsiPatternMatchersUtil.findChildPsiElementIdentifiers(methodReference));
    }

    @Test
    public void getMethodReferenceChild_returnsMethodReferenceOrNull() {
        when(psiElement.getChildren()).thenReturn(new PsiElement[]{methodReference});
        assertEquals(methodReference, PsiPatternMatchersUtil.getMethodReferenceChild(psiElement));

        when(psiElement.getChildren()).thenReturn(new PsiElement[]{psiElement});
        assertNull(PsiPatternMatchersUtil.getMethodReferenceChild(psiElement));
    }

    @Test
    public void findProgenitorMethodRefImpl_findsHighestAncestor() {
        when(methodReference.getParent()).thenReturn(methodReferenceParent);
        when(methodReferenceParent.getParent()).thenReturn(methodReferenceGrandParent);
        when(methodReferenceGrandParent.getParent()).thenReturn(psiElement);
        assertEquals(methodReferenceGrandParent, PsiPatternMatchersUtil.findProgenitorMethodRefImpl(methodReference));

        when(methodReference.getParent()).thenReturn(psiElement);
        assertEquals(psiElement, PsiPatternMatchersUtil.findProgenitorMethodRefImpl(methodReference));
    }

    @Test
    public void findDescendantOfClass_findsDescendantClassInstance() {
        when(psiElement.getFirstChild())
            .thenReturn(psiElement)
                .thenReturn(psiElement)
                    .thenReturn(methodReference);

        assertEquals(methodReference, PsiPatternMatchersUtil.findDescendantOfClass(psiElement, MethodReferenceImpl.class).get());

        when(psiElement.getFirstChild())
            .thenReturn(psiElement)
            .thenReturn(psiElement)
            .thenReturn(null);

        assertTrue(PsiPatternMatchersUtil.findDescendantOfClass(psiElement, MethodReferenceImpl.class).isEmpty());
    }
}

package com.amtgard.buildertraitscompletions.util;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PsiPatternMatchersUtilTest {

    @Mock
    PsiElement psiElement = Mockito.mock(PsiElement.class);

    @Mock
    MethodReferenceImpl methodReference = Mockito.mock(MethodReferenceImpl.class);

    @Test
    public void getMethodReferenceChild_returnsMethodReference() {
        Mockito.when(psiElement.getChildren()).thenReturn(new PsiElement[]{methodReference});
        assertEquals(methodReference, PsiPatternMatchersUtil.getMethodReferenceChild(psiElement));
    }
}

package com.amtgard.buildertraitscompletions.chainmatcher.matcher;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StaticBuilderPatternTest {
    @Mock
    PsiElement identifier = Mockito.mock(PsiElement.class);

    @Mock
    MethodReferenceImpl methodReference = Mockito.mock(MethodReferenceImpl.class);

    @BeforeEach
    void setUp() {

    }

    @Test
    public void staticPattern_matchesStaticBuilderPattern() {
//        Mockito.when(identifier.getParent().getFirstChild()).thenReturn(methodReference);
    }
}

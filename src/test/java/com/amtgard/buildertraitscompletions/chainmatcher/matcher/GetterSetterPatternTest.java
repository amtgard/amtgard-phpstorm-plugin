package com.amtgard.buildertraitscompletions.chainmatcher.matcher;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.impl.FieldReferenceImpl;
import com.jetbrains.php.lang.psi.elements.impl.VariableImpl;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.Mockito;

import static com.amtgard.buildertraitscompletions.util.BuilderMode.GETTER_SETTER_MODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class GetterSetterPatternTest {

    @Mock
    FieldReferenceImpl fieldReference = Mockito.mock(FieldReferenceImpl.class);

    @Mock
    VariableImpl variable = Mockito.mock(VariableImpl.class);

    @Mock
    PsiElement identifier = Mockito.mock(PsiElement.class);

    @Mock
    PhpType phpType = Mockito.mock(PhpType.class);

    @Mock
    CompletionParameters parameters = Mockito.mock(CompletionParameters.class);

    @Mock
    PsiElement position = Mockito.mock(PsiElement.class);

    GetterSetterPattern pattern;

    static final String BUILDER_TYPE = "\\Penguin\\Penguin";

    @BeforeEach
    public void setup() {
        pattern = new GetterSetterPattern();
    }

    @Test
    public void getterSetterBuilderMode_isGETTER_SETTER_MODE() {
        assertEquals(GETTER_SETTER_MODE, pattern.getBuilderMode());
    }

    @Test
    public void builderVarMatchesGetterSetter() {
        Mockito.when(parameters.getPosition()).thenReturn(position);
        Mockito.when(position.getPrevSibling()).thenReturn(identifier);
        Mockito.when(identifier.getParent()).thenReturn(fieldReference);
        PsiElement[] children = new PsiElement[1];
        children[0] = variable;
        Mockito.when(fieldReference.getChildren()).thenReturn(children);
        Mockito.when(variable.getGlobalType()).thenReturn(phpType);
        Mockito.when(phpType.toString()).thenReturn(BUILDER_TYPE);

        assertTrue(pattern.matchGetterSetter(identifier).truthy());
        assertEquals(BUILDER_TYPE, pattern.matchGetterSetter(identifier).get());

        assertTrue(pattern.match(parameters).truthy());
    }

    @Test
    public void whenPatternDoesntMatch_thenReturnFalse() {
        Mockito.when(parameters.getPosition()).thenReturn(position);
        Mockito.when(position.getPrevSibling()).thenReturn(identifier);
        Mockito.when(identifier.getParent()).thenReturn(fieldReference);
        PsiElement[] children = new PsiElement[0];
        Mockito.when(fieldReference.getChildren()).thenReturn(children);


        assertFalse(pattern.matchGetterSetter(identifier).truthy());
        assertFalse(pattern.match(parameters).truthy());
    }
}

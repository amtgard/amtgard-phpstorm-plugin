package com.amtgard.buildertraitscompletions.util;

import com.amtgard.buildertraitscompletions.model.FqnString;
import com.google.common.collect.ImmutableList;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil.BUILDER_TRAIT;
import static com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil.BUILDER_TRAIT_ROOT;
import static com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil.GETTER_TRAIT;
import static com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil.SETTER_TRAIT;
import static com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil.TO_BUILDER_TRAIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AmtgardBuilderUtilTest {
    private static final String PENGUIN_CLASS_NAME = "\\Penguin\\Penguin";

    @Mock
    PhpClass penguinClass = Mockito.mock(PhpClass.class);

    @Mock
    PhpIndex phpIndexInstance = Mockito.mock(PhpIndex.class);

    @Mock
    Project project = Mockito.mock(Project.class);

    MockedStatic<PhpIndex> phpIndex;

    static final Field PENGUIN_FIELD = Mockito.mock(Field.class);

    static final List<String> BUILDER_TRAIT_LIST = ImmutableList.of(BUILDER_TRAIT_ROOT + BUILDER_TRAIT, BUILDER_TRAIT_ROOT + GETTER_TRAIT, BUILDER_TRAIT_ROOT + SETTER_TRAIT, BUILDER_TRAIT_ROOT + TO_BUILDER_TRAIT);

    static final List<Field> PENGUIN_FIELD_LIST = ImmutableList.of(PENGUIN_FIELD);

    static List<PhpClass> penguinClassList;

    @BeforeEach
    void setUp() {
        phpIndex = Mockito.mockStatic(PhpIndex.class);
        phpIndex.when(() -> PhpIndex.getInstance(project)).thenReturn(phpIndexInstance);
        penguinClassList = ImmutableList.of(penguinClass);
        when(phpIndexInstance.getClassesByFQN(PENGUIN_CLASS_NAME)).thenReturn(penguinClassList);
    }

    @AfterEach
    public void tearDown() {
        phpIndex.close();
    }

    @Test
    public void getClassFieldListHappyCase() {
        when(penguinClass.getFields()).thenReturn(PENGUIN_FIELD_LIST);
        when(PENGUIN_FIELD.getName()).thenReturn("field1");

        assertEquals(ImmutableList.of("field1"), AmtgardBuilderUtil.getClassFieldList(project, FqnString.builder().fqn(PENGUIN_CLASS_NAME).build()));
    }

    @Test
    public void getClassesReturnsFqn() {
        when(PENGUIN_FIELD.getName()).thenReturn("field1");

        assertEquals(penguinClassList, AmtgardBuilderUtil.getClassesForFqn(project, PENGUIN_CLASS_NAME));
    }

    @Test
    public void hasSpecificTrait() {
        when(penguinClass.getTraitNames()).thenReturn(BUILDER_TRAIT_LIST.toArray(new String[BUILDER_TRAIT_LIST.size()]));

        assertTrue(AmtgardBuilderUtil.hasAmtgardBuilderTrait(project, PENGUIN_CLASS_NAME, BUILDER_TRAIT));
    }

    @Test
    public void ifNoTraitMatches_thenNoTraitFound() {
        when(penguinClass.getTraitNames()).thenReturn(new String[]{});

        assertFalse(AmtgardBuilderUtil.hasAmtgardBuilderTrait(project, PENGUIN_CLASS_NAME, BUILDER_TRAIT));
        assertFalse(AmtgardBuilderUtil.hasAmtgardBuilderTrait(project, PENGUIN_CLASS_NAME));
    }

    @Test
    public void hasAnyTrait() {
        when(penguinClass.getTraitNames()).thenReturn(BUILDER_TRAIT_LIST.toArray(new String[BUILDER_TRAIT_LIST.size()]));

        assertTrue(AmtgardBuilderUtil.hasAmtgardBuilderTrait(project, PENGUIN_CLASS_NAME));
    }

    @Test
    public void whenSpecifiedTraitMatches_thenTraitFound() {
        when(penguinClass.getTraitNames()).thenReturn(BUILDER_TRAIT_LIST.toArray(new String[BUILDER_TRAIT_LIST.size()]));

        assertTrue(AmtgardBuilderUtil.hasAmtgardBuilderBuilderTrait(project, PENGUIN_CLASS_NAME));
        assertTrue(AmtgardBuilderUtil.hasAmtgardBuilderGetterTrait(project, PENGUIN_CLASS_NAME));
        assertTrue(AmtgardBuilderUtil.hasAmtgardBuilderSetterTrait(project, PENGUIN_CLASS_NAME));
        assertTrue(AmtgardBuilderUtil.hasAmtgardBuilderToBuilderTrait(project, PENGUIN_CLASS_NAME));
    }
}

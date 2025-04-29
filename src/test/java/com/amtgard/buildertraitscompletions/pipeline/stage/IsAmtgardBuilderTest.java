package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.intellij.openapi.project.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class IsAmtgardBuilderTest {
    MockedStatic<AmtgardBuilderUtil> amtgardBuilderUtilMockedStatic;

    @BeforeEach
    public void setup() {
        amtgardBuilderUtilMockedStatic = Mockito.mockStatic(AmtgardBuilderUtil.class);
    }

    @AfterEach
    public void teardown() {
        amtgardBuilderUtilMockedStatic.close();
    }

    @Mock
    Project project = Mockito.mock(Project.class);

    @Test
    public void happyPath() {
        FqnString fqn = FqnString.builder().fqn("fqn").build();
        StageContext stageContext = StageContext
            .builder()
            .completions(new ArrayList<>())
            .mode(BuilderMode.BUILDER_MODE)
            .project(project)
            .fqnString(fqn)
            .build();
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.hasAmtgardBuilderGetterTrait(project, "fqn")).thenReturn(true);
        IsAmtgardBuilder isAmtgardBuilder = new IsAmtgardBuilder();
        Assertions.assertNotNull(isAmtgardBuilder.execute(stageContext).get());
    }

    @Test
    public void whenNotAmtgardBuilderReturnsFalse() {
        FqnString fqn = FqnString.builder().fqn("fqn").build();
        StageContext stageContext = StageContext
            .builder()
            .completions(new ArrayList<>())
            .mode(BuilderMode.BUILDER_MODE)
            .project(project)
            .fqnString(fqn)
            .build();
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.hasAmtgardBuilderGetterTrait(project, "fqn")).thenReturn(false);
        IsAmtgardBuilder isAmtgardBuilder = new IsAmtgardBuilder();
        Assertions.assertFalse(isAmtgardBuilder.execute(stageContext).truthy());
    }
}

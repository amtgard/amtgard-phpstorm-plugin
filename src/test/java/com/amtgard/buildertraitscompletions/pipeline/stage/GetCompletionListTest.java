package com.amtgard.buildertraitscompletions.pipeline.stage;

import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.provider.DocumentPatternMatcherProvider;
import com.amtgard.buildertraitscompletions.util.AmtgardBuilderUtil;
import com.amtgard.buildertraitscompletions.util.BuilderMode;
import com.google.common.collect.ImmutableList;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class GetCompletionListTest {
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
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.hasAmtgardBuilderTrait(project, "fqn")).thenReturn(true);
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.getClassFieldList(any(), any())).thenReturn(List.of("field"));
        GetCompletionList getCompletionList = new GetCompletionList();
        getCompletionList.execute(stageContext);
        assertTrue(stageContext.getCompletions().stream().anyMatch(c -> c.getDisplay().equals("field()")));
    }

    @Test
    public void whenBuilder_thenBuilderMethodsAppended() {
        FqnString fqn = FqnString.builder().fqn("fqn").build();
        StageContext stageContext = StageContext
            .builder()
            .completions(new ArrayList<>())
            .mode(BuilderMode.BUILDER_MODE)
            .project(project)
            .fqnString(fqn)
            .build();
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.hasAmtgardBuilderTrait(project, "fqn")).thenReturn(true);
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.getClassFieldList(any(), any())).thenReturn(List.of("field"));
        GetCompletionList getCompletionList = new GetCompletionList();
        getCompletionList.appendBuilderCompletions(stageContext);
        assertTrue(stageContext.getCompletions().stream().anyMatch(c -> c.getDisplay().equals("field()")));
    }

    @Test
    public void whenGetter_thenGetterMethodsAppended() {
        FqnString fqn = FqnString.builder().fqn("fqn").build();
        StageContext stageContext = StageContext
            .builder()
            .completions(new ArrayList<>())
            .mode(BuilderMode.GETTER_SETTER_MODE)
            .project(project)
            .fqnString(fqn)
            .build();
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.hasAmtgardBuilderGetterTrait(project, "fqn")).thenReturn(true);
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.getClassFieldList(any(), any())).thenReturn(List.of("field"));
        GetCompletionList getCompletionList = new GetCompletionList();
        getCompletionList.appendGetterCompletions(stageContext);
        assertTrue(stageContext.getCompletions().stream().anyMatch(c -> c.getDisplay().equals("getField()")));
    }

    @Test
    public void whenSetter_thenSetterMethodsAppended() {
        FqnString fqn = FqnString.builder().fqn("fqn").build();
        StageContext stageContext = StageContext
            .builder()
            .completions(new ArrayList<>())
            .mode(BuilderMode.GETTER_SETTER_MODE)
            .project(project)
            .fqnString(fqn)
            .build();
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.hasAmtgardBuilderSetterTrait(project, "fqn")).thenReturn(true);
        amtgardBuilderUtilMockedStatic.when(() -> AmtgardBuilderUtil.getClassFieldList(any(), any())).thenReturn(List.of("field"));
        GetCompletionList getCompletionList = new GetCompletionList();
        getCompletionList.appendSetterCompletions(stageContext);
        assertTrue(stageContext.getCompletions().stream().anyMatch(c -> c.getDisplay().equals("setField()")));
    }
}

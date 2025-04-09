package com.amtgard.buildertraitscompletions.pipeline;

import com.amtgard.buildertraitscompletions.model.FqnString;
import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.provider.ProductionPipelineProvider;
import com.amtgard.buildertraitscompletions.util.Boolish;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CompletionProductionPipelineTest {

    MockedStatic<ProductionPipelineProvider> productionPipelineProvider;

    @Mock
    StageContext stageContext;

    @BeforeEach
    void setUp() {
        productionPipelineProvider = Mockito.mockStatic(ProductionPipelineProvider.class);
    }

    @AfterEach
    void tearDown() {
        productionPipelineProvider.close();
    }

    static boolean isExecuted = false;

    @Test
    public void testPipelineOperation() {
        Mockito.when(stageContext.getFqnString()).thenReturn(FqnString.builder().fqn("FQN").build());
        productionPipelineProvider.when(() -> ProductionPipelineProvider.providePipeline()).thenReturn(ImmutableList.of(new AbstractPipelineStage() {
            @Override
            public Boolish execute(StageContext context) {
                isExecuted = context.getFqnString().getFqn().equals("FQN");
                return Boolish.maybe(context);
            }
        }, new AbstractPipelineStage() {
            @Override
            public Boolish execute(StageContext context) {
                isExecuted = context.getFqnString().getFqn().equals("FQN");
                return Boolish.maybe(context);
            }
        }));
        CompletionProductionPipeline.run(stageContext);
        Mockito.verify(stageContext, Mockito.times(2)).getFqnString();
        assertTrue(isExecuted);
    }
}

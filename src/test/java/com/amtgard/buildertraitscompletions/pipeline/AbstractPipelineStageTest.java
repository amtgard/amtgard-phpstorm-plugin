package com.amtgard.buildertraitscompletions.pipeline;

import com.amtgard.buildertraitscompletions.model.StageContext;
import com.amtgard.buildertraitscompletions.util.Boolish;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractPipelineStageTest {
    @Mock
    StageContext context = Mockito.mock(StageContext.class);

    @Test
    public void testDefault_returnsStage() {
        AbstractPipelineStage stage = new AbstractPipelineStage() {
            @Override
            public Boolish execute(StageContext context) {
                return super.execute(context);
            }
        };

        assertEquals(context, stage.execute(context).get());
    }
}

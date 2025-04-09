package com.amtgard.buildertraitscompletions.util;

import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
public class BoolishTest {

    @Builder
    public static class MaybeValue {

        @Getter
        private int value;

        public MaybeValue(int value) {
            this.value = value;
        }
    }

    @Test
    public void testWhenBoolish_isFalseyThenReturnFalse() {
        Boolish boolish = Boolish.falsey();
        assertFalse(boolish.truthy());
    }

    @Test
    public void testWhenBoolish_hasValue_thenTruthyGetsValues() {
        Boolish boolish = Boolish.maybe(MaybeValue.builder().value(2).build());
        if (boolish.truthy()) {
            assertEquals(2, ((MaybeValue)boolish.get()).getValue());
        }
    }

    @Test
    public void testWhenBoolish_hasValue_thenTruthyFunctionGetsValues() {
        Boolish boolish = Boolish.maybe(MaybeValue.builder().value(2).build());
        assertEquals(2, ((MaybeValue)boolish.truthy(v -> v)).getValue());
    }
}

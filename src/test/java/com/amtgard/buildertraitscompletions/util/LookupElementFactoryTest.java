package com.amtgard.buildertraitscompletions.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LookupElementFactoryTest {

    @Test
    public void lookupElementHasProvidedValue() {
        assertEquals("value", LookupElementFactory.build("value").getLookupString());
    }
}

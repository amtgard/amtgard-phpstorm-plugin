package com.amtgard.buildertraitscompletions.util;

import com.intellij.codeInsight.lookup.LookupElement;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;
import org.jetbrains.annotations.NotNull;

public class LookupElementFactory {
    public static LookupElement build(@NonNull String value) {
        return new LookupElement() {
            @Override
            public @NotNull String getLookupString() {
                return value;
            }
        };
    }
}

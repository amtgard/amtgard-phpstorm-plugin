package com.amtgard.buildertraitscompletions.util;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Builder;

import java.util.Optional;
import java.util.function.Function;

@Builder
public class Boolish<T>  {

    private Optional<T> optional;

    public boolean truthy() {
        return this.optional.isPresent();
    }

    public <R> R truthy(@NotNull Function<T, R> f) {
        return this.optional.map(f).orElse(null);
    }

    public T get() {
        return this.optional.get();
    }

    public static <T> Boolish maybe(T target) {
        return Boolish.builder().optional(Optional.of(target)).build();
    }

    public static Boolish falsey() {
        return Boolish.builder().optional(Optional.empty()).build();
    }
}

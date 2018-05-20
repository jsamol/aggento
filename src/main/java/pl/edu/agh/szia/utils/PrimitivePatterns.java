package pl.edu.agh.szia.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public interface PrimitivePatterns {

    abstract class Handler<F> {
        public abstract void apply(F message);
    }

    default <F, T> Handler<F> handle(Function<F, T> caster, Consumer<T> consumer) {
        return new Handler<F>() {
            @Override
            public void apply(F message) {
                consumer.accept(caster.apply(message));
            }
        };
    }
}

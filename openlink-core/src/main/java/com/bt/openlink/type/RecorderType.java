package com.bt.openlink.type;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecorderType extends AbstractType<String> {

    private static final long serialVersionUID = 1974738602126787503L;

    private RecorderType(final String value) {
        super(value);
    }

    @Nonnull
    public static Optional<RecorderType> from(@Nullable final String value) {
        return value == null || value.isEmpty() ? Optional.empty() : Optional.of(new RecorderType(value));
    }

    @Nonnull
    public static RecorderType from(@Nonnull final AbstractType<String> type) {
        return new RecorderType(type.value());
    }

}

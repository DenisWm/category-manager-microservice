package com.course.admin.catalogo.domain.castmember;

import com.course.admin.catalogo.domain.Identifier;
import com.course.admin.catalogo.domain.utils.IDUtils;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {

    protected final String value;

    private CastMemberID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }
    public static CastMemberID unique() {
        return CastMemberID.from(IDUtils.uuid());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CastMemberID that = (CastMemberID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}

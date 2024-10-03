package com.course.admin.catalogo.domain.castmember;

import com.course.admin.catalogo.domain.AggregateRoot;
import com.course.admin.catalogo.domain.exceptions.NotificationException;
import com.course.admin.catalogo.domain.utils.InstantUtils;
import com.course.admin.catalogo.domain.validation.ValidationHandler;
import com.course.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    private CastMember(final CastMemberID anId, final String aName, final CastMemberType aType, Instant aCreatedAt, final Instant aUpdatedAt) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var now = InstantUtils.now();
        return new CastMember(CastMemberID.unique(), aName, aType, now, now);

    }

    public static CastMember with(final CastMemberID anId, final String aName, final CastMemberType aType, Instant aCreatedAt, final Instant aUpdatedAt){
        return new CastMember(anId, aName, aType, aCreatedAt, aUpdatedAt);
    }
    public static CastMember with(final CastMember aMember){
        return new CastMember(aMember.getId(), aMember.getName(), aMember.getType(), aMember.getCreatedAt(), aMember.getUpdatedAt());
    }

    private void selfValidate() {
        final var notification = Notification.create();

        validate(notification);

        if(notification.hasErrors()){
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }
    }

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CastMemberValidator(handler, this).validate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CastMemberType getType() {
        return type;
    }

    public void setType(CastMemberType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


}
